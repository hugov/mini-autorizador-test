package br.com.decimal.miniautorizador.controller;

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

import br.com.decimal.miniautorizador.dto.CartaoDTO;
import br.com.decimal.miniautorizador.service.CartaoService;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {
	
	@Autowired
    private CartaoService cartaoService;

    @PostMapping
    public ResponseEntity<CartaoDTO> cadastrarCartao(@RequestBody CartaoDTO cartaoDTO) {
        try {
			CartaoDTO cartaoCriado = cartaoService.cadastrarCartao(cartaoDTO);
			return new ResponseEntity<>(cartaoCriado, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(cartaoDTO, HttpStatus.UNPROCESSABLE_ENTITY);
		}
    }
    
    @GetMapping("/{numeroCartao}")
    public ResponseEntity<BigDecimal> consultarSaldoCartao(@PathVariable Long numeroCartao) {
    	try {
    		BigDecimal saldo = cartaoService.consultarSaldoCartao(numeroCartao);
    		return new ResponseEntity<>(saldo, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    }

}
