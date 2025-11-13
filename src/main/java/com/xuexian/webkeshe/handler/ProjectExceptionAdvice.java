package com.xuexian.webkeshe.handler;

import com.xuexian.webkeshe.util.Code;
import com.xuexian.webkeshe.exception.BusinessException;
import com.xuexian.webkeshe.exception.SystemException;
import com.xuexian.webkeshe.vo.Result;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Hidden
@RestControllerAdvice
public class ProjectExceptionAdvice {
    @ExceptionHandler(SystemException.class)
    public Result doSystemException(SystemException ex) {
        return new Result(ex.getCode(), ex.getMessage(),null);
    }

    @ExceptionHandler(BusinessException.class)
    public Result doBusinessException(BusinessException ex) {
        return new Result(ex.getCode(),ex.getMessage(),null);
    }

    @ExceptionHandler(Exception.class)
    public Result doException(Exception ex) {
        return new Result(Code.SYSTEM_UNKNOWN_ERR, ex.getMessage(),null);
    }
}
