package br.com.decimal.miniautorizador.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class StatusTransacaoException extends RuntimeException {
	
	private static final long serialVersionUID = 4322940650349985044L;

	public StatusTransacaoException(String message) {
        super(message);
    }

}
