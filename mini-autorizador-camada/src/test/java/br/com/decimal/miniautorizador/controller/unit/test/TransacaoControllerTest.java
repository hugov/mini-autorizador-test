package br.com.decimal.miniautorizador.controller.unit.test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.math.BigDecimal;
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

import br.com.decimal.miniautorizador.controller.TransacaoController;
import br.com.decimal.miniautorizador.dto.TransacaoDTO;
import br.com.decimal.miniautorizador.exception.StatusTransacaoException;
import br.com.decimal.miniautorizador.service.TransacaoService;

@SpringBootTest
public class TransacaoControllerTest {
	
	@InjectMocks
	TransacaoController controller;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@Mock
	private TransacaoService transacaoService;
	
	private MockMvc mockMvc;
	
	private TransacaoDTO transacaoDTO;
	
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).alwaysDo(print()).build();
		
		// Estratégia para obter um numero de cartão único
		LocalDateTime dataHoraAtual = LocalDateTime.now();
		ZonedDateTime zoneDateTime = dataHoraAtual.atZone(ZoneId.of("America/Sao_Paulo"));
		long millisegundos = zoneDateTime.toInstant().toEpochMilli();
		
		transacaoDTO = new TransacaoDTO();
		transacaoDTO.setNumeroCartao(String.valueOf(millisegundos));
		transacaoDTO.setSenhaCartao("1234");
		transacaoDTO.setValor(BigDecimal.valueOf(10.0));
		
	}
	
	@Test
    public void deveRetornarDebitoComSucesso() throws Exception {
		
		mockMvc.perform(post("/transacoes")
				.contentType("application/json")
	            .content(objectMapper.writeValueAsString(transacaoDTO)))
	        	.andExpect(MockMvcResultMatchers.status().isCreated())
	        	.andReturn();
			
		verify(transacaoService, times(1)).realizarTransacao(transacaoDTO);
		
    }
	
	@Test
    public void deveRetornarCartaoInexistente() throws Exception {
		
		Mockito.doThrow(new StatusTransacaoException("CARTAO_INEXISTENTE"))
			.when(transacaoService).realizarTransacao(transacaoDTO);
		
		mockMvc.perform(post("/transacoes")
				.contentType("application/json")
		        .content(objectMapper.writeValueAsString(transacaoDTO)))
		    	.andExpect(MockMvcResultMatchers.status().is(422))
		    	.andExpect(MockMvcResultMatchers.content().string("CARTAO_INEXISTENTE")) 
		    	.andReturn();
			
		verify(transacaoService, times(1)).realizarTransacao(transacaoDTO);
    }
	
	@Test
    public void deveRetornarSaldoInsuficiente() throws Exception {
    	
		Mockito.doThrow(new StatusTransacaoException("SALDO_INSUFICIENTE"))
			.when(transacaoService).realizarTransacao(transacaoDTO);
			
		mockMvc.perform(post("/transacoes")
			.contentType("application/json")
            .content(objectMapper.writeValueAsString(transacaoDTO)))
        	.andExpect(MockMvcResultMatchers.status().is(422))
        	.andExpect(MockMvcResultMatchers.content().string("SALDO_INSUFICIENTE")) 
        	.andReturn();
		
		verify(transacaoService, times(1)).realizarTransacao(transacaoDTO);
		
    }
	
	@Test
    public void deveRetornarSenhaIncorreta() throws Exception {
		
		Mockito.doThrow(new StatusTransacaoException("SENHA_INVALIDA"))
			.when(transacaoService).realizarTransacao(transacaoDTO);
			
		mockMvc.perform(post("/transacoes")
			.contentType("application/json")
	        .content(objectMapper.writeValueAsString(transacaoDTO)))
	    	.andExpect(MockMvcResultMatchers.status().is(422))
	    	.andExpect(MockMvcResultMatchers.content().string("SENHA_INVALIDA")) 
	    	.andReturn();
		
		verify(transacaoService, times(1)).realizarTransacao(transacaoDTO);
	
    }
	

}
