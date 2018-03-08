package com.yglbs.common;

/**
 * 错误信息处理
 * @author yeelxd
 * @date 2018-03-02
 */
public class MessageException extends Exception {

	private static final long serialVersionUID = -3126656932486860163L;

	private int errCode;
	
	private String errMsg;

	public MessageException() {  
    }  

    public MessageException(Throwable cause) {  
        super(cause); 
        this.errMsg = cause.getMessage();
    }

    public MessageException(int errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public MessageException(String errMsg, Throwable cause) {  
        super(errMsg, cause);  
        this.errMsg = errMsg+"["+cause.getMessage()+"]";
    }

    public MessageException(int errCode, String errMsg, Throwable cause) {
        super(errMsg, cause);
        this.errCode = errCode;
        this.errMsg = errMsg+"["+cause.getMessage()+"]";
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	@Override
    public String toString() {
        return "MessageException{" +
                "errCode=" + errCode +
                ", errMsg='" + errMsg + '\'' +
                '}';
    }
}
