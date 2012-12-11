package fr.pssoftware.scoretarot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
public class MainActivity extends ListActivity {
	static final int NEW_PARTIE_REQUEST = 1;  // The request code

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		refresh_data();
	  }
		
	private void refresh_data(){
		PartieAdapter adapter= new PartieAdapter(getApplicationContext(),PartiesDB.getInstance(getApplicationContext()).getListParties());
/*	    ArrayList<Map<String, String>> list = buildData();
	    String[] from = {"partie_nom", "partie_description", "partie_id" };
	    int[] to = {  R.id.partie_nom, R.id.partie_description,R.id.partie_id };

	    SimpleAdapter adapter = new SimpleAdapter(this.getBaseContext(), list,
	        R.layout.list_parties, from, to);*/
	    setListAdapter(adapter);
	}
/*	  private ArrayList<Map<String, String>> buildData() {
	    ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
	    List<Partie> lp =PartiesDB.getInstance(getApplicationContext()).getListParties();
	    for (Partie p: lp){
	    	List<String> lj=p.getListJoueurs();
	    	String js = "";
	    	for (String j: lj){
	    		js += j +",";
	    	}
	    	list.add(putData(p.getDescription(),p.getNbJoueurs()+" joueurs : "+js,String.valueOf(p.getId())));
	    }
	    return list;
	  }

	  private HashMap<String, String> putData(String name, String purpose,String id) {
	    HashMap<String, String> item = new HashMap<String, String>();
	    item.put("partie_nom", name);
	    item.put("partie_description", purpose);
	    item.put("partie_id", id);
	    return item;
	  }
*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add_partie:
			Intent intent = new Intent(MainActivity.this, NewPartieActivity.class);
			startActivityForResult(intent,NEW_PARTIE_REQUEST);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == NEW_PARTIE_REQUEST) {
			super.onActivityResult(requestCode, resultCode, data);
			refresh_data();
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
