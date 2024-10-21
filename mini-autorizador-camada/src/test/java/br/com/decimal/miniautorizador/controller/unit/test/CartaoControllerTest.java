package br.com.decimal.miniautorizador.controller.unit.test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.decimal.miniautorizador.controller.CartaoController;
import br.com.decimal.miniautorizador.dto.CartaoDTO;
import br.com.decimal.miniautorizador.service.CartaoService;

@SpringBootTest
public class CartaoControllerTest {
	
	@InjectMocks
	CartaoController controller;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@Mock
	private CartaoService cartaoService;
	
	private MockMvc mockMvc;
	
	private CartaoDTO cartaoDTO;
	
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).alwaysDo(print()).build();
		
		// Estratégia para obter um numero de cartão único
		LocalDateTime dataHoraAtual = LocalDateTime.now();
		ZonedDateTime zoneDateTime = dataHoraAtual.atZone(ZoneId.of("America/Sao_Paulo"));
		long millisegundos = zoneDateTime.toInstant().toEpochMilli();
		
		cartaoDTO = new CartaoDTO();
		cartaoDTO.setNumeroCartao(String.valueOf(millisegundos));
		cartaoDTO.setSenha("1234");
		
	}
	
	@Test
	public void deveCadastrarUmCartaoComSucesso() throws Exception {
		
		mockMvc.perform(post("/cartoes")
			.contentType("application/json")
            .content(objectMapper.writeValueAsString(cartaoDTO)))
        	.andExpect(MockMvcResultMatchers.status().isCreated())
        	.andReturn();
		
		verify(cartaoService, times(1)).cadastrarCartao(cartaoDTO);
		
	}
	
	@Test
    public void deveRetornarCartaoJaCadastrado() throws Exception {
		
		Mockito.doThrow(new RuntimeException("Cartão já existe"))
			.when(cartaoService).cadastrarCartao(cartaoDTO);
		
		mockMvc.perform(post("/cartoes")
				.contentType("application/json")
	            .content(objectMapper.writeValueAsString(cartaoDTO)))
	        	.andExpect(MockMvcResultMatchers.status().is(422))
	        	.andExpect(MockMvcResultMatchers.jsonPath("$.numeroCartao").exists())
	        	.andExpect(MockMvcResultMatchers.jsonPath("$.senha").exists())
	        	.andReturn();
		
		verify(cartaoService, times(1)).cadastrarCartao(cartaoDTO);
    }
	
	@Test
	public void deveConsultarSaldoCartaoComSucesso() throws Exception {
		
		mockMvc.perform(get(String.format("/cartoes/%s" , cartaoDTO.getNumeroCartao()))
			.contentType("application/json"))
        	.andExpect(MockMvcResultMatchers.status().isOk())
        	.andReturn();
		
		verify(cartaoService, times(1)).consultarSaldoCartao(Long.valueOf(cartaoDTO.getNumeroCartao()));
		
	}
	
	@Test
    public void deveRetornarCartaoNaoEncontrado() throws Exception {
		
		Mockito.doThrow(new RuntimeException("Cartão não existe"))
			.when(cartaoService).consultarSaldoCartao(Long.valueOf(cartaoDTO.getNumeroCartao()));
		
		mockMvc.perform(get(String.format("/cartoes/%s" , cartaoDTO.getNumeroCartao()))
				.contentType("application/json")
	            .content(objectMapper.writeValueAsString(cartaoDTO)))
	        	.andExpect(MockMvcResultMatchers.status().is(404))
	        	.andReturn();
		
		verify(cartaoService, times(1)).consultarSaldoCartao(Long.valueOf(cartaoDTO.getNumeroCartao()));
    }

}
