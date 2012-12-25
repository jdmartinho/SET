package cripto;

import java.io.*;

public class DadosAssinados implements Serializable{

	private static final long serialVersionUID = 12L;
	private Object dados;
	private Object sintese;
	private int chave;

    /**
     * Cria dados assinados
     * @param dados dados a assinar
     * @param chave chave utilizada
     */
	public DadosAssinados(Object dados, int chave){
		this.chave = chave;
		this.dados = dados;
	}

    /**
     * Obtem os dados 
     * @param chave chave utilizada
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

    /**
     * Obtem a sintese
     * @return sintese
     */
	public Object getSintese(){
		return sintese;
	}



}
