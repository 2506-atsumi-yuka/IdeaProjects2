package com.example.forum.controller.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ReportForm {
    private int id;
    @NotBlank
    private String content;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}
