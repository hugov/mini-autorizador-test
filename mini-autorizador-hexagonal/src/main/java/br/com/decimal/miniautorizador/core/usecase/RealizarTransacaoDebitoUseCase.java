package br.com.decimal.miniautorizador.core.usecase;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.decimal.miniautorizador.adapter.outbound.CartaoRepositoryPort;
import br.com.decimal.miniautorizador.core.domain.Cartao;
import br.com.decimal.miniautorizador.core.exception.StatusTransacaoException;
import br.com.decimal.miniautorizador.core.port.inbound.RealizarTransacaoDebitoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class RealizarTransacaoDebitoUseCase implements RealizarTransacaoDebitoService {
	
	@Autowired
	private final CartaoRepositoryPort cartaoRepositoryPort;

	@Override
	public void autorizarTransacao(String numeroCartao, String senha, BigDecimal valor) {
		Long numeroCartaoTmp = Long.parseLong(numeroCartao);
        log.info("Executando o serviço de criação de cartão {}", numeroCartaoTmp);
        
        Optional<Cartao> cartaoExistente = cartaoRepositoryPort.findByNumeroCartao(numeroCartaoTmp);
        
        Cartao cartao = cartaoExistente.orElseThrow(() -> new StatusTransacaoException("CARTAO_INEXISTENTE"));
        
        if (!cartao.isSenhaCorreta(senha)) {
            throw new StatusTransacaoException("SENHA_INVALIDA");
        }
        
        cartao.debito(valor);
        cartaoRepositoryPort.save(cartao);
		
	}

}
