package fr.pssoftware.scoretarot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.R.color;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class TableDonneActivity extends Activity {
	private Partie partie=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table_donne);
		Bundle b=getIntent().getExtras();
		partie=PartiesDB.getInstance(getApplicationContext()) .getPartie(b.getLong("id_partie"));
		
		LinearLayout header= (LinearLayout) findViewById(R.id.td_header);
		header.removeAllViewsInLayout();;
		for (String j:partie.getListJoueurs()){
			TextView child=new TextView(this);
			child.setText(j);
			child.setBackgroundColor(Color.parseColor("#000000"));
			child.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(0 ,LinearLayout.LayoutParams.WRAP_CONTENT ) ;
			layoutParam.weight=1;
			layoutParam.setMargins(1, 1, 1, 1);
			header .addView( child , layoutParam );
		}
		
		
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
