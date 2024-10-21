package br.com.decimal.miniautorizador.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import br.com.decimal.miniautorizador.dto.TransacaoDTO;
import br.com.decimal.miniautorizador.entity.Cartao;
import br.com.decimal.miniautorizador.entity.Transacao;
import br.com.decimal.miniautorizador.exception.StatusTransacaoException;
import br.com.decimal.miniautorizador.repository.CartaoRepository;
import br.com.decimal.miniautorizador.repository.TransacaoRepository;
import lombok.Locked;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransacaoService {

	@Autowired
	private TransacaoRepository transacaoRepository;

	@Autowired
	private CartaoRepository cartaoRepository;

	enum StatusTransacaoEnum {
		OK, SALDO_INSUFICIENTE, SENHA_INVALIDA, CARTAO_INEXISTENTE
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	@Locked
	public String realizarTransacao(TransacaoDTO transacaoDTO) {
		Long numeroCartao = Long.parseLong(transacaoDTO.getNumeroCartao());
        log.info("Realizando a transação no cartão {}", numeroCartao);
        
        Transacao transacao = criarTransacao(transacaoDTO, numeroCartao);
        transacaoRepository.save(transacao);

        Cartao cartao = cartaoRepository.findByNumeroCartao(numeroCartao)
                .orElseThrow(() -> new StatusTransacaoException(StatusTransacaoEnum.CARTAO_INEXISTENTE.toString()));

        validarSenha(transacaoDTO.getSenhaCartao(), cartao.getSenha());
        validarSaldo(transacaoDTO.getValor(), cartao.getSaldo());

        atualizarSaldo(cartao, transacaoDTO.getValor());
        
        return StatusTransacaoEnum.OK.toString();

	}

	private Transacao criarTransacao(TransacaoDTO dto, Long numeroCartao) {
		return Transacao.builder()
				.numeroCartao(numeroCartao)
				.senhaCartao(dto.getSenhaCartao())
				.valor(dto.getValor())
				.build();
	}
	
	private void validarSenha(String senhaDTO, String senhaCartao) {
        if (!senhaDTO.equals(senhaCartao)) {
            throw new StatusTransacaoException(StatusTransacaoEnum.SENHA_INVALIDA.toString());
        }
    }

    private void validarSaldo(BigDecimal valorTransacao, BigDecimal saldo) {
        if (saldo.compareTo(valorTransacao) < 0) {
            throw new StatusTransacaoException(StatusTransacaoEnum.SALDO_INSUFICIENTE.toString());
        }
    }

    private void atualizarSaldo(Cartao cartao, BigDecimal valorTransacao) {
        cartao.setSaldo(cartao.getSaldo().subtract(valorTransacao));
        cartaoRepository.save(cartao);
    }

}
