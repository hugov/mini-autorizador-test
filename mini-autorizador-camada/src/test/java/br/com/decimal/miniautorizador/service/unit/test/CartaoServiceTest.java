package br.com.decimal.miniautorizador.service.unit.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.com.decimal.miniautorizador.dto.CartaoDTO;
import br.com.decimal.miniautorizador.entity.Cartao;
import br.com.decimal.miniautorizador.repository.CartaoRepository;
import br.com.decimal.miniautorizador.service.CartaoService;

@SpringBootTest
public class CartaoServiceTest {

    @Autowired
    private CartaoService cartaoService;

    @MockBean
    private CartaoRepository cartaoRepository;

    @Test
    public void cadastrarCartaoSucesso() {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao("6549873025634501");
        cartaoDTO.setSenha("1234");

        when(cartaoRepository.findByNumeroCartao(anyLong())).thenReturn(Optional.empty());

        CartaoDTO resultado = cartaoService.cadastrarCartao(cartaoDTO);

        assertEquals("6549873025634501", resultado.getNumeroCartao());
        assertEquals("1234", resultado.getSenha());
    }

    @Test
    public void cadastrarCartaoJaExistente() {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao("6549873025634501");
        cartaoDTO.setSenha("1234");

        Cartao cartao = new Cartao();
        cartao.setNumeroCartao(6549873025634501L);

        when(cartaoRepository.findByNumeroCartao(anyLong())).thenReturn(Optional.of(cartao));

        //FIXME: Implementar um exceção especifica
        assertThrows(RuntimeException.class, () -> cartaoService.cadastrarCartao(cartaoDTO));
    }
    
    @Test
    public void deveRetornarSaldoComSucesso() {
        Long numeroCartao = 6549873025634501L;
        
        Cartao cartao = new Cartao();
        cartao.setNumeroCartao(numeroCartao);
        cartao.setSaldo(new BigDecimal("123.45"));

        when(cartaoRepository.findByNumeroCartao(numeroCartao)).thenReturn(Optional.of(cartao));

        BigDecimal saldo = cartaoService.consultarSaldoCartao(numeroCartao);

        assertEquals(new BigDecimal("123.45"), saldo);
        verify(cartaoRepository, times(1)).findByNumeroCartao(numeroCartao);
    }

    @Test
    public void deveLancarExcecaoQuandoCartaoNaoExistir() {
        Long numeroCartao = 0L;

        when(cartaoRepository.findByNumeroCartao(numeroCartao)).thenReturn(Optional.empty());

        //FIXME: Corrigir o tipo de exceção
        assertThrows(RuntimeException.class, () -> cartaoService.consultarSaldoCartao(numeroCartao));

        verify(cartaoRepository, times(1)).findByNumeroCartao(numeroCartao);
    }

}
