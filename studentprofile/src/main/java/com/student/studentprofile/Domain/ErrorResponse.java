package com.student.studentprofile.Domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponse {

    public String errorCode;
    public String errorDetail;
    
}
