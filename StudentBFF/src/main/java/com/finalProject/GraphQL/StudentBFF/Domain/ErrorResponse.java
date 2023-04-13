package com.finalProject.GraphQL.StudentBFF.Domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponse {

    private String errorCode;
    private String errorDetail;

}
