package fr.pssoftware.scoretarot;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class GraphActivity extends SherlockActivity {
	private ScoreTarotDB bdd;
	private Partie partie=null;
	private LinearLayout layout;
	private Spinner preneur;
	private Spinner appele;
	private Spinner mort;
	private Spinner contrat;
	private Spinner petit;
	private Spinner poignee;
	private Spinner chelem;
	private ToggleButton attaqueButton;
	private EditText points;
	private EditText bouts;
	private List<GraphViewStyle>  styles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		styles=new ArrayList<GraphViewStyle>();
		styles.add(new GraphViewStyle(Color.rgb(200, 00, 00),2));
		styles.add(new GraphViewStyle(Color.rgb(00, 200, 00),2));
		styles.add(new GraphViewStyle(Color.rgb(00, 00, 200),2));
		styles.add(new GraphViewStyle(Color.rgb(200, 200, 00),2));
		styles.add(new GraphViewStyle(Color.rgb(200, 00, 200),2));
		styles.add(new GraphViewStyle(Color.rgb(00, 200, 200),2));
		bdd=ScoreTarotDB.getDB(this);
		setContentView(R.layout.activity_graph);
	    ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		Bundle b=getIntent().getExtras();
		partie=bdd.getPartie(b.getLong("id_partie"));
		layout = (LinearLayout) findViewById(R.id.graphLayout);  
		init();
	}
	
	private void init(){
		List<Donne> listDonne=bdd.getListDonnes(partie.getId());
		int max=0;
		int num = listDonne.size();
		int[] sc=new int[partie.getNbJoueurs()];
		List<String> joueurs=partie.getListJoueurs();
		layout.removeAllViews();
		LineGraphView graphView = new LineGraphView( this,partie.getDescription());  

		for (int i=0; i<partie.getNbJoueurs();i++){
			GraphViewData[]  data= new GraphViewData[num];
			GraphViewSeries s;
			int j=0;
			sc[i]=0;
			for(Donne d:listDonne){
				sc[i]+= d.getPointJoueur(i);
				if (Math.abs(sc[i]) > max) max=Math.abs(sc[i]);
				data[j++] = new GraphViewData(j,sc[i]);  
			}
			 s = new GraphViewSeries(joueurs.get(i), styles.get(i), data);
			 graphView.addSeries(s);
		}
		max=100*(1+max/100);  
		graphView.setViewPort(1, 4*(1+num/4));  
		graphView.setShowLegend(true);  
		graphView.setLegendAlign(LegendAlign.TOP); 
		graphView.setManualYAxisBounds(max, -max);
		layout.addView(graphView);  
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_graph, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.menu_add_donne:
			createDialog(0);
			return true;
		case R.id.menu_tableau:
			finish();
			return true;
		case android.R.id.home:
			intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void createDialog(final long id){
		LayoutInflater factory = LayoutInflater.from(this);
		final View alertDialogView = factory.inflate(
				R.layout.activity_new_donne, null);
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setView(alertDialogView);
		adb.setTitle(R.string.title_activity_new_donne);
		adb.setPositiveButton(getString(R.string.save),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						int po;
						int bo;
						Donne d = new Donne();
						if (id != 0)
							d.setId(id);
						d.setPartie(partie);
						if (contrat.getSelectedItemPosition() > 0) {
							d.setContrat(contrat.getSelectedItemPosition());
							d.setPreneur(preneur.getSelectedItemPosition());
							if (partie.getNbJoueurs() > 4)
								d.setAppele(appele.getSelectedItemPosition());
							if (partie.getNbJoueurs() > 5){
								int m=mort.getSelectedItemPosition();
								if (m==preneur.getSelectedItemPosition() || m==appele.getSelectedItemPosition()){
									Toast.makeText(GraphActivity.this, getString(R.string.mort_preneur), Toast.LENGTH_LONG).show();
									return ;
								}
								d.setMort(m);
							}
							if (attaqueButton.isChecked()) {
								po = Integer.valueOf(points.getText().toString());
								bo = Integer.valueOf(bouts.getText().toString());
							} else {
								po = 91 - Integer.valueOf(points.getText().toString());
								bo = 3 - Integer.valueOf(bouts.getText().toString());
							}
							d.setPoints(po);
							d.setBouts(bo);
							d.setPetit(petit.getSelectedItemPosition());
							d.setPoignee(poignee.getSelectedItemPosition());
							d.setChelem(chelem.getSelectedItemPosition());
						}
						bdd.insertDonne(d);
						init();
					}
				});

		adb.setNegativeButton(getString(R.string.cancel), null);
		
		String[] listjJoueurs = partie.getListJoueurs().toArray(
				new String[partie.getListJoueurs().size()]);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listjJoueurs);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		preneur = (Spinner) alertDialogView.findViewById(R.id.nd_preneur);
		preneur.setAdapter(adapter);
		appele = (Spinner) alertDialogView.findViewById(R.id.nd_appele);
		appele.setAdapter(adapter);
		if (partie.getNbJoueurs() > 4) alertDialogView.findViewById(R.id.nd_lappele).setVisibility(View.VISIBLE);
		mort = (Spinner) alertDialogView.findViewById(R.id.nd_mort);
		mort.setAdapter(adapter);
		if (partie.getNbJoueurs() == 6) alertDialogView.findViewById(R.id.nd_lmort).setVisibility(View.VISIBLE);
		contrat = (Spinner) alertDialogView.findViewById(R.id.nd_contrat);
		contrat.setFocusable(true);
		contrat.setFocusableInTouchMode(true);
		contrat.requestFocus();
		petit = (Spinner) alertDialogView.findViewById(R.id.nd_petit);
		poignee = (Spinner) alertDialogView.findViewById(R.id.nd_poignee);
		chelem = (Spinner) alertDialogView.findViewById(R.id.nd_chelem);
		attaqueButton = (ToggleButton) alertDialogView.findViewById(R.id.nd_attaque);
		attaqueButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) alertDialogView.findViewById(R.id.nd_attaque_layout).setBackgroundColor(Color.GREEN);
				else alertDialogView.findViewById(R.id.nd_attaque_layout).setBackgroundColor(Color.DKGRAY);
			}
		});
		points = (EditText) alertDialogView.findViewById(R.id.nd_points);
		bouts = (EditText) alertDialogView.findViewById(R.id.nd_bouts);
		adb.show();
	}
}
