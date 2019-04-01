package br.com.cr.rest.jerseyagendamentos.exceptions;

public enum ErrorCode {
	BAD_REQUEST(400), UNAUTHORIZED(401),  FORBIDDEN(403), NOT_FOUND(404), CONFLICT(409), SERVER_ERROR(500);
	
	private Integer code;
	
	private ErrorCode(Integer code) {
		this.code = code;
	}
	
	public Integer getCode() {
		return code;
	}
}
