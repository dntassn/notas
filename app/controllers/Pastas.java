package controllers;

import java.util.List;

import models.Pasta;
import models.Usuario;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Pastas extends Controller {

	public static void form() {
		List<Usuario> usuarios = Usuario.findAll();
		render(usuarios);
	}
	
	public static void editar(Long id) {
		Pasta p = Pasta.findById(id);
		renderTemplate("Pastas/form.html", p);
	}
	
	public static void remover(Long id) {
		Pasta p = Pasta.findById(id);
			p.delete();
		Logins.logout();
	}
	
	public static void listar(String termo) {	
		List<Pasta> pastas = null;
		if (termo == null || termo.isEmpty()) {
			pastas = Pasta.find("usuario_id = ?1", session.get("usuarioLogadoId")).fetch();		
		} else {
			pastas = Pasta.find("usuario_id = ?1", session.get("usuarioLogadoId")).fetch();		
		}
		render(pastas, termo);
	}
	
	public static void detalhar(Long id) {
		Pasta pasta = Pasta.findById(id);
		render(pasta);
	}
	
	public static void salvar(@Valid Pasta pasta) {
		if (validation.hasErrors()) {
			validation.keep();
			editar(pasta.id);
		}

		Pasta pasta2 = Pasta.find("nome = ?1", pasta.nome).first();
		if (pasta2 != null) {
			flash.error("Já existe uma pasta cadastrada com o nome informado");
			Notas.listar();
		}
		
		pasta.save();	
		Notas.listar();	
	}	
	private static void redirecionarErros() {
		params.flash();
		validation.keep();
		form();
	}


}