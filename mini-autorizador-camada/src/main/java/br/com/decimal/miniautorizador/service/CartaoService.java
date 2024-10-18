package br.com.decimal.miniautorizador.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.decimal.miniautorizador.dto.CartaoDTO;
import br.com.decimal.miniautorizador.entity.Cartao;
import br.com.decimal.miniautorizador.repository.CartaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartaoService {
	
	@Autowired
	private CartaoRepository cartaoRepository;
	
	public CartaoDTO cadastrarCartao(CartaoDTO cartaoDTO) {
        Long numeroCartao = Long.parseLong(cartaoDTO.getNumeroCartao());
        log.info(String.format("Criando o cartão %d", numeroCartao));

        Optional<Cartao> cartaoExistente = cartaoRepository.findByNumeroCartao(numeroCartao);
        if (cartaoExistente.isPresent()) {
            throw new RuntimeException("Cartão já existe");
        }

        Cartao novoCartao = new Cartao();
        novoCartao.setNumeroCartao(numeroCartao);
        novoCartao.setSenha(cartaoDTO.getSenha());
        novoCartao.setDataValidade(LocalDateTime.now().plusYears(5));
        novoCartao.setSaldo(BigDecimal.ZERO);
        novoCartao.setSituacao(1); // 1 significa ativo

        cartaoRepository.save(novoCartao);

        return cartaoDTO;
    }
	
	public BigDecimal consultarSaldoCartao(Long numeroCartao) {
        log.info(String.format("Consultado o saldo do cartão %d", numeroCartao));

        Optional<Cartao> cartaoExistente = cartaoRepository.findByNumeroCartao(numeroCartao);
        if (cartaoExistente.isPresent()) {
            return cartaoExistente.get().getSaldo();
            //return cartaoExistente.get().getSaldo().doubleValue();
        } else {
        	throw new RuntimeException("Cartão não existe");
        }
    }

}
