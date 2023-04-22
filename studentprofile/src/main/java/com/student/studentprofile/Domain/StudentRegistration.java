package com.student.studentprofile.Domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "studentRegistration")
@Getter
@Setter
@RequiredArgsConstructor
public class StudentRegistration {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String registrationId;
    private String studentId;
    private String CourseId;
    private String CourseName;
    @Enumerated(EnumType.STRING)
    private Major dept;

}
