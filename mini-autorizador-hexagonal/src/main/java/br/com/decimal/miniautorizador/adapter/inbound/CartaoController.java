package br.com.decimal.miniautorizador.adapter.inbound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.decimal.miniautorizador.core.port.inbound.CartaoService;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {
	
	@Autowired
    private CartaoService cartaoService;

    @PostMapping
    public ResponseEntity<CartaoRequest> cadastrarCartao(@RequestBody CartaoRequest cartaoRequest) {
        try {
        	cartaoService.criarCartao(cartaoRequest.getNumeroCartao(), cartaoRequest.getSenha());
			return new ResponseEntity<>(cartaoRequest, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(cartaoRequest, HttpStatus.UNPROCESSABLE_ENTITY);
		}
    }

}
