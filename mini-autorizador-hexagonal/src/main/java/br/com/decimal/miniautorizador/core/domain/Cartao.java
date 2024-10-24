package br.com.decimal.miniautorizador.core.domain;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Table(name = "cartoes")
@Getter
@Setter
public class Cartao {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private Long numeroCartao;
    private BigDecimal saldo = BigDecimal.valueOf(500.00);
    private String senha;
    
    public Cartao(Long numeroCartao, String senha) {
		super();
		this.numeroCartao = numeroCartao;
		this.senha = senha;
	}
    
}
