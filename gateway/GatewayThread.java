package gateway;

import java.io.*;
import java.net.Socket;
import java.util.*;

import cripto.*;
import mensagens.*;

class GatewayThread extends Thread {

	private Socket socket = null;
	ObjectOutputStream outVendedor = null;
	ObjectInputStream inVendedor = null;        
	private Certificado certV;
	private Certificado certG;
	private Mensagem msg;
	private int idMsg;

	//mensagens que vai usar
	AuthorizationRequest areq;
	AuthorizationResponse ares;
	CaptureRequest creq;
	CaptureResponse cres;

	PaymentInstructions pi;

	/**
	 * Construtor da classe
	 * 
	 * @param inSoc O socket de comunicacao com o vendedor que vai usar
	 * @param certV O certificado do vendedor
	 * @param certG O certificado da gateway
	 */
	GatewayThread(Socket inSoc, Certificado certV, Certificado certG) {
		socket = inSoc;
		this.certV = certV;
		this.certG = certG;
		System.out.println(">> Novo Vendedor: " + socket.getRemoteSocketAddress());
	}

	public void run(){
		try {
			outVendedor = new ObjectOutputStream(socket.getOutputStream());
			inVendedor = new ObjectInputStream(socket.getInputStream());
			msg = (Mensagem) inVendedor.readObject();
		}catch(Exception e){
			System.out.println(e.toString());
		}

		idMsg = msg.getId();
		switch(idMsg){
		case 1: processaAuthorizationRequest(msg.getAuthorizationRequest()); break;
		case 2: processaCaptureRequest(msg.getListaRequests()); break;
		default: break;
		}
	}

	/**
	 * Metodo que processa um pedido de autorizacao por parte do vendedor
	 * @param areq O pedido de autorizacao do vendedor
	 */
	private void processaAuthorizationRequest(AuthorizationRequest areq){

		int quantia = 0, tid = 0, chaveSimetrica1, chaveSimetrica2, tidPi, quantiaPi, nCartao = 0, nSeguranca, i;
		Scanner sc;
		String morada, dados, sinteseDadosRecebida, piStr, resposta, duplaAssinatura, duplaAssGerada;
		boolean autorizacao = true;

		resposta = "Autorizacao negada";

		//bateria de testes
		while (autorizacao){

			//--------------ALGORITMO 4.5----------------

			//obtem um AuthorizationRequest do vendedor		
			this.areq = areq;

			System.out.println(">> AuthorizationRequest");

			//***PASSO 5***
			//verifica a validade dos certificados recebidos
			if(areq.getCertificadoVendedor().getValidade()){
				certV = areq.getCertificadoVendedor();
			}else{
				break;
			}

			//***PASSO 6***
			//obtem a chave simetrica 2 decifrando-a com a sua chave privada
			chaveSimetrica2 = Integer.parseInt(Criptografia.decifrar(areq.getChaveSimetrica2Cifrada(), certG.getChavePublica()).toString());

			//decifra os dados usando a chave simetrica 2
			dados = Criptografia.decifrar(areq.getDadosCifrados(), chaveSimetrica2).toString();

			sc = new Scanner(dados);
			quantia = sc.nextInt();
			tid = sc.nextInt();
			sc.close();

			//***PASSO 7***                         	
			//verifica a assinatura dos dados que recebeu
			if(Criptografia.verificaAssinatura(areq.getDadosAssinados(), certV)){}
			else{break;}

			//obtem a assinatura e decifra-a com a chave publica do vendedor                
			sinteseDadosRecebida = areq.getDadosAssinados().getDados(certV.getChavePrivada()).toString();

			//compara as sinteses 
			if(Criptografia.verificaSintese(dados, sinteseDadosRecebida)){}
			else{break;};

			//***PASSO 8***
			//nao pode ser implementado devido a nao existerem certificados nos clientes
			// (verificacao da validade do certificado do cliente)

			//***PASSO 9***
			//decifra a chave simetrica 1 com a sua chave privada
			chaveSimetrica1 = Integer.parseInt(Criptografia.decifrar(areq.getChaveSimetrica1Cifrada(), certG.getChavePublica()).toString());

			//decifrar o PI com a chave simetrica 1
			piStr = Criptografia.decifrar(areq.getPiCifrado(),chaveSimetrica1).toString();

			sc = new Scanner(piStr);
			tidPi = 0;
			nCartao = 0;
			nSeguranca = 0;
			quantiaPi = 0;
			morada = null;
			i = 0;
			while(sc.hasNext()){
				if(sc.hasNextInt()){
					if(i==0) tidPi = sc.nextInt();
					if(i==1) nCartao = sc.nextInt();
					if(i==2) nSeguranca = sc.nextInt();
					if(i==3) quantiaPi = sc.nextInt();
					i++;
				}else if(sc.next().equals("moradaDonoCartao:")){
					morada = sc.next();
				}
			} 
			sc.close();
			//construir o PI apos o ter decifrado
			pi = new PaymentInstructions(tidPi,nCartao,nSeguranca,morada,quantiaPi);

			//***PASSO 10***
			//a gateway decifra a dupla assinatura cifrada usando a sua chave privada
			duplaAssinatura = areq.getDuplaCifrada().getDados(certV.getChavePrivada()).toString();

			//calcula a sintese do PaymentInstructions e concatena com a sintese
			//do OrderInformation recebida e compara com a dupla assinatura
			String sinteseOi = Criptografia.decifrar(areq.getSinteseOiCifrada(), certG.getChavePublica()).toString();
			duplaAssGerada = Criptografia.sintese(sinteseOi + Criptografia.sintese(pi.toString()));

			if(duplaAssinatura.equals(duplaAssGerada)){
				System.out.println("-- Dupla assinatura valida");                	
			}else{
				System.out.println("-- Dupla assinatura invalida");
				break;
			}

			//***PASSO 11***
			//comparar a informacao do PI com a autorizacao do vendedor
			if(tid == tidPi && quantia == quantiaPi){
				System.out.println("-- Verificacao cliente/vendedor valida");
			}else {
				System.out.println("-- Verificacao cliente/vendedor invalida");
				break;
			}

			//***PASSO 12***
			//contactar o banco do cliente com a autorizacao		
			if(Gateway.autorizaTransferencia(nCartao, quantia, nSeguranca)){}
			else{
				break;
			}

			// ja efectuou a bateria de testes toda
			autorizacao = false;
			resposta = "Autorizacao concedida";
		}

		//***PASSO 13,14,15 e 16***
		//banco envia autorizacao a gateway				
		ares = new AuthorizationResponse(resposta, quantia, nCartao, tid, certV, certG);

		//***PASSO 17***
		//enviar AuthorizationResponse
		System.out.println("-- "+resposta);
		System.out.println("<< AuthorizationResponse");

		try{
			outVendedor.writeObject(ares);
			outVendedor.close();
			inVendedor.close();
			socket.close();
		}catch(IOException e){
			System.out.println(e.toString());
		}
	}
	
	/**
	 * Metodo que processa os pedidos de captura do vendedor 
	 * @param listaPedidos A lista com os pedidos de captura do vendedor
	 */
	private void processaCaptureRequest(ArrayList<CaptureRequest> listaPedidos){
		ArrayList <CaptureResponse> listaRespostas = new ArrayList<CaptureResponse>();
		Scanner sc;
		int chaveSimetrica4, chaveSimetrica5, tidFinal, quantiaFinal, nConta, tidCaptk, quantiaCaptk, nCartaoCaptk, i;
		String dadosFinais, sinteseDadosRecebida, respostaBancoClienteFinal;

		//--------------ALGORITMO 4.6----------------

		System.out.println(">> CaptureRequest");

		for(int cont = 0; cont < listaPedidos.size(); cont++){
			creq = (CaptureRequest) listaPedidos.get(cont);

			//***PASSO 5***
			//verifica a validade dos certificados recebidos
			if(creq.getCertificado().getValidade()){
				certV = creq.getCertificado();
			}

			//***PASSO 6***
			//decifra a chave simetrica 5 com a sua chave privada
			chaveSimetrica5 = Integer.parseInt(Criptografia.decifrar(creq.getChaveSimetrica5Cifrada(), certG.getChavePublica()).toString());		

			//decifra os dados com a chave simetrica 5 e obtem a quantia final e o tid
			dadosFinais = Criptografia.decifrar(creq.getDadosCifrados(), chaveSimetrica5).toString();
			sc = new Scanner(dadosFinais);
			quantiaFinal = sc.nextInt();
			tidFinal = sc.nextInt();			
			nConta = sc.nextInt();
			sc.close();

			//***PASSO 7***
			//verifica a assinatura dos dados que recebeu
			Criptografia.verificaAssinatura(creq.getDadosAssinados(), certV);			

			//obtem a assinatura e decifra-a com a chave publica do vendedor                
			sinteseDadosRecebida = creq.getDadosAssinados().getDados(certV.getChavePrivada()).toString();

			//compara as sinteses
			Criptografia.verificaSintese(dadosFinais, sinteseDadosRecebida);

			//***PASSO 8***
			//decifra a chave simetrica 4 com a sua chave privada
			chaveSimetrica4 = Integer.parseInt(Criptografia.decifrar(creq.getChaveSimetrica4Cifrada(), certG.getChavePrivada()).toString());

			//decifra a capture token com a chave simetrica 4
			String captureTokenStr = Criptografia.decifrar(creq.getCaptureTokenCifrada(), chaveSimetrica4).toString();
			sc = new Scanner(captureTokenStr);
			i = 0;
			quantiaCaptk = 0;
			nCartaoCaptk = 0;
			tidCaptk = 0;
			while(sc.hasNext()){
				if(sc.hasNextInt()){
					if(i==0) quantiaCaptk = sc.nextInt();
					if(i==1) nCartaoCaptk = sc.nextInt();
					if(i==2) tidCaptk = sc.nextInt();					
					i++;
				}else sc.next();
			} 
			sc.close();

			//cria o captureToken depois de decifrado e verifica se esta na lista de tokens validos
			CaptureToken ctk = new CaptureToken(quantiaCaptk,nCartaoCaptk,tidCaptk);
			System.out.println(Gateway.validaToken(ctk));

			//***PASSO 9***
			//compara a informacao da capture token com a informacao que vem no capture request
			if(quantiaFinal == quantiaCaptk && tidFinal == tidCaptk){
				System.out.println("-- Dados vendedor/token validos");
			}else System.out.println("-- Dados vendedor/token invalidos");

			//***PASSO 10***
			//envia um pedido de transferencia de dinheiro ao banco do cliente
			respostaBancoClienteFinal = Gateway.transferencia(nCartaoCaptk, nConta, quantiaCaptk);
			System.out.println(respostaBancoClienteFinal);
			System.out.println("-- Cliente:  " + Gateway.getConta(nCartaoCaptk).toString());
			System.out.println("-- Vendedor: " + Gateway.getConta(nConta).toString());


			//***PASSO 11 e 12***
			//cria uma CaptureResponse para enviar ao vendedor
			cres = new CaptureResponse(respostaBancoClienteFinal, certV, certG);
			listaRespostas.add(cres);
		}

		//***PASSO 13***

		Mensagem msg = new Mensagem(3,listaPedidos ,listaRespostas);

		System.out.println("<< CaptureResponse");

		try{
			outVendedor.writeObject(msg);

			System.out.println(">> Sair Vendedor");

			outVendedor.close();
			inVendedor.close();
			socket.close();
		}catch(IOException e){
			System.out.println(e.toString());
		}
	}
}
