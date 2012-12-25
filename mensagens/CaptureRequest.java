package mensagens;

import cripto.*;
import java.io.*;

public class CaptureRequest implements Serializable{
	
	private static final long serialVersionUID = 22L;
	
	private Certificado certV;
	private DadosAssinados dadosAssinados;
	private DadosCifrados chaveSimetrica5Cifrada;
	private DadosCifrados dadosCifrados;
	
	private DadosCifrados captureTokenCifrada;
	private DadosCifrados chaveSimetrica4Cifrada;

    /**
     * Construtor da classe
     * @param tid O identificador da transaccao
     * @param quantiaFinal A quantia a transaccionar
     * @param nConta O numero da conta para a qual quer capturar o dinheiro
     * @param certV O certificado do vendedor
     * @param certG O certificado da gateway
     * @param captureToken A capture token cifrada
     * @param chave4 A chave simetrica 4 cifrada
     */
	public CaptureRequest(int tid, int quantiaFinal, int nConta, Certificado certV,
			Certificado certG, DadosCifrados captureToken, DadosCifrados chave4) {
		
		this.certV = certV;			
		
		//***PASSO 2***
		//gera a sintese dos dados
		String dados = Integer.toString(quantiaFinal) + " " + Integer.toString(tid) + " " + Integer.toString(nConta);
		//assina a sintese dos dados
		dadosAssinados =  Criptografia.assinatura(Criptografia.sintese(dados), certV);
		
		//***PASSO 3***
		//gera a chave simetrica 5
		int chaveSimetrica5 = Criptografia.geraChaveSimetrica();
		
		//Cifrar os dados com a nova chave simetrica
		dadosCifrados = Criptografia.cifrar(dados, chaveSimetrica5);
		
		//Cifrar a chave simetrica com a chave publica da gateway
		chaveSimetrica5Cifrada = Criptografia.cifrar(Integer.toString(chaveSimetrica5), certG.getChavePublica());
		
		//***PASSO 4***
		this.captureTokenCifrada = captureToken;
		this.chaveSimetrica4Cifrada = chave4;		
	}

    /**
     * Devolve o certificado do vendedor
     * @return certV O certificado do vendedor
     */
	public Certificado getCertificado(){
		return certV;
	}
	
    /**
     * Devolve os dados assinados do pedido de autorizacao
     * @return dadosAssinados Consiste na sintese da quantia, tid e numero de conta
     */
	public DadosAssinados getDadosAssinados(){
		return dadosAssinados;
	}
	
    /**
     * Devolve os dados cifrados da mensagem
     * @return dadosCifrados Consiste na quantia, no tid e no numero de conta
     */
	public DadosCifrados getDadosCifrados() {
		return dadosCifrados;
	}
	
    /**
     * Devolve a chave simetrica 5 cifrada
     * @return chaveSimetrica5Cifrada Chave simetrica 5 cifrada com a chave publica
     * 								  da gateway
     */
	public DadosCifrados getChaveSimetrica5Cifrada() {
		return chaveSimetrica5Cifrada;
	}
	
    /**
     * Devolve a capture token cifrada
     * @return captureTokenCifrada Capture Token cifrada com a chave simetrica 4
     */
	public DadosCifrados getCaptureTokenCifrada(){
		return captureTokenCifrada;
	}
	
    /**
     * Devolve a chave simetrica 4 cifrada
     * @return chaveSimetrica4Cifrada A chave simetrica 4 cifrada com a chave 
     * 								  publica da gateway
     */
	public DadosCifrados getChaveSimetrica4Cifrada(){
		return chaveSimetrica4Cifrada;
	}
	
}
