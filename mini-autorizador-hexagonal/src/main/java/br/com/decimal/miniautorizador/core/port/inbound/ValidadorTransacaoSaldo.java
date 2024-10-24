package br.com.decimal.miniautorizador.core.port.inbound;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import br.com.decimal.miniautorizador.core.exception.StatusTransacaoException;

@Service
public class ValidadorTransacaoSaldo implements ValidadorTransacao<BigDecimal> {
	
	@Override
    public void validar(BigDecimal valorTransacao, BigDecimal saldo) {
        if (saldo.compareTo(valorTransacao) < 0)
            throw new StatusTransacaoException(StatusTransacaoEnum.SALDO_INSUFICIENTE.toString());
    }

}
