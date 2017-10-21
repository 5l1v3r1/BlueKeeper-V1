package br.com.gilbertopapa.bluekeeper.dao;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Factory para criação de objetos DAO
 */
public class DAOFactory {

	/**
	 * Nome da classe de DAO para ser instanciada
	 */
	private static final String SENHA_SERVICO_DAO_CLASSNAME;
	
	static {
		try {
			// Recupera o caminho de dao.properties
			Path path = Paths.get(DAOFactory.class.getResource("/dao.properties").toURI());
			
			// Lê as linhas do arquivo
			List<String> lines = Files.readAllLines(path);
			
			// Pega a primeira linha, que contém o nome da classe a ser instanciada
			SENHA_SERVICO_DAO_CLASSNAME = lines.get(0);
			
		} catch (URISyntaxException | IOException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * Cria um objeto de SenhaServicoDAO
	 * @return
	 */
	public static SenhaServicoDAO getSenhaServicoDAO() {
		try {
			// Instancia o objeto via Reflection API
			return (SenhaServicoDAO) Class.forName(SENHA_SERVICO_DAO_CLASSNAME).newInstance();

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
