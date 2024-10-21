package br.com.decimal.miniautorizador.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.decimal.miniautorizador.entity.Cartao;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {
	
    Optional<Cartao> findByNumeroCartao(Long numeroCartao);
    
}
