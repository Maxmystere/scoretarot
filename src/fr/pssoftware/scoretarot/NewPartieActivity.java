package fr.pssoftware.scoretarot;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Arrays;

public class NewPartieActivity extends Activity {
	private int value=4;
	private ImageButton plusButton;
	private ImageButton minusButton;
	private Button saveButton;
	private EditText editValue;
	private AutoCompleteTextView[] Joueurs=new AutoCompleteTextView[6];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_new_partie);
	
			String[] listJoueurs = getResources().getStringArray(R.array.liste_joueurs);
			Arrays.sort(listJoueurs);
			 Log.d("BinarySearch (-1 pour non trouvé)"," "+Arrays.binarySearch(listJoueurs, "Geppetto"));
	 		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, listJoueurs);
	 		
	 		LinearLayout layout = (LinearLayout) findViewById(R.id.partie_joueurs);
	 		for (int i=0;i<6;i++) {
	 			Joueurs[i] = new AutoCompleteTextView(this);
	 			Joueurs[i].setAdapter(adapter);
	 			Joueurs[i].setHint("Joueur "+ (i+1) );
	 			Joueurs[i].setThreshold(1);
	 			layout.addView(Joueurs[i]);
	 		};
				 		
	 		plusButton = (ImageButton) findViewById(R.id.partie_plus);
	 		plusButton.setOnClickListener(new OnClickListener() {
	 			@Override
	 			public void onClick(View v) {
	 				value++;
	 				if (value>6) value=6;
	 				editValue.setText(value + "");
	 				masque_joueurs();
	 			}
	 		});
	 		
	 		editValue = (EditText) findViewById(R.id.partie_nbj);
	 		 
	 		minusButton = (ImageButton) findViewById(R.id.partie_minus);
	 		minusButton.setOnClickListener(new OnClickListener() {
	 			@Override
	 			public void onClick(View v) {
	 				value--;
	 				if (value<3) value=3;
	 				editValue.setText(value + "");
	 				masque_joueurs();
	 			}
	 		});
	 		
	 		saveButton = (Button) findViewById(R.id.partie_save);
	 		saveButton.setOnClickListener(new OnClickListener() {
	 			@Override
	 			public void onClick(View v) {
	 				//TODO
	 			    setResult(1);
	 			    finish();
	 			    }
	 		});
	 		masque_joueurs();
	}
	
	private void masque_joueurs(){
 		for (int i=0;i<value;i++) {
 			Joueurs[i].setVisibility(View.VISIBLE);
 		};
 		for(int i=value;i<6;i++) {
 			Joueurs[i].setVisibility(View.GONE);
 		};
	}
}

