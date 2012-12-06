package fr.pssoftware.scoretarot;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TableDonneCell extends FrameLayout {
	private int contrat=0;
	private int points=0;
	private int total_points=0;
	private int score=0;
	private int role=0;
	private int petit=0;
	private int poignee=0;
	private int chelem=0;
	private RelativeLayout wFond;
	private TextView wPoints;
	private TextView wTotalPoints;
	private TextView wPetit;
	private TextView wPoignee;
	private TextView wScore;
	private TextView wChelem;
	private ImageView wImg;
	
	public TableDonneCell(Context context) {
		super(context);
		init(context,null, 0);
	}

	public TableDonneCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs, 0);
	}

	public TableDonneCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context,attrs, defStyle);
	}

	private void init(Context ctx,AttributeSet attrs, int defStyle) {
		// Load attributes
		final TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.TableDonneCell, defStyle, 0);
		LayoutInflater.from(ctx).inflate(R.layout.table_donne_cell, this, true);

		contrat = a.getInt(R.styleable.TableDonneCell_contrat, 0);
		points = a.getInt(R.styleable.TableDonneCell_points, 0);
		total_points = a.getInt(R.styleable.TableDonneCell_total_points, 0);
		score = a.getInt(R.styleable.TableDonneCell_score, 0);
		role = a.getInt(R.styleable.TableDonneCell_role, 0);
		petit = a.getInt(R.styleable.TableDonneCell_petit, 0);
		poignee = a.getInt(R.styleable.TableDonneCell_poignee, 0);
		chelem = a.getInt(R.styleable.TableDonneCell_chelem, 0);
		a.recycle();
		
		wFond=(RelativeLayout) findViewById(R.id.tdc_fond);
		wImg=(ImageView) findViewById(R.id.tdc_img);
		wPoints=(TextView) findViewById(R.id.tdc_points);
		wTotalPoints=(TextView) findViewById(R.id.tdc_total_points);
		wScore=(TextView) findViewById(R.id.tdc_score);
		wPetit=(TextView) findViewById(R.id.tdc_petit);
		wPoignee=(TextView) findViewById(R.id.tdc_poignee);
		wChelem=(TextView) findViewById(R.id.tdc_chelem);
	}
	

	public int getContrat() {
		return contrat;
	}

	public void setContrat(int contrat) {
		this.contrat = contrat;

		String tr="";
		switch(contrat){
		case 0:
			tr="00";
			break;
		case 1:
			tr="44";
			break;
		case 2:
			tr="88";
			break;
		case 3:
			tr="AA";
			break;
		case 4:
			tr="EE";
			break;
		}
		if (total_points<0){
			tr="#"+tr+"0000";
		}else if (total_points>=0){
			tr="#00"+tr+"00";
			
		}
		wFond.setBackgroundColor(Color.parseColor(tr));
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
		wPoints.setText(points);
	}

	public int getTotal_Points() {
		return total_points;
	}

	public void setTotal_Points(int points) {
		this.total_points = points;
		wTotalPoints.setText(points);
	}
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
		wScore.setText(score);
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
		switch (role){
		case -1:
			wImg.setImageResource(R.drawable.ic_mort);
		case 0:
			wImg.setVisibility(View.INVISIBLE);
		case 1:
			wImg.setImageResource(R.drawable.ic_appele);
		case 2:
			wImg.setImageResource(R.drawable.ic_preneur);
		}
	}

	public int getPetit() {
		return petit;
	}

	public void setPetit(int petit) {
		this.petit = petit;
		if (petit<0) {
			wPetit.setText("-1");
		}else if(petit>0){
			wPetit.setText("+1");
		}
	}

	public int getPoignee() {
		return poignee;
	}

	public void setPoignee(int poignee) {
		this.poignee = poignee;
		switch(poignee){
		case -3:
			wPoignee.setText("-PPP");
			break;
		case -2:
			wPoignee.setText("-PP");
			break;
		case -1:
			wPoignee.setText("-P");
			break;
		case 1:
			wPoignee.setText("+P");
			break;
		case 2:
			wPoignee.setText("+PP");
			break;
		case 3:
			wPoignee.setText("+PPP");
			break;
		}
		
	}

	public int getChelem() {
		return chelem;
	}

	public void setChelem(int chelem) {
		this.chelem = chelem;
		switch(chelem){
		case -1:
			wChelem.setText("-C");
			break;
		case 1:
			wChelem.setText("+C");
			break;
		case 2:
			wChelem.setText("+C");
			break;
		}
	}
}
