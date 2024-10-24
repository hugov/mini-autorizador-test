package br.com.decimal.miniautorizador.core.port.inbound;

public interface ValidadorTransacao<T> {
	void validar(T valor, T referencia);
}
