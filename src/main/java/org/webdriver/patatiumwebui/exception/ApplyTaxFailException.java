package org.webdriver.patatiumwebui.exception;

/**
 * 部分税种报税失败异常包装类
 * @author hongjinqiu 2018.12.31
 */
public class ApplyTaxFailException extends RuntimeException {
	private static final long serialVersionUID = -6448531730347863278L;

	public ApplyTaxFailException() {
		super();
	}

	public ApplyTaxFailException(String message) {
		super(message);
	}

	public ApplyTaxFailException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplyTaxFailException(Throwable cause) {
		super(cause);
	}
}
