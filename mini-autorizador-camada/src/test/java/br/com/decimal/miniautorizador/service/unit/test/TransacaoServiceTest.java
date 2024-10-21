package br.com.decimal.miniautorizador.service.unit.test;

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

import br.com.decimal.miniautorizador.dto.TransacaoDTO;
import br.com.decimal.miniautorizador.entity.Cartao;
import br.com.decimal.miniautorizador.exception.StatusTransacaoException;
import br.com.decimal.miniautorizador.repository.CartaoRepository;
import br.com.decimal.miniautorizador.service.TransacaoService;

@SpringBootTest
public class TransacaoServiceTest {
	
	@Autowired
    private TransacaoService transacaoService;

    @MockBean
    private CartaoRepository cartaoRepository;
    
	@Test
    public void deveRetornarDebitoComSucesso() {
        Long numeroCartao = 6549873025634501L;
        Cartao cartao = prepareCartao(numeroCartao, 500.0d, "1234");
        TransacaoDTO transacaoDTO = prepareTransacao(numeroCartao, 20.00d, "1234");

        when(cartaoRepository.findByNumeroCartao(numeroCartao)).thenReturn(Optional.of(cartao));
        
        String resultado = transacaoService.realizarTransacao(transacaoDTO);

        assertEquals("OK", resultado);
        verify(cartaoRepository, times(1)).findByNumeroCartao(numeroCartao);
    }
	
	@Test
    public void deveRetornarCartaoInexistente() {
        Long numeroCartao = 0L;
        TransacaoDTO transacaoDTO = prepareTransacao(numeroCartao, 20.0d, "1234");

        StatusTransacaoException exception = assertThrows(StatusTransacaoException.class, () -> transacaoService.realizarTransacao(transacaoDTO));
        assertEquals("CARTAO_INEXISTENTE", exception.getMessage());
        
        verify(cartaoRepository, times(1)).findByNumeroCartao(numeroCartao);
    }
	
	@Test
    public void deveRetornarSaldoInsuficiente() {
        Long numeroCartao = 6549873025634501L;
        Cartao cartao = prepareCartao(numeroCartao, 500.0d, "1234");
        TransacaoDTO transacaoDTO = prepareTransacao(numeroCartao, 500.01d, "1234");
        
        when(cartaoRepository.findByNumeroCartao(numeroCartao)).thenReturn(Optional.of(cartao));

        StatusTransacaoException exception = assertThrows(StatusTransacaoException.class, () -> transacaoService.realizarTransacao(transacaoDTO));
        assertEquals("SALDO_INSUFICIENTE", exception.getMessage());
        
        verify(cartaoRepository, times(1)).findByNumeroCartao(numeroCartao);
    }
	
	@Test
    public void deveRetornarSenhaIncorreta() {
        Long numeroCartao = 6549873025634501L;
        Cartao cartao = prepareCartao(numeroCartao, 500.0d, "1234");
        TransacaoDTO transacaoDTO = prepareTransacao(numeroCartao, 20.0d, "12345");
        
        when(cartaoRepository.findByNumeroCartao(numeroCartao)).thenReturn(Optional.of(cartao));

        StatusTransacaoException exception = assertThrows(StatusTransacaoException.class, () -> transacaoService.realizarTransacao(transacaoDTO));
        assertEquals("SENHA_INVALIDA", exception.getMessage());
        
        verify(cartaoRepository, times(1)).findByNumeroCartao(numeroCartao);
    }
	
	private Cartao prepareCartao( long numeroCartao, Double saldo, String senha ) {
		Cartao cartao = new Cartao();
        cartao.setNumeroCartao(numeroCartao);
        cartao.setSaldo(BigDecimal.valueOf(saldo));
        cartao.setSenha(senha);
        
        return cartao;
	}
	
	private TransacaoDTO prepareTransacao(long numeroCartao, Double valor, String senha) {
		TransacaoDTO transacaoDTO = new TransacaoDTO();
        transacaoDTO.setNumeroCartao(String.valueOf(numeroCartao));
        transacaoDTO.setSenhaCartao(senha);
        transacaoDTO.setValor(BigDecimal.valueOf(valor));
        
        return transacaoDTO;
	}

}
// 106