package fr.pssoftware.scoretarot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
	private int value=4;
	private ImageButton plusButton;
	private ImageButton minusButton;
	private EditText editValue;
	private AutoCompleteTextView[] Joueurs=new AutoCompleteTextView[6];
	static final int NEW_PARTIE_REQUEST = 1;  // The request code

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
			//TODO
			Toast.makeText(getApplicationContext(), String.valueOf(resultCode), Toast.LENGTH_SHORT);
		}
	}
	
	@Override
	  protected void onListItemClick(ListView l, View v, int position, long id) {
		  HashMap<String, String> map = (HashMap<String, String>) getListAdapter().getItem(position);
		  
			Intent intent = new Intent(MainActivity.this, TableDonneActivity.class);
        startActivity(intent);
	  }
	
	private void masque_joueurs(){
 		for (int i=0;i<value;i++) {
 			Joueurs[i].setVisibility(View.VISIBLE);
 		};
 		for(int i=value;i<6;i++) {
 			Joueurs[i].setVisibility(View.INVISIBLE);
 		};
	}
}
