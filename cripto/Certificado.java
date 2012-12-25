package cripto;
import java.io.*;

public class Certificado implements Serializable{

	private static final long serialVersionUID = 11L;
	
	private int chavePublica;
	private int chavePrivada;
	private boolean validade;
	
    /**
     * Cria o certificado
     * @param chavePublica 
     * @param chavePrivada 
     */
	public Certificado(int chavePublica, int chavePrivada){
		validade = true;
		this.chavePublica = chavePublica;
		this.chavePrivada = chavePrivada;
	}
	
    /**
     * Obtem a chave publica
     * @return chavePublica
     */
	public int getChavePublica(){
		return chavePublica;
	}
	
    /**
     * Obtem a chave privada
     * @return chavePrivada
     */
	public int getChavePrivada(){
		return chavePrivada;
	}
	
    /**
     * Obtem a validade do certificado
     * @return validade
     */
	public boolean getValidade(){
		return validade;
	}
}
