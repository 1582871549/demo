package com.dudu.common.exception;

import com.dudu.common.enums.ReturnCodeEnum;
import com.dudu.common.base.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理所有不可知的异常
     *
     * @param e 异常
     * @return json
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    //服务内部错误
    String handleException(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage(), e);
        return error(ReturnCodeEnum.BASE_ERROR.getCode());
    }

    /**
     * 处理自定义异常
     *
     * @param e 异常
     * @return json
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    String handleBusinessException(BusinessException e) {
        e.printStackTrace();
        log.error(e.getMessage(), e);
        return error(e.getCode());
    }

}
