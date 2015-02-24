package fr.pssoftware.scoretarot;

import java.util.List;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class TableDonneActivity extends Activity {
	private ScoreTarotDB bdd;
	private Partie partie = null;
	private ListView list;
	private DonneAdapter adapter;
	private Donne donne = null;
	private int item_selected = 0;
	final private static int MODIF_DONNE_DIALOG = 1;
	private boolean tri=false;
    private AdView mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        themeUtils.initTheme(this);
		super.onCreate(savedInstanceState);
 		bdd = ScoreTarotDB.getDB(this);
		setContentView(R.layout.activity_table_donne);
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		Bundle b = getIntent().getExtras();
		partie = bdd.getPartie(b.getLong("id_partie"));

        int scale=Math.round(this.getResources().getDisplayMetrics().scaledDensity);
		LinearLayout header = (LinearLayout) findViewById(R.id.td_header);
        header.removeAllViewsInLayout();
        TextView child = new TextView(this);
        child.setText("Donne");
        child.setGravity(Gravity.CENTER);
        child.setEnabled(true);
        child.setTextAppearance(this, android.R.style.TextAppearance_Medium);
        child.setBackgroundColor(themeUtils.getBackground(this));
        child.setLines(1);
        child.setPadding(scale,0,0,0);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParam.weight = 0.3f;
        layoutParam.setMargins(scale, scale, scale, scale);
        header.addView(child, layoutParam);
        layoutParam = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParam.weight = 1;
        layoutParam.setMargins(scale, scale,scale, scale);
        for (String j : partie.getListJoueurs()) {
			child = new TextView(this);
			child.setText(j);
            child.setTextAppearance(this,android.R.style.TextAppearance_Medium);
            child.setBackgroundColor(themeUtils.getBackground(this));
 			child.setGravity(Gravity.CENTER);
			child.setLines(1);
			header.addView(child, layoutParam);
		}

		list = (ListView) findViewById(R.id.td_list);
		registerForContextMenu(list);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Donne o = (Donne)list.getItemAtPosition(position);
				Toast.makeText(
						getApplicationContext(),
						String.format(getString(R.string.num_donne),
								(Math.abs((tri?-1:list.getAdapter().getCount())-position))) + "\n" + o.toString(getApplicationContext()),
						Toast.LENGTH_LONG).show();
			}
		});
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		android.view.MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_donne_context, menu);
	}

	public boolean onContextItemSelected(android.view.MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		donne = (Donne) list.getItemAtPosition(info.position);
		item_selected = Math.abs((tri?-1:list.getAdapter().getCount())-info.position);
		switch (item.getItemId()) {
		case R.id.menu_donne_edit:
			showDialog(MODIF_DONNE_DIALOG);
			return true;
		case R.id.menu_donne_delete:
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setMessage(String.format(getString(R.string.delete_donne),
					item_selected));
			adb.setTitle(R.string.attention);
			adb.setIcon(android.R.drawable.ic_dialog_alert);
			adb.setPositiveButton(getString(R.string.ok),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							bdd.deleteDonne(donne.getId());
							refresh_data();
						}
					});

			adb.setNegativeButton(getString(R.string.cancel), null);
			adb.show();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void refresh_data() {
		List<Donne> listD = bdd.getListDonnes(partie.getId(),tri);
		adapter = new DonneAdapter(this, listD);
		list.setAdapter(adapter);
	}

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
	public void onResume() {
		super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
		refresh_data();
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_table_donne, menu);
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.menu_tri:
			tri=(!tri);
			refresh_data();
			return true;
		case R.id.menu_add_donne:
			donne = null;
			showDialog(MODIF_DONNE_DIALOG);
			return true;
		case R.id.menu_graph:
			intent = new Intent(TableDonneActivity.this, GraphActivity.class);
			intent.putExtra("id_partie", partie.getId());
			startActivity(intent);
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
						item_selected));
			else
				dial.setTitle(getString(R.string.title_activity_new_donne));

			dial.setDonne(donne);
			dial.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					TableDonneActivity.this.refresh_data();
				}
			});
			break;
		}
	}
}
