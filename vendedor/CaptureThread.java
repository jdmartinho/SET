package vendedor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

import mensagens.CaptureResponse;
import cripto.Certificado;
import cripto.Criptografia;
import gateway.Mensagem;

class CaptureThread extends Thread {

	private Scanner sc;
	private CaptureResponse cres;
	private Mensagem msg;
	private ArrayList <CaptureResponse> listaRespostas;
	private ObjectInputStream inGateway = null;
	private ObjectOutputStream outGateway = null;
	private Socket gateway;
	private int porto;
	private String ipGateway;
	private Certificado certV;
	private Certificado certG;

	/**
	 * Construtor da classe
	 * @param ipGateway O ip da gateway
	 * @param porto O porto da gateway
	 * @param certV O certificado do vendedor
	 * @param certG O certificado da gateway
	 */
	CaptureThread(String ipGateway, int porto, Certificado certV, Certificado certG){
		this.ipGateway = ipGateway;
		this.porto = porto;
		this.certV = certV;
		this.certG = certG;
		sc = new Scanner(System.in);		
	}

	public void run(){
		while(true){
			System.out.println(sc.next());
			
			//cria uma ligacao para a gateway
			try{
				gateway = new Socket(ipGateway,porto);		
				this.inGateway = new ObjectInputStream(gateway.getInputStream());
				this.outGateway = new ObjectOutputStream(gateway.getOutputStream());
			}catch(IOException e){
				System.out.println(e.toString());
			}
			
			//cria uma mensagem com a lista de pedidos de captura
			msg = new Mensagem(2,Vendedor.listaCaptureReq);

			System.out.println("<< CaptureRequest");
			
			//envia a mensagem
			try{
				outGateway.writeObject(msg);
			}catch(IOException e){
				System.out.println(e.toString());
			}

			System.out.println(">> CaptureResponse");
			
			//recebe uma mensagem com a lista de respostas
			try{
				msg = (Mensagem) inGateway.readObject();
			}catch(Exception e){
				System.out.println(e.toString());
			}

			listaRespostas = msg.getListaResponses();
			
			//processa cada uma das respostas
			for(int i = 0; i < listaRespostas.size(); i++){

				//adiciona CaptureResponse a lista do vendedor
				cres = listaRespostas.get(i);
				Vendedor.addCaptureRes(cres);

				//***PASSO 14***

				//verifica a validade do certificado da gateway e actualiza
				if(cres.getCertGateway().getValidade()){
					certG = cres.getCertGateway();
				}

				//***PASSO 15***
				//decifra a chave simetrica 6 com a sua chave privada
				int chaveSimetrica6 = Integer.parseInt(Criptografia.decifrar(cres.getChaveSimetrica6Cifrada(), certV.getChavePublica()).toString());

				//decifra a resposta com a chave simetrica 6
				String respostaFinal = Criptografia.decifrar(cres.getRespostaCifrada(), chaveSimetrica6).toString();
				System.out.println(respostaFinal);

				//***PASSO 16***
				//verifica a assinatura da resposta que recebeu
				Criptografia.verificaAssinatura(cres.getSinteseRespostaAssinada(), certG);

				//retira a sintese da assinatura
				String sinteseResposta = cres.getSinteseRespostaAssinada().getDados(certG.getChavePrivada()).toString();

				//gera uma sintese para a resposta recebida		
				Criptografia.verificaSintese(respostaFinal, sinteseResposta);		
			}

			try{				
				gateway.close();
				inGateway.close();
				outGateway.close();						
			}catch(IOException e){
				System.out.println(e.toString());
			}
		}
	}
}