package vendedor;

public class Utilizador {
	private String user;

	private String sintesePass;

    /**
     * Construtor da classe
     * @param user Nome de utilizador
     * @param sintesePass Sintese da password
     */
	public Utilizador(String user, String sintesePass) {
		this.user = user;
		this.sintesePass = sintesePass;
	}

    /**
     * Devolve o nome de utilizador
     * @return user O nome de utilizador
     */
	public String getUser(){
		return user;
	}
    /**
     * Devolve a sintese da password
     * @return sintesePass A sintese da password
     */
	public String getSintesePass(){
		return sintesePass;
	}
	
    /**
     * Devolve a representacao do utilizador para string
     * @return String A representacao do utilizador em string
     */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("-- Username: ");
                sb.append(user +"\t");
		sb.append(" Sintese password: ");
		sb.append(sintesePass);
		return sb.toString();
	}
	
}
