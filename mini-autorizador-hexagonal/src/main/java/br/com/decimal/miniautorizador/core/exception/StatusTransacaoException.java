package br.com.decimal.miniautorizador.core.exception;

public class StatusTransacaoException extends RuntimeException {
	
	private static final long serialVersionUID = 4322940650349985044L;

	public StatusTransacaoException(String message) {
        super(message);
    }

}
