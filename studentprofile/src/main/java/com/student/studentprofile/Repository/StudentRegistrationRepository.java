package com.student.studentprofile.Repository;

import com.student.studentprofile.Domain.StudentRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRegistrationRepository extends JpaRepository<StudentRegistration, String> {

    List<StudentRegistration> findBystudentId(String studentId);
}
