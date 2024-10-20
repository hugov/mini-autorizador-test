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
import br.com.decimal.miniautorizador.core.usecase.ConsultarSaldoCartaoUseCase;

@SpringBootTest
public class ConsultarSaldoCartaoUseCaseTest {

	@MockBean
	private CartaoRepositoryPort cartaoRepositoryPort;
	
	@Autowired
	private ConsultarSaldoCartaoUseCase consultarSaldoCartaoUseCase;

    @Test
    public void deveRetornarSaldoComSucesso() {
        Long numeroCartao = 123456789L;
        
        Cartao cartao = new Cartao();
        cartao.setNumeroCartao(numeroCartao);
        cartao.setSaldo(new BigDecimal("123.45"));

        when(cartaoRepositoryPort.findByNumeroCartao(numeroCartao)).thenReturn(Optional.of(cartao));

        BigDecimal saldo = consultarSaldoCartaoUseCase.consultarSaldoCartao(numeroCartao);

        assertEquals(new BigDecimal("123.45"), saldo);
        verify(cartaoRepositoryPort, times(1)).findByNumeroCartao(numeroCartao);
    }
    
    @Test
    public void deveRetornarCartaoNaoExiste() {
        Long numeroCartao = 123456789L;
        
        when(cartaoRepositoryPort.findByNumeroCartao(numeroCartao)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> consultarSaldoCartaoUseCase.consultarSaldoCartao(numeroCartao));
        verify(cartaoRepositoryPort, times(1)).findByNumeroCartao(numeroCartao);
    }

}
