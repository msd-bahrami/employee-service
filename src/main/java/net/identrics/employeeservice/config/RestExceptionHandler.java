package net.identrics.employeeservice.config;

import org.springframework.data.rest.webmvc.RepositoryRestExceptionHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice(basePackageClasses = {RepositoryRestExceptionHandler.class}, annotations = {RestController.class})
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        List<String> violations = bindingResult.getFieldErrors().stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .toList();
        return generateErrorMessage(violations);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Object> handleMethodArgumentNotValid(ConstraintViolationException e) {
        List<String> violations = e.getConstraintViolations().stream()
                .map(v -> String.format("%s: %s", v.getPropertyPath(), v.getMessage()))
                .toList();
        return generateErrorMessage(violations);
    }

    private ResponseEntity<Object> generateErrorMessage(List<String> violations) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", violations);
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }
}
