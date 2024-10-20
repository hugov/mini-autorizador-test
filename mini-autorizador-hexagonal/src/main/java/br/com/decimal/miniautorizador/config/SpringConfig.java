package br.com.decimal.miniautorizador.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.decimal.miniautorizador.adapter.outbound.CartaoRepositoryPort;
import br.com.decimal.miniautorizador.core.port.inbound.CriarCartaoService;
import br.com.decimal.miniautorizador.core.usecase.CriarCartaoUseCase;

@Configuration
public class SpringConfig {

    @Bean
    public CriarCartaoService cartaoService(CartaoRepositoryPort cartaoRepositoryPort) {
        return new CriarCartaoUseCase(cartaoRepositoryPort);
    }
    
}