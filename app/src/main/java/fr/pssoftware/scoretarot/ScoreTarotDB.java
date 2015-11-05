package fr.pssoftware.scoretarot;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class ScoreTarotDB extends SQLiteOpenHelper {
	private static ScoreTarotDB INSTANCE=null;
	private SQLiteDatabase bdd;
	private static final int VERSION_DB = 2;
	private static final String NAME_DB = "scoretarot.db";
	private static final String TABLE_PARTIES = "parties";
	private static final String TABLE_DONNES = "donnes";
	private static final String TABLE_PARTIE_JOUEURS = "partie_joueurs";
	private static final String PARTIES_ID = "id";
	private static final String PARTIES_DESCR = "description";
	private static final String PARTIES_NBJOUEURS = "nbjoueurs";
	private static final String PARTIE_JOUEURS_ID = "id";
	private static final String PARTIE_JOUEURS_ID_PARTIE = "id_partie";
	private static final String PARTIE_JOUEURS_ORDRE = "ordre";
	private static final String PARTIE_JOUEURS_PSEUDO = "joueur";
	private static final String DONNES_ID = "id";
	private static final String DONNES_ID_PARTIE = "id_partie";
	private static final String DONNES_CONTRAT = "contrat";
	private static final String DONNES_PRENEUR = "preneur";
	private static final String DONNES_APPELE = "appele";
	private static final String DONNES_MORT = "mort";
	private static final String DONNES_POINTS = "points";
	private static final String DONNES_BOUTS = "bouts";
	private static final String DONNES_PETIT = "petit";
	private static final String DONNES_POIGNEE = "poignee";
	private static final String DONNES_CHELEM = "chelem";
	private static final String DONNES_MISERE = "misere";
	private static final String CREATE_PARTIES = "CREATE TABLE " + TABLE_PARTIES + " (" + PARTIES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
																						PARTIES_DESCR+" TEXT NOT NULL, " +
																						PARTIES_NBJOUEURS+" INTEGER NOT NULL);";
	private static final String CREATE_PARTIE_JOUEURS = "CREATE TABLE "+TABLE_PARTIE_JOUEURS+" ("+PARTIE_JOUEURS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
																								PARTIE_JOUEURS_ID_PARTIE+" INTEGER NOT NULL," +
																								PARTIE_JOUEURS_ORDRE+" INTEGER NOT NULL," +
																								PARTIE_JOUEURS_PSEUDO+" TEXT NOT NULL);";
	private static final String CREATE_DONNES = "CREATE TABLE "+TABLE_DONNES+" ("+DONNES_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
																					DONNES_ID_PARTIE+" INTEGER NOT NULL," +
																					DONNES_CONTRAT+" INTEGER NOT NULL," +
																					DONNES_PRENEUR+" INTEGER NOT NULL," +
																					DONNES_APPELE+"	INTEGER NOT NULL," +
																					DONNES_MORT+" INTEGER NOT NULL," +
																					DONNES_POINTS+" INTEGER NOT NULL," +
																					DONNES_BOUTS+" INTEGER NOT NULL," +
																					DONNES_PETIT+" INTEGER NOT NULL," +
																					DONNES_POIGNEE+" INTEGER NOT NULL," +
																					DONNES_CHELEM+" INTEGER NOT NULL,"+
																					DONNES_MISERE+" INTEGER NOT NULL DEFAULT -1);";

	private ScoreTarotDB(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		bdd=this.getWritableDatabase();
//		getWritableDatabase().execSQL("DROP TABLE parties;");
//		getWritableDatabase().execSQL("DROP TABLE partie_joueurs;");
//		getWritableDatabase().execSQL("DROP TABLE donnes;");
//		onCreate(getWritableDatabase());
	}
	
	public static ScoreTarotDB getDB(Context context){
		if (INSTANCE == null){
			INSTANCE = new ScoreTarotDB(context, NAME_DB, null, VERSION_DB);
		}
		return INSTANCE;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PARTIES);
		db.execSQL(CREATE_PARTIE_JOUEURS);
		db.execSQL(CREATE_DONNES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.e("DatabaseVersion", String.valueOf(oldVersion) + " -> " + String.valueOf(newVersion));
		if (oldVersion == 1 && newVersion== 2){
			db.execSQL("ALTER TABLE donnes ADD COLUMN misere INTEGER NOT NULL DEFAULT -1");
		}
	}
	
	public long insertPartie(Partie partie){
		ContentValues values = new ContentValues();
		values.put(PARTIES_DESCR, partie.getDescription());
		values.put(PARTIES_NBJOUEURS, partie.getNbJoueurs());
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
	
	public void deletePartie(long id){
		bdd.delete(TABLE_PARTIE_JOUEURS, PARTIE_JOUEURS_ID_PARTIE+"=?" , new String[]{String.valueOf(id)});
		bdd.delete(TABLE_PARTIES, PARTIES_ID+"=?" , new String[]{String.valueOf(id)});
	}
	
	public List<Partie> getListParties(String tri){
		List<Partie> l= new ArrayList<Partie>();
		Cursor c = bdd.query( TABLE_PARTIES, new String[] {PARTIES_ID, PARTIES_DESCR, PARTIES_NBJOUEURS },null,null,null,null,PARTIES_ID+" "+tri);
		if (c.getCount() > 0) {
			c.moveToFirst();
			do {
				Partie partie = new Partie();
				partie.setId(c.getLong(0));
				partie.setDescription(c.getString(1));
				partie.setNbJoueurs(c.getInt(2));
				partie.setListJoueurs(getListJoueurs(c.getLong(0)));
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
			partie.setId(c.getLong(0));
			partie.setDescription(c.getString(1));
			partie.setNbJoueurs(c.getInt(2));
			partie.setListJoueurs(getListJoueurs(c.getLong(0)));
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
		Cursor c = bdd.query(true, TABLE_PARTIE_JOUEURS, new String[] {PARTIE_JOUEURS_PSEUDO},null,null,null,null,null,null);
			if (c.getCount() > 0) {
			c.moveToFirst();
			do {
				l.add(c.getString(0));
			} while (c.moveToNext());
		}
		c.close();
		return (String[]) l.toArray(new String[l.size()]);	
		
	}
	public long insertDonne(Donne donne){
		long ret=0;
		ContentValues values = new ContentValues();
		values.put(DONNES_ID_PARTIE, donne.getPartie().getId());
		values.put(DONNES_CONTRAT, donne.getContrat());
		values.put(DONNES_PRENEUR, donne.getPreneur());
		values.put(DONNES_APPELE, donne.getAppele());
		values.put(DONNES_MORT, donne.getMort());
		values.put(DONNES_POINTS, donne.getPoints());
		values.put(DONNES_BOUTS, donne.getBouts());
		values.put(DONNES_PETIT, donne.getPetit());
		values.put(DONNES_POIGNEE, donne.getPoignee());
		values.put(DONNES_CHELEM, donne.getChelem());
		values.put(DONNES_MISERE, donne.getMisere());
		if (donne.getId()==0){
			ret=bdd.insert(TABLE_DONNES, null, values);
		}else{
			ret=bdd.update(TABLE_DONNES, values, DONNES_ID + "=?", new String[] { String.valueOf(donne.getId()) });
		}
		return ret;
	}
	public void deleteDonne(long id){
		bdd.delete(TABLE_DONNES, DONNES_ID+"=?" , new String[]{String.valueOf(id)});
	}
	
	
	public List<Donne> getListDonnes(long idPartie, boolean tri){
		Partie p=getPartie(idPartie);
		int[] score=new int[p.getNbJoueurs()];
		List<Donne> l= new ArrayList<Donne>();
		Cursor c = bdd.query( TABLE_DONNES,
							new String[] {DONNES_ID, DONNES_ID_PARTIE, DONNES_CONTRAT,DONNES_PRENEUR, DONNES_APPELE,DONNES_MORT,DONNES_POINTS,DONNES_BOUTS,DONNES_PETIT,DONNES_POIGNEE,DONNES_CHELEM,DONNES_MISERE},
							DONNES_ID_PARTIE + "=?",new String[] { String.valueOf(idPartie) },
							null,null,null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			do {
				Donne donne = new Donne();
				donne.setId(c.getLong(0));
				donne.setPartie(p);
				donne.setContrat(c.getInt(2));
				donne.setPreneur(c.getInt(3));
				if (p.getNbJoueurs()>4) donne.setAppele(c.getInt(4));
				if (p.getNbJoueurs()>5) donne.setMort(c.getInt(5));
				donne.setPoints(c.getInt(6));
				donne.setBouts(c.getInt(7));
				donne.setPetit(c.getInt(8));
				donne.setPoignee(c.getInt(9));
				donne.setChelem(c.getInt(10));
				donne.setMisere(c.getInt(11));
				for (int i=0; i<p.getNbJoueurs(); i++){
					score[i]+=donne.getPointJoueur(i);
					donne.setScore(i, score[i]);
				}
				if (tri) l.add(donne);
				else l.add(0,donne);
			} while (c.moveToNext());
		}
		//On ferme le cursor
		c.close();
		return l;
	}
	
	public Donne getDonne(long id) {
		Donne donne = null;
		Cursor c = bdd.query(TABLE_DONNES,
				new String[] { DONNES_ID, DONNES_ID_PARTIE, DONNES_CONTRAT,DONNES_PRENEUR, DONNES_APPELE,DONNES_MORT,DONNES_POINTS,DONNES_BOUTS,DONNES_PETIT,DONNES_POIGNEE,DONNES_CHELEM,DONNES_MISERE },
				DONNES_ID + "=?", new String[] { String.valueOf(id) },
				null, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			donne = new Donne();
			donne.setId(c.getLong(0));
			donne.setPartie(getPartie(c.getLong(1)));
			donne.setContrat(c.getInt(2));
			donne.setPreneur(c.getInt(3));
			donne.setAppele(c.getInt(4));
			donne.setMort(c.getInt(5));
			donne.setPoints(c.getInt(6));
			donne.setBouts(c.getInt(7));
			donne.setPetit(c.getInt(8));
			donne.setPoignee(c.getInt(9));
			donne.setChelem(c.getInt(10));
			donne.setMisere(c.getInt(11));
		}
		c.close();
		return donne;
	}

}
