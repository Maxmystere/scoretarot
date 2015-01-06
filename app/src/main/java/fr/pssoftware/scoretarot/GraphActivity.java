package fr.pssoftware.scoretarot;

import android.app.Activity;
import android.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class GraphActivity extends Activity {
	public static final String TYPE = "type";

	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

	private GraphicalView mChartView;

	int[] colors = new int[] { Color.BLUE, Color.YELLOW, Color.RED,
			Color.GREEN, Color.MAGENTA, Color.CYAN };

	private ScoreTarotDB bdd;
	private Partie partie = null;
	private List<Donne> listDonne;
	final private static int MODIF_DONNE_DIALOG = 1;
	private int pos = 0;
	private Donne donne = null;
	private XYSeries[] series;
	private XYSeriesRenderer[] renderer;
	private int[] sc;

    private AdView mAdView;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
		mRenderer.setLabelsTextSize(12 * this.getResources().getDisplayMetrics().scaledDensity);
		mRenderer.setLegendTextSize(12 * this.getResources().getDisplayMetrics().scaledDensity);
		mRenderer.setMargins(new int[] { 10, 20, 0, 10 });
		mRenderer.setFitLegend(true);
		mRenderer.setPointSize(3* this.getResources().getDisplayMetrics().scaledDensity );
		mRenderer.setPanEnabled(false);
		bdd = ScoreTarotDB.getDB(this);
		setContentView(R.layout.activity_graph);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		Bundle b = getIntent().getExtras();
		partie = bdd.getPartie(b.getLong("id_partie"));

        mAdView = (AdView) findViewById(R.id.adViewGraph);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
	}

	@Override
	protected void onResume() {
		super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
		listDonne = bdd.getListDonnes(partie.getId(),true);
		if (mChartView == null) {
			sc = new int[partie.getNbJoueurs()];
			series = new XYSeries[partie.getNbJoueurs()];
			renderer = new XYSeriesRenderer[partie.getNbJoueurs()];
			LinearLayout layout = (LinearLayout) findViewById(R.id.graphLayout);
			mChartView = ChartFactory.getLineChartView(this, mDataset,
					mRenderer);
			mRenderer.setClickEnabled(true);
			mChartView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
					if (seriesSelection == null) {
						Toast.makeText(getApplicationContext(),
								"Il faut toucher un point !",
								Toast.LENGTH_SHORT).show();
					} else {
						pos = (int) (seriesSelection.getPointIndex());
						donne = listDonne.get(pos);
						Toast.makeText(
								getApplicationContext(),
								String.format(getString(R.string.num_donne),
										pos + 1)
										+ "\n"
										+ donne.toString(getApplicationContext()),
								Toast.LENGTH_LONG).show();
					}
				}
			});
			mChartView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					SeriesSelection seriesSelection = mChartView
							.getCurrentSeriesAndPoint();
					if (seriesSelection == null) {
						Toast.makeText(getApplicationContext(),
								"Il faut toucher un point !",
								Toast.LENGTH_SHORT).show();
						return true;
					} else {
						pos = (int) (seriesSelection.getPointIndex());
						donne = listDonne.get(pos);
						registerForContextMenu(v);
						openContextMenu(v);
						return true;
					}
				}
			});
			layout.addView(mChartView, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			List<String> joueurs = partie.getListJoueurs();
			for (int i = 0; i < partie.getNbJoueurs(); i++) {
				series[i] = new XYSeries(joueurs.get(i));
				mDataset.addSeries(series[i]);
				renderer[i] = new XYSeriesRenderer();
				mRenderer.addSeriesRenderer(renderer[i]);
				renderer[i].setPointStyle(PointStyle.CIRCLE);
				renderer[i].setColor(colors[i]);
				renderer[i].setFillPoints(true);
				sc[i] = 0;
				int j = 1;
				series[i].clear();
				for (Donne d : listDonne) {
					sc[i] += d.getPointJoueur(i);
					series[i].add(j++, sc[i]);
				}
			}
		} else {
			for (int i = 0; i < partie.getNbJoueurs(); i++) {
				sc[i] = 0;
				int j = 1;
				series[i].clear();
				for (Donne d : listDonne) {
					sc[i] += d.getPointJoueur(i);
					series[i].add(j++, sc[i]);
				}
			}
			mChartView.invalidate();
		}
	}
    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

   /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		android.view.MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_donne_context, menu);
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
					(pos + 1)));
			adb.setTitle(R.string.attention);
			adb.setIcon(android.R.drawable.ic_dialog_alert);
			adb.setPositiveButton(getString(R.string.ok),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							bdd.deleteDonne(donne.getId());
							onResume();
						}
					});

			adb.setNegativeButton(getString(R.string.cancel), null);
			adb.show();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_graph, menu);
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
			dialogDetails = new DonneDialog(this, partie);
			break;
		}

		return dialogDetails;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		switch (id) {
		case MODIF_DONNE_DIALOG:
			DonneDialog dial = (DonneDialog) dialog;
			dial.setPartie(partie);
			if (donne != null)
				dial.setTitle(String.format(
						getString(R.string.title_activity_edit_donne),
						(pos + 1)));
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
