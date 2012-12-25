package gateway;

public class Conta {
    
    private static final long serialVersionUID = 10L;
    private int nib;
    private int numCartao;
    private int nSeguranca;
    private String nome;
    private String morada;
    private int saldo;
    private int saldoContab;
    
    /** Creates a new instance of Conta */
    public Conta(int nib, int numCartao, int nSeg, String nome, String morada, int saldo) {
        this.nib = nib;
        this.numCartao = numCartao;
        this.nSeguranca = nSeg;
        this.nome = nome;
        this.morada = morada;
        this.saldo = saldo;
        this.saldoContab = saldo;
    }
    
    /**
     * Devolve o numero da conta
     * @return Um inteiro que representa o numero da conta.
     */
    public int getNib(){
        return this.nib;
    }
    
    /**
     * Devolve o numero do cartao
     * @return Um inteiro que representa o numero do cartao.
     */
    public int getNCartao(){
        return this.numCartao;
    }
    
    /**
     * Devolve o numero da seguranca
     * @return Um inteiro que representa o numero de seguranca do cartao.
     */
    public int getNSeguranca(){
        return this.nSeguranca;
    }
    
    /**
     * Devolve o saldo da conta
     * @return Um inteiro que representa o saldo da conta
     */
    public int getSaldo(){
        return this.saldo;
    }
    
    /**
     * Devolve o saldo contabilistico  da conta
     * @return Um inteiro que representa o saldo contabilistico da conta
     */
    public int getSaldoContab(){
        return this.saldoContab;
    }
    
    /**
     * Coloca um novo saldo na conta
     * @param saldo O novo saldo 
     */
    public void setSaldo(int saldo){
    	this.saldo = saldo;
    }
    
    /**
     * Coloca um novo saldo contabilistico na conta
     * @param saldo O novo saldo contabilistico
     */
    public void setSaldoContab(int saldo){
    	this.saldoContab = saldo;
    }
    
    /**
     * Devolvo o nome do dono da conta
     * @return nome O nome do dono da conta
     */
    public String getNome(){
        return this.nome;
    }

    /**
     * Devolve a morada do dono da conta
     * @return morada A morada do dono da conta
     */
    public String getMorada(){
        return this.morada;
    }
    
    
    /**
     * Devolve uma String que representa a conta
     * @return String A string que representa a conta
     */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("-- Conta:");
		sb.append(" nConta: ");
		sb.append(nib);
		sb.append(" nCartao: ");
		sb.append(numCartao);
                sb.append(" nSeguranca: ");
		sb.append(nSeguranca);
		sb.append(" saldo: ");
		sb.append(saldo);
		sb.append(" saldoContab: ");
		sb.append(saldoContab);
		
		return sb.toString();
	}
}
