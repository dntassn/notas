package controllers;

import models.Usuario;
import play.data.validation.Valid;
import play.mvc.Controller;

public class Logins extends Controller {

	public static void login() {
		if(session.get("usuarioLogado") != null){
			Notas.listar();
		} else {
			render();
		}
		
	}

	public static void logar(String email, String senha) {
		
		Usuario usuario = Usuario.find("email = ?1 and senha = ?2", email, senha).first();
		if (usuario != null) {
			session.put("usuarioLogado", usuario.nome);
			session.put("usuarioLogadoId", usuario.id);
			Notas.listar();
		}
		
		flash.error("Credenciais inválidas");
		login();
	}
	
	private static void redirecionarErros() {
		params.flash();
		validation.keep();
		login();
	}
	
	public static void salvar(@Valid Usuario usuario) {
		usuario.nome = usuario.nome.toUpperCase();
		usuario.email = usuario.email.toLowerCase();
		
		if (validation.hasErrors()) {
			redirecionarErros();
		}
		
		Usuario usuarios = Usuario.find("email = ?1", usuario.email).first();
		if (usuarios != null) {
			flash.error("Já existe uma conta cadastrada com o e-mail informado");
			redirecionarErros();
		}
		
		usuario.save();
		flash.success("Usuário cadastrado com sucesso.");

		Logins.login();
	}
	
	
	public static void logout() {
		session.clear();
		login();
	}
}