package fr.pssoftware.scoretarot;

import java.util.ArrayList;
import java.util.List;

public class Partie {
	private long id;
	private String description;
	private int nbJoueurs;
	private List<String> listJoueurs = new ArrayList<String>();
	
	public List<String> getListJoueurs() {
		return listJoueurs;
	}
	public void setListJoueurs(List<String> listJoueurs) {
		this.listJoueurs = listJoueurs;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getNbJoueurs() {
		return nbJoueurs;
	}
	public void setNbJoueurs(int nbJoueurs) {
		this.nbJoueurs = nbJoueurs;
	}
	
	public Partie() {
	}

	public Partie(String descr, int nbj) {
		description = descr;
		nbJoueurs =  nbj;
	}

}
