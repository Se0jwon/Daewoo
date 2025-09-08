package com.example.daewoo.review;

import lombok.Data;

@Data
public class ReviewDto {
    private long id;
    private long userId;
    private int userId2;
    private String title;
    private String content;
    private int score;
}