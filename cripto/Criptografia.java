package cripto;

public class Criptografia {
	
	private static final String SINTESE_VALIDA = "-- Sintese valida";
	private static final String SINTESE_INVALIDA = "-- Sintese invalida";
	private static final String ASSINATURA_VALIDA = "-- Assinatura valida";
	private static final String ASSINATURA_INVALIDA = "-- Assinatura invalida";

    /**
     * Metodo que simula a verificacao da validade de um certificado
     * @param cert certificado
     * @return validade
     */
	public static boolean verificaCertificado(Certificado cert){
		return cert.getValidade();
	}


    /**
     * Metodo que simula a geracao de uma sintese de um conjunto de dados
     * @param dados a criar a sintese
     * @return sintese dos dados
     */
	public static String sintese(String dados){
		String sintese = null;
		sintese = " + sintese(" + dados + ")";
		return sintese;
	}
	
    /**
     * Metodo que faz a verificacao de uma sintese em relacao aos dados recebidos
     * @param dados dados para criar a sintese e comparar com a sintese dada
     * @param sintese sintese dada para comparar com a sintese dos dados
     * @return true se a sintese for valida , false cc
     */
	public static boolean verificaSintese(String dados, String sintese){
		String sinteseGerada = Criptografia.sintese(dados);
		if(sinteseGerada.equals(sintese)){
			System.out.println(SINTESE_VALIDA);
			return true;
		}else{
			System.out.println(SINTESE_INVALIDA);
			return false;
		}
	}

	
    /**
     * Metodo que simula a assinatura digital de um conjunto de dados
     * @param dados dados a assinar
     * @param cert certificado utilizado na assinatura
     * @return dadosAssinados
     */
	public static DadosAssinados assinatura(String dados, Certificado cert){
		int chave = cert.getChavePrivada();
		DadosAssinados dadosAssinados = new DadosAssinados(dados, chave);
		return dadosAssinados;
	}

    /**
     * Metodo que simula a verificacao de uma assinatura digital de um conjunto de dados
     * @param dados dadosAssinados
     * @param cert certificado utilizado na assinatura 
     * @return true caso seja valido, false cc
     */
	public static boolean verificaAssinatura(DadosAssinados dados, Certificado cert){
		
		//como nao existe uma relacao entre a chave privada e publica dos certificados
		//comparamos a propria chave privada		
		if(dados.getChave() == cert.getChavePrivada()){
			System.out.println(ASSINATURA_VALIDA);
			return true;
		}else {
			System.out.println(ASSINATURA_INVALIDA);
			return false;
		}
	}

    /**
     * Metodo que gera uma chave simetrica
     * @return chave
     */
	public static int geraChaveSimetrica(){
		int chave = 1;
		return chave;
	}

    /**
     * Metodo que simula o cifrar de dados
     * @param dados dados a cifrar
     * @param chave chave utilizada 
     * @return dadosCifrados
     */
	public static DadosCifrados cifrar(String dados, int chave){
		DadosCifrados dadosCifrados = new DadosCifrados(dados,chave);		
		return dadosCifrados;
	}

    /**
     * Metodo que simula o decifrar de dados
     * @param dados dados a decifrar
     * @param chave chave utilizada
     * @return dados decifrados
     */
	public static Object decifrar(DadosCifrados dados, int chave){
		return dados.getDados(chave);
	}

}
