package mensagens;

import java.io.*;

public class PaymentInstructions implements Serializable{
	
	private static final long serialVersionUID = 15L;
	
	private int tid;
	private int nCartao;
	private int nSeguranca;
	private String moradaDonoCartao;
	private int quantia;
	
	
    /**
     * Construtor da classe
     * @param tid O identificador de transaccao
     * @param nCartao O numero do cartao de credito
     * @param nSeguranca O numero de seguranca do cartao de credito
     * @param quantia A quantia a ser transaccionada
     */
	public PaymentInstructions(int tid, int nCartao, int nSeguranca, String moradaDonoCartao, int quantia){
		
		this.tid = tid;
		this.nCartao = nCartao;
		this.nSeguranca = nSeguranca;
		this.moradaDonoCartao = moradaDonoCartao;
		this.quantia = quantia;
	}
	
    /**
     * Devolve uma representacao do objecto em string
     * @return String A representacao do objecto
     */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("-- PI:");
		sb.append(" tid: ");
		sb.append(tid);
		sb.append(" nCartao: ");
		sb.append(nCartao);
		sb.append(" nSeguranca: ");
		sb.append(nSeguranca);
		sb.append(" quantia: ");
		sb.append(quantia);		
		sb.append(" moradaDonoCartao: ");
		sb.append(moradaDonoCartao);		
		return sb.toString();
	}
}
