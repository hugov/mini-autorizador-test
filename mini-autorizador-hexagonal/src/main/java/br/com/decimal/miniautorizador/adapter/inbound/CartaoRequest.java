package br.com.decimal.miniautorizador.adapter.inbound;

import lombok.Data;

@Data
public class CartaoRequest {
	
	private String numeroCartao;
	private String senha;

}
