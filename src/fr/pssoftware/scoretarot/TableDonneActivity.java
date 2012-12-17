package fr.pssoftware.scoretarot;

import java.util.List;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TableDonneActivity extends SherlockActivity {
	static final int NEW_DONNE_REQUEST = 2;  // The request code
	private ScoreTarotDB bdd;
	private Partie partie=null;
	private ListView list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bdd=ScoreTarotDB.getDB(this);
		setContentView(R.layout.activity_table_donne);
	    ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
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
	    list.setOnItemClickListener(new OnItemClickListener(){
		@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Object o=list.getItemAtPosition(position);
		         Toast.makeText(getApplicationContext(),o.toString(),Toast.LENGTH_LONG).show();
		    }
	    });
	    list.setOnItemLongClickListener(new OnItemLongClickListener(){
		@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Donne donne=(Donne) list.getItemAtPosition(position);
					Intent intent = new Intent(TableDonneActivity.this, NewDonneActivity.class);
					intent.putExtra("id_partie", partie.getId());
					intent.putExtra("id", donne.getId());
					startActivityForResult(intent,NEW_DONNE_REQUEST);
		         return true;
		    }
	    });
	    refresh_data();    
	}

	private void refresh_data(){
	    List<Donne> listD = bdd.getListDonnes(partie.getId());
	    DonneAdapter adapter = new DonneAdapter(this, listD);
	    list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
			startActivityForResult(intent,NEW_DONNE_REQUEST);
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
		if (requestCode == NEW_DONNE_REQUEST) {
			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode != RESULT_CANCELED ) refresh_data();
		}
	}
}
