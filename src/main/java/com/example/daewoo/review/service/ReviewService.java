package com.example.daewoo.review.service;

import com.example.daewoo.review.dto.ReviewEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository repository;

    public void insert(ReviewEntity entity){
        this.repository.save(entity);
    }

    public List<ReviewEntity> findAll(){
        return this.repository.findAll();
    }

    public Optional<ReviewEntity> findById(Long id){
        return this.repository.findById(id);
    }

    public void update(ReviewEntity entity){
        this.repository.save(entity);
    }

    public void delete(Long id){
        this.repository.deleteById(id);
    }
}
