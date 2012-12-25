package mensagens;

import java.io.Serializable;

import cripto.*;

public class PurchaseResponse implements Serializable {
	
	private static final long serialVersionUID = 19L;
	private DadosAssinados assinatura;
	private String resposta;
	
    /**
     * Construtor da classe
     * @param resposta A resposta do vendedor
     * @param certV O certificado do vendedor
     */
	public PurchaseResponse(String resposta, Certificado certV){
		this.resposta = resposta;
		assinatura = Criptografia.assinatura(Criptografia.sintese(resposta), certV);
	}
	
    /**
     * Devolve a sintese da resposta assinada
     * @return assinatura A sintese da resposta assinada com a chave privada do vendedor
     */
	public DadosAssinados getAssinatura(){
		return assinatura;
	}
	
    /**
     * Devolve a resposta do vendedor
     * @return resposta A resposta do vendedor serve de comprovativo de compra
     * 					para o cliente
     */
	public String getResposta(){
		return resposta;
	}

}
