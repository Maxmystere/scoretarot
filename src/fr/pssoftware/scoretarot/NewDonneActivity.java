package fr.pssoftware.scoretarot;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;
import android.app.Activity;
import android.graphics.Color;

public class NewDonneActivity extends Activity {
	private ScoreTarotDB bdd;
	private Partie partie=null;
	private long id=0;
	private Spinner preneur;
	private Spinner appele;
	private Spinner mort;
	private Spinner contrat;
	private Spinner petit;
	private Spinner poignee;
	private Spinner chelem;
	private Button saveButton;
	private ToggleButton attaqueButton;
	private EditText points;
	private EditText bouts;
	
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
		points = (EditText) findViewById(R.id.nd_points);
		bouts = (EditText) findViewById(R.id.nd_bouts);
		 
 		saveButton = (Button) findViewById(R.id.nd_save);
 		saveButton.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				int po;
 				int bo;
 				Donne d= new Donne();
 				if (id != 0) d.setId(id);
 				d.setPartie(partie);
 				d.setPreneur(preneur.getSelectedItemPosition());
 				if (partie.getNbJoueurs()>4) d.setAppele(appele.getSelectedItemPosition());
 				if (partie.getNbJoueurs()>5) d.setMort(mort.getSelectedItemPosition());
 				d.setContrat(contrat.getSelectedItemPosition());
				if (attaqueButton.isChecked()) {
 					po=Integer.valueOf(points.getText().toString());
					bo=Integer.valueOf(bouts.getText().toString());
				}else{
 					po=91-Integer.valueOf(points.getText().toString());
					bo=3-Integer.valueOf(bouts.getText().toString());
				}
 				d.setPoints(po);
 				d.setBouts(bo);
 				d.setPetit(petit.getSelectedItemPosition());
 				d.setPoignee(poignee.getSelectedItemPosition());
 				d.setChelem(chelem.getSelectedItemPosition());
			    setResult((int) bdd.insertDonne(d));
 			    finish();
 			  }
 		});
 		if (id !=0) initData();
	}
	
	private void initData(){
		Donne d=bdd.getDonne(id);
		preneur.setSelection(d.getPreneur());
		if (partie.getNbJoueurs()>4) appele.setSelection(d.getAppele());
		if (partie.getNbJoueurs()>45) mort.setSelection(d.getMort());
		contrat.setSelection(d.getContrat());
		points.setText(String.valueOf(d.getPoints()));
		bouts.setText(String.valueOf(d.getBouts()));
		petit.setSelection(d.getPetit());
		poignee.setSelection(d.getPoignee());
		chelem.setSelection(d.getChelem());
	}

}
