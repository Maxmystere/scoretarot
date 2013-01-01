package fr.pssoftware.scoretarot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class PartieDialog extends AlertDialog {
	private Context ctx;
	private ScoreTarotDB bdd;
	private EditText descr;
	private TextView lnbj;
	private AutoCompleteTextView[] Joueurs= new AutoCompleteTextView[6];
	private SeekBar nbj;
	private int value=4;

	public PartieDialog(Context context) {
		super(context);
		ctx=context;
		bdd = ScoreTarotDB.getDB(ctx);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LayoutInflater factory = LayoutInflater.from(ctx);
		final View alertDialogView = factory.inflate(
				R.layout.activity_new_partie, null);
		setView(alertDialogView);
		setTitle(R.string.title_activity_new_partie);
		setButton(BUTTON_POSITIVE,ctx.getString(R.string.save),
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
					}
				});

		setButton(BUTTON_NEGATIVE,ctx.getString(R.string.cancel),new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();
			}});
		String[] listJoueurs = bdd.getListTotalJoueurs();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx,
				android.R.layout.simple_dropdown_item_1line, listJoueurs);
		LinearLayout layout = (LinearLayout) alertDialogView
				.findViewById(R.id.partie_joueurs);
		descr = (EditText) alertDialogView.findViewById(R.id.partie_name);
		java.text.DateFormat df= DateFormat.getMediumDateFormat(ctx);
		descr.setText(df.format(new Date()));
		lnbj = (TextView) alertDialogView
				.findViewById(R.id.partie_label_nbj);
		lnbj.setText(String.format(ctx.getString(R.string.NBJ), 4));
		for (int i = 0; i < 6; i++) {
			Joueurs[i] = new AutoCompleteTextView(ctx);
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
				lnbj.setText(String.format(ctx.getString(R.string.NBJ),
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
	    super.onCreate(savedInstanceState);
	}
	
	public PartieDialog(Context context, int theme) {
		super(context, theme);
	}

	public PartieDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

}
