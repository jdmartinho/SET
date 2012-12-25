package gateway;

import mensagens.*;
import java.io.*;
import java.util.*;

public class Mensagem implements Serializable{
	
	private static final long serialVersionUID = 24L;
	
	private int idMsg;
	
	private AuthorizationRequest areq;
	private ArrayList<CaptureRequest> listaRequests;
	private ArrayList<CaptureResponse> listaResponses;
	
    /**
     * Construtor da classe
     * @param id O identificador da mensagem
     * @param areq O pedido de autorizacao
     */
	public Mensagem(int id, AuthorizationRequest areq){
		this.idMsg = id;
		this.areq = areq;		
	}
	
    /**
     * Construtor da classe
     * @param id O identificador da mensagem
     * @param lista A lista de pedidos de captura
     */
	public Mensagem(int id, ArrayList<CaptureRequest> lista){
		this.idMsg = id;
		this.listaRequests = lista;
	}
	
    /**
     * Construtor da classe
     * @param id O identificador da mensagem
     * @param lista1 A lista de pedidos de captura
     * @param lista2 A lista de respostas de captura
     */
	public Mensagem(int id, ArrayList<CaptureRequest> lista1 ,ArrayList<CaptureResponse> lista2){
		this.idMsg = id;
		this.listaRequests = lista1;
		this.listaResponses = lista2;
	}
	
	
	
    /**
     * Devolve o identificador da mensagem
     * @return idMsg O identificador da mensagem 
     */
	public int getId(){
		return idMsg;
	}
	
    /**
     * Devolve o pedido de autorizacao
     * @return areq Pedido de autorizacao
     */
	public AuthorizationRequest getAuthorizationRequest(){
		return areq;
	}
	
    /**
     * Devolve a lista de pedidos de captura
     * @return listaRequests A lista dos pedidos de captura
     */
	public ArrayList<CaptureRequest> getListaRequests(){
		return listaRequests;
	}
	
    /**
     * Devolve a lista de respostas aos pedidos de captura
     * @return listaResponses A lista de respostas aos pedidos de captura
     */
	public ArrayList<CaptureResponse> getListaResponses(){
		return listaResponses;
	}
	
}
