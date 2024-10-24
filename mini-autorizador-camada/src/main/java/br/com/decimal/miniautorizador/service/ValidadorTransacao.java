package br.com.decimal.miniautorizador.service;

public interface ValidadorTransacao<T> {
	void validar(T valor, T referencia);
}
