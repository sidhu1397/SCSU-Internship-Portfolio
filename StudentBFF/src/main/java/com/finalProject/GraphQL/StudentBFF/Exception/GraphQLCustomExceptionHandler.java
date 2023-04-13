package com.finalProject.GraphQL.StudentBFF.Exception;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class GraphQLCustomExceptionHandler extends DataFetcherExceptionResolverAdapter {
    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {

        if(ex instanceof GraphQLAppException)
        {
            Map<String,Object> extensions = new HashMap<>();
            List<Object> path = new ArrayList<>();

            try {
                //Set GraphQL Path in the error response
                path.add(env.getFieldDefinition().getName());
                //Set ErrorCode form downstream call
                extensions.put("errorCode", ((GraphQLAppException) ex).getErrorCode());
                //Set ErrorDetail form downstream call
                extensions.put("errorDetail", ((GraphQLAppException) ex).getErrorDetail());
                String detail = ((GraphQLAppException) ex).getErrorDetail();
                return new CustomGraphQLException(detail,extensions,path);
            }catch (Exception e){
                log.error("Error happened while Processing Exception - {}",e.getMessage());
                return super.resolveToSingleError(ex,env);
            }
        }
        else {
            return super.resolveToSingleError(ex, env);
        }
    }
}
