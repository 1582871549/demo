package com.dudu.common.exception;

import com.dudu.common.enums.ReturnCodeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 全局异常处理类
 * 处理所有业务异常
 */
@Getter
@Setter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 610381362157914886L;

    private String code;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(ReturnCodeEnum codeEnum) {
        super(codeEnum.getName());
        this.code = codeEnum.getCode();
    }

    public BusinessException(ReturnCodeEnum codeEnum, Throwable cause) {
        super(codeEnum.getName(), cause);
        this.code = codeEnum.getCode();
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }
}
