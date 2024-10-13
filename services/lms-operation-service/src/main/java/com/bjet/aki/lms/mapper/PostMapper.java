package com.bjet.aki.lms.mapper;

import com.bjet.aki.lms.asset.ResultMapper;
import com.bjet.aki.lms.jpa.PostEntity;
import com.bjet.aki.lms.model.Post;
import com.bjet.aki.lms.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class PostMapper {

    private final PostRepository postRepository;

    public ResultMapper<Post, PostEntity> toEntity(Long courseId){
        return domain -> postRepository.findById(domain.getId())
                .orElseGet(PostEntity::new)
                .setCourseId(courseId)
                .setMessage(domain.getMessage())
                .setPostedBy(domain.getPostedBy())
                .setPostedOn(LocalDateTime.now());
    }

    public ResultMapper<PostEntity, Post> toDomain(){
        return entity -> new Post()
                .setId(entity.getId())
                .setMessage(entity.getMessage())
                .setPostedBy(entity.getPostedBy())
                .setPostedOn(entity.getPostedOn());
    }
}
