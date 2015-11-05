package fr.pssoftware.scoretarot;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TableDonneCell extends FrameLayout {
	public final static int CONTRAT_PASSE=0;
	public final static int CONTRAT_PRISE=1;
	public final static int CONTRAT_GARDE=2;
	public final static int CONTRAT_GARDE_SANS=3;
	public final static int CONTRAT_GARDE_CONTRE=4;
	public final static int ROLE_DEFENSE=0;
	public final static int ROLE_MORT=-1;
	public final static int ROLE_APPELE=1;
	public final static int ROLE_PRENEUR=2;

	private int contrat=0;
	private String points="";
	private int total_points=0;
	private int score=0;
	private int role=0;
	private int petit=0;
	private int poignee=0;
	private int chelem=0;
	private int misere=0;
	private int footerVisibility;
	private TextView wPoints;
	private TextView wTotalPoints;
	private TextView wPetit;
	private TextView wPoignee;
	private TextView wScore;
	private TextView wChelem;
	private TextView wMisere;
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
		misere = a.getInt(R.styleable.TableDonneCell_misere, 0);
		footerVisibility = a.getInt(R.styleable.TableDonneCell_footerVisibility, 0);
		
		wImg=(ImageView) findViewById(R.id.tdc_img);
		wPoints=(TextView) findViewById(R.id.tdc_points);
		wTotalPoints=(TextView) findViewById(R.id.tdc_total_points);
		wScore=(TextView) findViewById(R.id.tdc_score);
		wPetit=(TextView) findViewById(R.id.tdc_petit);
		wPoignee=(TextView) findViewById(R.id.tdc_poignee);
		wChelem=(TextView) findViewById(R.id.tdc_chelem);
		wMisere=(TextView) findViewById(R.id.tdc_misere);
		wFooter=(LinearLayout) findViewById(R.id.tdc_footer);
		refresh();
		
		a.recycle();
	}
	
	public void refresh(){
		String tr="";
 		switch(contrat){
		case CONTRAT_PASSE:
			tr="00";
			break;
		case CONTRAT_PRISE:
			tr="88";
			break;
		case CONTRAT_GARDE:
			tr="66";
			break;
		case CONTRAT_GARDE_SANS:
			tr="44";
			break;
		case CONTRAT_GARDE_CONTRE:
			tr="22";
			break;
		}
        if (role>ROLE_DEFENSE) {
            wScore.setTextColor(themeUtils.getLight((Activity) this.getContext()));
            wPoints.setTextColor(themeUtils.getLight((Activity) this.getContext()));
            wTotalPoints.setTextColor(themeUtils.getLight((Activity) this.getContext()));
            wPetit.setTextColor(themeUtils.getLight((Activity) this.getContext()));
            wPoignee.setTextColor(themeUtils.getLight((Activity) this.getContext()));
            wChelem.setTextColor(themeUtils.getLight((Activity) this.getContext()));
        }else {
            wScore.setTextColor(themeUtils.getForeground((Activity) this.getContext()));
            wPoints.setTextColor(themeUtils.getForeground((Activity) this.getContext()));
            wTotalPoints.setTextColor(themeUtils.getForeground((Activity) this.getContext()));
            wPetit.setTextColor(themeUtils.getForeground((Activity) this.getContext()));
            wPoignee.setTextColor(themeUtils.getForeground((Activity) this.getContext()));
            wChelem.setTextColor(themeUtils.getForeground((Activity) this.getContext()));
        }
		if (total_points<0 && role>ROLE_DEFENSE ){
            setBackgroundColor(Color.parseColor("#"+tr+"0000"));
		}else if (total_points>0 && role>ROLE_DEFENSE ){
            setBackgroundColor(Color.parseColor("#00"+tr+"00"));
		}else{
            setBackgroundColor(themeUtils.getBackground((Activity) this.getContext()))            ;
         }
		wPoints.setText(String.valueOf(points));
		wTotalPoints.setText(String.valueOf(total_points));
		wImg.setVisibility(View.VISIBLE);
		switch (role){
		case ROLE_MORT:
			wImg.setImageResource(R.drawable.ic_mort);
			break;
		case ROLE_DEFENSE:
			wImg.setVisibility(View.INVISIBLE);
			break;
		case ROLE_APPELE:
			wImg.setImageResource(R.drawable.ic_appele);
			break;
		case ROLE_PRENEUR:
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
		if (misere==0){
			wMisere.setText("");
		}else if(misere==-1){
			wMisere.setText("-M");
		}else if(misere==1) {
			wMisere.setText("+M");
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
		case 4:
			wChelem.setText("-C");
			break;
		}
		wScore.setText(String.valueOf(score));
//		wFooter.setVisibility(footerVisibility);
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

	public void setMisere(int misere) {
		this.misere = misere;
	}
	public int getMisere() {
		return misere;
	}

	public void setChelem(int chelem) {
		this.chelem = chelem;
	}
}
