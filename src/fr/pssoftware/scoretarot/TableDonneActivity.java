package fr.pssoftware.scoretarot;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;

public class TableDonneActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_table_donne, menu);
		return true;
	}

}
