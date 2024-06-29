package com.ilyap.commonloggingstarter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code @Logged} is used to mark type or method that should be logged for execution information.
 * When applied at the class level, all methods within the class will be considered for logging.
 * When applied at the method level, only the annotated method will be logged.
 *
 * <p>Usage:
 * <pre>
 *  // Apply at the class level to log all methods
 * {@literal @}Logged
 *  public class TaskServiceImpl implements TaskService {
 *      ...
 *  }
 *
 *  // Apply at the method level to log a concrete method
 * {@literal @}Logged
 *  public Page<TaskReadDto> findAllByUsername(String username, Pageable pageable) {
 *      ...
 *  }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Logged {
}