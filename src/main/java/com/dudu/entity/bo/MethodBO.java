package com.dudu.entity.bo;

/**
 * @author mengli
 * @create 2019/12/2
 * @since 1.0.0
 */
public class MethodBO {

    private int methodBegin;
    private int methodEnd;
    private String methodName;

    public MethodBO(int methodBegin, int methodEnd, String methodName) {
        this.methodBegin = methodBegin;
        this.methodEnd = methodEnd;
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return "MethodBO{" +
                "methodBegin=" + methodBegin +
                ", methodEnd=" + methodEnd +
                ", methodName='" + methodName + '\'' +
                '}';
    }

    public int getMethodBegin() {
        return methodBegin;
    }

    public void setMethodBegin(int methodBegin) {
        this.methodBegin = methodBegin;
    }

    public int getMethodEnd() {
        return methodEnd;
    }

    public void setMethodEnd(int methodEnd) {
        this.methodEnd = methodEnd;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
