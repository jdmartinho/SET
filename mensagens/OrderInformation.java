package mensagens;

import java.io.*;

public class OrderInformation implements Serializable{
	
	private static final long serialVersionUID = 14L;
	private int tid;
	private int prodId;
	
    /**
     * Construtor da classe
     * @param tid O identificador da transaccao
     * @param prodId O identificador do produto
     */
	public OrderInformation(int tid, int prodId){
		this.tid = tid;
		this.prodId = prodId;
	}

    /**
     * Devolve o identificador do produto
     * @return prodId O identificador do produto
     */
	public int getProdId(){
		return prodId;
	}
	
    /**
     * Devolve o identificador de transaccao
     * @return tid O identificador de transaccao
     */
	public int getTid(){
		return tid;
	}
	
    /**
     * Devolve uma representacao do objecto para string
     * @return String A representacao do objecto
     */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("-- OI:");
		sb.append(" tid: ");
		sb.append(tid);
		sb.append(" prodId: ");
		sb.append(prodId);
		
		return sb.toString();
	}

}
