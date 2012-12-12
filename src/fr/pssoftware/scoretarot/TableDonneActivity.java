package fr.pssoftware.scoretarot;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TableDonneActivity extends Activity {
	static final int NEW_DONNE_REQUEST = 2;  // The request code
	private ScoreTarotDB bdd;
	private Partie partie=null;
	private ListView list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bdd=ScoreTarotDB.getDB(this);
		setContentView(R.layout.activity_table_donne);
		Bundle b=getIntent().getExtras();
		partie=bdd.getPartie(b.getLong("id_partie"));
		
		LinearLayout header= (LinearLayout) findViewById(R.id.td_header);
		header.removeAllViewsInLayout();;
		for (String j:partie.getListJoueurs()){
			TextView child=new TextView(this);
			child.setText(j);
			child.setBackgroundColor(Color.parseColor("#000000"));
			child.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(0 ,LinearLayout.LayoutParams.WRAP_CONTENT ) ;
			layoutParam.weight=1;
			layoutParam.setMargins(1, 1, 1, 1);
			header .addView( child , layoutParam );
		}
			
	    list = (ListView)findViewById(R.id.td_list);
	    refresh_data();    
	}

	private void refresh_data(){
	    List<Donne> listD = partie.getListDonnes();
	    DonneAdapter adapter = new DonneAdapter(this, listD);
	    list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_table_donne, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add_donne:
			Intent intent = new Intent(TableDonneActivity.this, NewDonneActivity.class);
			intent.putExtra("id_partie", partie.getId());
			intent.putExtra("id", 0);
			startActivityForResult(intent,NEW_DONNE_REQUEST);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == NEW_DONNE_REQUEST) {
			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode != RESULT_CANCELED ) refresh_data();
		}
	}
}
