package br.com.decimal.miniautorizador.core.usecase.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.com.decimal.miniautorizador.adapter.outbound.CartaoRepositoryPort;
import br.com.decimal.miniautorizador.core.domain.Cartao;
import br.com.decimal.miniautorizador.core.domain.StatusTransacaoException;
import br.com.decimal.miniautorizador.core.domain.Transacao;
import br.com.decimal.miniautorizador.core.usecase.RealizarTransacaoDebitoUseCase;

@SpringBootTest
public class RealizarTransacaoDebitoUseCaseTest {

	@MockBean
	private CartaoRepositoryPort cartaoRepositoryPort;
	
	@Autowired
	private RealizarTransacaoDebitoUseCase realizarTransacaoDebitoUseCase;

    @Test
    public void deveRetornarSaldoComSucesso() {
        Long numeroCartao = 6549873025634501L;
        Cartao cartao = prepareCartao(numeroCartao, 500.0d, "1234");
        Transacao transacao = prepareTransacao(numeroCartao, 20.00d, "1234");

        when(cartaoRepositoryPort.findByNumeroCartao(numeroCartao)).thenReturn(Optional.of(cartao));
        
        realizarTransacaoDebitoUseCase.autorizarTransacao(transacao.getNumeroCartao().toString(), transacao.getSenhaCartao(), transacao.getValor());

        verify(cartaoRepositoryPort, times(1)).findByNumeroCartao(numeroCartao);
    }
	
	@Test
    public void deveRetornarCartaoInexistente() {
        Long numeroCartao = 0L;
        Transacao transacao = prepareTransacao(numeroCartao, 20.0d, "1234");

        StatusTransacaoException exception = assertThrows(StatusTransacaoException.class, () -> realizarTransacaoDebitoUseCase.autorizarTransacao(transacao.getNumeroCartao().toString(), transacao.getSenhaCartao(), transacao.getValor()));
        assertEquals("CARTAO_INEXISTENTE", exception.getMessage());
        
        verify(cartaoRepositoryPort, times(1)).findByNumeroCartao(numeroCartao);
    }
	
	@Test
    public void deveRetornarSaldoInsuficiente() {
        Long numeroCartao = 6549873025634501L;
        Cartao cartao = prepareCartao(numeroCartao, 500.0d, "1234");
        Transacao transacao = prepareTransacao(numeroCartao, 500.01d, "1234");
        
        when(cartaoRepositoryPort.findByNumeroCartao(numeroCartao)).thenReturn(Optional.of(cartao));

        StatusTransacaoException exception = assertThrows(StatusTransacaoException.class, () -> realizarTransacaoDebitoUseCase.autorizarTransacao(transacao.getNumeroCartao().toString(), transacao.getSenhaCartao(), transacao.getValor()));
        assertEquals("SALDO_INSUFICIENTE", exception.getMessage());
        
        verify(cartaoRepositoryPort, times(1)).findByNumeroCartao(numeroCartao);
    }
	
	@Test
    public void deveRetornarSenhaIncorreta() {
        Long numeroCartao = 6549873025634501L;
        Cartao cartao = prepareCartao(numeroCartao, 500.0d, "1234");
        Transacao transacao = prepareTransacao(numeroCartao, 20.0d, "12345");
        
        when(cartaoRepositoryPort.findByNumeroCartao(numeroCartao)).thenReturn(Optional.of(cartao));

        StatusTransacaoException exception = assertThrows(StatusTransacaoException.class, () -> realizarTransacaoDebitoUseCase.autorizarTransacao(transacao.getNumeroCartao().toString(), transacao.getSenhaCartao(), transacao.getValor()));
        assertEquals("SENHA_INVALIDA", exception.getMessage());
        
        verify(cartaoRepositoryPort, times(1)).findByNumeroCartao(numeroCartao);
    }
	
	private Cartao prepareCartao( long numeroCartao, Double saldo, String senha ) {
		Cartao cartao = new Cartao();
        cartao.setNumeroCartao(numeroCartao);
        cartao.setSaldo(BigDecimal.valueOf(saldo));
        cartao.setSenha(senha);
        
        return cartao;
	}
	
	private Transacao prepareTransacao(long numeroCartao, Double valor, String senha) {
		Transacao transacao = new Transacao();
		transacao.setNumeroCartao(numeroCartao);
		transacao.setSenhaCartao(senha);
		transacao.setValor(BigDecimal.valueOf(valor));
        
        return transacao;
	}

}
