package br.com.decimal.miniautorizador.core.usecase;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import br.com.decimal.miniautorizador.adapter.outbound.CartaoRepositoryPort;
import br.com.decimal.miniautorizador.core.domain.Cartao;
import br.com.decimal.miniautorizador.core.exception.StatusTransacaoException;
import br.com.decimal.miniautorizador.core.port.inbound.RealizarTransacaoDebitoService;
import br.com.decimal.miniautorizador.core.port.inbound.StatusTransacaoEnum;
import br.com.decimal.miniautorizador.core.port.inbound.ValidadorTransacaoSaldo;
import br.com.decimal.miniautorizador.core.port.inbound.ValidadorTransacaoSenha;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class RealizarTransacaoDebitoUseCase implements RealizarTransacaoDebitoService {
	
	private CartaoRepositoryPort cartaoRepositoryPort;
	private ValidadorTransacaoSenha validadorTransacaoSenha;
	private ValidadorTransacaoSaldo validadorTransacaoSaldo;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public String autorizarTransacao(String numeroCartao, String senha, BigDecimal valor) {
        Long numeroCartaoTmp = Long.parseLong(numeroCartao);
        log.info("Realizando a transação no cartão {}", numeroCartaoTmp);
        
        Cartao cartao = cartaoRepositoryPort.findByNumeroCartao(numeroCartaoTmp)
                .orElseThrow(() -> new StatusTransacaoException(StatusTransacaoEnum.CARTAO_INEXISTENTE.toString()));

        validadorTransacaoSenha.validar(senha, cartao.getSenha());
        validadorTransacaoSaldo.validar(valor, cartao.getSaldo());

        atualizarSaldo(cartao, valor);
        
        return StatusTransacaoEnum.OK.toString();
    }

    private void atualizarSaldo(Cartao cartao, BigDecimal valorTransacao) {
        cartao.setSaldo(cartao.getSaldo().subtract(valorTransacao));
        cartaoRepositoryPort.save(cartao);
    }

}
