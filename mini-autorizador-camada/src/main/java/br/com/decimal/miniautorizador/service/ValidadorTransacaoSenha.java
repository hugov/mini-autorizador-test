package br.com.decimal.miniautorizador.service;

import org.springframework.stereotype.Service;

import br.com.decimal.miniautorizador.exception.StatusTransacaoException;

@Service
public class ValidadorTransacaoSenha implements ValidadorTransacao<String> {
	
	@Override
    public void validar(String senhaDTO, String senhaCartao) {
        if (!senhaDTO.equals(senhaCartao)) {
            throw new StatusTransacaoException(StatusTransacaoEnum.SENHA_INVALIDA.toString());
        }
    }

}
