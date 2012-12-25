package mensagens;

import java.io.Serializable;

import cripto.Certificado;
import cripto.Criptografia;
import cripto.DadosCifrados;

public class PurchaseRequest implements Serializable{
	
	private static final long serialVersionUID = 18L;
	private DadosCifrados chaveSimetrica1Cifrada;
	private DadosCifrados piCifrado;
	private DadosCifrados oiCifrado;
	private DadosCifrados sinteseOiCifrada;
	private DadosCifrados sintesePiCifrada;
	private DadosCifrados duplaCifrada;
	private DadosCifrados usernameCifrado;
	private DadosCifrados sintesePasswordCifrada;
	
    /**
     * Construtor da classe
     * @param tid O identificador de transaccao
     * @param nCartao O numero do cartao de credito
     * @param nSeguranca O numero de seguranca do cartao de credito
     * @param quantia A quantia a ser transaccionada
     * @param prodId O identificador do produto
     * @param certV O certificado do vendedor
     * @param certG O certificado da gateway
     * @param username O nome de utilizador no sistema do vendedor
     * @param password A password no sistema do vendedor
     */
	public PurchaseRequest(int tid, int nCartao, int nSeguranca, String moradaDonoCartao, int quantia, int prodId, Certificado certV,
			Certificado certG, String username, String password){
		
		//***PASSO 8***
		//cria a Order Information
		OrderInformation oi = new OrderInformation(tid,prodId);
		//cifra a Order Information com a chave publica do vendedor
		oiCifrado = Criptografia.cifrar(oi.toString(), certV.getChavePublica());
		
		//***PASSO 9***
		PaymentInstructions pi = new PaymentInstructions(tid, nCartao, nSeguranca, moradaDonoCartao, quantia);
		
		//***PASSO 10***
		String sinteseOi = Criptografia.sintese(oi.toString());
		String sintesePi = Criptografia.sintese(pi.toString());
		String dupla = Criptografia.sintese((sinteseOi + sintesePi));
		
		//aqui deveria-se assinar dupla com a chave privada do cliente
		//nao pode ser feito pois o cliente nao possui certificado
		
		//cifra as sinteses dos dados para que apenas os destinatarios os possam confirmar
		sinteseOiCifrada = Criptografia.cifrar(sinteseOi,certG.getChavePublica());
		sintesePiCifrada = Criptografia.cifrar(sintesePi,certV.getChavePublica());
		
		//gera uma sintese para a password
		String sintesePassword = Criptografia.sintese(password);
		//assina a dupla assinatura com a sintese da password
		duplaCifrada = Criptografia.cifrar(dupla, sintesePassword.length());

		//de forma a garantir autenticacao por parte do cliente
		//partimos do principio que esta registado no sistema do vendedor
		//por meio de um par username/password
		
		//cifra a sintese da password para o vendedor com a chave publica deste
		sintesePasswordCifrada = Criptografia.cifrar(sintesePassword, certV.getChavePublica());
		
		//cifra o username com a chave publica do vendedor
		usernameCifrado = Criptografia.cifrar(username, certV.getChavePublica());
		
		
		//***PASSO 11***
		int chaveSimetrica1 = Criptografia.geraChaveSimetrica();
		piCifrado = Criptografia.cifrar(pi.toString(), chaveSimetrica1);
		chaveSimetrica1Cifrada = Criptografia.cifrar(Integer.toString(chaveSimetrica1),
				certG.getChavePublica());		
	}
	
    /**
     * Devolve a chave simetrica 1 cifrada
     * @return chaveSimetrica1Cifrada A chave simetrica 1 cifrada com a chave
     * 								  publica da gateway
     */
	public DadosCifrados getChaveSimetricaCifrada(){
		return chaveSimetrica1Cifrada;
	}
	
    /**
     * Devolve o PI cifrado
     * @return piCifrado O PI cifrado com a chave simetrica 1
     */
	public DadosCifrados getPiCifrado(){
		return piCifrado;
	}
	
    /**
     * Devolve o OI cifrado
     * @return oiCifrado O OI cifrado com a chave publica do vendedor
     */
	public DadosCifrados getOiCifrado(){
		return oiCifrado;
	}
	
    /**
     * Devolve a sintese do OI cifrada
     * @return sinteseOiCifrada A sintese do OI cifrada com a chave publica do vendedor
     */
	public DadosCifrados getSinteseOiCifrada(){
		return sinteseOiCifrada;
	}
	
    /**
     * Devolve a sintese do PI cifrada  
     * @return sintesePiCifrada A sintese do PI cifrada com a chave publica da gateway
     */
	public DadosCifrados getSintesePiCifrada(){
		return sintesePiCifrada;
	}
	
    /**
     * Devolve a dupla assinatura cifrada
     * @return duplaCifrada A dupla assinatura cifrada com a sintese da password
     */
	public DadosCifrados getDuplaAssinaturaCifrada(){
		return duplaCifrada;
	}
	
    /**
     * Devolve o username cifrado
     * @return usernameCifrado O username cifrado com a chave publica do vendedor
     */
	public DadosCifrados getUsernameCifrado(){
		return usernameCifrado;
	}
	
    /**
     * Devolve a sintese da password cifrada
     * @return sintesePasswordCifrada A sintese da password cifrada com a chave publica
     * 								  do vendedor
     */
	public DadosCifrados getSintesePasswordCifrada(){
		return sintesePasswordCifrada;
	}
}
