package fr.pssoftware.scoretarot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ScoreTarotDB extends SQLiteOpenHelper {
	private static ScoreTarotDB INSTANCE=null;
	private static final int VERSION_DB = 1;
	private static final String NAME_DB = "scoretarot.db";
	private static final String CREATE_PARTIES = "CREATE TABLE parties (id INTEGER PRIMARY KEY AUTOINCREMENT, description TEXT NOT NULL, nbjoueurs INTEGER NOT NULL);";
	private static final String CREATE_PARTIE_JOUEURS = "CREATE TABLE partie_joueurs (id INTEGER PRIMARY KEY AUTOINCREMENT, id_partie INTEGER NOT NULL, ordre INTEGER NOT NULL , joueur TEXT NOT NULL);";
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

	public ScoreTarotDB(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
//		getWritableDatabase().execSQL("DROP TABLE parties;");
//		getWritableDatabase().execSQL("DROP TABLE partie_joueurs;");
//		getWritableDatabase().execSQL("DROP TABLE donnes;");
//		onCreate(getWritableDatabase());
	}
	
	public static SQLiteDatabase getDB(Context context){
		if (INSTANCE == null){
			INSTANCE = new ScoreTarotDB(context, NAME_DB, null, VERSION_DB);
		}
		return INSTANCE.getWritableDatabase();
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
}
