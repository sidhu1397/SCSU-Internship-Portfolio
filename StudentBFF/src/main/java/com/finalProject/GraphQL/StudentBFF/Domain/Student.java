package com.finalProject.GraphQL.StudentBFF.Domain;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    private String studentId;
    private String name;
    private Major dept;
    private Degree level;
    private Date DOB;
    private String phone;
}
