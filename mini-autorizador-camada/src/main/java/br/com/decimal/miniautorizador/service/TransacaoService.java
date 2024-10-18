package br.com.decimal.miniautorizador.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.decimal.miniautorizador.dto.TransacaoDTO;
import br.com.decimal.miniautorizador.entity.Cartao;
import br.com.decimal.miniautorizador.entity.Transacao;
import br.com.decimal.miniautorizador.exception.StatusTransacaoException;
import br.com.decimal.miniautorizador.repository.CartaoRepository;
import br.com.decimal.miniautorizador.repository.TransacaoRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransacaoService {
	
	@Autowired
	private TransacaoRepository transacaoRepository;
	
	@Autowired
	private CartaoRepository cartaoRepository;
	
	enum StatusTransacaoEnum {
		OK,
	    SALDO_INSUFICIENTE,
	    SENHA_INVALIDA,
	    CARTAO_INEXISTENTE
	}
	
	public String realizarTransacao(TransacaoDTO transacaoDTO) {
		 Long numeroCartao = Long.parseLong(transacaoDTO.getNumeroCartao());
	     log.info(String.format("Obtendo o cartão %d da transação", numeroCartao));
	     
	     Transacao transacao = new Transacao();
	     transacao.setNumeroCartao(numeroCartao);
	     transacao.setSenhaCartao(transacaoDTO.getSenhaCartao());
	     transacao.setValor(transacaoDTO.getValor());
	     transacaoRepository.save(transacao);
		
	     Optional<Cartao> cartaoExistente = cartaoRepository.findByNumeroCartao(numeroCartao);
	     
	     if(cartaoExistente.isPresent()) {
	    	 Cartao cartao = cartaoExistente.get();
	    	 
	    	 if(cartao.getSenha().equals(transacaoDTO.getSenhaCartao())) {
	    		 
	    		 BigDecimal saldo = cartao.getSaldo();
	    		 if(saldo.doubleValue() > 0 && saldo.doubleValue() >= transacaoDTO.getValor().doubleValue()) {
	    			 cartao.setSaldo(cartao.getSaldo().subtract(transacaoDTO.getValor()));
	    			 cartaoRepository.save(cartao);
	    		 } else {
	    			 throw new StatusTransacaoException(StatusTransacaoEnum.SALDO_INSUFICIENTE.toString());
	    		 }
	    		 
	    	 } else {
	    		 throw new StatusTransacaoException(StatusTransacaoEnum.SENHA_INVALIDA.toString());
	    	 }
	    	 
	     } else {
	    	 throw new StatusTransacaoException(StatusTransacaoEnum.CARTAO_INEXISTENTE.toString());
	     }
		
		return StatusTransacaoEnum.OK.toString();
		
    }
	
}
