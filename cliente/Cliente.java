package cliente;

import java.io.*;

import cripto.*;
import mensagens.*;

public class Cliente {
	
	// certificado do vendedor e da gateway
	private static Certificado certV;
	private static Certificado certG;
	
	// interface e comunicacao do cliente
	private ClienteInterface clif;
	private ClienteComunicacao clicom;
	
	/**
	 * Main
	 * @param args args
	 */
	public static void main(String[] args) {	
		new Cliente();
				
	}//main 
	
	
	
	/** Creates a new instance of Cliente */
	public Cliente() {
		// cria a interface responsavel por obter os dados do cliente
		this.clif = new ClienteInterface();
		// cria a comunicacao necessaria do cliente
		this.clicom = new ClienteComunicacao(clif.getIp(), clif.getPorto());
		// efectua o pedido de compra ao vendedor (Algoritmo 4.4)
		this.pedidoCompra(clif.getUsername(), clif.getPassword(), clif.getTipoCartao(), clif.getNCartao(), clif.getNSeguranca(), clif.getQuantia(), clif.getProdID());
	}
	
	/**
	 * Envia um pedido de compra a um vendedor
	 * @param username nome do utilizador registado no vendedor
	 * @param password password do utilizador registado no vendedor
	 * @param tipoCartao tipo de cartao do utilizador
	 * @param nCartao numero do cartao do utilizador
	 * @param nSeguranca numero de seguranca do cartao do utilizador
	 * @param quantia preco do produto a comprar
	 * @param prodId identificador do produto a comprar
	 */
	private void pedidoCompra(String username, String password, String tipoCartao,int nCartao, int nSeguranca, int quantia, int prodId){
		//mensagens que vao ser trocadas
		InitiateRequest initreq = null;
		InitiateResponse initres = null;
		PurchaseRequest preq = null;
		PurchaseResponse pres = null; 
		
		//variaveis auxiliares
		String dados = null;
		int tid = 0;
		String resposta = null;
		String sinteseResposta = null;
		
		//--------------ALGORITMO 4.4----------------
		
		//***PASSO 1***
		// o utilizador escolhe o produto
		
		//***PASSO 2***
		//cria InitiateRequest e envia para o vendedor
		initreq = new InitiateRequest(tipoCartao);
		System.out.println("<< InitiateRequest");
		try{
			clicom.getOutVendedor().writeObject(initreq);
		}catch(IOException e){
			System.out.println(e.toString());
		}
		
		//***PASSO 6***
		//recebe InitiateResponse do vendedor
		try{
			initres = (InitiateResponse) clicom.getInVendedor().readObject();
		}catch(Exception e){
			System.out.println(e.toString());
		}
		System.out.println(">> InitiateResponse");
		
		//verifica a validade dos certificados recebidos
		if(initres.getCertVendedor().getValidade()){
			certV = initres.getCertVendedor();
		}
		if(initres.getCertGateway().getValidade()){
			certG = initres.getCertGateway();
		}		
		
		//***PASSO 7***
		//verifica a assinatura dos dados que recebeu
		Criptografia.verificaAssinatura(initres.getAssinatura(), initres.getCertVendedor());					
		
		//retira a sintese da assinatura
		dados = initres.getAssinatura().getDados(certV.getChavePrivada()).toString();
		
		//decifra o tid usando a chave publica do vendedor
		tid = Integer.parseInt(Criptografia.decifrar(initres.getTidCifrado(), certV.getChavePrivada()).toString());
		
		//cria uma sintese para o tid e compara com a recebida
		Criptografia.verificaSintese(Integer.toString(tid),dados);			
		
		//***PASSO 8,9,10,11***
		//cliente cria PuchaseRequest para enviar ao vendedor
		preq = new PurchaseRequest(tid,nCartao, nSeguranca, clif.getMoradaDonoCartao(),quantia,prodId,certV,certG,username,password);
		System.out.println("<< PurchaseRequest");
		
		//***PASSO 12***
		try{
			clicom.getOutVendedor().writeObject(preq);
		}catch(IOException e){
			System.out.println(e.toString());
		}
		
		//recepcao da resposta final do vendedor
		
		System.out.println(">> PurchaseResponse");
		try{
			pres = (PurchaseResponse) clicom.getInVendedor().readObject();
		}catch(Exception e){
			System.out.println(e.toString());
		}
		
		//verifica a validade do certificado do vendedor e actualiza
		if(initres.getCertVendedor().getValidade()){
			certV = initres.getCertVendedor();
		}
		
		//confirma a assinatura e obtem a sintese da resposta
		//verifica a assinatura dos dados que recebeu
		Criptografia.verificaAssinatura(pres.getAssinatura(),certV);
		sinteseResposta = pres.getAssinatura().getDados(certV.getChavePrivada()).toString();
		resposta = pres.getResposta();
		//compara a sintese recebida com a sintese gerada para a resposta
		Criptografia.verificaSintese(resposta,sinteseResposta);
                
                //imprime resposta
                System.out.println("-- "+resposta);
	}	
}
