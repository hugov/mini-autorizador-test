package br.com.decimal.miniautorizador.entity;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Transacao {
	
	private Long numeroCartao;
	private String senhaCartao;
	private BigDecimal valor;
	
}
