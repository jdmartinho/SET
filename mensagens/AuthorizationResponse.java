package mensagens;

import java.io.*;
import cripto.*;
import gateway.Gateway;

public class AuthorizationResponse implements Serializable{

	private static final long serialVersionUID = 21L; 

	private Certificado certG;
	private DadosAssinados resAssinada;
	private DadosCifrados chaveSimetrica3Cifrada;
	private DadosCifrados respostaCifrada;
	private DadosCifrados chaveSimetrica4Cifrada;
	private DadosCifrados captureTokenCifrada;
	private DadosAssinados captureTokenAssinada;

    /**
     * Construtor da classe
     * @param resposta A resposta ao pedido de autorizacao
     * @param quantia A quantia que foi autorizada
     * @param nConta O numero da conta de onde foi autorizada a transferencia
     * @param tid O identificador da transaccao
     * @param certV O certificado do vendedor
     * @param certG O certificado da gateway
     */
	public AuthorizationResponse(String resposta, int quantia, int nConta, int tid, Certificado certV, Certificado certG){

		this.certG = certG;

		//***PASSO 13***
		//assinar a resposta com a chave privada da gateway
		resAssinada = Criptografia.assinatura(Criptografia.sintese(resposta), certG);

		//***PASSO 14***
		//gerar uma chave simetrica
		int chaveSimetrica3 = Criptografia.geraChaveSimetrica();

		//cifra a resposta com a chave simetrica
		respostaCifrada = Criptografia.cifrar(resposta, chaveSimetrica3);		

		//cifra a chave simetrica com a chave publica do vendedor
		chaveSimetrica3Cifrada = Criptografia.cifrar(Integer.toString(chaveSimetrica3), certV.getChavePublica());

		//***PASSO 15***
		//cria uma capture token, caso a autorizacao tenha sido concedida
		if(resposta.equals("Autorizacao concedida")){
			CaptureToken captk = new CaptureToken(quantia,nConta,tid);
			//adiciona a lista de tokens
			Gateway.adicionaToken(captk);
			System.out.println(captk.toString());
			//gera a sintese da capture token
			String sinteseCaptureToken = Criptografia.sintese(captk.toString());
			//assina a sintese da capture token com a sua chave privada
			captureTokenAssinada = Criptografia.assinatura(sinteseCaptureToken, certG);

			//***PASSO 16***
			//gera uma nova chave simetrica
			int chaveSimetrica4 = Criptografia.geraChaveSimetrica();
			//cifra a capture token com esta chave simetrica
			captureTokenCifrada = Criptografia.cifrar(captk.toString(), chaveSimetrica4);
			//cifra a chave 4 com a chave privada da gateway
			chaveSimetrica4Cifrada = Criptografia.cifrar(Integer.toString(chaveSimetrica4), certG.getChavePrivada());
		}
	}

    /**
     * Devolve o certificado da gateway
     * @return certG O certificado da gateway
     */
	public Certificado getCertGateway(){
		return certG;
	}

    /**
     * Devolve a sintese da resposta assinada
     * @return resAssinada A sintese da resposta assinada com a chave privada
     * 					   da gateway
     */
	public DadosAssinados getResAssinada(){
		return resAssinada;
	}

    /**
     * Devolve a chave simetrica 3 cifrada
     * @return chaveSimetrica3Cifrada A chave simetrica 3 cifrada com a chave 
     * 								  publica do vendedor
     */
	public DadosCifrados getChaveSimetrica3Cifrada(){
		return chaveSimetrica3Cifrada;
	}

    /**
     * Devolve a chave simetrica 4 cifrada
     * @return chaveSimetrica4Cifrada A chave simetrica 4 cifrada com a chave 
     * 								  publica da gateway
     */
	public DadosCifrados getChaveSimetrica4Cifrada(){
		return chaveSimetrica4Cifrada;
	}

    /**
     * Devolve a resposta cifrada 
     * @return respostaCifrada A resposta cifrada com a chave simetrica 3
     */
	public DadosCifrados getRespostaCifrada(){
		return respostaCifrada;
	}

    /**
     * Devolve a capture token cifrada
     * @return captureTokenCifrada Capture Token cifrada com a chave simetrica 4
     */
	public DadosCifrados getCaptureTokenCifrada(){
		return captureTokenCifrada;
	}

    /**
     * Devolve a sintese da capture token assinada
     * @return captureTokenAssinada A sintese da capture token assinada com 
     * 							    a chave privada da gateway
     */
	public DadosAssinados getCaptureTokenAssinadaCifrada(){
		return captureTokenAssinada;
	}

}
