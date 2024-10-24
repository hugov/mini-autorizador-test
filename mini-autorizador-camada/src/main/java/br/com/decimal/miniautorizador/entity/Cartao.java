package br.com.decimal.miniautorizador.entity;

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
@Table(name = "cartoes")
@NoArgsConstructor
@Getter
@Setter
public class Cartao {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long numeroCartao;
    private BigDecimal saldo;
    private String senha;
	
    public Cartao(Long numeroCartao, BigDecimal saldo, String senha) {
		super();
		this.numeroCartao = numeroCartao;
		this.saldo = saldo;
		this.senha = senha;
	}
    
}
