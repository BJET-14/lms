package com.bjet.aki.lms.service;

import com.bjet.aki.lms.asset.ListResultBuilder;
import com.bjet.aki.lms.asset.PagedResult;
import com.bjet.aki.lms.asset.PagedResultBuilder;
import com.bjet.aki.lms.asset.ResultBuilder;
import com.bjet.aki.lms.exception.CommonException;
import com.bjet.aki.lms.jpa.*;
import com.bjet.aki.lms.mapper.CourseScheduleMapper;
import com.bjet.aki.lms.mapper.PostMapper;
import com.bjet.aki.lms.model.*;
import com.bjet.aki.lms.mapper.CourseMapper;
import com.bjet.aki.lms.repository.*;
import com.bjet.aki.lms.specification.CourseSpecification;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.converters.ModelConverterRegistrar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final ModelConverterRegistrar modelConverterRegistrar;
    Logger logger = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final CourseMapper courseMapper;
    private final UserService userService;
    private final CourseScheduleMapper courseScheduleMapper;
    private final CourseScheduleRepository courseScheduleRepository;
    private final ClassScheduleRepository classScheduleRepository;
    private final EmailNotificationService notificationService;
    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Transactional
    public Long saveCourse(Course course) {
        logger.info("Saving request for course");
        CourseEntity courseEntity = courseMapper.toEntity().map(course);
        courseEntity = courseRepository.save(courseEntity);

        // Save the modules associated with the course
        List<ModuleEntity> modules = courseEntity.getModules();
        if (modules != null) {
            for (ModuleEntity module : modules) {
                module.setCourse(courseEntity);
                moduleRepository.save(module);
            }
        }

        return courseEntity.getId();
    }

    @Transactional
    public List<Course> findAllCourses(String title, LocalDate startDateFrom, LocalDate startDateTo, Boolean isComplete) {
        List<CourseEntity> courses = courseRepository.findAll(CourseSpecification.findCourses(title, startDateFrom, startDateTo, isComplete));
        return ListResultBuilder.build(courses, courseMapper.toDomain());
    }

    @Transactional
    public PagedResult<Course> findAllCourses(Pageable pageable, String title, LocalDate startDateFrom, LocalDate startDateTo, Boolean isComplete) {
        Page<CourseEntity> courses = courseRepository.findAll(CourseSpecification.findCourses(title, startDateFrom, startDateTo, isComplete), pageable);
        return PagedResultBuilder.build(courses, courseMapper.toDomain());
    }

    @Transactional
    public Course findCourseById(Long id) {
        CourseEntity courseEntity = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Could not find course with id: " + id));
        return ResultBuilder.build(courseEntity, courseMapper.toDomain());
    }

    @Transactional
    public void updateCourse(Course course) {
        CourseEntity courseEntity = courseMapper.toEntity().map(course);
        List<ModuleEntity> modules = new ArrayList<>(courseEntity.getModules());
        modules.clear();
        modules.addAll(courseEntity.getModules().stream().map(module -> module.setCourse(courseEntity)).toList());
        courseEntity.setModules(modules);
        courseRepository.save(courseEntity);
    }

    public boolean isExist(long id) {
        return courseRepository.existsById(id);
    }

    public void deleteModule(Long moduleId) {
        moduleRepository.deleteById(moduleId);
    }

    @Transactional
    public void assignTeacherToCourse(Long courseId, TeacherAssigningToCourseRequest request) {
        logger.info("Assigning teacher to course. CourseId {}", courseId);
        CourseEntity entity = courseRepository.findById(courseId).orElseThrow(() -> new CommonException("03", "Could not find course"));
        if(courseScheduleRepository.findAllByCourse_Id(courseId).isEmpty()){
            throw new CommonException("04", "Course schedule not added yet");
        }
        Teacher teacher = userService.findTeacher(request.getTeacherId());
        if(teacher == null) {
            throw new CommonException("02", "Could not find teacher");
        }
        entity.setTeacherId(request.getTeacherId());
        courseRepository.save(entity);

        sendEmailNotificationWithSchedule(teacher.getEmail(), entity, teacher);
    }

    private void sendEmailNotificationWithSchedule(String receiverEmailAddress, CourseEntity course) {
        Teacher teacher = userService.findTeacher(course.getTeacherId());
        if(teacher == null) {
            throw new CommonException("02", "Could not find teacher");
        }
        sendEmailNotificationWithSchedule(receiverEmailAddress, course, teacher);
    }

    private void sendEmailNotificationWithSchedule(String receiverEmailAddress, CourseEntity course, Teacher teacher) {
        List<ClassScheduleEntity> classSchedules = classScheduleRepository.findAllByCourse_Id(course.getId());
        ClassScheduleSentToEmailRequest request = courseMapper.toEmailNotificationToTeacherWithClassSchedule(receiverEmailAddress, teacher, course, classSchedules);
        logger.info("Sending course schedule. Email to: {}", request.getReceiverEmailAddress());
        if (request.isSendingToStudent()) {
            notificationService.sendEmailWithClassScheduleToStudent(request);
        } else {
            notificationService.sendEmailWithClassScheduleToTeacher(request);
        }
    }

    @Transactional
    public void schedule(Long courseId, CourseScheduleRequest request) {
        logger.info("Scheduling the course. CourseId {}", courseId);
        CourseEntity course = courseRepository.findById(courseId).orElseThrow(() -> new CommonException("03", "Could not find course"));
        List<CourseSchedule> schedules = request.getSchedules();
        List<CourseScheduleEntity> scheduleEntities = schedules.stream()
                .map(schedule -> {
                    CourseScheduleEntity entity = courseScheduleMapper.toEntity().map(schedule);
                    entity.setCourse(course);
                    return entity;
                })
                .toList();
        courseScheduleRepository.saveAllAndFlush(scheduleEntities);
        List<ClassScheduleEntity> classSchedule = generateClassSchedule(course);
        classScheduleRepository.saveAll(classSchedule);
    }

    @Transactional
    public List<ClassSchedule> getClassSchedule(Long courseId) {
        List<ClassScheduleEntity> classScheduleEntities = classScheduleRepository.findAllByCourse_Id(courseId);
        return classScheduleEntities.stream()
                .map(entity -> new ClassSchedule()
                        .setId(entity.getId())
                        .setDate(entity.getClassDateTime().toLocalDate())
                        .setTime(entity.getClassDateTime().toLocalTime())
                        .setTimeSlot(entity.getTimeSlot()))
                .toList();
    }

    public List<ClassScheduleEntity> generateClassSchedule(CourseEntity course){
        List<CourseScheduleEntity> schedules = courseScheduleRepository.findAllByCourse_Id(course.getId());
        if(course.getStartDate() != null){
            List<ClassScheduleEntity> classScheduleEntities = new ArrayList<>();
            LocalDate currentDate = course.getStartDate();
            int totalClasses = course.getModules().size();
            int classCount = 0;
            while (classCount < totalClasses) {
                DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
                for(CourseScheduleEntity courseSchedule : schedules){
                    if(courseSchedule.getDays().equals(dayOfWeek)){
                        LocalTime time = courseSchedule.getTimeSlot().equals(TimeSlot.AM_9) ? LocalTime.of(9, 0) : LocalTime.of(11, 0);
                        classScheduleEntities.add(new ClassScheduleEntity(0L, course, LocalDateTime.of(currentDate, time), courseSchedule.getTimeSlot()));
                        classCount++;
                    }
                }
                currentDate = currentDate.plusDays(1);
            }
            classScheduleEntities.sort((o1, o2) -> o1.getClassDateTime().isEqual(o2.getClassDateTime()) ? 0 : o1.getClassDateTime().isAfter(o2.getClassDateTime()) ? 1 : -1);
            return classScheduleEntities;
        } else{
            logger.warn("Course start date is not set");
        }
        return null;
    }

    @Transactional
    public Long enroll(Long courseId, StudentEnrollmentRequest request) {
        boolean exists = studentEnrollmentRepository.existsByCourseIdAndStudentId(courseId, request.getStudentId());
        if(exists){
            throw new CommonException("02", "Student already enrolled");
        }
        Student student = userService.findStudent(request.getStudentId());

        StudentEnrollmentEntity entity = new StudentEnrollmentEntity();
        entity.setCourseId(courseId);
        entity.setStudentId(student.getId());
        entity.setEnrollmentDate(LocalDate.now());
        entity = studentEnrollmentRepository.save(entity);

        // sending email to students with class schedule
        CourseEntity course = courseRepository.findById(courseId).orElseThrow(() -> new CommonException("03", "Could not find course"));
        sendEmailNotificationWithSchedule(student.getEmail(), course);
        return entity.getId();
    }

    @Transactional
    public List<StudentEnrollment> getAllEnrolledStudents(Long courseId) {
        List<StudentEnrollmentEntity> enrollmentEntities = studentEnrollmentRepository.findAllByCourseId(courseId);
        if(enrollmentEntities.isEmpty()){
            return emptyList();
        }
        return enrollmentEntities.stream()
                .map(entity -> {
                    StudentEnrollment studentEnrollment = new StudentEnrollment();
                    studentEnrollment.setId(entity.getId());
                    studentEnrollment.setStudentId(entity.getStudentId());
                    Student student = userService.findStudent(entity.getStudentId());
                    studentEnrollment.setStudentName(student.getFirstName() + " " + student.getLastName());
                    studentEnrollment.setCourseId(entity.getCourseId());
                    Course course = this.findCourseById(entity.getCourseId());
                    studentEnrollment.setCourseName(course.getTitle());
                    studentEnrollment.setEnrollmentDate(entity.getEnrollmentDate());
                    return studentEnrollment;
                })
                .toList();
    }

    public List<Student> findStudentsByCourse(Long courseId) {
        List<StudentEnrollmentEntity> enrollmentEntities = studentEnrollmentRepository.findAllByCourseId(courseId);
        return enrollmentEntities.stream()
                .map(entity -> {
                    Student student = userService.findStudent(entity.getStudentId());
                    return student;
                }).toList();
    }

    public void savePost(Long courseId, Post post) {
        if(post.getMessage() != null){
            postRepository.save(postMapper.toEntity(courseId).map(post));
        }
    }

    public List<Post> getPosts(Long courseId){
        List<PostEntity> posts = postRepository.findAllByCourseIdOrderById(courseId);
        return ListResultBuilder.build(posts, postMapper.toDomain());
    }
}
