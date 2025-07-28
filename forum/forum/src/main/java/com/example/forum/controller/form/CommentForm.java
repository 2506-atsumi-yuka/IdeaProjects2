package com.example.forum.controller.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CommentForm {
    private int id;
    @NotBlank
    private String comment;
    private int contentId;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}
