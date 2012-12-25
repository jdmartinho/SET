package vendedor;

import java.util.Scanner;

public class VendedorInterface {
    
    private int portoEscuta, contaVendedor;
    private String ipGateway;
    private Scanner sc;
    
    /** Creates a new instance of VendedorInterface */
    public VendedorInterface() {     
        System.out.println("================================================");
        System.out.println("Vendedor - Grupo 8");
        System.out.println("v1.0 15/05/07");
        System.out.println("================================================"); 
        sc = new Scanner(System.in);
        pedeIpGateway();
        pedePortoEscuta();
        pedeContaVendedor();
        System.out.println("================================================"); 
        System.out.println("Pressione 'C' para pedido de captura");
        System.out.println("================================================"); 
       
    }
     
    /**
     * Metodo que pede o ip da gateway
     *
     */
    private void pedeIpGateway(){
        System.out.print("Introduza o ip da gateway:");
        ipGateway = sc.next();
    }
    
    /**
     * Metodo que pede o porto de escuta do vendedor
     *
     */
    private void pedePortoEscuta(){
        System.out.print("Introduza o porto de escuta do vendedor:");
        portoEscuta = sc.nextInt();
    }
    
    /**
     * Metodo que pede no numero de conta do vendedor
     *
     */
    private void pedeContaVendedor(){
        System.out.print("Introduza o numero da conta do vendedor:");
        contaVendedor = sc.nextInt();
    }
    
    /**
     * Devolve o porto de escuta
     * @return portoEscuta O porto onde esta o vendedor a escuta
     */
    public int getPortoEscuta(){
        return portoEscuta;
    }
    
    /**
     * Devolve o ip da gateway
     * @return ipGateway O endereco ip da gateway
     */
    public String getIpGateway(){
        return ipGateway;
    } 
    
    /**
     * Devolve o numero da conta do vendedor
     * @return contaVendedor O numero da conta do vendedor
     */
    public int getContaVendedor(){
        return contaVendedor;
    }
    
}


