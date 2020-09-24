package br.com.servtec.domain.exception;

public class NegocioException extends RuntimeException{

	private static final long serialVersionUID = 1251123229814207074L;
	
	public NegocioException(String message) {
		super(message);
	}

}
