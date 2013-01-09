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
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GraphActivity extends SherlockActivity {
	private ScoreTarotDB bdd;
	private Partie partie=null;
	private LinearLayout layout;
	private List<GraphViewStyle>  styles;
	List<Donne> listDonne;
	final private static int MODIF_DONNE_DIALOG = 1;
	private int pos=0;
	Donne donne=null;
	boolean shortclick=false;

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
	}
	   
	
	protected void onResume(){
		super.onResume();
		listDonne=bdd.getListDonnes(partie.getId());
		int max=0;
		int num = listDonne.size();
		int[] sc=new int[partie.getNbJoueurs()];
		List<String> joueurs=partie.getListJoueurs();
		layout.removeAllViews();
		final LineGraphView graphView = new LineGraphView( this,partie.getDescription());  
		registerForContextMenu(graphView);
		graphView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				pos=(int) (graphView.getXValue(event.getX()));
				donne=listDonne.get(pos);
				if (event.getAction()==MotionEvent.ACTION_UP && shortclick){
					Toast.makeText(
							getApplicationContext(),
							String.format(getString(R.string.num_donne),
									(pos + 1)) + "\n" + donne.toString(getApplicationContext()),
							Toast.LENGTH_LONG).show();
				}else{
					shortclick=true;
				}
				return false;
			}
		});

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
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		android.view.MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_donne_context, menu);
		shortclick=false;
	}

	@SuppressWarnings("deprecation")
	public boolean onContextItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_donne_edit:
			showDialog(MODIF_DONNE_DIALOG);
			return true;
		case R.id.menu_donne_delete:
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setMessage(String.format(getString(R.string.delete_donne),
					(pos+1)));
			adb.setTitle(R.string.attention);
			adb.setIcon(android.R.drawable.ic_dialog_alert);
			adb.setPositiveButton(getString(R.string.ok),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							bdd.deleteDonne(donne.getId());
							onResume();
							}
					});

			adb.setNegativeButton(getString(R.string.cancel),null);
			adb.show();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_graph, menu);
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.menu_add_donne:
			showDialog(MODIF_DONNE_DIALOG);
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
	protected Dialog onCreateDialog(int id) {

		AlertDialog dialogDetails = null;

		switch (id) {
		case MODIF_DONNE_DIALOG:
			dialogDetails = new DonneDialog(this,partie);
			break;
	}

		return dialogDetails;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		switch (id) {
		case MODIF_DONNE_DIALOG:
			DonneDialog dial=(DonneDialog) dialog;
			dial.setPartie(partie);
			if (donne != null )
				dial.setTitle(String.format(getString(R.string.title_activity_edit_donne),(pos+1)));
			else
				dial.setTitle(getString(R.string.title_activity_new_donne));
				
			dial.setDonne(donne);
			dial.setOnDismissListener(new OnDismissListener() {
			    @Override
			    public void onDismiss(DialogInterface dialog) {
			        onResume();
			    }
			});
			break;
		}
	}
}
