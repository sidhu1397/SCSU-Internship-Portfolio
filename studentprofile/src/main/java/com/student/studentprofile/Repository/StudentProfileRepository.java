package com.student.studentprofile.Repository;

import com.student.studentprofile.Domain.Major;
import com.student.studentprofile.Domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentProfileRepository extends JpaRepository<Student,String> {

    List<Student> findByDept(Major dept);
}
