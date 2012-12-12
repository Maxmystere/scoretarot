package fr.pssoftware.scoretarot;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.app.Activity;
import android.graphics.Color;

public class NewDonneActivity extends Activity {
	private ScoreTarotDB bdd;
	private Partie partie=null;
	private long id;
	private Spinner preneur;
	private Spinner appele;
	private Spinner mort;
	private Spinner contrat;
	private Spinner petit;
	private Spinner poignee;
	private Spinner chelem;
	private Button saveButton;
	private ToggleButton attaqueButton;
	private TextView points;
	private TextView bouts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_donne);
		bdd=ScoreTarotDB.getDB(this);
		Bundle b = getIntent().getExtras();
		partie=bdd.getPartie(b.getLong("id_partie"));
		id=b.getLong("id");
		
		String[] listjJoueurs=partie.getListJoueurs().toArray(new String[partie.getListJoueurs().size()]);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listjJoueurs);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		preneur = (Spinner) findViewById(R.id.nd_preneur);
		preneur.setAdapter(adapter);
		appele = (Spinner) findViewById(R.id.nd_appele);
		appele.setAdapter(adapter);
		if (partie.getNbJoueurs()>4) findViewById(R.id.nd_lappele).setVisibility(View.VISIBLE);
		mort = (Spinner) findViewById(R.id.nd_mort);
		mort.setAdapter(adapter);
		if (partie.getNbJoueurs()==6)findViewById(R.id.nd_lmort).setVisibility(View.VISIBLE);
		contrat = (Spinner) findViewById(R.id.nd_contrat);
		petit = (Spinner) findViewById(R.id.nd_petit);
		poignee = (Spinner) findViewById(R.id.nd_poignee);
		chelem = (Spinner) findViewById(R.id.nd_chelem);
		attaqueButton = (ToggleButton) findViewById(R.id.nd_attaque);
		attaqueButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
 			@Override
 			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
 				if (isChecked) findViewById(R.id.nd_attaque_layout).setBackgroundColor(Color.GREEN);
 				else findViewById(R.id.nd_attaque_layout).setBackgroundColor(Color.DKGRAY);
  			 }
		});
		points = (TextView) findViewById(R.id.nd_points);
		bouts = (TextView) findViewById(R.id.nd_bouts);
		 
 		saveButton = (Button) findViewById(R.id.nd_save);
 		saveButton.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
/* 				Partie p=new Partie(descr.getText().toString(), value);
 				List<String> l = new ArrayList<String>();
 				for (int i=0; i<value;i++){
 					if (Joueurs[i].getText().toString().equals("")){
 						Toast.makeText(getApplicationContext(), R.string.joueur_vide, Toast.LENGTH_SHORT).show();
 						return;
 					}
 					l.add(Joueurs[i].getText().toString());
 				}
 				p.setListJoueurs(l);
 			    setResult((int) bdd.insertPartie(p));
*/ 			    finish();
 			  }
 		});
	}

}
