package fr.pssoftware.scoretarot;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

public class MainActivity extends SherlockListActivity {
	private ScoreTarotDB bdd;
	final private static int MODIF_PARTIE_DIALOG = 4;
	private static Context ctx=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerForContextMenu(getListView());
		bdd = ScoreTarotDB.getDB(this);
	}

	protected void onResume() {
		super.onResume();
		PartieAdapter adapter = new PartieAdapter(this, bdd.getListParties());
		setListAdapter(adapter);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		android.view.MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_context, menu);
	}

	public boolean onContextItemSelected(android.view.MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		final Partie p = (Partie) getListAdapter().getItem(info.position);
		switch (item.getItemId()) {
		case R.id.menu_partie_open:
			Intent intent = new Intent(MainActivity.this,
					TableDonneActivity.class);
			intent.putExtra("id_partie", p.getId());
			startActivity(intent);
			return true;
		case R.id.menu_partie_delete:
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setMessage(String.format(getString(R.string.delete_partie),
					p.getDescription()));
			adb.setTitle(R.string.attention);
			adb.setIcon(android.R.drawable.ic_dialog_alert);
			adb.setPositiveButton(getString(R.string.ok),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							bdd.deletePartie(p.getId());
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add_partie:
			showDialog(MODIF_PARTIE_DIALOG);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Partie p = (Partie) getListAdapter().getItem(position);
		Intent intent = new Intent(MainActivity.this, TableDonneActivity.class);
		intent.putExtra("id_partie", p.getId());
		startActivity(intent);
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		AlertDialog dialogDetails = null;

		switch (id) {
		case MODIF_PARTIE_DIALOG:
			dialogDetails = new PartieDialog(this);
			break;
		}

		return dialogDetails;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		switch (id) {
		case MODIF_PARTIE_DIALOG:
			PartieDialog dial = (PartieDialog) dialog;
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
