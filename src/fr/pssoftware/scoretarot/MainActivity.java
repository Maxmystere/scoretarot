package fr.pssoftware.scoretarot;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends SherlockListActivity {
	private ScoreTarotDB bdd;
	private AutoCompleteTextView[] Joueurs = new AutoCompleteTextView[6];
	private EditText descr;
	private SeekBar nbj;
	private TextView lnbj;
	private int value = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerForContextMenu(getListView());
		bdd = ScoreTarotDB.getDB(this);
	}

	protected void onResume() {
		super.onResume();
		refresh_data();
	}

	private void refresh_data() {
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add_partie:
			LayoutInflater factory = LayoutInflater.from(this);
			final View alertDialogView = factory.inflate(
					R.layout.activity_new_partie, null);
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setView(alertDialogView);
			adb.setTitle(R.string.title_activity_new_partie);
			adb.setPositiveButton(getString(R.string.save),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Partie p = new Partie(descr.getText().toString(),
									value);
							List<String> l = new ArrayList<String>();
							for (int i = 0; i < value; i++) {
								if (Joueurs[i].getText().toString().equals(""))
									l.add(Joueurs[i].getHint().toString());
								else
									l.add(Joueurs[i].getText().toString());
							}
							p.setListJoueurs(l);
							bdd.insertPartie(p);
							refresh_data();
						}
					});

			adb.setNegativeButton(getString(R.string.cancel), null);
			String[] listJoueurs = bdd.getListTotalJoueurs();
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, listJoueurs);
			LinearLayout layout = (LinearLayout) alertDialogView
					.findViewById(R.id.partie_joueurs);
			descr = (EditText) alertDialogView.findViewById(R.id.partie_name);
			lnbj = (TextView) alertDialogView
					.findViewById(R.id.partie_label_nbj);
			lnbj.setText(String.format(getString(R.string.NBJ), 4));
			for (int i = 0; i < 6; i++) {
				Joueurs[i] = new AutoCompleteTextView(this);
				Joueurs[i].setAdapter(adapter);
				Joueurs[i].setHint("Joueur " + (i + 1));
				Joueurs[i].setThreshold(1);
				Joueurs[i].setSingleLine(true);
				if (i > 3)
					Joueurs[i].setVisibility(View.GONE);
				layout.addView(Joueurs[i]);
			}
			nbj = (SeekBar) alertDialogView.findViewById(R.id.partie_nbj);
			nbj.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					lnbj.setText(String.format(getString(R.string.NBJ),
							progress + 3));
					value = progress + 3;
					for (int i = 3; i < value; i++)
						Joueurs[i].setVisibility(View.VISIBLE);
					for (int i = value; i < 6; i++)
						Joueurs[i].setVisibility(View.GONE);
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
				}

			});
			adb.show();
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

}
