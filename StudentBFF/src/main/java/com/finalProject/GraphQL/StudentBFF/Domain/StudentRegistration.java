package com.finalProject.GraphQL.StudentBFF.Domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class StudentRegistration {

    private String studentId;
    private String CourseId;
    private String CourseName;
    private Major dept;

}
