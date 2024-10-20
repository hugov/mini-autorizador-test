package br.com.decimal.miniautorizador.core.usecase.test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.com.decimal.miniautorizador.adapter.outbound.CartaoRepositoryPort;
import br.com.decimal.miniautorizador.core.domain.Cartao;
import br.com.decimal.miniautorizador.core.usecase.CriarCartaoUseCase;

@SpringBootTest
public class CriarCartaoUseCaseTest {

	@MockBean
	private CartaoRepositoryPort cartaoRepositoryPort;
	
	@Autowired
	private CriarCartaoUseCase criarCartaoUseCase;

    @Test
    public void cadastrarCartaoSucesso() {
        String numeroCartao = "123456789";
        String senha = "1234";

        criarCartaoUseCase.criarCartao(numeroCartao, senha);
        verify(cartaoRepositoryPort, times(1)).save(any(Cartao.class));
    }
    
    @Test
    public void cadastrarCartaoJaExistente() {
    	String numeroCartao = "123456789";
        String senha = "1234";

        Cartao cartao = new Cartao();
        cartao.setNumeroCartao(123456789L);

        when(cartaoRepositoryPort.findByNumeroCartao(anyLong())).thenReturn(Optional.of(cartao));

        assertThrows(RuntimeException.class, () -> criarCartaoUseCase.criarCartao(numeroCartao, senha));
        
        verify(cartaoRepositoryPort, times(1)).findByNumeroCartao(Long.valueOf(numeroCartao));
        verify(cartaoRepositoryPort, times(0)).save(any(Cartao.class));
    }

}
