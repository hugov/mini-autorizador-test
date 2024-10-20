package br.com.decimal.miniautorizador.core.port.inbound;

public interface CartaoService {
	
    void criarCartao(String numeroCartao, String senha);
    
}