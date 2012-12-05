package fr.pssoftware.scoretarot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    ArrayList<Map<String, String>> list = buildData();
	    String[] from = {"partie_nom", "partie_description", "partie_id" };
	    int[] to = {  R.id.partie_nom, R.id.partie_description,R.id.partie_id };

	    SimpleAdapter adapter = new SimpleAdapter(this.getBaseContext(), list,
	        R.layout.list_parties, from, to);
	    setListAdapter(adapter);
	  }

	  private ArrayList<Map<String, String>> buildData() {
	    ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
	    list.add(putData("Partie du 28 novembre 2012", "4 joueurs : Philippe, Pat Mok, Laurence, Xavier","1"));
	    list.add(putData("Partie du 23 décembre 2012", "5 joueurs : Nanou, Roxane, Gepetto, Morgane, Philippe","2"));
	    list.add(putData("Partie du 24 décembre 2012",  "5 joueurs : Nanou, Roxane, Gepetto, Morgane, Philippe","3"));
	    return list;
	  }

	  private HashMap<String, String> putData(String name, String purpose,String id) {
	    HashMap<String, String> item = new HashMap<String, String>();
	    item.put("partie_nom", name);
	    item.put("partie_description", purpose);
	    item.put("partie_id", id);
	    return item;
	  }

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
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	  protected void onListItemClick(ListView l, View v, int position, long id) {
		  HashMap<String, String> map = (HashMap<String, String>) getListAdapter().getItem(position);
			Intent intent = new Intent(MainActivity.this, TableDonneActivity.class);
			startActivity(intent);
	  }
}
