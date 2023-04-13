package com.finalProject.GraphQL.StudentBFF.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalProject.GraphQL.StudentBFF.Domain.ErrorResponse;
import com.finalProject.GraphQL.StudentBFF.Domain.Major;
import com.finalProject.GraphQL.StudentBFF.Domain.Student;
import com.finalProject.GraphQL.StudentBFF.Exception.GraphQLAppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StudentProfileService {

    @Autowired
    public WebClient webClient;
    public String StudentProfileURL = "http://localhost:8085/studentResourceServer/student/{studentId}";

    public String GetStudentsListURL = "http://localhost:8085/studentResourceServer/student/list?dept={dept}";
    public String AddStudentURL= "http://localhost:8085/studentResourceServer/student";

    public Student getStudentProfile(String studentId) throws GraphQLAppException {
        Student response = null;

        try {
            response = webClient.method(HttpMethod.GET)
                    .uri(StudentProfileURL, studentId)
                    .retrieve()
                    .onStatus(HttpStatus::isError, errorResponse -> {
                        Mono<String> errorMsg = errorResponse.bodyToMono(String.class);
                        return errorMsg.flatMap(msg -> Mono.error(new GraphQLAppException("d400", msg, HttpStatus.INTERNAL_SERVER_ERROR)));
                    })
                    .bodyToMono(Student.class)
                    .block();
            return response;
        }catch (Exception e)
        {
            log.error("Exeception Inside getStudentProfile by id",e);
            String msg = e.getMessage().substring(e.getMessage().indexOf("{"));
            ErrorResponse errorResponse = parseExceptionMessage(msg);
            throw new GraphQLAppException(errorResponse.getErrorCode(), errorResponse.getErrorDetail(), HttpStatus.INTERNAL_SERVER_ERROR);


        }
    }

    public List<Student> getStudentsList(Major dept) throws GraphQLAppException {
        Map<String,Object> params = new HashMap<>();
        if (dept != null)
        {
            params.put("dept",dept);
        }

        List<Student> studentList = new ArrayList<>();
        try {
            studentList = webClient.method(HttpMethod.GET)
                    .uri(GetStudentsListURL, dept)
                    .retrieve()
                    .onStatus(HttpStatus::isError,errorResponse ->{
                        Mono<String> errorMsg = errorResponse.bodyToMono(String.class);
                        return errorMsg.flatMap(msg->Mono.error(new GraphQLAppException("d400",msg,HttpStatus.INTERNAL_SERVER_ERROR)));
                    })
                    .bodyToMono(new ParameterizedTypeReference<List<Student>>() {
                    })
                    .block();
            return studentList;
        }catch (Exception e)
        {
            log.error("Exception Inside getStudentList",e);
            String msg = e.getMessage().substring(e.getMessage().indexOf("{"));
            ErrorResponse errorResponse = parseExceptionMessage(msg);
            throw new GraphQLAppException(errorResponse.getErrorCode(), errorResponse.getErrorDetail(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public Student addStudentProfile(Student student) throws GraphQLAppException {
        Student response = null;
        try {
            response = webClient.method(HttpMethod.POST)
                    .uri(AddStudentURL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(student)
                    .retrieve()
                    .onStatus(HttpStatus::isError,errorResponse ->{
                        Mono<String> errorMsg = errorResponse.bodyToMono(String.class);
                        return errorMsg.flatMap(msg->Mono.error(new GraphQLAppException("d400",msg,HttpStatus.INTERNAL_SERVER_ERROR)));
                    })
                    .bodyToMono(Student.class)
                    .block();

        }catch (Exception e)
        {
            log.error("Exception in Add StudentProfile ",e);
            String msg = e.getMessage().substring(e.getMessage().indexOf("{"));
            ErrorResponse errorResponse = parseExceptionMessage(msg);
            throw new GraphQLAppException(errorResponse.getErrorCode(), errorResponse.getErrorDetail(), HttpStatus.INTERNAL_SERVER_ERROR);


        }
        return response;
    }

    public Student updateStudentProfile(Student student) throws GraphQLAppException {
        Student response = null;
        try {
            response = webClient.method(HttpMethod.POST)
                    .uri(StudentProfileURL,student.getStudentId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(student)
                    .retrieve()
                    .onStatus(HttpStatus::isError,errorResponse ->{
                        Mono<String> errorMsg = errorResponse.bodyToMono(String.class);
                        return errorMsg.flatMap(msg->Mono.error(new GraphQLAppException("d400",msg,HttpStatus.INTERNAL_SERVER_ERROR)));
                    })
                    .bodyToMono(Student.class)
                    .block();

        }catch (Exception e)
        {
            log.error("Exception in Update StudentProfile ",e);
            String msg = e.getMessage().substring(e.getMessage().indexOf("{"));
            ErrorResponse errorResponse = parseExceptionMessage(msg);
            throw new GraphQLAppException(errorResponse.getErrorCode(), errorResponse.getErrorDetail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public ErrorResponse parseExceptionMessage(String msg) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ErrorResponse errorResponse = objectMapper.readValue(msg, ErrorResponse.class);
            return errorResponse;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
