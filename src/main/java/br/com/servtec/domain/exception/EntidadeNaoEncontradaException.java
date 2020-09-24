package br.com.servtec.domain.exception;

public class EntidadeNaoEncontradaException extends NegocioException {

    private static final long serialVersionUID = -6560331123312883198L;

    public EntidadeNaoEncontradaException(String message) {
	super(message);
    }
}
