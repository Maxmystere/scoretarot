package fr.pssoftware.scoretarot;

import java.util.List;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

public class TableDonneActivity extends SherlockActivity {
	static final int MOD_DONNE_REQUEST = 2; // The request code
	static final int ADD_DONNE_REQUEST = 3; // The request code
	private ScoreTarotDB bdd;
	private Partie partie = null;
	private ListView list;
	private DonneAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bdd = ScoreTarotDB.getDB(this);
		setContentView(R.layout.activity_table_donne);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		Bundle b = getIntent().getExtras();
		partie = bdd.getPartie(b.getLong("id_partie"));

		LinearLayout header = (LinearLayout) findViewById(R.id.td_header);
		header.removeAllViewsInLayout();
		;
		for (String j : partie.getListJoueurs()) {
			TextView child = new TextView(this);
			child.setText(j);
			child.setBackgroundColor(Color.parseColor("#000000"));
			child.setGravity(Gravity.CENTER);
			child.setLines(1);
			LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
					0, LinearLayout.LayoutParams.WRAP_CONTENT);
			layoutParam.weight = 1;
			layoutParam.setMargins(1, 1, 1, 1);
			header.addView(child, layoutParam);
		}

		list = (ListView) findViewById(R.id.td_list);
		registerForContextMenu(list);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Object o = list.getItemAtPosition(position);
				Toast.makeText(getApplicationContext(), o.toString(),
						Toast.LENGTH_LONG).show();
			}
		});
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		android.view.MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_donne_context,  menu);
	}

	public boolean onContextItemSelected(android.view.MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		final Donne d = (Donne) list.getItemAtPosition(info.position);
		switch (item.getItemId()) {
		case R.id.menu_donne_edit:
			Intent intent = new Intent(TableDonneActivity.this,
					NewDonneActivity.class);
			intent.putExtra("id_partie", partie.getId());
			intent.putExtra("id", d.getId());
			startActivityForResult(intent, MOD_DONNE_REQUEST);
			return true;
		case R.id.menu_donne_delete:
		        AlertDialog.Builder adb = new AlertDialog.Builder(this);
		        adb.setMessage(String.format(getString(R.string.delete_donne),d.getId()));
		        adb.setTitle(R.string.attention);
		        adb.setIcon(android.R.drawable.ic_dialog_alert);
		        adb.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	bdd.deleteDonne(d.getId());
		            	refresh_data();
		          } });
		 
		        adb.setNegativeButton(getString(R.string.cancel), null);
		        adb.show();			
			
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	private void refresh_data(){
		List<Donne> listD = bdd.getListDonnes(partie.getId());
		adapter = new DonneAdapter(this, listD);
		list.setAdapter(adapter);
	}
	
	public void onResume(){
		super.onResume();
		refresh_data();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_table_donne, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.menu_add_donne:
			intent = new Intent(TableDonneActivity.this, NewDonneActivity.class);
			intent.putExtra("id_partie", partie.getId());
			intent.putExtra("id", 0);
			startActivityForResult(intent, ADD_DONNE_REQUEST);
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ADD_DONNE_REQUEST) {
			super.onActivityResult(requestCode, resultCode, data);
			adapter.add(bdd.getDonne(resultCode));
			adapter.notifyDataSetChanged();
		}else if (requestCode == MOD_DONNE_REQUEST) {
			super.onActivityResult(requestCode, resultCode, data);
			refresh_data();
		}
	}

}
