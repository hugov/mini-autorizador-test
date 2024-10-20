package br.com.decimal.miniautorizador.core.domain;

public class SaldoInsuficienteException extends RuntimeException {
	
	private static final long serialVersionUID = -8589566573361391558L;

	public SaldoInsuficienteException() {
        super("SALDO_INSUFICIENTE");
    }

}
