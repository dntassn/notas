package controllers;

import java.util.List;

import models.Nota;
import models.Usuario;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Usuarios extends Controller {

	public static void form() {
		render();
	}
	
	public static void editar(Long id) {
		Usuario p = Usuario.findById(id);
		renderTemplate("Usuarios/form.html", p);
	}
	
	public static void remover(Long id) {
		Usuario p = Usuario.findById(id);
		String uId = Long.toString(p.id);
		if(uId.equals(session.get("usuarioLogadoId"))){
			p.delete();
		Logins.logout();
		} else {
			Notas.listar();
		}
		
	}
	
	private static void redirecionarErros() {
		params.flash();
		validation.keep();
		form();
	}
	
	public static void listar(String termo) {	




		List<Usuario> usuarios = null;
		if (termo == null || termo.isEmpty()) {
						usuarios = Usuario.find("id = ?1", session.get("usuarioLogadoId")).fetch();		
		} else {
			usuarios = Usuario.find("lower(nome) like ?1 or lower(email) like ?1",
					"%"+ termo.toLowerCase() +"%").fetch();
		}
		render(usuarios, termo);
	}
	
	public static void detalhar(Long id) {
		Usuario usuario = Usuario.findById(id);
			String uId = Long.toString(usuario.id);

			if(uId.equals(session.get("usuarioLogadoId"))){
				render(usuario);
				
			} else {
				Notas.listar();
			}


		
		
	}
	
	public static void salvar(@Valid Usuario usuario) {
		if (validation.hasErrors()) {
			validation.keep();
			editar(usuario.id);
		}

		



		usuario.nome = usuario.nome.toUpperCase();
		usuario.email = usuario.email.toLowerCase();

		


		Usuario usuario2 = Usuario.find("email = ?1", usuario.email).first();
		if (usuario2 != null) {
			flash.error("Já existe uma conta cadastrada com o email informado");
			editar(usuario.id);
		}
		
		usuario.save();
		Usuarios.detalhar(usuario.id);
	}
}