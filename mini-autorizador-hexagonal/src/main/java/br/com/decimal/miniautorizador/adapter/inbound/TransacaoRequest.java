package br.com.decimal.miniautorizador.adapter.inbound;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransacaoRequest {
	
	private String numeroCartao;
	private String senhaCartao;
	private BigDecimal valor;

}
