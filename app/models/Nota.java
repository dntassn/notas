package models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Nota extends Model {
	
	@Required
	public String nome;
	
	public String nota;
	
	public Nota() {
		status = Status.ACTIVE;
		visualizacao = Visualizacao.PRIVATE;
		}
	

	@Enumerated(EnumType.STRING)
	public Status status;
	
	@Enumerated(EnumType.STRING)
	public Visualizacao visualizacao;
	
	@ManyToOne
	public Usuario usuario;
	
	@ManyToOne
	public Pasta pasta;
}