package com.finalProject.GraphQL.StudentBFF.Security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.finalProject.GraphQL.StudentBFF.Security.AuthConstants.STUDENTBFF_APPROLE;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("(hasAnyAuthority('"+ STUDENTBFF_APPROLE+"'))")
public @interface HasWriteAccess {
}
