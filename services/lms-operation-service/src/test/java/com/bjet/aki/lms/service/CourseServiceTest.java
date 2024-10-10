package com.bjet.aki.lms.service;

import com.bjet.aki.lms.jpa.*;
import com.bjet.aki.lms.model.TimeSlot;
import com.bjet.aki.lms.repository.CourseScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    CourseScheduleRepository courseScheduleRepository;

    @InjectMocks
    CourseService courseService;

    CourseEntity courseEntity;

    @BeforeEach
    void before(){
        courseEntity = new CourseEntity().setId(1L).setTitle("Course 1").setDescription("Course 1").setStartDate(LocalDate.of(2024, 12, 1));
        List<ModuleEntity> moduleEntityList = new ArrayList<>();
        moduleEntityList.add(new ModuleEntity().setId(1L).setCourse(courseEntity).setTitle("Module 1").setDescription("Module 1"));
        moduleEntityList.add(new ModuleEntity().setId(2L).setCourse(courseEntity).setTitle("Module 2").setDescription("Module 2"));
        moduleEntityList.add(new ModuleEntity().setId(3L).setCourse(courseEntity).setTitle("Module 3").setDescription("Module 3"));
        moduleEntityList.add(new ModuleEntity().setId(4L).setCourse(courseEntity).setTitle("Module 4").setDescription("Module 4"));
        moduleEntityList.add(new ModuleEntity().setId(5L).setCourse(courseEntity).setTitle("Module 5").setDescription("Module 5"));
        moduleEntityList.add(new ModuleEntity().setId(6L).setCourse(courseEntity).setTitle("Module 6").setDescription("Module 6"));
        moduleEntityList.add(new ModuleEntity().setId(7L).setCourse(courseEntity).setTitle("Module 7").setDescription("Module 7"));
        moduleEntityList.add(new ModuleEntity().setId(8L).setCourse(courseEntity).setTitle("Module 8").setDescription("Module 8"));
        moduleEntityList.add(new ModuleEntity().setId(9L).setCourse(courseEntity).setTitle("Module 9").setDescription("Module 9"));
        moduleEntityList.add(new ModuleEntity().setId(10L).setCourse(courseEntity).setTitle("Module 10").setDescription("Module 10"));
        courseEntity.setModules(moduleEntityList);

        List<CourseScheduleEntity> courseScheduleEntities = new ArrayList<>();
        courseScheduleEntities.add(new CourseScheduleEntity().setId(1L).setCourse(courseEntity).setDays(DayOfWeek.SUNDAY).setTimeSlot(TimeSlot.AM_9));
        courseScheduleEntities.add(new CourseScheduleEntity().setId(2L).setCourse(courseEntity).setDays(DayOfWeek.TUESDAY).setTimeSlot(TimeSlot.AM_11));
        courseScheduleEntities.add(new CourseScheduleEntity().setId(3L).setCourse(courseEntity).setDays(DayOfWeek.THURSDAY).setTimeSlot(TimeSlot.AM_9));

        when(courseScheduleRepository.findAllByCourse_Id(anyLong())).thenReturn(courseScheduleEntities);
    }

    @Test
    void checkClassSchedule() {
        List<ClassScheduleEntity> classSchedule = courseService.generateClassSchedule(courseEntity);
        assertThat(classSchedule.size()).isEqualTo(10);
        assertThat(classSchedule.get(0).getClassDateTime()).isEqualTo(LocalDateTime.of(2024, 12, 1, 9, 0));
        assertThat(classSchedule.get(0).getTimeSlot()).isEqualTo(TimeSlot.AM_9);
        assertThat(classSchedule.get(4).getClassDateTime()).isEqualTo(LocalDateTime.of(2024, 12, 10, 11, 0));
        assertThat(classSchedule.get(4).getTimeSlot()).isEqualTo(TimeSlot.AM_11);
        assertThat(classSchedule.get(9).getClassDateTime()).isEqualTo(LocalDateTime.of(2024, 12, 22, 9, 0));
        assertThat(classSchedule.get(9).getTimeSlot()).isEqualTo(TimeSlot.AM_9);
    }
}
