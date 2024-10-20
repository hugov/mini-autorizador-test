package br.com.decimal.miniautorizador.core.domain;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "cartoes")
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
    
    public void debito(BigDecimal valor) {
    	if (saldo.compareTo(valor) >= 0) {
    		saldo = saldo.subtract(valor);
        } else {
            throw new StatusTransacaoException("SALDO_INSUFICIENTE");
        }
    }
    
    public boolean isSenhaCorreta(String senha) {
    	return this.senha.equals(senha);
    }

}
