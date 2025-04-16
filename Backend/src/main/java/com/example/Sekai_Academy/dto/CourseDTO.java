package com.example.Sekai_Academy.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDTO {

    private Long id;
    private String title;
    private String description;
    private String imgUrl;
    private BigDecimal price;
    private String duration;

    private List<ModuleDTO> modules;

    @JsonIgnore
    private List<EnrollmentDTO> enrollments;
}
