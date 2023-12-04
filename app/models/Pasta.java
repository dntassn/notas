package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Pasta extends Model {
	public String nome;	
	@ManyToOne
	public Usuario usuario;
}