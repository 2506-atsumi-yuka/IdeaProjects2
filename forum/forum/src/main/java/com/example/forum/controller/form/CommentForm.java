package com.example.forum.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CommentForm {
    private int id;
    private String comment;
    private int contentId;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}
