package com.exercise.carrotproject.web.member.error;

import com.exercise.carrotproject.web.member.MemberController;
import com.exercise.carrotproject.web.member.error.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice(assignableTypes = {MemberController.class})
public class MemberErrorController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> bindingExceptionHandle(MethodArgumentNotValidException e,
                                                              HttpServletRequest request) {
        log.warn("MethodArgumentNotValidException 발생!!! url:{}, trace:{}",request.getRequestURI(), e.getStackTrace());
        ErrorResult errorResult = makeErrorResponse(e.getBindingResult());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    private ErrorResult makeErrorResponse(BindingResult bindingResult) {
        String code = "";
        String description = "";
        String detail = "";
        //에러가 있다면
        if (bindingResult.hasErrors()) {
            //form에서 설정한 meaasge값을 가져온다
            detail = bindingResult.getFieldError().getDefaultMessage();
            //form에서 유효성체크를 걸어놓은 어노테이션명을 가져온다.
            String bindResultCode = bindingResult.getFieldError().getCode();
            switch (bindResultCode) {
                case "NotNull":
                    code = ErrorCode.NOT_NULL.getCode();
                    description = ErrorCode.NOT_NULL.getDescription();
                    break;
                case "NotBlank":
                    code = ErrorCode.NOT_BLANK.getCode();
                    description = ErrorCode.NOT_BLANK.getDescription();
                    break;
                case "Size":
                    code = ErrorCode.SIZE.getCode();
                    description = ErrorCode.SIZE.getDescription();
                    break;
            }
        }
        return new ErrorResult(code, description, detail);
    }
}
