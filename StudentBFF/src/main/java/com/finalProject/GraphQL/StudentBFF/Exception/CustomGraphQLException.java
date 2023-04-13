package com.finalProject.GraphQL.StudentBFF.Exception;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomGraphQLException extends RuntimeException implements GraphQLError {

    private Map<String,Object> parameters = new HashMap<>();
    List<Object> path = new ArrayList<>();
    public CustomGraphQLException(String message)
    {
        super(message);
    }
    public CustomGraphQLException(String message,Map<String, Object> additionalParams,List<Object> additionPath)
    {
        this(message);
        if(additionalParams!=null)
        {parameters = additionalParams;}
        if(additionPath != null)
        {path = additionPath;}
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorClassification getErrorType() {
        return ErrorType.DataFetchingException;
    }

    @Override
    public List<Object> getPath() {
        return this.path ;
    }

    @Override
    public Map<String, Object> getExtensions() {
        return this.parameters;
    }
}
