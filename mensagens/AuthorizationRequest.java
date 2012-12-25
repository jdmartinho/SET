package mensagens;
import cripto.*;

public class AuthorizationRequest implements java.io.Serializable{

	private static final long serialVersionUID = 20L; 


	private DadosAssinados dadosAssinados;
	private DadosCifrados dadosCifrados;
	private DadosCifrados chaveSimetrica2Cifrada;
	private Certificado certV;
	private DadosCifrados chaveSimetrica1Cifrada;
	private DadosCifrados piCifrado;
	private DadosCifrados sinteseOiCifrado;
	private DadosCifrados duplaCifrada;


	/**
	 * Construtor da classe
	 * @param quantia A quantia para a qual vai pedir autorizacao
	 * @param tid O identificador da transaccao
	 * @param certV O certificado do vendedor
	 * @param certG O certificado da gateway
	 * @param chaveSimetrica1Cifrada A chave simetrica cifrada que o cliente enviou
	 * @param piCifrado O PI cifrado que o cliente enviou
	 * @param sinteseOiCifrado A sintese do OI cifrada que o cliente enviou
	 * @param duplaCifrada A dupla assinatura cifrada com a chave privada do vendedor
	 */
	public AuthorizationRequest(int quantia, int tid, Certificado certV, Certificado certG, 
			DadosCifrados chaveSimetrica1Cifrada, DadosCifrados piCifrado, DadosCifrados sinteseOiCifrado,
			DadosCifrados duplaCifrada) {
		
		//***PASSO 1***
		//criar os dados de pedido de autotizacao
		String dados = Integer.toString(quantia)+ " " + Integer.toString(tid);
		
		//***PASSO 2***
		//criar a assinatura dos dados para enviar a gateway
		dadosAssinados = Criptografia.assinatura(Criptografia.sintese(dados), certV);
		
		//***PASSO 3***
		//gera uma chave simetrica
		int chaveSimetrica2 = Criptografia.geraChaveSimetrica();
		//cifra os dados com a chave simetrica
		dadosCifrados = Criptografia.cifrar(dados, chaveSimetrica2);
		//cifra a chave simetrica com a chave publica da gateway
		chaveSimetrica2Cifrada = Criptografia.cifrar(Integer.toString(chaveSimetrica2), certG.getChavePublica());
		
		//***PASSO 4***
		//outras informacoes provenientes do cliente para enviar
		this.chaveSimetrica1Cifrada = chaveSimetrica1Cifrada;
		this.piCifrado = piCifrado;
		this.sinteseOiCifrado = sinteseOiCifrado;
		this.duplaCifrada = duplaCifrada;
		this.certV = certV;
	}
	
    /**
     * Devolve os dados assinados do pedido de autorizacao
     * @return dadosAssinados Consiste na sintese da quantia e no tid
     */
	public DadosAssinados getDadosAssinados(){
		return dadosAssinados;
	}
	
    /**
     * Devolve os dados cifrados da mensagem
     * @return dadosCifrados Consiste na quantia e no tid
     */
	public DadosCifrados getDadosCifrados(){
		return dadosCifrados;
	}
	
    /**
     * Devolve a chave simetrica 2 cifrada
     * @return chaveSimetrica2Cifrada Chave simetrica 2 cifrada com a chave publica
     * 								  da gateway
     */
	public DadosCifrados getChaveSimetrica2Cifrada(){
		return chaveSimetrica2Cifrada;
	}
	
    /**
     * Devolve a chave simetrica 1 cifrada
     * @return chaveSimetrica1Cifrada A chave simetrica 1 cifrada com a chave publica
     * 								  da gateway
     */
	public DadosCifrados getChaveSimetrica1Cifrada(){
		return chaveSimetrica1Cifrada;
	}
	
    /**
     * Devolve a Payment Instructions cifrada
     * @return piCifrado O PI cifrado com a chave simetrica 1
     */
	public DadosCifrados getPiCifrado(){
		return piCifrado;
	}
	
    /**
     * Devolve a sintese do OI cifrada
     * @return sinteseOiCifrado A sintese do OI cifrada com a chave publica da gateway
     */
	public DadosCifrados getSinteseOiCifrada(){
		return sinteseOiCifrado;
	}
	
    /**
     * Devolve a assinatura dupla cifrada 
     * @return duplaCifrada A assinatura dupla cifrada com a chave privada do vendedor
     */
	public DadosCifrados getDuplaCifrada(){
		return duplaCifrada;
	}
	
    /**
     * Devolve a o certificado do vendedor
     * @return certV O certificado do vendedor
     */
	public Certificado getCertificadoVendedor(){
		return certV;
	}

}
