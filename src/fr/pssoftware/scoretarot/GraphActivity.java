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

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

public class GraphActivity extends SherlockActivity {
	private ScoreTarotDB bdd;
	private Partie partie=null;
	private LinearLayout layout;
	static final int ADD_DONNE_REQUEST = 3; // The request code

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		List<GraphViewStyle>  styles=new ArrayList<GraphViewStyle>();
		styles.add(new GraphViewStyle(Color.rgb(200, 00, 00),2));
		styles.add(new GraphViewStyle(Color.rgb(00, 200, 00),2));
		styles.add(new GraphViewStyle(Color.rgb(00, 00, 200),2));
		styles.add(new GraphViewStyle(Color.rgb(200, 200, 00),2));
		styles.add(new GraphViewStyle(Color.rgb(200, 00, 200),2));
		styles.add(new GraphViewStyle(Color.rgb(00, 200, 200),2));
		
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
			intent = new Intent(GraphActivity.this, NewDonneActivity.class);
			intent.putExtra("id_partie", partie.getId());
			intent.putExtra("id", 0);
			startActivityForResult(intent, ADD_DONNE_REQUEST);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ADD_DONNE_REQUEST) {
			super.onActivityResult(requestCode, resultCode, data);
			layout.removeAllViews();
			init();
		}
	}
}
