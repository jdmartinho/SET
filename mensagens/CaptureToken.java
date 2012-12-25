package mensagens;
import java.io.*;

public class CaptureToken implements Serializable{
	
	private static final long serialVersionUID = 25L;
	
	private int quantia;
	private int tid;
	private int nConta;
	
    /**
     * Construtor da classe
     * @param quantia A quantia autorizada
     * @param nConta O numero da conta de onde foi autorizada a transferencia
     * @param tid O identificador da transaccao
     */
	public CaptureToken(int quantia, int nConta, int tid){
		this.quantia = quantia;
		this.tid = tid;
		this.nConta = nConta;		
	}
	
    /**
     * Devolve a quantia autorizada
     * @return quantia A quantia que pode ser transferida
     */
	public int getQuantia(){
		return quantia;
	}
	
    /**
     * Devolve o numero da conta
     * @return nConta O numero da conta de onde foi autorizada a transferencia
     */
	public int getNConta(){
		return nConta;
	}
	
    /**
     * Devolve o identificador de transaccao
     * @return tid O identificador da transaccao
     */
	public int getTid(){
		return tid;
	}
	
    /**
     * Devolve uma representacao da capture token em string
     * @return String A representacao da capture token 
     */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("-- CaptureToken:");
		sb.append(" quantia: ");
		sb.append(quantia);
		sb.append(" nConta: ");
		sb.append(nConta);
		sb.append(" tid: ");
		sb.append(tid);
		
		return sb.toString();
	}

}
