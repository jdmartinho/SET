package mensagens;

import java.io.*;
import cripto.*;

public class CaptureResponse implements Serializable{
	
	private static final long serialVersionUID = 23L;
	
	private DadosCifrados respostaCifrada;
	private DadosAssinados sinteseRespostaAssinada;
	private DadosCifrados chaveSimetrica6Cifrada;
	private Certificado certG;
	
    /**
     * Construtor da classe
     * @param respostaBanco A resposta ao pedido de captura 
     * @param certV O certificado do vendedor
     * @param certG O certificado da gateway
     */
	public CaptureResponse(String respostaBanco, Certificado certV, Certificado certG) {
		
		this.certG = certG;
		
        //***PASSO 11***
		//gera uma sintese da resposta e assina-a com a sua chave privada
		sinteseRespostaAssinada = Criptografia.assinatura(Criptografia.sintese(respostaBanco),certG);
		
        //***PASSO 12***
		//Gera uma nova chave simetrica 6
		int chaveSimetrica6 = Criptografia.geraChaveSimetrica();
		
		//Cifra os dados com a nova chave simetrica 6
		respostaCifrada =  Criptografia.cifrar(respostaBanco,chaveSimetrica6);
		
		//Cifrar a chave simetrica com a chave publica do vendedor
		chaveSimetrica6Cifrada = Criptografia.cifrar(Integer.toString(chaveSimetrica6), certV.getChavePublica());		
	}
	
    /**
     * Devolve a resposta cifrada com a chave simetrica 6
     * @return respostaCifrada A resposta cifrada com a chave simetrica 6
     */
	public DadosCifrados getRespostaCifrada() {
		return respostaCifrada;
	}
	
    /**
     * Devolve uma sintese da resposta assinada
     * @return sinteseRespostaAssinada A sintese da resposta assinada com 
     * 								   a chave privada da gateway
     */
	public DadosAssinados getSinteseRespostaAssinada(){
		return sinteseRespostaAssinada;
	}
	
    /**
     * Devolve a chave simetrica 6 cifrada
     * @return chaveSimetrica6Cifrada A chave simetrica 6 cifrada com a chave 
     * 								  publica do vendedor
     */
	public DadosCifrados getChaveSimetrica6Cifrada() {
		return chaveSimetrica6Cifrada;
	}
	
    /**
     * Devolve o certificado da gateway
     * @return certG O certificado da gateway
     */
	public Certificado getCertGateway(){
		return certG;
	}

}
