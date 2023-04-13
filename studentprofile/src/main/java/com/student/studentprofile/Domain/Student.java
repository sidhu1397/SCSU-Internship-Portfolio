package com.student.studentprofile.Domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Table(name = "studentprofile")
@Getter
@Setter
@RequiredArgsConstructor
public class Student {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String studentId;
    private String name;
    @Enumerated(EnumType.STRING)
    private Major dept;
    @Enumerated(EnumType.STRING)
    private Degree level;
    private Date DOB;
    private String phone;
}
