package fr.pssoftware.scoretarot;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ScoreTarotDB extends SQLiteOpenHelper {
	private static ScoreTarotDB INSTANCE=null;
	private SQLiteDatabase bdd;
	private static final int VERSION_DB = 1;
	private static final String NAME_DB = "scoretarot.db";
	private static final String TABLE_PARTIES = "parties";
	private static final String TABLE_PARTIE_JOUEURS = "partie_joueurs";
	private static final String PARTIES_ID = "id";
	private static final String PARTIES_DESCR = "description";
	private static final String PARTIES_NBJOUEURS = "nbjoueurs";
	private static final String PARTIE_JOUEURS_ID = "id";
	private static final String PARTIE_JOUEURS_ID_PARTIE = "id_partie";
	private static final String PARTIE_JOUEURS_ORDRE = "ordre";
	private static final String PARTIE_JOUEURS_PSEUDO = "joueur";
	private static final String CREATE_PARTIES = "CREATE TABLE " + TABLE_PARTIES + " (" + PARTIES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
																						PARTIES_DESCR+" TEXT NOT NULL, " +
																						PARTIES_NBJOUEURS+" INTEGER NOT NULL);";
	private static final String CREATE_PARTIE_JOUEURS = "CREATE TABLE "+TABLE_PARTIE_JOUEURS+" ("+PARTIE_JOUEURS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
																								PARTIE_JOUEURS_ID_PARTIE+" INTEGER NOT NULL," +
																								PARTIE_JOUEURS_ORDRE+" INTEGER NOT NULL," +
																								PARTIE_JOUEURS_PSEUDO+" TEXT NOT NULL);";
	private static final String CREATE_DONNES = "CREATE TABLE donnes (id INTEGER PRIMARY KEY AUTOINCREMENT," +
																		"id_partie INTEGER NOT NULL," +
																		"contrat INTEGER NOT NULL," +
																		"preneur INTEGER NOT NULL," +
																		"appele	INTEGER NOT NULL," +
																		"mort INTEGER NOT NULL," +
																		"points INTEGER NOT NULL," +
																		"bouts INTEGER NOT NULL," +
																		"petit INTEGER NOT NULL," +
																		"poignee INTEGER NOT NULL," +
																		"chelem INTEGER NOT NULL);";

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
		db.execSQL("DROP TABLE parties;");
		db.execSQL("DROP TABLE partie_joueurs;");
		db.execSQL("DROP TABLE donnes;");
		onCreate(db);
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
}
