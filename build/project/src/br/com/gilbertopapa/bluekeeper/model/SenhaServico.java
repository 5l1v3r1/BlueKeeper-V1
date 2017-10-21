package br.com.gilbertopapa.bluekeeper.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SenhaServico {

	private IntegerProperty id = new SimpleIntegerProperty();
	private StringProperty servico = new SimpleStringProperty();
	private StringProperty login = new SimpleStringProperty();
	private StringProperty senha = new SimpleStringProperty();
	private StringProperty observacoes = new SimpleStringProperty();

	public int getId() {
		return id.get();
	}

	public void setId(int id) {
		this.id.set(id);
	}

	public String getServico() {
		return servico.get();
	}

	public void setServico(String servico) {
		this.servico.set(servico);
	}

	public String getLogin() {
		return login.get();
	}

	public void setLogin(String login) {
		this.login.set(login);
	}

	public String getSenha() {
		return senha.get();
	}

	public void setSenha(String senha) {
		this.senha.set(senha);
	}

	public String getObservacoes() {
		return observacoes.get();
	}

	public void setObservacoes(String observacoes) {
		this.observacoes.set(observacoes);
	}

	public StringProperty servicoProperty() {
		return servico;
	}

	public StringProperty loginProperty() {
		return login;
	}

	public StringProperty senhaProperty() {
		return senha;
	}

	public StringProperty observacoesProperty() {
		return observacoes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SenhaServico other = (SenhaServico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SenhaServico [id=" + id + ", servico=" + servico + ", login=" + login + ", senha=" + senha
				+ ", observacoes=" + observacoes + "]";
	}
}
