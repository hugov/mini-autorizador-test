package br.com.decimal.miniautorizador.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.decimal.miniautorizador.dto.CartaoDTO;
import br.com.decimal.miniautorizador.entity.Cartao;
import br.com.decimal.miniautorizador.repository.CartaoRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CartaoService {
	
	private static final Double SALDO_INICIAL = 500.0d;
	
	@Autowired
	private CartaoRepository cartaoRepository;
	
	public CartaoDTO cadastrarCartao(CartaoDTO cartaoDTO) {
        Long numeroCartao = Long.parseLong(cartaoDTO.getNumeroCartao());
        log.info("Executando o serviço de criação de cartão {}", numeroCartao);

        Optional<Cartao> cartaoExistente = cartaoRepository.findByNumeroCartao(numeroCartao);
        if (cartaoExistente.isPresent()) {
            throw new RuntimeException("Cartão já existe");
        }

        Cartao novoCartao = new Cartao();
        novoCartao.setNumeroCartao(numeroCartao);
        novoCartao.setSenha(cartaoDTO.getSenha());
        novoCartao.setSaldo(BigDecimal.valueOf(SALDO_INICIAL));

        cartaoRepository.save(novoCartao);

        return cartaoDTO;
    }
	
	public BigDecimal consultarSaldoCartao(Long numeroCartao) {
        log.info("Consultado o saldo do cartão {}", numeroCartao);

        Optional<Cartao> cartaoExistente = cartaoRepository.findByNumeroCartao(numeroCartao);
        if (cartaoExistente.isPresent()) {
            return cartaoExistente.get().getSaldo();
        } else {
        	throw new RuntimeException("Cartão não existe");
        }
    }

}
