package br.com.decimal.miniautorizador.adapter.inbound;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.decimal.miniautorizador.core.port.inbound.ConsultarSaldoCartaoService;
import br.com.decimal.miniautorizador.core.port.inbound.CriarCartaoService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/cartoes")
public class CartaoController {
	
	@Autowired
    private CriarCartaoService criarCartaoService;
	
	@Autowired
	private ConsultarSaldoCartaoService consultarSaldoCartaoService;

    @PostMapping
    public ResponseEntity<CartaoRequest> cadastrarCartao(@RequestBody CartaoRequest cartaoRequest) {
    	log.info("Cadastrando o cartão {} ", cartaoRequest);
        
    	try {
        	criarCartaoService.criarCartao(cartaoRequest.getNumeroCartao(), cartaoRequest.getSenha());
			return new ResponseEntity<>(cartaoRequest, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(cartaoRequest, HttpStatus.UNPROCESSABLE_ENTITY);
		}
    }
    
    @GetMapping("/{numeroCartao}")
    public ResponseEntity<BigDecimal> consultarSaldoCartao(@PathVariable Long numeroCartao) {
    	log.info("Consultando o saldo do cartão {} ", numeroCartao);
    	
    	try {
    		BigDecimal saldo = consultarSaldoCartaoService.consultarSaldoCartao(numeroCartao);
    		return new ResponseEntity<>(saldo, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    }

}
