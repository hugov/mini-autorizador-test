package br.com.decimal.miniautorizador.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.decimal.miniautorizador.dto.TransacaoDTO;
import br.com.decimal.miniautorizador.service.TransacaoService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/transacoes")
public class TransacaoController {
	
	@Autowired
    private TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<String> realizarTransacao(@RequestBody TransacaoDTO transacaoDTO) {
    	log.info("Realizando a transação de débito do cartão {} ", transacaoDTO);
    	
    	try {
			String resultado = transacaoService.realizarTransacao(transacaoDTO);
			return new ResponseEntity<>(resultado, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
    }

}
