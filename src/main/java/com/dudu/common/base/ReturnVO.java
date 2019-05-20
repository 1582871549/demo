package com.dudu.common.base;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class ReturnVO implements Serializable {

    private static final long serialVersionUID = 3695513906730758767L;

    @JSONField(
            ordinal = 1
    )
    private int status;
    @JSONField(
            ordinal = 2
    )
    private String returnCode;
    @JSONField(
            ordinal = 3
    )
    private String returnMsg;
    @JSONField(
            ordinal = 4
    )
    private Object data;
    @JSONField(
            ordinal = 5
    )
    private int total;

    public ReturnVO(int status, String returnCode, String returnMsg) {
        this.status = status;
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
        this.data = null;
        this.total = 0;
    }

    public ReturnVO(int status, String returnCode, String returnMsg, Object returnData) {
        this.status = status;
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
        this.data = returnData;
        this.total = 0;
    }

    public ReturnVO(int status, String returnCode, String returnMsg, Object returnData, int total) {
        this.status = status;
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
        this.data = returnData;
        this.total = total;
    }
}