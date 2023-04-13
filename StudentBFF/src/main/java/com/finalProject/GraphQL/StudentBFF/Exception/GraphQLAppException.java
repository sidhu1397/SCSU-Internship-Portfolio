package com.finalProject.GraphQL.StudentBFF.Exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class GraphQLAppException extends Exception {
    public String errorCode;
    public String errorDetail;
    public HttpStatus statusCode;

    public GraphQLAppException(String errorCode,String errorDetail,HttpStatus statusCode)
    {
        super(errorDetail);
        this.errorCode = errorCode;
        this.errorDetail = errorDetail;
        this.statusCode = statusCode;
    }
}
