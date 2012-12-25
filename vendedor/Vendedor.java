package vendedor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import cripto.Certificado;
import cripto.Criptografia;
import mensagens.CaptureRequest;
import mensagens.CaptureResponse;

public class Vendedor {
    
    // certificados
    public static Certificado certV;
    public static Certificado certG;
    // inteface e comunicacao
    private VendedorInterface vdif;
    private VendedorComunicacao vdcom;
    // valor inical do transacion id
    private int tidGlobal = 1;
    
    //ficheiros
    private final static String CLIENTES = "clientes.set";
    
    public static ArrayList <CaptureRequest> listaCaptureReq;
    public static ArrayList <CaptureResponse> listaCaptureRes;
    public static ArrayList <Utilizador> listaUtilizadores;
    
    //mensagems de erro
    private final static String ERRO1 = "Ficheiro nao encontrado";
    private final static String ERRO2 = "Ficheiro corrompido";
     
    /**
     * Construtor da classe
     *
     */
    public Vendedor() {
    	//inicializacao das listas de capture request e response
    	listaCaptureReq = new ArrayList<CaptureRequest>();
    	listaCaptureRes = new ArrayList<CaptureResponse>();
    	listaUtilizadores = new ArrayList<Utilizador>();
    	

        // cria a interface
        this.vdif = new VendedorInterface();
               
       
        // cria a comunicacao necessaria do cliente
        this.vdcom = new VendedorComunicacao(vdif.getIpGateway(),certV,certG);
        
        // carrega dados
        getClientes(true);
        
        
        
        //cria uma thread para as capturas        
        CaptureThread newCaptureThread = new CaptureThread(vdif.getIpGateway(),vdcom.getGatewayPort(),vdcom.getCertVendedor(),vdcom.getCertGateway());
        newCaptureThread.start();
               
        //inicializa o servidor
        vdcom.startServer(vdif.getPortoEscuta(), vdif.getContaVendedor(), tidGlobal);        
    }
    
    /**
     * Metodo que carrega os utilizadores do ficheiro para o array.
     * @throw Exception se algum erro acontecer.
     * @param print escreve o conteudo do ficheiro
     */
    private static void getClientes(boolean print){
        //limpa a lista
        listaUtilizadores.clear();
        //imprime a etiqueta
        if(print){
            System.out.println("== Utilizadores ==");}
        try{
            Scanner sc = new Scanner(new File(CLIENTES));
            sc.useDelimiter("\\s*;\\s*");
            while(sc.hasNext()){
                if(sc.hasNext()){
                    String user = sc.next();
                    String pass = sc.next();
                    String sintesePass = Criptografia.sintese(pass);
                    //adiciona cada cliente
                    Utilizador c = new Utilizador(user,sintesePass);
                    listaUtilizadores.add(c);
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
     * Metodo que verifica se um dado utilizador existe no sistema
     * @param user O nome de utilizador
     * @param sintesePass A sintese da password
     * @return resultado True se existir, false caso contrario
     */
    public static boolean verificaUtilizador(String user, String sintesePass){
    	boolean resultado = false;
    	Utilizador c = null;
    	for(int i = 0; i < listaUtilizadores.size(); i++){
    		c = listaUtilizadores.get(i);
    		if(c.getUser().equals(user) && c.getSintesePass().equals(sintesePass)){    			
    			resultado = true;    			
    		}
    	}
    	return resultado;
    }
    
    /**
     * Metodo que adiciona um pedido de captura a lista de pedidos
     * @param creq O pedido de captura
     */
    public static void addCaptureReq(CaptureRequest creq){
    	listaCaptureReq.add(creq);
    }
    
    /**
     * Metodo que adiciona uma resposta a um pedido de captura a lista de respostas
     * @param cres A resposta ao pedido de captura
     */
    public static void addCaptureRes(CaptureResponse cres){
    	listaCaptureRes.add(cres);
    }
    
    /**
     * Main
     * @param args args
     */
    public static void main(String[] args) {
        //inicializacao dos certificados do vendedor e gateway
        certV = new Certificado(123,321);
        certG = new Certificado(789,987);
        new Vendedor();
        
    }
    

    
    
    
    
}
