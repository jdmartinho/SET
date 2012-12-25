package cliente;

import java.util.Scanner;

public class ClienteInterface {
    
	// dados obtidos a partir da interface
    private String ip;
    private int porto;
    private int nCartao;
    private int nSeguranca;
    private String tipoCartao;
    private String moradaDonoCartao;
    private int prodID;
    private int quantia;
    private String username, password;
    private Scanner sc;
    
    
    /*
     * Cria a Interface principal
     */
    public ClienteInterface(){
        System.out.println("================================================");
        System.out.println("Cliente - Grupo 8");
        System.out.println("v1.0 15/05/07");
        System.out.println("================================================");
        sc = new Scanner(System.in);
        pedeIpPorto();
        menu();
        
        sc.close();
        
    }
    
    /*
     * Cria a interface que pede o ip e o porto do vendedor
     */
    private void pedeIpPorto(){
        System.out.print("Introduza o ip do vendedor:");
        ip = sc.next();
        System.out.print("Introduza o porto do vendedor:");
        porto = sc.nextInt();
    }
    
    
    /*
     * Cria a interface q pede o tipo de cartao
     */
    private void pedeTipoCartao(){
    	int tCartao;
        System.out.println("Introduza o tipo de cartao: ");
        System.out.println(" 1 - VISA");
        System.out.println(" 2 - MASTERCARD");
        System.out.println(" 3 - AMERICAN EXPRESS");
        System.out.println(" ========================== ");
        tCartao = sc.nextInt();
        switch(tCartao){
            case 1:tipoCartao = "Visa";break;
            case 2:tipoCartao = "MasterCard";break;
            case 3:tipoCartao = "American Express";break;
            default:break;
        }
        
    }
    
    /*
     * Cria a interface q pede o numero de cartao de credito
     */
    private void pedeNCartao(){
        System.out.println("Introduza o numero do cartao de credito: ");
        nCartao = sc.nextInt();
    }
    
    /*
     * Cria a interface q pede o numero de digitos de seguranca
     */
    private void pedeDigitosSeg(){
        System.out.println("Introduza os digitos de seguranca: ");
        nSeguranca = sc.nextInt();
    }
    
    /*
     * Cria a interface q pede a morada do dono do cartao
     */
    private void pedeMoradaDonoCartao(){
        System.out.println("Introduza a morada do dono do cartao: ");
        moradaDonoCartao = sc.next();
    }
    
    /*
     * Cria a interface q pede os dados de login utilizador/vendedor
     */
    private void login(){
        System.out.println("Username: ");
        username = sc.next();
        System.out.println("Password: ");
        password = sc.next();
    }
    
    /*
     * Cria a interface q apresenta a lista de produtos, e da a escolher
     * ao utilizador o produto desejado
     */
    private void escolhaProduto(){
        System.out.println(" ======== PRODUTOS ======== ");
        System.out.println(" 1 - BATATAS  - 23 Euros ");
        System.out.println(" 2 - LARANJAS - 54 Euros ");
        System.out.println(" 3 - CEBOLAS  - 89 Euros ");
        System.out.println(" ========================== ");
        prodID = sc.nextInt();
        switch(prodID){
            case 1:quantia = 23;break;
            case 2:quantia = 54;break;
            case 3:quantia = 89;break;
            default: break;
        }
        
    }
    
    /*
     * Cria o menu principal
     */
    private void menu(){
        int opcao = -1;
        boolean loginFlag= false, produtoFlag = false, finalizarFlag = true;
        
        while(finalizarFlag){
            System.out.println("/****MENU****/");
            System.out.println("1 - Login");
            System.out.println("2 - Escolher produto");
            System.out.println("3 - Finalizar Pagamento");
            opcao = sc.nextInt();
            switch(opcao){
                case 1: loginFlag = true ;login();break;
                case 2: produtoFlag = true;escolhaProduto();break;
                case 3:
                    if(loginFlag && produtoFlag){
                        finalizarFlag = false;
                        pedeTipoCartao();
                        pedeNCartao();
                        pedeDigitosSeg();
                        pedeMoradaDonoCartao();
                        break;} else{
                        System.out.println("Falta de informacao, introduza todos os dados.");
                        }
                default: break;
            }
        }
    }
    
    /**
     * Obtem o Ip do vendedor
     * @return ip
     */
    public String getIp(){
        return ip;
    }
    
    /**
     * Obtem o porto do vendedor
     * @return porto
     */
    public int getPorto(){
        return porto;
    }
    
    /**
     * Obtem o username
     * @return username
     */
    public String getUsername(){
        return username;
    }
    
    /**
     * Obtem a password
     * @return password
     */
    public String getPassword(){
        return password;
    }
    
    /**
     * Obtem o tipo de cartao
     * @return tipoCartao
     */
    public String getTipoCartao(){
    	return tipoCartao;
    }
    
    
    /**
     * Obtem a morada do dono do cartao
     * @return morada dono do Cartao
     */
    public String getMoradaDonoCartao(){
        return moradaDonoCartao;
    }
    
    /**
     * Obtem o numero do cartao
     * @return numero do cartao
     */
    public int getNCartao(){
        return nCartao;
    }
    
    /**
     * Obtem a quantia do produto
     * @return quantia do produto
     */
    public int getQuantia(){
        return quantia;
    }
    
    /**
     * Obtem o id do produto
     * @return prodID
     */
    public int getProdID(){
        return prodID;
    }
    
    /**
     * Obtem o numero de seguranca
     * @return nSeguranca
     */
    public int getNSeguranca(){
        return nSeguranca;
    }
 
    
}
