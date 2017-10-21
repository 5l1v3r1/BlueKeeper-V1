package br.com.gilbertopapa.bluekeeper.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.com.gilbertopapa.bluekeeper.model.SenhaServico;
import br.com.gilbertopapa.bluekeeper.util.CryptoUtils;

public class SenhaServicoXMLDAO implements SenhaServicoDAO {
	
	private static final byte[] SECRET_KEY = "LDJGOGDLKJFSDYFK".getBytes();
	private static final Path STORAGE_FILE = Paths.get(System.getProperty("user.home"), "senhas.xml");

	@Override
	public List<SenhaServico> load() {
		List<SenhaServico> senhasServico = new ArrayList<>();
		
		try {
			if (!Files.exists(STORAGE_FILE)) {
				return senhasServico;
			}
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(STORAGE_FILE.toFile());
			
			NodeList senhaServicoTags = doc.getElementsByTagName("SenhaServico");
			
			for (int i = 0; i < senhaServicoTags.getLength(); i++) {
				Element senhaServicoTag = (Element) senhaServicoTags.item(i);
				SenhaServico senhaServico = new SenhaServico();
				senhaServico.setId(Integer.parseInt(senhaServicoTag.getAttribute("id")));
				senhaServico.setServico(senhaServicoTag.getElementsByTagName("Servico").item(0).getTextContent());
				senhaServico.setLogin(senhaServicoTag.getElementsByTagName("Login").item(0).getTextContent());
				senhaServico.setSenha(decrypt(senhaServicoTag.getElementsByTagName("Senha").item(0).getTextContent()));
				senhaServico.setObservacoes(senhaServicoTag.getElementsByTagName("Observacoes").item(0).getTextContent());
				senhasServico.add(senhaServico);
			}
			
			return senhasServico;
		
		} catch (SAXException | IOException | ParserConfigurationException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void store(List<SenhaServico> senhasServico) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.newDocument();
			
			Element rootTag = doc.createElement("BlueKeeper");
			doc.appendChild(rootTag);
			
			senhasServico.forEach(e -> createSenhaServicoTag(doc, e));
			
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(STORAGE_FILE.toFile());
			t.transform(source, result);

		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	
	@Override
	public List<SenhaServico> filter(String text) {
		List<SenhaServico> itens = load();
		String textUpper = text.toUpperCase();
		
		return itens.stream()
			.filter(s -> s.getLogin().toUpperCase().contains(textUpper) || s.getServico().toUpperCase().contains(text))
			.collect(Collectors.toList());
	}
	
	@Override
	public int generateId() {
		List<SenhaServico> itens = load();
		
		OptionalInt maxId = itens.stream()
			.mapToInt(s -> s.getId())
			.max();
		
		if (maxId.isPresent()) {
			return maxId.getAsInt() + 1;
		}
		
		return 1;
	}
	
	private Element createSenhaServicoTag(Document doc, SenhaServico senhaServico) {
		Element rootTag = doc.getDocumentElement();
		
		Element senhaServicoTag = doc.createElement("SenhaServico");
		senhaServicoTag.setAttribute("id", String.valueOf(senhaServico.getId()));
		
		Element servicoTag = doc.createElement("Servico");
		servicoTag.appendChild(doc.createTextNode(senhaServico.getServico()));
		senhaServicoTag.appendChild(servicoTag);
		
		Element loginTag = doc.createElement("Login");
		loginTag.appendChild(doc.createTextNode(senhaServico.getLogin()));
		senhaServicoTag.appendChild(loginTag);
		
		Element senhaTag = doc.createElement("Senha");
		senhaTag.appendChild(doc.createTextNode(encrypt(senhaServico.getSenha())));
		senhaServicoTag.appendChild(senhaTag);
		
		Element observacoes = doc.createElement("Observacoes");
		observacoes.appendChild(doc.createCDATASection(senhaServico.getObservacoes() == null ? "" : senhaServico.getObservacoes()));
		senhaServicoTag.appendChild(observacoes);
		
		rootTag.appendChild(senhaServicoTag);

		return senhaServicoTag;
	}
	
	private String encrypt(String senha) {
		try {
			byte[] encBytes = CryptoUtils.encryptAES(SECRET_KEY, senha.getBytes());
			byte[] base64Bytes = Base64.getEncoder().encode(encBytes);
			return new String(base64Bytes);
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	
	private String decrypt(String senha) {
		try {
			byte[] base64Bytes = senha.getBytes();
			byte[] encBytes = Base64.getDecoder().decode(base64Bytes);
			byte[] decBytes = CryptoUtils.decryptAES(SECRET_KEY, encBytes);
			return new String(decBytes);
		
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
}
