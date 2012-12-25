package cripto;

import java.io.*;

public class DadosCifrados implements Serializable{
	
	private static final long serialVersionUID = 13L;
	private Object dados;
	private int chave;
	
    /**
     * Cria dados cifrados
     * @param dados dados a cifrar
     * @param chave chave utilizada
     */
	public DadosCifrados(Object dados, int chave){
		this.chave = chave;
		this.dados = dados;
	}
	
    /**
     * Obtem os dados
     * @param chave 
     * @return dados
     */
	public Object getDados(int chave){
		if(this.chave == chave){
			return dados;
		}else return "";		
	}

    /**
     * Obtem a chave
     * @return chave
     */
	public int getChave(){
		return chave;
	}
	

}
