package fr.pssoftware.scoretarot;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TableDonneCell extends FrameLayout {
	private int contrat=0;
	private String points="";
	private int total_points=0;
	private int score=0;
	private int role=0;
	private int petit=0;
	private int poignee=0;
	private int chelem=0;
	private int footerVisibility;
	private TextView wPoints;
	private TextView wTotalPoints;
	private TextView wPetit;
	private TextView wPoignee;
	private TextView wScore;
	private TextView wChelem;
	private ImageView wImg;
	private LinearLayout wFooter;
	
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
		final TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.TableDonneCell, defStyle, 0);
		LayoutInflater.from(ctx).inflate(R.layout.table_donne_cell, this, true);
		contrat = a.getInt(R.styleable.TableDonneCell_contrat, 0);
		points = a.getString(R.styleable.TableDonneCell_points);
		total_points = a.getInt(R.styleable.TableDonneCell_total_points, 0);
		score = a.getInt(R.styleable.TableDonneCell_score, 0);
		role = a.getInt(R.styleable.TableDonneCell_role, 0);
		petit = a.getInt(R.styleable.TableDonneCell_petit, 0);
		poignee = a.getInt(R.styleable.TableDonneCell_poignee, 0);
		chelem = a.getInt(R.styleable.TableDonneCell_chelem, 0);
		footerVisibility = a.getInt(R.styleable.TableDonneCell_footerVisibility, 0);
		
		wImg=(ImageView) findViewById(R.id.tdc_img);
		wPoints=(TextView) findViewById(R.id.tdc_points);
		wTotalPoints=(TextView) findViewById(R.id.tdc_total_points);
		wScore=(TextView) findViewById(R.id.tdc_score);
		wPetit=(TextView) findViewById(R.id.tdc_petit);
		wPoignee=(TextView) findViewById(R.id.tdc_poignee);
		wChelem=(TextView) findViewById(R.id.tdc_chelem);
		wFooter=(LinearLayout) findViewById(R.id.tdc_footer);
		refresh();
		
		a.recycle();
	}
	
	public void refresh(){
		String tr="";
		switch(contrat){
		case 0:
			tr="00";
			break;
		case 1:
			tr="88";
			break;
		case 2:
			tr="66";
			break;
		case 3:
			tr="44";
			break;
		case 4:
			tr="22";
			break;
		}
		if (total_points<0){
			tr="#"+tr+"0000";
		}else if (total_points>0){
			tr="#00"+tr+"00";
		}else{
			tr="#000000";
		}
		setBackgroundColor(Color.parseColor(tr));
		wPoints.setText(String.valueOf(points));
		wTotalPoints.setText(String.valueOf(total_points));
		wImg.setVisibility(View.VISIBLE);
		switch (role){
		case -1:
			wImg.setImageResource(R.drawable.ic_mort);
			break;
		case 0:
			wImg.setVisibility(View.INVISIBLE);
			break;
		case 1:
			wImg.setImageResource(R.drawable.ic_appele);
			break;
		case 2:
			wImg.setImageResource(R.drawable.ic_preneur);
			break;
		}
		wImg.invalidate();
		if (petit==0) {
			wPetit.setText("");
		}else if (petit==1) {
			wPetit.setText("+1");
		}else if(petit==2){
			wPetit.setText("-1");
		}
		switch(poignee){
		case 0:
			wPoignee.setText("");
			break;
		case 1:
			wPoignee.setText("P");
			break;
		case 2:
			wPoignee.setText("PP");
			break;
		case 3:
			wPoignee.setText("PPP");
			break;
		}
		switch(chelem){
		case 0:
			wChelem.setText("");
			break;
		case 1:
			wChelem.setText("C");
			break;
		case 2:
			wChelem.setText("CA");
			break;
		case 3:
			wChelem.setText("-C");
			break;
		}
		wScore.setText(String.valueOf(score));
		wFooter.setVisibility(footerVisibility);
	}
	
	public void setFooterVisibility(int visible){
		footerVisibility=visible;
	}
	
	public int getFooterVisibility(){
		return footerVisibility;
	}
	
	public int getContrat() {
		return contrat;
	}

	public void setContrat(int contrat) {
		this.contrat = contrat;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public int getTotal_Points() {
		return total_points;
	}

	public void setTotal_Points(int points) {
		this.total_points = points;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getPetit() {
		return petit;
	}

	public void setPetit(int petit) {
		this.petit = petit;
	}

	public int getPoignee() {
		return poignee;
	}

	public void setPoignee(int poignee) {
		this.poignee = poignee;
	}

	public int getChelem() {
		return chelem;
	}

	public void setChelem(int chelem) {
		this.chelem = chelem;
	}
}
