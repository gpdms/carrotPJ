package com.exercise.carrotproject.web.member.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice{
/*    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }*/

    /**
     * validation 실패
     * @ModelAttribute
     */
    @ExceptionHandler({BindException.class})
    public ResponseEntity<ErrorResponse> handleBindException(BindException e,
                                                      HttpServletRequest request) {
        log.warn("fail Validation, url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return ResponseEntity.badRequest().body(errorResponse);
    }
    /**
     * validation 실패
     * @RequestBody
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleFailedValidation(MethodArgumentNotValidException e,
                                                                HttpServletRequest request) {
        log.warn("fail Validation, url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(NumberFormatException e,
                                                                                   HttpServletRequest request) {
        log.warn("MethodArgumentTypeMismatchException발생 url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.TYPE_MISMATCH);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected String handleNoHandlerFoundException(NoHandlerFoundException e,
                                                   HttpServletRequest request) {
        log.warn("NoHandlerFoundExceptionn발생 url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
        return "redirect:/";
    }

    @ExceptionHandler({NoSuchElementException.class})
    public String handleNoSuchElementException(NoSuchElementException e,
                                               HttpServletRequest request) {
        log.warn("NoSuchElementException발생 url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
        return "redirect:/";
    }

}
