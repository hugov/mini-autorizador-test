package br.com.decimal.miniautorizador.service.unit.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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
}
