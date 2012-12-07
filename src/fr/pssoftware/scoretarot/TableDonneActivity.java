package fr.pssoftware.scoretarot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class TableDonneActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table_donne);
	    ArrayList<Donne> listD = new ArrayList<Donne>();
	    listD.add(new Donne());
	    listD.add(new Donne());
			
		//Création et initialisation de l'Adapter pour les personnes
	    DonneAdapter adapter = new DonneAdapter(this, listD);
	        
	    //Récupération du composant ListView
	    ListView list = (ListView)findViewById(R.id.td_list);
	        
	    //Initialisation de la liste avec les données
	    list.setAdapter(adapter);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_table_donne, menu);
		return true;
	}

}
