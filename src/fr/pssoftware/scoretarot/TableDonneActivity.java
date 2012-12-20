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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class TableDonneActivity extends SherlockActivity {
	private ScoreTarotDB bdd;
	private Partie partie = null;
	private ListView list;
	private DonneAdapter adapter;
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
			createDialog(d.getId(),info.position);
			return true;
		case R.id.menu_donne_delete:
		        AlertDialog.Builder adb = new AlertDialog.Builder(this);
		        adb.setMessage(String.format(getString(R.string.delete_donne),info.position));
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
			createDialog(0,0);
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

	private void createDialog(final long id,int position){
		LayoutInflater factory = LayoutInflater.from(this);
		final View alertDialogView = factory.inflate(
				R.layout.activity_new_donne, null);
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setView(alertDialogView);
		if (id==0) adb.setTitle(R.string.title_activity_new_donne);
		else adb.setTitle(String.format(getString(R.string.title_activity_edit_donne),position+1));
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
									Toast.makeText(TableDonneActivity.this, getString(R.string.mort_preneur), Toast.LENGTH_LONG).show();
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
						refresh_data();
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
		if (id != 0) initData(id);
		adb.show();
	}

	private void initData(long id) {
		Donne d = bdd.getDonne(id);
		preneur.setSelection(d.getPreneur());
		if (partie.getNbJoueurs() > 4)
			appele.setSelection(d.getAppele());
		if (partie.getNbJoueurs() > 5)
			mort.setSelection(d.getMort());
		contrat.setSelection(d.getContrat());
		points.setText(String.valueOf(d.getPoints()));
		bouts.setText(String.valueOf(d.getBouts()));
		petit.setSelection(d.getPetit());
		poignee.setSelection(d.getPoignee());
		chelem.setSelection(d.getChelem());
	}

}
