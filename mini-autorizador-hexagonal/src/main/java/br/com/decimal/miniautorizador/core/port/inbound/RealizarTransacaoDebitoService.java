package br.com.decimal.miniautorizador.core.port.inbound;

import java.math.BigDecimal;

public interface RealizarTransacaoDebitoService {
	
	String autorizarTransacao(String numeroCartao, String senha, BigDecimal valor);
    
}