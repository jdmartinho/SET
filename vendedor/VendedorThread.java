package vendedor;

import java.net.Socket;
import java.io.*;
import java.util.*;

import gateway.Mensagem;
import mensagens.*;
import cripto.*;

class VendedorThread extends Thread {

	private Socket socketCliente = null;
	ObjectOutputStream outCliente = null;
	ObjectInputStream inCliente = null;
	ObjectInputStream inGateway = null;
	ObjectOutputStream outGateway = null;

	InitiateRequest initreq;
	InitiateResponse initres;
	PurchaseRequest preq;
	PurchaseResponse pres;
	AuthorizationRequest areq;
	AuthorizationResponse ares;
	CaptureRequest creq;
	CaptureResponse cres;

	private int tid;
	private int quantiaFinal;
	private int prodId;
	private int contaVendedor;
	private Certificado certV;
	private Certificado certG;
	private OrderInformation oi;
	private DadosCifrados captureTokenCifrada;
	private DadosCifrados chaveSimetrica4Cifrada;

	/**
	 * Construtor da classe
	 * @param cliente Socket para o cliente
	 * @param inGateway Stream de entrada da gateway
	 * @param outGateway Stream de saida para a gateway
	 * @param tid Identificador da transaccao
	 * @param contaVendedor O numero da conta do vendedor
	 * @param certV O certificado do vendedor
	 * @param certG O certificado do cliente
	 */
	VendedorThread(Socket cliente, ObjectInputStream inGateway, ObjectOutputStream outGateway, int tid,
			int contaVendedor, Certificado certV, Certificado certG) {
		socketCliente = cliente;
		this.inGateway = inGateway;
		this.outGateway = outGateway;
		System.out.println(">> Novo Cliente: " + socketCliente.getRemoteSocketAddress());
		this.tid = tid;
		this.contaVendedor = contaVendedor;
		this.certV = certV;
		this.certG = certG;
	}

	public void run(){

		String resposta = "Autorizacao negada";
		boolean autorizacao = true;

		try{
			outCliente = new ObjectOutputStream(socketCliente.getOutputStream());
			inCliente = new ObjectInputStream(socketCliente.getInputStream());

		}catch(IOException e){
			System.out.println(e.toString());
		}
		
		//--------------ALGORITMO 4.4----------------

		//***PASSO 3***
		//vendedor recebe pedido inicial InitiateRequest do cliente
		try{
			initreq = (InitiateRequest) inCliente.readObject();
		}catch(Exception e){
			System.out.println(e.toString());
		}

		System.out.println(
				">> InitiateRequest: " +socketCliente.getRemoteSocketAddress()
				+ " Tipo de cartao: " + initreq.getTipoCartao());

		//***PASSO 4***
		//criar uma resposta do tipo InitiateResponse
		initres = new InitiateResponse(tid,certV,certG);
		System.out.println("<< InitiateResponse: " + socketCliente.getRemoteSocketAddress());

		//***PASSO 5***
		try{
			outCliente.writeObject(initres);
		}catch(IOException e){
			System.out.println(e.toString());
		}

		//***PASSO 13***
		//o vendedor recebe o PurchaseRequest do cliente
		try{
			preq = (PurchaseRequest) inCliente.readObject();
		}catch(Exception e){
			System.out.println(e.toString());
		}
		System.out.println(">> PurchaseRequest: " +socketCliente.getRemoteSocketAddress());

		// bateria de testes
		while(autorizacao){
			//***PASSO 14***
			//obtem o username decifrando-o com a sua chave privada
			String username = Criptografia.decifrar(preq.getUsernameCifrado(), certV.getChavePublica()).toString();
			//obtem a sintese da password decifrando-a com a sua chave privada
			String sintesePassword = Criptografia.decifrar(preq.getSintesePasswordCifrada(), certV.getChavePublica()).toString();
			//verifica se o utilizador e valido no sistema
			if(Vendedor.verificaUtilizador(username, sintesePassword)){

				System.out.println("-- Utilizador/Password valido");
			}else{
				System.out.println("-- Utilizador/Password invalido");
				break;
			}

			//o vendedor decifra a dupla assinatura cifrada usando a sintese da password que recebeu do vendedor
			String duplaAssinatura = Criptografia.decifrar(preq.getDuplaAssinaturaCifrada(), sintesePassword.length()).toString();

			//o vendedor obtem o OrderInformation decifrando-o com a sua chave privada
			String oiStr = Criptografia.decifrar(preq.getOiCifrado(), certV.getChavePublica()).toString();
			Scanner sc = new Scanner(oiStr);
			int cont = 0;
			int tidOi = 0;
			int prodIdOi = 0;
			while(sc.hasNext()){
				if(sc.hasNextInt()){
					if(cont==0) tidOi = sc.nextInt();
					if(cont==1) prodIdOi = sc.nextInt();					
					cont++;
				}else sc.next();
			} 
			sc.close();
			
			//constroi o OI depois de o ter decifrado
			oi = new OrderInformation(tidOi, prodIdOi);

			//calcula a sintese do OrderInformation e concatena com a sintese
			//do PaymentInstructions recebida e decifrada com a sua chave privada
			//e compara com a dupla assinatura
			String sintesePi = Criptografia.decifrar(preq.getSintesePiCifrada(), certV.getChavePublica()).toString();
			String duplaAssGerada = Criptografia.sintese(Criptografia.sintese(oi.toString()) + sintesePi);

			if(duplaAssinatura.equals(duplaAssGerada)){
				System.out.println("-- Dupla assinatura valida");
			}else{
				System.out.println("-- Dupla assinatura invalida");
				break;
			}

			//***PASSO 15***
			prodId = oi.getProdId();

			switch(prodId){
			case 1: quantiaFinal = 23; break;
			case 2: quantiaFinal = 54; break;
			case 3: quantiaFinal = 89; break;
			default: quantiaFinal = 0; break;
			}

			//--------------ALGORITMO 4.5----------------

			//***PASSO 1,2,3 e 4***

			//Cifra a dupla assinatura com a sua chave privada para garantir a gateway que veio de si
			DadosCifrados duplaAssinaturaCifrada = Criptografia.cifrar(duplaAssinatura, certV.getChavePrivada());
			//Criacao e envio de um AuthorizationRequest
			AuthorizationRequest areq = new AuthorizationRequest(quantiaFinal, tid, certV, certG,
					preq.getChaveSimetricaCifrada(), preq.getPiCifrado(), preq.getSinteseOiCifrada(),
					duplaAssinaturaCifrada);

			System.out.println("<< AuthorizationRequest");

			Mensagem msg = new Mensagem(1, areq);

			try{
				outGateway.writeObject(msg);
			}catch(IOException e){
				System.out.println(e.toString());
			}

			System.out.println(">> AuthorizationResponse");

			try{
				ares = (AuthorizationResponse) inGateway.readObject();
			}catch(Exception e){
				System.out.println(e.toString());
			}

			//***PASSO 18***
			//verifica a validade do certificado da gateway e actualiza
			if(ares.getCertGateway().getValidade()){
				certG = ares.getCertGateway();
			}

			//***PASSO 19***
			//obtem a chave simetrica 3 com a sua chave privada
			int chaveSimetrica3 = Integer.parseInt(Criptografia.decifrar(ares.getChaveSimetrica3Cifrada(), certV.getChavePublica()).toString());

			//obtem a resposta com a chave simetrica 3
			resposta = Criptografia.decifrar(ares.getRespostaCifrada(), chaveSimetrica3).toString();

			//***PASSO 20***
			//verifica a assinatura dos dados que recebeu
			Criptografia.verificaAssinatura(ares.getResAssinada(), certG);

			//retira a sintese da assinatura
			String sinteseResposta = ares.getResAssinada().getDados(certG.getChavePrivada()).toString();

			//gera uma sintese para a resposta recebida e compara
			Criptografia.verificaSintese(resposta, sinteseResposta);

			//***PASSO 21***
			//guarda a capture token e a chave simetrica 4
			captureTokenCifrada = ares.getCaptureTokenCifrada();
			chaveSimetrica4Cifrada = ares.getChaveSimetrica4Cifrada();

			autorizacao = false;

		}
		//***PASSO 22***

		//--------------ALGORITMO 4.4----------------

		//***PASSO 16***
		//Enviar a resposta final para o cliente
		pres = new PurchaseResponse(resposta,certV);
		System.out.println("<< PurchaseResponse: " + socketCliente.getRemoteSocketAddress());

		//***PASSO 17***
		try{
			outCliente.writeObject(pres);
		}catch(IOException e){
			System.out.println(e.toString());
		}

		//--------------ALGORITMO 4.6----------------

		//***PASSO 1***
		//vendedor cria um pedido de captura CaptureRequest para um tid e uma quantia final

		//***PASSO 2,3 e 4***

		// se a autorizacao for concedida, e criado um captureRequest, para futura captura
		// do dinheiro
		if(resposta.equals("Autorizacao concedida")){
			creq = new CaptureRequest(tid, quantiaFinal, contaVendedor, certV, certG,
					captureTokenCifrada, chaveSimetrica4Cifrada);

			//adiciona um CaptureRequest a lista do vendedor
			Vendedor.addCaptureReq(creq);}

		System.out.println(">> Sair Cliente: " +socketCliente.getRemoteSocketAddress());

		try{
			outCliente.close();
			inCliente.close();
			socketCliente.close();
		}catch(IOException e){
			System.out.println(e.toString());
		}
	}
}