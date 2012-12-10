package fr.pssoftware.scoretarot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ScoreTarotDB extends SQLiteOpenHelper {
	private static final String CREATE_PARTIES = "CREATE TABLE parties (id INTEGER PRIMARY KEY AUTOINCREMENT, description TEXT NOT NULL);";
	private static final String CREATE_JOUEURS = "CREATE TABLE joueurs (id INTEGER PRIMARY KEY AUTOINCREMENT, pseudo TEXT NOT NULL);";
	private static final String CREATE_PARTIES_JOUEURS = "CREATE TABLE parties_joueurs (id INTEGER PRIMARY KEY AUTOINCREMENT, id_partie INTEGER NOT NULL, id_joueur INTEGER NOT NULL);";
	private static final String CREATE_DONNES = "CREATE TABLE donnes (id INTEGER PRIMARY KEY AUTOINCREMENT," +
																		"id_partie INTEGER NOT NULL," +
																		"contrat INTEGER NOT NULL," +
																		"preneur INTEGER NOT NULL" +
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
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PARTIES);
		db.execSQL(CREATE_JOUEURS);
		db.execSQL(CREATE_PARTIES_JOUEURS);
		db.execSQL(CREATE_DONNES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE parties;");
		db.execSQL("DROP TABLE joueurs;");
		db.execSQL("DROP TABLE parties_joueurs;");
		db.execSQL("DROP TABLE donnes;");
		onCreate(db);
	}
}
