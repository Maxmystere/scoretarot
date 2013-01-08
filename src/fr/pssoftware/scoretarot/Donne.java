package fr.pssoftware.scoretarot;

import android.content.Context;
import android.content.res.Resources;


public class Donne {
	private long id;
	private Partie partie;
	private int contrat;
	private int preneur=-1;
	private int appele=-1;
	private int mort=-1;
	private int points;
	private int bouts;
	private int petit;
	private int poignee;
	private int chelem;
	private int score[]=new int[6];
	
	public int getScore(int i) {
		return score[i];
	}

	public void setScore(int indice,int score) {
		this.score[indice] = score;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Partie getPartie() {
		return partie;
	}

	public void setPartie(Partie partie) {
		this.partie = partie;
	}

	public int getContrat() {
		return contrat;
	}
	
	public String getStringContrat() {
		String st="";
		switch (contrat){
		case 0:
			st="Passe";
			break;
		case 1:
			st="Prise ";
			break;
		case 2:
			st="Garde ";
			break;
		case 3:
			st="Sans ";
			break;
		case 4:
			st="Contre ";
			break;
		}
		return st;
	}

	public void setContrat(int contrat) {
		this.contrat = contrat;
	}

	public int getPreneur() {
		return preneur;
	}

	public void setPreneur(int preneur) {
		this.preneur = preneur;
	}

	public int getAppele() {
		return appele;
	}

	public void setAppele(int appele) {
		this.appele = appele;
	}

	public int getMort() {
		return mort;
	}

	public void setMort(int mort) {
		this.mort = mort;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getBouts() {
		return bouts;
	}

	public void setBouts(int bouts) {
		this.bouts = bouts;
	}

	public int getPetit() {
		return petit;
	}

	public void setPetit(int petit) {
		this.petit = petit;
	}

	public int getPoignee() {
		return poignee;
	}

	public void setPoignee(int poignee) {
		this.poignee = poignee;
	}

	public int getChelem() {
		return chelem;
	}

	public void setChelem(int chelem) {
		this.chelem = chelem;
	}

	public Donne() {
	}
	
	public int getPasse(){
		int ret=0;
		switch (bouts){
		case 0:
			ret=points-56;
			break;
		case 1:
			ret=points-51;
			break;
		case 2:
			ret=points-41;
			break;
		case 3:
			ret=points-36;
			break;
		}
		return ret;
	}
	
	public int getPointJoueur(int joueur){
		int p=getPasse();
		p=p+(p<0 ? -25: 25);
		switch (petit){
		case 1:
			p+=10;
			break;
		case 2:
			p-=10;
			break;
		}
		switch (contrat){
		case 0:
			p=0;
			break;
		case 1:
			break;
		case 2:
			p*=2;
			break;
		case 3:
			p*=4;
			break;
		case 4:
			p*=6;
			break;
		}
		switch (poignee){
		case 1:
			p+=(p<0 ? -20: 20);
			break;
		case 2:
			p+=(p<0 ? -30: 30);
			break;
		case 3:
			p+=(p<0 ? -40: 40);
			break;
		}
		switch (chelem){
		case 1:
			p+=200;
			break;
		case 2:
			p+=400;
			break;
		case 3:
			p-=200;
			break;
		case 4:
			p-=200;
			break;
		}
		switch (partie.getNbJoueurs()){
		case 6:
			if (joueur==mort) p=0;
		case 5:
			if (joueur==preneur && joueur==appele) p*=4;
			else if (joueur==preneur) p*=2;
			else if (joueur==appele) p*=1;
			else p*=-1;
			break;
		case 4:
			if (joueur==preneur) p*=3;
			else p*=-1;
			break;
		case 3:
			if (joueur==preneur) p*=2;
			else p*=-1;
			break;
		}
		return p;
	}
	
	public String toString(Context ctx){
		Resources ress=ctx.getResources();
		String ret = ress.getStringArray(R.array.donne_contrats)[contrat]+" ";
		if (contrat==0) return ret;
		int pass=getPasse();
		if (pass<0)ret+=String.format(ress.getString(R.string.donne_chutee),Math.abs(pass));
		else ret+=String.format(ress.getString(R.string.donne_passee),Math.abs(pass));
		ret+=String.format(ress.getString(R.string.donne_preneur),partie.getListJoueurs().get(preneur));
		if (partie.getNbJoueurs()>4) ret+=String.format(ress.getString(R.string.donne_appele),partie.getListJoueurs().get(appele));
		if (partie.getNbJoueurs()>5) ret+=String.format(ress.getString(R.string.donne_mort),partie.getListJoueurs().get(mort));
		ret+=String.format(ress.getString(R.string.donne_points),Math.abs(points));
		ret+=String.format(ress.getString(R.string.donne_bouts),Math.abs(bouts));
		ret+=ress.getStringArray(R.array.donne_petit)[petit];
		ret+=ress.getStringArray(R.array.donne_poignee)[poignee];
		ret+=ress.getStringArray(R.array.donne_chelem)[chelem];
		return ret;
	}
}
