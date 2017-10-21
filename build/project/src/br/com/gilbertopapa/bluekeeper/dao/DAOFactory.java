package br.com.gilbertopapa.bluekeeper.dao;

public class DAOFactory {

	private static final String SENHA_SERVICO_DAO_CLASSNAME = "br.com.softblue.bluekeeper.dao.SenhaServicoXMLDAO";

	public static SenhaServicoDAO getSenhaServicoDAO() {
		try {
			SenhaServicoDAO dao = (SenhaServicoDAO) Class.forName(SENHA_SERVICO_DAO_CLASSNAME).newInstance();
			return dao;

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
