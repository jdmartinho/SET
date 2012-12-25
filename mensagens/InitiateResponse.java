package mensagens;

import java.io.Serializable;

import cripto.Certificado;
import cripto.Criptografia;
import cripto.DadosAssinados;
import cripto.DadosCifrados;

public class InitiateResponse implements Serializable{
	
	private Certificado certVendedor;
	private Certificado certGateway;
	private DadosAssinados dadosAssinados;
	private DadosCifrados tidCifrado;	
	private static final long serialVersionUID = 17L;
		
    /**
     * Construtor da classe
     * @param tid O identificador da transaccao
     * @param certV O certificado do vendedor
     * @param certG O certificado da gateway
     */
	public InitiateResponse(int tid, Certificado certV, Certificado certG){		
		certVendedor = certV;
		certGateway = certG;
		//cifra o tid com a sua chave privada
		tidCifrado = Criptografia.cifrar(Integer.toString(tid), certVendedor.getChavePrivada());
		//assina a síntese do tid com a sua chave privada
		dadosAssinados = Criptografia.assinatura(Criptografia.sintese(Integer.toString(tid)), certV);		
	}
	
    /**
     * Devolve o certificado do vendedor
     * @return certVendedor O certificado do vendedor
     */
	public Certificado getCertVendedor(){
		return certVendedor;
	}
	
    /**
     * Devolve o certificado da gateway
     * @return certGateway O certificado da gateway
     */
	public Certificado getCertGateway(){
		return certGateway;
	}
	
    /**
     * Devolve a sintese dos dados assinada com a chave privada do vendedor
     * @return dadosAssinados Sintese dos dados assinada com a chave privada do vendedor
     */
	public DadosAssinados getAssinatura(){
		return dadosAssinados;
	}
	
    /**
     * Devolve os identificador de transaccao cifrado
     * @return tidCifrado O identificador de transaccao cifrado com a chave
     * 					  privada do vendedor
     */
	public DadosCifrados getTidCifrado(){
		return tidCifrado;
	}

}
