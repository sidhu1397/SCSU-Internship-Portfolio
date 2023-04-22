package com.finalProject.GraphQL.StudentBFF.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalProject.GraphQL.StudentBFF.Domain.ErrorResponse;
import com.finalProject.GraphQL.StudentBFF.Domain.Student;
import com.finalProject.GraphQL.StudentBFF.Domain.StudentRegistration;
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
import java.util.List;

@Service
@Slf4j
public class StudentRegistrationService {
    @Autowired
    public WebClient webClient;
    public String registerStudentURL = "http://localhost:8085/studentResourceServer/studentRegistration/{studentId}";


    public StudentRegistration registerCourse(StudentRegistration studentRegistration) throws GraphQLAppException {
        StudentRegistration response = null;
        try {
            response = webClient.method(HttpMethod.POST)
                    .uri(registerStudentURL,studentRegistration.getStudentId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(studentRegistration)
                    .retrieve()
                    .onStatus(HttpStatus::isError, errorResponse ->{
                        Mono<String> errorMsg = errorResponse.bodyToMono(String.class);
                        return errorMsg.flatMap(msg->Mono.error(new GraphQLAppException("d400",msg,HttpStatus.INTERNAL_SERVER_ERROR)));
                    })
                    .bodyToMono(StudentRegistration.class)
                    .block();

        }catch (Exception e)
        {
            log.error("Exception in Course Registration ",e);
            String msg = e.getMessage().substring(e.getMessage().indexOf("{"));
            ErrorResponse errorResponse = parseExceptionMessage(msg);
            throw new GraphQLAppException(errorResponse.getErrorCode(), errorResponse.getErrorDetail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public List<StudentRegistration> getStudentRegistrations(String studentId) throws GraphQLAppException {

        List<StudentRegistration> studentRegistrationList = new ArrayList<>();
        try {
            studentRegistrationList = webClient.method(HttpMethod.GET)
                    .uri(registerStudentURL,studentId)
                    .retrieve()
                    .onStatus(HttpStatus::isError,errorResponse ->{
                        Mono<String> errorMsg = errorResponse.bodyToMono(String.class);
                        return errorMsg.flatMap(msg->Mono.error(new GraphQLAppException("d400",msg,HttpStatus.INTERNAL_SERVER_ERROR)));
                    })
                    .bodyToMono(new ParameterizedTypeReference<List<StudentRegistration>>() {
                    })
                    .block();
            return studentRegistrationList;
        }catch (Exception e)
        {
            log.error("Exception Inside getStudentRegistrationList",e);
            String msg = e.getMessage().substring(e.getMessage().indexOf("{"));
            ErrorResponse errorResponse = parseExceptionMessage(msg);
            throw new GraphQLAppException(errorResponse.getErrorCode(), errorResponse.getErrorDetail(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
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
