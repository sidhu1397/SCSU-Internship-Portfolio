package com.student.studentprofile.Controller;

import com.student.studentprofile.Domain.Major;
import com.student.studentprofile.Domain.Student;
import com.student.studentprofile.Exception.StudentProfileException;
import com.student.studentprofile.Repository.StudentProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/studentResourceServer")
@Slf4j
public class StudentProfileController {

    @Autowired
    StudentProfileRepository studentProfileRepository;

    public static final String ID = "id";
    public static final String DEPT = "dept";
    public static final String DEPT_DEFAULT = null;

    @GetMapping("/student/{id}")
    public ResponseEntity<Student> getStudentDetails(@PathVariable(ID) @NotBlank String studentId) throws StudentProfileException {

        Optional<Student> studentdata = studentProfileRepository.findById(studentId);
        if (studentdata.isPresent()) {
            return new ResponseEntity<>(studentdata.get(), HttpStatus.OK);
        } else {
            throw new StudentProfileException(HttpStatus.NOT_FOUND.toString(), "Data Not found",HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/student/list")
    public ResponseEntity<List<Student>> getStudentList(@RequestParam(required = false) Major dept) throws StudentProfileException {
        List<Student> studentList = new ArrayList<>();

            if (dept == null) {
                studentProfileRepository.findAll().forEach(studentList::add);
            } else {
                studentProfileRepository.findByDept(dept).forEach(studentList::add);
            }
            return new ResponseEntity<>(studentList, HttpStatus.OK);

    }

    @PostMapping("/student")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) throws StudentProfileException {

            Student tempStudent = new Student();
            tempStudent.setName(student.getName());
            tempStudent.setDept(student.getDept());
            tempStudent.setPhone(student.getPhone());
            tempStudent.setLevel(student.getLevel());
            tempStudent.setDOB(student.getDOB());
            Student response = studentProfileRepository.save(tempStudent);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }


    @PostMapping("/student/{id}")
    public ResponseEntity<Student> updateStudentProfile(@PathVariable(ID) @NotBlank String studentId, @RequestBody Student student) throws StudentProfileException {

            Optional<Student> studentdata = studentProfileRepository.findById(studentId);
            if (studentdata.isPresent()) {
                Student tempStudent = studentdata.get();
                tempStudent.setStudentId(student.getStudentId());
                tempStudent.setName(student.getName());
                tempStudent.setDept(student.getDept());
                tempStudent.setPhone(student.getPhone());
                tempStudent.setLevel(student.getLevel());
                tempStudent.setDOB(student.getDOB());
                return new ResponseEntity<>(studentProfileRepository.save(tempStudent), HttpStatus.OK);
            } else {
                throw new StudentProfileException(HttpStatus.NOT_FOUND.toString(), "Student Data Not found for update",HttpStatus.NOT_FOUND);
            }

    }


}
