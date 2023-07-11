package com.exercise.carrotproject.web.member.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
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
     * validation 에러 (@ModelAttribute)
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler({BindException.class})
    public ResponseEntity<?> handleBindException(BindException e,
                                                      HttpServletRequest request) {
        log.warn("BindException 발생 url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * validation 에러 (@RequestBody)
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleNotValidException(MethodArgumentNotValidException e,
                                                    HttpServletRequest request) {
        log.warn("MethodArgumentNotValidException 발생 url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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
