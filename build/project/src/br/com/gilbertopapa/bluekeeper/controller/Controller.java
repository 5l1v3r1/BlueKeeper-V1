package br.com.gilbertopapa.bluekeeper.controller;

import java.util.List;

import br.com.gilbertopapa.bluekeeper.dao.DAOFactory;
import br.com.gilbertopapa.bluekeeper.dao.SenhaServicoDAO;
import br.com.gilbertopapa.bluekeeper.model.SenhaServico;
import br.com.gilbertopapa.bluekeeper.util.StringUtils;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {

	@FXML
	private TableView<SenhaServico> table;
	@FXML
	private TextField txtServico;
	@FXML
	private TextField txtLogin;
	@FXML
	private TextField txtSenha;
	@FXML
	private TextArea txtObservacoes;
	@FXML
	private Button btnNovo;
	@FXML
	private Button btnEditar;
	@FXML
	private Button btnExcluir;
	@FXML
	private Button btnCancel;
	@FXML
	private Button btnConfirm;
	@FXML
	private TextField txtSearch;
	@FXML
	private Button btnSearch;
	@FXML
	private Button btnClearSearch;

	private BooleanProperty editMode = new SimpleBooleanProperty();
	
	private BooleanProperty resultsFiltered = new SimpleBooleanProperty();

	private SenhaServico currentSenhaServico;

	private SenhaServicoDAO dao;

	@FXML
	private void initialize() {
		dao = DAOFactory.getSenhaServicoDAO();
		List<SenhaServico> itens = dao.load();
		table.setItems(FXCollections.observableArrayList(itens));

		table.getSelectionModel().selectedItemProperty().addListener((event, oldValue, newValue) -> {
			unbindData(oldValue);
			bindData(newValue);
		});

		btnNovo.disableProperty().bind(editMode);
		btnEditar.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull().or(editMode));
		btnExcluir.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull().or(editMode));
		btnCancel.disableProperty().bind(editMode.not());
		btnConfirm.disableProperty().bind(editMode.not());
		btnSearch.disableProperty().bind(txtSearch.textProperty().isEmpty());
		btnClearSearch.disableProperty().bind(resultsFiltered.not());

		txtServico.disableProperty().bind(editMode.not());
		txtLogin.disableProperty().bind(editMode.not());
		txtSenha.disableProperty().bind(editMode.not());
		txtObservacoes.disableProperty().bind(editMode.not());

		table.disableProperty().bind(editMode);
	}

	@FXML
	public void exit() {
		Platform.exit();
	}

	private void bindData(SenhaServico senhaServico) {
		if (senhaServico != null) {
			txtServico.textProperty().bindBidirectional(senhaServico.servicoProperty());
			txtLogin.textProperty().bindBidirectional(senhaServico.loginProperty());
			txtSenha.textProperty().bindBidirectional(senhaServico.senhaProperty());
			txtObservacoes.textProperty().bindBidirectional(senhaServico.observacoesProperty());
		}
	}

	private void unbindData(SenhaServico senhaServico) {
		if (senhaServico != null) {
			txtServico.textProperty().unbindBidirectional(senhaServico.servicoProperty());
			txtLogin.textProperty().unbindBidirectional(senhaServico.loginProperty());
			txtSenha.textProperty().unbindBidirectional(senhaServico.senhaProperty());
			txtObservacoes.textProperty().unbindBidirectional(senhaServico.observacoesProperty());

			clearForm();
		}
	}

	public void onNew() {
		table.getSelectionModel().clearSelection();
		editMode.set(true);
		currentSenhaServico = new SenhaServico();
		bindData(currentSenhaServico);
		txtServico.requestFocus();
	}

	@FXML
	public void onEdit() {
		editMode.set(true);
		currentSenhaServico = table.getSelectionModel().getSelectedItem();
	}

	@FXML
	public void onDelete() {
		table.getItems().remove(table.getSelectionModel().getSelectedItem());
		dao.store(table.getItems());
	}

	@FXML
	public void onCancel() {
		editMode.set(false);

		if (currentSenhaServico.getId() == 0) {
			unbindData(currentSenhaServico);
			clearForm();
		}
	}

	@FXML
	public void onConfirm() {
		String errorMessage = validateForm();
		if (!errorMessage.isEmpty()) {
			showValidationError(errorMessage);
			return;
		}

		editMode.set(false);

		if (currentSenhaServico.getId() == 0) {
			currentSenhaServico.setId(dao.generateId());
			table.getItems().add(currentSenhaServico);
			unbindData(currentSenhaServico);
			clearForm();
			table.getSelectionModel().select(currentSenhaServico);
		}

		dao.store(table.getItems());
	}

	@FXML
	public void search() {
		List<SenhaServico> filterList = dao.filter(txtSearch.getText());
		table.setItems(FXCollections.observableArrayList(filterList));
		resultsFiltered.set(true);
	}
	
	@FXML
	public void clearSearch() {
		List<SenhaServico> itens = dao.load();
		table.setItems(FXCollections.observableArrayList(itens));
		txtSearch.clear();
		resultsFiltered.set(false);
	}

	private void clearForm() {
		txtServico.clear();
		txtLogin.clear();
		txtSenha.clear();
		txtObservacoes.clear();
	}

	private String validateForm() {
		StringBuilder errorMessage = new StringBuilder();

		if (StringUtils.isEmpty(currentSenhaServico.getServico())) {
			errorMessage.append("Preencha o site/serviço").append(StringUtils.newLine());
		}

		if (StringUtils.isEmpty(currentSenhaServico.getLogin())) {
			errorMessage.append("Preencha o login").append(StringUtils.newLine());
		}

		if (StringUtils.isEmpty(currentSenhaServico.getSenha())) {
			errorMessage.append("Preencha a senha").append(StringUtils.newLine());
		}

		return errorMessage.toString();
	}

	private void showValidationError(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Erro de validação");
		alert.setHeaderText("Informação incorreta");
		alert.setContentText(message);
		alert.showAndWait();
	}
}
