package vendedor;

import cripto.Certificado;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

public class VendedorComunicacao {
    
    //comunicacao
    private ServerSocketFactory ssf;
    private ServerSocket sSoc;
    private final int GATEWAY_PORT = 5664;
    private Socket gateway;
    private String ipGateway;
    private Certificado certV;
    private Certificado certG;
    ObjectInputStream inGateway = null;
	ObjectOutputStream outGateway = null;
	    
    /**
     * Construtor da classe
     * @param ipGateway O endereco da gateway
     * @param certV O certificado do vendedor
     * @param certG O certificado da gateway
     */
    public VendedorComunicacao(String ipGateway, Certificado certV, Certificado certG){
    	this.ipGateway = ipGateway;    	    	
    	this.certV = certV;
    	this.certG = certG;
    	
    }
    
    /**
     * Devolve o socket para a gateway
     * @return gateway O socket para a gateway
     */
    public Socket getSocketGateway(){
    	return gateway;
    }
    
    /**
     * Devolve o certificado do vendedor
     * @return certV O certificado do vendedor
     */
    public Certificado getCertVendedor(){
    	return certV;
    }
    
    /**
     * Devolve o certificado da gateway
     * @return certG O certificado da gateway
     */
    public Certificado getCertGateway(){
    	return certG;
    }
    
    /**
     * Devolve o stream de entrada para a gateway
     * @return inGateway O stream de entrada para a gateway
     */
    public ObjectInputStream getGatewayInputStream(){
    	return inGateway;
    }
    
    /**
     * Devolve o stream de saida para a gateway
     * @return outGateway O stream de saida para a gateway
     */
    public ObjectOutputStream getGatewayOutputStream(){
    	return outGateway;
    }
    
    /**
     * Devolve o porto da gateway
     * @return GATEWAY_PORT O porto da gateway
     */
    public int getGatewayPort(){
    	return GATEWAY_PORT;
    }
        
    /**
     * Inicializa o servidor do vendedor
     */
    public void startServer(int portoVendedor, int contaVendedor, int tidGlobal){
        ssf = ServerSocketFactory.getDefault();
        sSoc = null;
        
        try {
            sSoc = ssf.createServerSocket(portoVendedor);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        

        while(true) {
            try {
                //aceita a ligacao do cliente
                Socket cliente = sSoc.accept();                
                
                //estabelece uma ligacao a gateway
            	try{
                this.gateway = new Socket(ipGateway, GATEWAY_PORT);
        		inGateway = new ObjectInputStream(gateway.getInputStream());
        		outGateway = new ObjectOutputStream(gateway.getOutputStream());
            	}catch(Exception e){
            		System.out.println(e.toString());
            	}
                
                //cria a thread responsavel pelo atendimento do cliente
                VendedorThread newVendedorThread = new VendedorThread(cliente,inGateway,outGateway,tidGlobal++,contaVendedor,certV,certG);
                newVendedorThread.start();
                
            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.exit(-1);
            }
            
        }
        //sSoc.close();
    }
    
    
    
}
