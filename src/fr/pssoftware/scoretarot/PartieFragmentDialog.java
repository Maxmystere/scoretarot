package fr.pssoftware.scoretarot;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class PartieFragmentDialog extends SherlockDialogFragment  {
	private static ScoreTarotDB bdd;

	public interface PartieFragmentDialogListener {
		void onFinishPartieDialog(boolean result);
	}

	public static PartieFragmentDialog newInstance(int title) {
		PartieFragmentDialog frag = new PartieFragmentDialog();
		bdd = ScoreTarotDB.getDB(frag.getActivity());
		Bundle args = new Bundle();
		args.putInt("title", R.string.title_activity_new_partie);
		frag.setArguments(args);
		return frag;
	}

	private EditText descr;
	private TextView lnbj;
	private AutoCompleteTextView[] Joueurs= new AutoCompleteTextView[6];;
	private SeekBar nbj;
	private int value=4;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int title = getArguments().getInt("title");
		LayoutInflater factory = LayoutInflater.from(getActivity());
		final View alertDialogView = factory.inflate(
				R.layout.activity_new_partie, null);
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setView(alertDialogView);
		adb.setIcon(android.R.drawable.ic_dialog_alert);
		adb.setTitle(title);
		adb.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						PartieFragmentDialogListener activity = (PartieFragmentDialogListener) getActivity();
						activity.onFinishPartieDialog(false);
					}
				});
		adb.setPositiveButton(R.string.save,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						Partie p = new Partie(descr.getText().toString(), value);
						List<String> l = new ArrayList<String>();
						for (int i = 0; i < value; i++) {
							if (Joueurs[i].getText().toString().equals(""))
								l.add(Joueurs[i].getHint().toString());
							else
								l.add(Joueurs[i].getText().toString());
						}
						p.setListJoueurs(l);
						bdd.insertPartie(p);
						PartieFragmentDialogListener activity = (PartieFragmentDialogListener) getActivity();
						activity.onFinishPartieDialog(true);
					}
				});
		String[] listJoueurs = bdd.getListTotalJoueurs();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_dropdown_item_1line, listJoueurs);
		LinearLayout layout = (LinearLayout) alertDialogView
				.findViewById(R.id.partie_joueurs);
		descr = (EditText) alertDialogView.findViewById(R.id.partie_name);
		lnbj = (TextView) alertDialogView.findViewById(R.id.partie_label_nbj);
		lnbj.setText(String.format(getActivity().getString(R.string.NBJ), 4));
		for (int i = 0; i < 6; i++) {
			Joueurs[i] = new AutoCompleteTextView(getActivity());
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
				lnbj.setText(String.format(getActivity()
						.getString(R.string.NBJ), progress + 3));
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

		return adb.create();
	}

}
