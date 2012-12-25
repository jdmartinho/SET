package mensagens;
import java.io.Serializable;

public class InitiateRequest implements Serializable{
	
	private String tipoCartao;
	private static final long serialVersionUID = 16L;
	
    /**
     * Construtor da classe
     * @param tipoCartao O tipo de cartao que o cliente tem
     */
	public InitiateRequest(String tipoCartao){
		this.tipoCartao = tipoCartao;	 
	}
	
    /**
     * Devolve o tipo de cartao
     * @return tipoCartao O tipo de cartao que o cliente tem
     */
	public String getTipoCartao(){
		return this.tipoCartao;
	}
	
    /**
     * Altera o tipo de cartao
     * @param tipoCartao O novo tipo de cartao
     */
	public void setTipoCartao(String tipoCartao){
		this.tipoCartao = tipoCartao;
	}
}
