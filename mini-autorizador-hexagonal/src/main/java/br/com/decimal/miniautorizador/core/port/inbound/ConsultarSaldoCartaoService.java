package br.com.decimal.miniautorizador.core.port.inbound;

import java.math.BigDecimal;

public interface ConsultarSaldoCartaoService {
	
	BigDecimal consultarSaldoCartao(Long numeroCartao);
    
}