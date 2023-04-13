package com.finalProject.GraphQL.StudentBFF.Controller;

import com.finalProject.GraphQL.StudentBFF.Domain.Degree;
import com.finalProject.GraphQL.StudentBFF.Domain.Major;
import com.finalProject.GraphQL.StudentBFF.Domain.Student;
import com.finalProject.GraphQL.StudentBFF.Exception.GraphQLAppException;
import com.finalProject.GraphQL.StudentBFF.Security.HasReadAccess;
import com.finalProject.GraphQL.StudentBFF.Security.HasWriteAccess;
import com.finalProject.GraphQL.StudentBFF.Service.StudentProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.sql.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudentController {

    private final StudentProfileService studentProfileService;
    @QueryMapping
    @HasReadAccess
    public Student studentProfile(@Argument String studentId) throws GraphQLAppException {
        return studentProfileService.getStudentProfile(studentId);
    }

    @QueryMapping
    @HasReadAccess
    public List<Student> allStudents(@Argument Major dept) throws GraphQLAppException {
        return studentProfileService.getStudentsList(dept);
    }

    @MutationMapping
    @HasWriteAccess
    public Student addStudent(@Argument   String studentId,@Argument String name,@Argument Major dept,@Argument Degree level,@Argument Date DOB,@Argument String phone) throws GraphQLAppException {
        Student student = new Student();
        student.setName(name);
        student.setDept(dept);
        student.setLevel(level);
        student.setDOB(DOB);
        student.setPhone(phone);
        return studentProfileService.addStudentProfile(student);
    }

    @MutationMapping
    @HasWriteAccess
    public Student updateStudent(@Argument   String studentId,@Argument String name,@Argument Major dept,@Argument Degree level,@Argument Date DOB,@Argument String phone) throws GraphQLAppException {
        Student student = new Student();
        student.setStudentId(studentId);
        student.setName(name);
        student.setDept(dept);
        student.setLevel(level);
        student.setDOB(DOB);
        student.setPhone(phone);
        return studentProfileService.updateStudentProfile(student);

    }


}
