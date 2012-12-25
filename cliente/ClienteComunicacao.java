package cliente;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClienteComunicacao {
	
	private ObjectInputStream inVendedor;
	private ObjectOutputStream outVendedor;
	
    /**
     * Cria a ligacao ao vendedor
     * @param ip ip do vendedor 
     * @param porto porto do vendedor
     */
	public ClienteComunicacao(String ip, int porto){
		try{
			Socket vendedor= new Socket(ip, porto);
			//Cria um canal para receber dados do vendedor
			inVendedor = new ObjectInputStream(vendedor.getInputStream());
			//Cria um canal para enviar dados ao vendedor
			outVendedor = new ObjectOutputStream(vendedor.getOutputStream());			
		}
		catch(Exception e){
			System.err.println(e.toString());
		}
	}
	
    /**
     * Obtem o stream  que recebe dados do vendedor
     * @return stream de dados recebidos do vendedor
     */
	public ObjectInputStream getInVendedor(){
		return inVendedor;
	}
	
    /**
     * Obtem o stream que envia dados para o vendedor
     * @return stream de dados para o vendedor
     */
	public ObjectOutputStream getOutVendedor(){
		return outVendedor;
	}

}
