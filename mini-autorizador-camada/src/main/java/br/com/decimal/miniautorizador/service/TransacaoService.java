package br.com.decimal.miniautorizador.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import br.com.decimal.miniautorizador.dto.TransacaoDTO;
import br.com.decimal.miniautorizador.entity.Cartao;
import br.com.decimal.miniautorizador.exception.StatusTransacaoException;
import br.com.decimal.miniautorizador.repository.CartaoRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransacaoService implements Transacao {

	private CartaoRepository cartaoRepository;
	private ValidadorTransacaoSenha validadorTransacaoSenha;
	private ValidadorTransacaoSaldo validadorTransacaoSaldo;

	@Autowired
	public TransacaoService(CartaoRepository cartaoRepository, ValidadorTransacaoSenha validadorTransacaoSenha,
			ValidadorTransacaoSaldo validadorTransacaoSaldo) {
		super();
		this.cartaoRepository = cartaoRepository;
		this.validadorTransacaoSenha = validadorTransacaoSenha;
		this.validadorTransacaoSaldo = validadorTransacaoSaldo;
	}

	@Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public String realizarTransacao(TransacaoDTO transacaoDTO) {
        Long numeroCartao = Long.parseLong(transacaoDTO.getNumeroCartao());
        log.info("Realizando a transação no cartão {}", numeroCartao);
        
        Cartao cartao = cartaoRepository.findByNumeroCartao(numeroCartao)
                .orElseThrow(() -> new StatusTransacaoException(StatusTransacaoEnum.CARTAO_INEXISTENTE.toString()));

        validadorTransacaoSenha.validar(transacaoDTO.getSenhaCartao(), cartao.getSenha());
        validadorTransacaoSaldo.validar(transacaoDTO.getValor(), cartao.getSaldo());

        atualizarSaldo(cartao, transacaoDTO.getValor());
        
        return StatusTransacaoEnum.OK.toString();
    }

    private void atualizarSaldo(Cartao cartao, BigDecimal valorTransacao) {
        cartao.setSaldo(cartao.getSaldo().subtract(valorTransacao));
        cartaoRepository.save(cartao);
    }

}
