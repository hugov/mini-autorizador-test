package br.com.decimal.miniautorizador.adapter.inbound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.decimal.miniautorizador.core.port.inbound.RealizarTransacaoDebitoService;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {
	
	@Autowired
    private RealizarTransacaoDebitoService realizarTransacaoDebitoService;
	
    @PostMapping
    public ResponseEntity<String> realizarTransacao(@RequestBody TransacaoRequest transacaoRequest) {
    	try {
			realizarTransacaoDebitoService.autorizarTransacao(transacaoRequest.getNumeroCartao(), transacaoRequest.getSenhaCartao(), transacaoRequest.getValor());
			return new ResponseEntity<>("OK", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
    }

}
