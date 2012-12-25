package gateway;

import cripto.Certificado;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ServerSocketFactory;

public class GatewayComunicacao {
    
    //comunicacao
    private ServerSocketFactory ssf;
    private ServerSocket sSoc;
    private final int GATEWAY_PORT = 5664;
    
    /** Creates a new instance of GatewayComunicacao */
    public GatewayComunicacao() {
    }
    
    /**
     * Inicializa a gateway
     */
    public void startGateway(Certificado certV, Certificado certG){
        // criacao de sockets para comunicacao
        ssf = ServerSocketFactory.getDefault();
        sSoc = null;
        

        try {
            sSoc = ssf.createServerSocket(GATEWAY_PORT);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        
        while(true) {
            try {
                Socket inSoc = sSoc.accept();
                GatewayThread newGatewayThread = new GatewayThread(inSoc,certV,certG);
                newGatewayThread.start();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
        //sSoc.close();
    }
    
}
