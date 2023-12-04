package controllers;

import java.util.Collections;
import java.util.List;

import models.Nota;
import models.Pasta;
import models.Status;
import models.Usuario;
import models.Visualizacao;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Notas extends Controller {
	
	public static void form() {
		List<Usuario> usuarios = Usuario.findAll();
		List<Pasta> pastas = Pasta.findAll();
		render(usuarios, pastas);
	}
	
	public static void salvar(Nota n) {
		if (validation.hasErrors()) {
			validation.keep();
			editar(n.id);
		}

		


		Nota n2 = Nota.find("nome = ?1", n.nome).first();
		if (n2 != null) {
			flash.error("Já existe uma nota cadastrada com o nome informado");
			listar();
		}
		n.save();
		
		listar();
	}

	public static void detalhar(Long id) {
		Nota nota = Nota.findById(id);
		

		if (nota.visualizacao == Visualizacao.PRIVATE){
			String uId = Long.toString(nota.usuario.id);
			if(uId.equals(session.get("usuarioLogadoId"))){
				String x = "x";
				render(nota, x);
			} else {
				listar();
			}
		} else {
						String uId = Long.toString(nota.usuario.id);

			if(uId.equals(session.get("usuarioLogadoId"))){
				String x = "x";
				render(nota, x);
				
			} else {
				render(nota);
			}
			
		}


		
		
	}

	public static void detalharLixeira(Long id) {
		Nota nota = Nota.findById(id);
		String uId = Long.toString(nota.usuario.id);
		if(uId.equals(session.get("usuarioLogadoId"))){
			System.out.print("JH");
			render(nota);
		} else {
			listar();
		}
	}
	
	

	
	public static void listar() {




String search = params.get("search");
		
		List<Nota> notas = Collections.EMPTY_LIST;
		if (search == null || search.isEmpty()) {
			notas = Nota.find("status = ?1 AND usuario_id = ?2", Status.ACTIVE, session.get("usuarioLogadoId")).fetch();
		} else {
			notas = Nota.find("(lower(nome) like ?1 OR pasta_id like ?2) AND status = ?3 AND usuario_id = ?4", 
					"%" + search.toLowerCase() + "%",
					"%" + search.toLowerCase() + "%",
					Status.ACTIVE,
					session.get("usuarioLogadoId")).fetch();
		}
		

		List<Pasta> pastas = Pasta.find("usuario_id = ?1", session.get("usuarioLogadoId")).fetch();		

		render(notas, pastas, search);
	}
	
	public static void deletar(Long id) {
		Nota nota = Nota.findById(id);
		String uId = Long.toString(nota.usuario.id);
		if(uId.equals(session.get("usuarioLogadoId"))){
			nota.status = Status.INACTIVE;
		nota.save();
		listar();
		} else {
			listar();
		}



		nota.status = Status.INACTIVE;
		nota.save();
		listar();
	}

	public static void Lixeira() {
		String search = params.get("search");
		
		List<Nota> notas = Collections.EMPTY_LIST;
		if (search == null || search.isEmpty()) {
			notas = Nota.find("status = ?1 AND usuario_id = ?2", Status.INACTIVE, session.get("usuarioLogadoId")).fetch();
		} else {
			notas = Nota.find("(lower(nome) like ?1 OR pasta_id like ?2) AND status = ?3 AND usuario_id = ?4", 
					"%" + search.toLowerCase() + "%",
					"%" + search.toLowerCase() + "%",
					Status.ACTIVE,
					session.get("usuarioLogadoId")).fetch();
		}
		render(notas, search);
	}


	public static void deletarPermanentemente(Long id) {
		Nota nota = Nota.findById(id);

		String uId = Long.toString(nota.usuario.id);
		if(uId.equals(session.get("usuarioLogadoId"))){
			nota.status = Status.DELETED;
		nota.save();
		listar();
		} else {
			listar();
		}

		
	}
	
	public static void recuperar(Long id) {
		Nota nota = Nota.findById(id);
		String uId = Long.toString(nota.usuario.id);

		if(uId.equals(session.get("usuarioLogadoId"))){
			nota.status = Status.ACTIVE;
		nota.save();
		listar();
		} else {
			listar();
		}

		
	}


	public static void editar(Long id) {
		Nota n = Nota.findById(id);
				List<Pasta> pastas = Pasta.findAll();
				List<Usuario> usuarios = Usuario.findAll();

		renderTemplate("Notas/form.html", n, pastas, usuarios);
	}

	public static void publico(Long id) {
		Nota nota = Nota.findById(id);
				String uId = Long.toString(nota.usuario.id);

		if(nota.visualizacao.toString() == "PUBLIC"){
			nota.visualizacao = Visualizacao.PRIVATE;
		} else {
			nota.visualizacao = Visualizacao.PUBLIC;
		}
		nota.save();
		detalhar(nota.id);
	}

	public static void publicas() {



		String search = params.get("search");
		
		List<Nota> notas = Collections.EMPTY_LIST;
		if (search == null || search.isEmpty()) {
			notas = Nota.find("visualizacao = ?1 AND status = ?2", Visualizacao.PUBLIC, Status.ACTIVE ).fetch();
		} else {
			notas = Nota.find("(lower(nome) like ?1 OR pasta.nome like ?2) AND visualizacao = ?3", 
					"%" + search.toLowerCase() + "%",
					"%" + search.toLowerCase() + "%",
					Visualizacao.PUBLIC).fetch();
		}
		render(notas, search);
	}

}