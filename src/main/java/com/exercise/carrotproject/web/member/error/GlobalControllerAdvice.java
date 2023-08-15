package com.exercise.carrotproject.web.member.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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
     * @ModelAttribute : validation 실패, 타입 불일치(캐스팅 불가)로 binding 실패
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e,
                                                      HttpServletRequest request) {
        log.warn("fail Validation, url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * @RequestBody : validation 실패
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleFailedValidation(MethodArgumentNotValidException e,
                                                                HttpServletRequest request) {
        log.warn("fail Validation, url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * @RequestBody : json형식 아닐 때, 타입 불일치(캐스팅 불가)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException (HttpMessageNotReadableException e,
                                                                                HttpServletRequest request) {
        log.warn("HttpMessageNotReadableException 발생, url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.TYPE_MISMATCH);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     *  누락된 필수 파라미터나 경로 변수, 요청 파라미터나 경로 변수로 넘어온 값이 타입 불일치
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,
                                                                                   HttpServletRequest request) {
        log.warn("MethodArgumentTypeMismatchException발생 url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.TYPE_MISMATCH);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e,
                                                                                         HttpServletRequest request) {
        log.warn("HttpRequestMethodNotSupportedException발생 url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
        final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
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
