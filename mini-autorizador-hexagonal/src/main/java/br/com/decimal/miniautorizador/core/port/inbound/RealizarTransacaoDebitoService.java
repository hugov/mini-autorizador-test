package br.com.decimal.miniautorizador.core.port.inbound;

import java.math.BigDecimal;

public interface RealizarTransacaoDebitoService {
	
	void autorizarTransacao(String numeroCartao, String senha, BigDecimal valor);
    
}