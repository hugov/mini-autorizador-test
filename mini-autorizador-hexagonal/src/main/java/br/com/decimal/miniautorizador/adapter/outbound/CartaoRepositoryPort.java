package br.com.decimal.miniautorizador.adapter.outbound;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.decimal.miniautorizador.core.domain.Cartao;

@Repository
public interface CartaoRepositoryPort extends JpaRepository<Cartao, Long> {
	
	Optional<Cartao> findByNumeroCartao(Long numeroCartao);

}
