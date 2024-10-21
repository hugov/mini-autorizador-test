package br.com.decimal.miniautorizador.adapter.inbound.test;

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

import br.com.decimal.miniautorizador.adapter.inbound.CartaoController;
import br.com.decimal.miniautorizador.adapter.inbound.CartaoRequest;
import br.com.decimal.miniautorizador.core.port.inbound.ConsultarSaldoCartaoService;
import br.com.decimal.miniautorizador.core.port.inbound.CriarCartaoService;

@SpringBootTest
public class CartaoControllerTest {
	
	@InjectMocks
	CartaoController controller;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@Mock
	private CriarCartaoService criarCartaoService;
	
	@Mock
	private ConsultarSaldoCartaoService consultarSaldoCartaoService;
	
	private MockMvc mockMvc;
	
	private CartaoRequest cartaoRequest;
	
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).alwaysDo(print()).build();
		
		// Estratégia para obter um numero de cartão único
		LocalDateTime dataHoraAtual = LocalDateTime.now();
		ZonedDateTime zoneDateTime = dataHoraAtual.atZone(ZoneId.of("America/Sao_Paulo"));
		long millisegundos = zoneDateTime.toInstant().toEpochMilli();
		
		cartaoRequest = new CartaoRequest();
		cartaoRequest.setNumeroCartao(String.valueOf(millisegundos));
		cartaoRequest.setSenha("1234");
		
	}
	
	@Test
	public void deveCadastrarUmCartaoComSucesso() throws Exception {
		
		mockMvc.perform(post("/cartoes")
			.contentType("application/json")
            .content(objectMapper.writeValueAsString(cartaoRequest)))
        	.andExpect(MockMvcResultMatchers.status().isCreated())
        	.andReturn();
		
		verify(criarCartaoService, times(1)).criarCartao(cartaoRequest.getNumeroCartao(), cartaoRequest.getSenha());
		
	}
	
	@Test
    public void deveRetornarCartaoJaCadastrado() throws Exception {
		
		Mockito.doThrow(new RuntimeException("Cartão já existe"))
			.when(criarCartaoService).criarCartao(cartaoRequest.getNumeroCartao(), cartaoRequest.getSenha());
		
		mockMvc.perform(post("/cartoes")
				.contentType("application/json")
	            .content(objectMapper.writeValueAsString(cartaoRequest)))
	        	.andExpect(MockMvcResultMatchers.status().is(422))
	        	.andExpect(MockMvcResultMatchers.jsonPath("$.numeroCartao").exists())
	        	.andExpect(MockMvcResultMatchers.jsonPath("$.senha").exists())
	        	.andReturn();
		
		verify(criarCartaoService, times(1)).criarCartao(cartaoRequest.getNumeroCartao(), cartaoRequest.getSenha());
    }
	
	@Test
	public void deveConsultarSaldoCartaoComSucesso() throws Exception {
		
		mockMvc.perform(get(String.format("/cartoes/%s" , cartaoRequest.getNumeroCartao()))
			.contentType("application/json"))
        	.andExpect(MockMvcResultMatchers.status().isOk())
        	.andReturn();
		
		verify(consultarSaldoCartaoService, times(1)).consultarSaldoCartao(Long.valueOf(cartaoRequest.getNumeroCartao()));
		
	}
	
	@Test
    public void deveRetornarCartaoNaoEncontrado() throws Exception {
		
		Mockito.doThrow(new RuntimeException("Cartão não existe"))
			.when(consultarSaldoCartaoService).consultarSaldoCartao(Long.valueOf(cartaoRequest.getNumeroCartao()));
		
		mockMvc.perform(get(String.format("/cartoes/%s" , cartaoRequest.getNumeroCartao()))
				.contentType("application/json")
	            .content(objectMapper.writeValueAsString(cartaoRequest)))
	        	.andExpect(MockMvcResultMatchers.status().is(404))
	        	.andReturn();
		
		verify(consultarSaldoCartaoService, times(1)).consultarSaldoCartao(Long.valueOf(cartaoRequest.getNumeroCartao()));
    }

}
