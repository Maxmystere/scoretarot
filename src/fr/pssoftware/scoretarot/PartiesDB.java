package fr.pssoftware.scoretarot;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PartiesDB {
	private static PartiesDB INSTANCE;
	private static final String TABLE_PARTIES = "parties";
	private static final String TABLE_PARTIE_JOUEURS = "partie_joueurs";
	private static final int NUM_PARTIES_ID = 0;
	private static final String PARTIES_ID = "id";
	private static final int NUM_PARTIES_DESCR = 1;
	private static final String PARTIES_DESCR = "description";
	private static final int NUM_PARTIES_NBJOUEURS = 2;
	private static final String PARTIES_NBJOUEURS = "nbjoueurs";
	private static final int NUM_PARTIE_JOUEURS_ID = 0;
	private static final String PARTIE_JOUEURS_ID = "id";
	private static final int NUM_PARTIE_JOUEURS_ID_PARTIE = 1;
	private static final String PARTIE_JOUEURS_ID_PARTIE = "id_partie";
	private static final int NUM_PARTIE_JOUEURS_ORDRE = 2;
	private static final String PARTIE_JOUEURS_ORDRE = "ordre";
	private static final int NUM_PARTIE_JOUEURS_PSEUDO = 3;
	private static final String PARTIE_JOUEURS_PSEUDO = "joueur";
	
	private SQLiteDatabase bdd;
	
	public static PartiesDB getInstance(Context context){
		if (INSTANCE == null ){
			INSTANCE= new PartiesDB(context);
		}
		return INSTANCE;
	}
	
	public PartiesDB(Context context) {
		bdd = ScoreTarotDB.getDB(context);
	}

	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public long insertPartie(Partie partie){
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(PARTIES_DESCR, partie.getDescription());
		values.put(PARTIES_NBJOUEURS, partie.getNbJoueurs());
		//on insère l'objet dans la BDD via le ContentValues
		long rowid=bdd.insert(TABLE_PARTIES, null, values);	;
		int n=0;
		for (String joueur: partie.getListJoueurs()){
			values.clear();
			values.put(PARTIE_JOUEURS_ID_PARTIE, rowid);
			values.put(PARTIE_JOUEURS_ORDRE, n++);
			values.put(PARTIE_JOUEURS_PSEUDO, joueur);
			bdd.insert(TABLE_PARTIE_JOUEURS, null, values);
		}
		return rowid;
	}
	
	public List<Partie> getListParties(){
		List<Partie> l= new ArrayList<Partie>();
		Cursor c = bdd.query( TABLE_PARTIES, new String[] {PARTIES_ID, PARTIES_DESCR, PARTIES_NBJOUEURS },null,null,null,null,null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			do {
				Partie partie = new Partie();
				partie.setId(c.getLong(NUM_PARTIES_ID));
				partie.setDescription(c.getString(NUM_PARTIES_DESCR));
				partie.setNbJoueurs(c.getInt(NUM_PARTIES_NBJOUEURS));
				partie.setListJoueurs(getListJoueurs(c.getLong(NUM_PARTIES_ID)));
				l.add(partie);
			} while (c.moveToNext());
		}
		//On ferme le cursor
		c.close();
		return l;
	}
	
	public Partie getPartie(long id) {
		Partie partie = null;
		Cursor c = bdd.query(TABLE_PARTIES, new String[] { PARTIES_ID,
				PARTIES_DESCR, PARTIES_NBJOUEURS }, PARTIES_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			partie = new Partie();
			partie.setId(c.getLong(NUM_PARTIES_ID));
			partie.setDescription(c.getString(NUM_PARTIES_DESCR));
			partie.setNbJoueurs(c.getInt(NUM_PARTIES_NBJOUEURS));
			partie.setListJoueurs(getListJoueurs(c.getLong(NUM_PARTIES_ID)));
		}
		c.close();
		return partie;
	}
	
	public List<String> getListJoueurs(long idPartie){
		List<String> l = new ArrayList<String>();
		Cursor c = bdd.query( TABLE_PARTIE_JOUEURS, new String[] {PARTIE_JOUEURS_PSEUDO},PARTIE_JOUEURS_ID_PARTIE + "=? ",new String[]{String.valueOf(idPartie)},null,null,PARTIE_JOUEURS_ORDRE);
			if (c.getCount() > 0) {
			c.moveToFirst();
			do {
				l.add(c.getString(0));
			} while (c.moveToNext());
		}
		c.close();
		return l;	
	}
	
	public String[] getListTotalJoueurs(){
		List<String> l = new ArrayList<String>();
		Cursor c = bdd.query( TABLE_PARTIE_JOUEURS, new String[] {PARTIE_JOUEURS_PSEUDO},null,null,null,null,null);
			if (c.getCount() > 0) {
			c.moveToFirst();
			do {
				l.add(c.getString(0));
			} while (c.moveToNext());
		}
		c.close();
		return (String[]) l.toArray(new String[l.size()]);	
		
	}
}
