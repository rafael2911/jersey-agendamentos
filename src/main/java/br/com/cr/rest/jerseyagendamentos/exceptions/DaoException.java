package br.com.cr.rest.jerseyagendamentos.exceptions;

public class DaoException extends RuntimeException {

	private static final long serialVersionUID = -5298506685976215435L;
	
	private Integer code;
	
	public DaoException(String message, ErrorCode code) {
		super(message);
		this.code = code.getCode();
	}
	
	public Integer getCode() {
		return code;
	}

}
