package br.com.decimal.miniautorizador.core.port.inbound;

public interface CriarCartaoService {
	
    void criarCartao(String numeroCartao, String senha);
    
}