package gateway;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import mensagens.CaptureToken;
import cripto.Certificado;

public class Gateway {

	private GatewayComunicacao gwcom;

	// a lista de contas
	private static ArrayList <Conta> contas;

	//ficheiros
	private final String CONTAS = "contas.set";

	//lista de tokens
	private static ArrayList <CaptureToken> tokens;

	//mensagems de erro
	private final String ERRO1 = "Ficheiro nao encontrado";
	private final String ERRO2 = "Ficheiro corrompido";

	private static Certificado certV;
	private static Certificado certG;

	/**
	 * Main
	 * @param args args
	 */
	public static void main(String[] args) {
		//inicializacao dos certificados do vendedor e gateway
		certV = new Certificado(123,321);
		certG = new Certificado(789,987);

		new Gateway();
	}


	/** Creates a new instance of Vendedor */
	public Gateway(){
		// cria a interface
		new GatewayInterface();
		// cria a comunicacao necessaria do gateway
		this.gwcom = new GatewayComunicacao();

		// inicializa a lista das contas
		contas = new ArrayList<Conta>();
		tokens = new ArrayList<CaptureToken>();

		// carrega dados
		getContas(true);

		// inicializa a gateway
		gwcom.startGateway(certV, certG);

	}
	/**
	 * Metodo que carrega os utilizadores do ficheiro para o array.
	 * @throw Exception se algum erro acontecer.
	 * @param print escreve o conteudo do ficheiro
	 */
	private void getContas(boolean print){
		//limpa a lista
		contas.clear();
		//imprime a etiqueta
		if(print){
			System.out.println("== Contas ==");}
		try{
			Scanner sc = new Scanner(new File(CONTAS));
			sc.useDelimiter("\\s*;\\s*");
			while(sc.hasNext()){
				if(sc.hasNextInt()){
					int nib = sc.nextInt();
					int numCartao = sc.nextInt();
					int nSeg = sc.nextInt();
					String nome = sc.next();
					String morada = sc.next();
					int saldo = sc.nextInt();
					//adiciona cada conta
					Conta c = new Conta(nib, numCartao, nSeg, nome, morada, saldo);
					contas.add(c);
					//imprime cada utilizador
					if(print){
						System.out.println(c.toString());}
				}else{
					break;
				}
			}
			sc.close();
		}catch (FileNotFoundException e1){
			System.err.println(ERRO1);
		}catch (Exception e2 ){
			System.err.println(ERRO2);
		}
	}

    /**
     * Metodo que autoriza uma transferencia bancaria caso a conta de origem
     * tenha saldo suficiente para a quantia em questao
     * @param nCartao O numero do cartao a que corresponde a conta
     * @param quantia A quantia a ser transferida
     * @param nSeguranca O numero de seguranca do cartao
     * @return boolean Devolve true se autorizar a transferencia, false caso contrario
     */
	public static synchronized boolean autorizaTransferencia(int nCartao, int quantia, int nSeguranca){
		boolean resposta = false;
		Conta c = Gateway.getConta(nCartao); 
		System.out.println(c);
		if(c.getSaldoContab() >= quantia && c.getNSeguranca() == nSeguranca){
			int novoSaldo = c.getSaldoContab() - quantia;			
			c.setSaldoContab(novoSaldo);
			contas.set(contas.indexOf(c), c);
			System.out.println("-- Autorizacao bancaria concedida");
			System.out.println(c);
			resposta = true;		
		}else{
			System.out.println("-- Autorizacao bancaria nao concedida");}
		return resposta;    	    	
	}

    /**
     * Metodo que efectua uma transferencia bancaria entre duas contas
     * @param nCartao O numero do cartao da conta de origem
     * @param nContaDestino O numero da conta de destino
     * @param quantia A quantia a ser transferida
     * @return String A resposta de transferencia efectuada
     */
	public static synchronized String transferencia(int nCartao, int nContaDestino, int quantia){
		Conta orig = Gateway.getConta(nCartao);
		Conta dest = Gateway.getConta(nContaDestino);
		String resposta = "-- Transferencia efectuada";
		orig.setSaldo(orig.getSaldo() - quantia);
		dest.setSaldo(dest.getSaldo() + quantia);
		dest.setSaldoContab(dest.getSaldoContab() + quantia);
		return resposta;
	}

    /**
     * Metodo que devolve uma conta com base num numero de cartao
     * @param nCartao O numero de cartao da conta
     * @return Conta A conta a que corresponde o cartao
     */
	public static Conta getConta(int nCartao){
		Conta c = null;    	
		for(int i = 0; i < contas.size(); i++){
			c = contas.get(i);
			if(c.getNCartao() == nCartao){    			
				return c;    			
			}
		}
		return c;
	}

    /**
     * Metodo que adiciona uma capture token a lista de 
     * capture tokens validas da gateway
     * @param ctk A capture token a ser adicionada
     */
	public synchronized static void adicionaToken(CaptureToken ctk){
		tokens.add(ctk);
	}

    /**
     * Metodo que valida uma capture token verificando se esta na lista
     * e retira-a de la caso seja valida (usa-a) 
     * @param ctk A capture token a ser verificada
     * @return String A resposta se a token e valida ou nao 
     */
	public synchronized static String validaToken(CaptureToken ctk){
		String resposta = "-- Token invalida";
		CaptureToken c;
		for(int i = 0; i < tokens.size(); i++){
			c = tokens.get(i);    		
			if(c.getQuantia() == ctk.getQuantia() && c.getTid() == ctk.getTid() && c.getNConta() == ctk.getNConta()){    			
				tokens.remove(c);    			
				resposta = "-- Token valida";
			}
		}    	    	
		return resposta; 
	}
}

