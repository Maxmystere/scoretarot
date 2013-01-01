package fr.pssoftware.scoretarot;

import  android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DonneDialog extends AlertDialog {

	private ScoreTarotDB bdd;
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
	private Context ctx;
	private Donne donne=null;
	final View alertDialogView;

	
	public Donne getDonne() {
		return donne;
	}

	public void setDonne(Donne donne) {
		this.donne = donne;
		if (donne !=null){
			preneur.setSelection(donne.getPreneur());
			if (partie.getNbJoueurs() > 4)
				appele.setSelection(donne.getAppele());
			if (partie.getNbJoueurs() > 5)
				mort.setSelection(donne.getMort());
			contrat.setSelection(donne.getContrat());
			points.setText(String.valueOf(donne.getPoints()));
			bouts.setText(String.valueOf(donne.getBouts()));
			petit.setSelection(donne.getPetit());
			poignee.setSelection(donne.getPoignee());
			chelem.setSelection(donne.getChelem());
		}else{
			preneur.setSelection(0);
			appele.setSelection(0);
			mort.setSelection(0);
			contrat.setSelection(0);
			points.setText("");
			bouts.setText("");
			petit.setSelection(0);
			poignee.setSelection(0);
			chelem.setSelection(0);
		}
		contrat.setFocusable(true);
		contrat.setFocusableInTouchMode(true);
		contrat.requestFocus();
	}

	private Partie partie;
	private Object mAcceptButton;

	public Partie getPartie() {
		return partie;
	}

	public void setPartie(Partie partie) {
		this.partie = partie;
	}

	protected DonneDialog(Context context,Partie partie) {
		super(context);
		ctx=context;
		bdd = ScoreTarotDB.getDB(ctx);
		this.partie=partie;
	    LayoutInflater factory = LayoutInflater.from(ctx);
		alertDialogView = factory.inflate(
				R.layout.activity_new_donne, null);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    LayoutInflater factory = LayoutInflater.from(ctx);
		final View alertDialogView = factory.inflate(
				R.layout.activity_new_donne, null);
		setView(alertDialogView);
		setTitle(R.string.title_activity_new_donne);
		setButton(BUTTON_POSITIVE,ctx.getString(R.string.save),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		this.setButton(BUTTON_NEGATIVE, ctx.getString(R.string.cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();
			}});
		String[] listjJoueurs = partie.getListJoueurs().toArray(
				new String[partie.getListJoueurs().size()]);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx,
				android.R.layout.simple_spinner_item, listjJoueurs);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		contrat = (Spinner) alertDialogView.findViewById(R.id.nd_contrat);
		preneur = (Spinner) alertDialogView.findViewById(R.id.nd_preneur);
		preneur.setAdapter(adapter);
		appele = (Spinner) alertDialogView.findViewById(R.id.nd_appele);
		appele.setAdapter(adapter);
		if (partie.getNbJoueurs() > 4) alertDialogView.findViewById(R.id.nd_lappele).setVisibility(View.VISIBLE);
		mort = (Spinner) alertDialogView.findViewById(R.id.nd_mort);
		mort.setAdapter(adapter);
		if (partie.getNbJoueurs() == 6) alertDialogView.findViewById(R.id.nd_lmort).setVisibility(View.VISIBLE);
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
	       super.onCreate(savedInstanceState);

	        mAcceptButton = getButton(DialogInterface.BUTTON_POSITIVE);
	        ((View) mAcceptButton).setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
					int po;
					int bo;
					if (donne == null ) {
						donne = new Donne();
						donne.setId(0);
					}
					if (points.getText().toString().isEmpty()) points.setText("0");
					if (bouts.getText().toString().isEmpty()) bouts.setText("0");
					donne.setPartie(partie);
					if (contrat.getSelectedItemPosition() > 0) {
						donne.setContrat(contrat.getSelectedItemPosition());
						donne.setPreneur(preneur.getSelectedItemPosition());
						if (partie.getNbJoueurs() > 4)
							donne.setAppele(appele.getSelectedItemPosition());
						if (attaqueButton.isChecked()) {
							po = Integer.valueOf(points.getText().toString());
							bo = Integer.valueOf(bouts.getText().toString());
						} else {
							po = 91 - Integer.valueOf(points.getText().toString());
							bo = 3 - Integer.valueOf(bouts.getText().toString());
						}
						donne.setPoints(po);
						donne.setBouts(bo);
						donne.setPetit(petit.getSelectedItemPosition());
						donne.setPoignee(poignee.getSelectedItemPosition());
						donne.setChelem(chelem.getSelectedItemPosition());
					}
					if (partie.getNbJoueurs() > 5){
						int m=mort.getSelectedItemPosition();
						if (m==preneur.getSelectedItemPosition() || m==appele.getSelectedItemPosition()){
							Toast.makeText(ctx, ctx.getString(R.string.mort_preneur), Toast.LENGTH_LONG).show();
							return ;
						}
						donne.setMort(m);
					}
					bdd.insertDonne(donne);
					dismiss();
	            }
	        });
		}		
}
