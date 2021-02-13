package org.webdriver.patatiumwebui.exception;

/**
 * RPA异常包装类
 * @author hongjinqiu 2018.10.17
 */
public class RPAException extends RuntimeException {
	private static final long serialVersionUID = -6448531730347863278L;

	public RPAException() {
		super();
	}

	public RPAException(String message) {
		super(message);
	}

	public RPAException(String message, Throwable cause) {
		super(message, cause);
	}

	public RPAException(Throwable cause) {
		super(cause);
	}
}
