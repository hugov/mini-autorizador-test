package br.com.decimal.miniautorizador.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.decimal.miniautorizador.adapter.outbound.CartaoRepositoryPort;
import br.com.decimal.miniautorizador.core.port.inbound.CartaoService;
import br.com.decimal.miniautorizador.core.usecase.CriarCartaoUseCase;

@Configuration
public class SpringConfig {

    @Bean
    public CartaoService cartaoService(CartaoRepositoryPort cartaoRepositoryPort) {
        return new CriarCartaoUseCase(cartaoRepositoryPort);
    }
    
}