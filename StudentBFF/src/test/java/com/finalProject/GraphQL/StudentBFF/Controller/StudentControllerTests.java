package com.finalProject.GraphQL.StudentBFF.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalProject.GraphQL.StudentBFF.Domain.Major;
import com.finalProject.GraphQL.StudentBFF.Domain.Student;
import com.finalProject.GraphQL.StudentBFF.Exception.GraphQLAppException;
import com.finalProject.GraphQL.StudentBFF.Service.StudentProfileService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.*;

@GraphQlTest(value = StudentController.class)
public class StudentControllerTests {

    private String MOCK_STUDENT_ID = "8a80806f87765bbc01877662e2040001";
    @Autowired
    private GraphQlTester graphQlTester;
    @MockBean
    private StudentProfileService studentProfileService;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll()
    {
        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    @Test
    void studentProfileTest() throws GraphQLAppException, IOException{

        String responseJson = new String(Files.readAllBytes(Paths.get("src/test/resources/graphql-test/getStudentProfile.json")));
        Student expectedResponse = objectMapper.readValue(responseJson,Student.class);
        given(this.studentProfileService.getStudentProfile(MOCK_STUDENT_ID)).willReturn(expectedResponse);

        this.graphQlTester.documentName("studentProfile")
                .execute()
                .path("studentProfile")
                .entity(Student.class)
                .satisfies(response ->{
                   assertThat(response).isNotNull();
                   assertEquals(response.getStudentId(),expectedResponse.getStudentId());
                });

    }

    @Test
    void getStudentProfileExceptionTest_404() throws GraphQLAppException, IOException{

        Map<String,Object> extensions = new HashMap<>();
        extensions.put("errorCode","404 NOT_FOUND");
        extensions.put("errorDetail","Data Not found");
        List<Object> path = new ArrayList<>();
        path.add("studentProfile");
        String responseJson = new String(Files.readAllBytes(Paths.get("src/test/resources/graphql-test/getStudentProfile.json")));
        Student expectedResponse = objectMapper.readValue(responseJson,Student.class);
        given(this.studentProfileService.getStudentProfile(MOCK_STUDENT_ID)).willThrow(new GraphQLAppException((String) extensions.get("errorCode"),
                (String) extensions.get("errorDetail"), HttpStatus.NOT_FOUND));

        this.graphQlTester.documentName("studentProfile")
                .execute()
                .errors()
                .satisfy(
                        responseErrors -> {
                            assertThat(responseErrors).hasSize(1);
                            assertEquals(responseErrors.get(0).getMessage(),extensions.get("errorDetail"));
                        }
                );
    }

    @Test
    void getAllStudentsTest() throws GraphQLAppException, IOException{

        String responseJson = new String(Files.readAllBytes(Paths.get("src/test/resources/graphql-test/allStudentsProfile.json")));
        List<Student> expectedResponse = objectMapper.readValue(responseJson, new TypeReference<List<Student>>() {
        });
        given(this.studentProfileService.getStudentsList(null)).willReturn(expectedResponse);

        this.graphQlTester.documentName("allStudents")
                .execute()
                .path("allStudents")
                .entity(new ParameterizedTypeReference<List<Student>>() {
                })
                .satisfies(response ->{
                    assertThat(response).isNotNull();
                    assertThat(response).hasSize(6);
                });

    }

    @Test
    void getStudentsByDepartmentTest() throws GraphQLAppException, IOException{

        String responseJson = new String(Files.readAllBytes(Paths.get("src/test/resources/graphql-test/getStudentProfileDept.json")));
        List<Student> expectedResponse = objectMapper.readValue(responseJson, new TypeReference<List<Student>>() {
        });
        given(this.studentProfileService.getStudentsList(Major.COMPUTER_SCIENCE)).willReturn(expectedResponse);

        this.graphQlTester.documentName("getStudentProfileByDept")
                .execute()
                .path("allStudents")
                .entity(new ParameterizedTypeReference<List<Student>>() {
                })
                .satisfies(response ->{
                    assertThat(response).isNotNull();
                    assertThat(response).hasSize(4);
                });

    }

    @Test
    void updateStudentException404() throws GraphQLAppException, IOException{
        Map<String,Object> extensions = new HashMap<>();
        extensions.put("errorCode","404 NOT_FOUND");
        extensions.put("errorDetail","Data Not found");

        String responseJson = new String(Files.readAllBytes(Paths.get("src/test/resources/graphql-test/updateStudent.json")));
        Student expectedResponse = objectMapper.readValue(responseJson,Student.class);
        expectedResponse.setDOB(Date.valueOf("1997-10-11"));
        given(this.studentProfileService.updateStudentProfile(expectedResponse)).willThrow(new GraphQLAppException((String) extensions.get("errorCode"),
                (String) extensions.get("errorDetail"), HttpStatus.NOT_FOUND)
        );

        this.graphQlTester.documentName("updateStudent")
                .execute()
                .errors()
                .satisfy(
                        responseErrors -> {
                            assertThat(responseErrors).hasSize(1);
                            assertEquals(responseErrors.get(0).getMessage(),extensions.get("errorDetail"));
                        }
                );

    }
}
