package br.com.decimal.miniautorizador.core.usecase;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.decimal.miniautorizador.adapter.outbound.CartaoRepositoryPort;
import br.com.decimal.miniautorizador.core.domain.Cartao;
import br.com.decimal.miniautorizador.core.port.inbound.CartaoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class CriarCartaoUseCase implements CartaoService {
	
	@Autowired
	private final CartaoRepositoryPort cartaoRepositoryPort;

	@Override
	public void criarCartao(String numeroCartao, String senha) {
		Long numeroCartaoTmp = Long.parseLong(numeroCartao);
        log.info("Executando o serviço de criação de cartão {}", numeroCartao);

        Optional<Cartao> cartaoExistente = cartaoRepositoryPort.findByNumeroCartao(numeroCartaoTmp);
        if (cartaoExistente.isPresent()) {
            throw new RuntimeException("Cartão já existe");
        }
        
		Cartao cartao = new Cartao(numeroCartaoTmp, senha);
		cartaoRepositoryPort.save(cartao);
	}

}
