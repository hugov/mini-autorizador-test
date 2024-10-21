package br.com.decimal.miniautorizador.core.usecase;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.decimal.miniautorizador.adapter.outbound.CartaoRepositoryPort;
import br.com.decimal.miniautorizador.core.domain.Cartao;
import br.com.decimal.miniautorizador.core.port.inbound.ConsultarSaldoCartaoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class ConsultarSaldoCartaoUseCase implements ConsultarSaldoCartaoService {
	
	@Autowired
	private final CartaoRepositoryPort cartaoRepositoryPort;

	@Override
	public BigDecimal consultarSaldoCartao(Long numeroCartao) {
		log.info("Consultando o saldo do cartão {}", numeroCartao);

        Optional<Cartao> cartaoExistente = cartaoRepositoryPort.findByNumeroCartao(numeroCartao);
        return cartaoExistente.orElseThrow(() -> new RuntimeException("Cartão não existe")).getSaldo();
        
	}

}
