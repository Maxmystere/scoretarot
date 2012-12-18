package fr.pssoftware.scoretarot;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

public class GraphActivity extends SherlockActivity {
	private ScoreTarotDB bdd;
	private Partie partie=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bdd=ScoreTarotDB.getDB(this);
		setContentView(R.layout.activity_graph);
	    ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		Bundle b=getIntent().getExtras();
		partie=bdd.getPartie(b.getLong("id_partie"));
		init();
	}
	
	private void init(){
		List<Donne> listDonne=bdd.getListDonnes(partie.getId());
		int num = listDonne.size();
		int[] sc=new int[partie.getNbJoueurs()];
		List<String> joueurs=partie.getListJoueurs();
		List<GraphViewStyle>  styles=new ArrayList<GraphViewStyle>();
		styles.add(new GraphViewStyle(Color.rgb(200, 00, 00),1));
		styles.add(new GraphViewStyle(Color.rgb(00, 200, 00),1));
		styles.add(new GraphViewStyle(Color.rgb(00, 00, 200),1));
		styles.add(new GraphViewStyle(Color.rgb(200, 200, 00),1));
		styles.add(new GraphViewStyle(Color.rgb(200, 00, 200),1));
		styles.add(new GraphViewStyle(Color.rgb(00, 200, 200),1));
		
		LineGraphView graphView = new LineGraphView( this,partie.getDescription());  

		for (int i=0; i<partie.getNbJoueurs();i++){
			GraphViewData[]  data= new GraphViewData[num];
			GraphViewSeries s;
			int j=0;
			sc[i]=0;
			for(Donne d:listDonne){
				sc[i]+= d.getPointJoueur(i);
				 data[j++] = new GraphViewData(j,sc[i]);  
			}
			 s = new GraphViewSeries(joueurs.get(i), styles.get(i), data);
			 graphView.addSeries(s);
		}
		  
		graphView.setViewPort(1, 8);  
		graphView.setScrollable(true);  
//		graphView.setScalable(true);  
		graphView.setShowLegend(true);  
		graphView.setLegendAlign(LegendAlign.TOP); 
		graphView.setLegendWidth(100);
		  
		LinearLayout layout = (LinearLayout) findViewById(R.id.graphLayout);  
		layout.addView(graphView);  
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
            finish();
            return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
