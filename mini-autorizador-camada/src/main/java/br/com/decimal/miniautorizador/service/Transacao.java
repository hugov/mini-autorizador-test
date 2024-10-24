package br.com.decimal.miniautorizador.service;

import br.com.decimal.miniautorizador.dto.TransacaoDTO;

public interface Transacao {
	
	String realizarTransacao(TransacaoDTO transacaoDTO);

}
