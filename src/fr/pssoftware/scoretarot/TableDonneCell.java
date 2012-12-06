package fr.pssoftware.scoretarot;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 * TODO: document your custom view class.
 */
public class TableDonneCell extends FrameLayout {
	private int contrat=0;
	private int points=0;
	private int total_points=0;
	private int score=0;
	private int role=0;
	private int petit=0;
	private int poignee=0;
	private int chelem=0;

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
		maj();
	}
	
	private void maj(){
		
	}

	public int getContrat() {
		return contrat;
	}

	public void setContrat(int contrat) {
		this.contrat = contrat;
		maj();
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
		maj();
	}

	public int getTotal_Points() {
		return total_points;
	}

	public void setTotal_Points(int points) {
		this.total_points = points;
		maj();
	}
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
		maj();
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
		maj();
	}

	public int getPetit() {
		return petit;
	}

	public void setPetit(int petit) {
		this.petit = petit;
		maj();
	}

	public int getPoignee() {
		return poignee;
}

	public void setPoignee(int poignee) {
		this.poignee = poignee;
		maj();
	}

	public int getChelem() {
		return chelem;
	}

	public void setChelem(int chelem) {
		this.chelem = chelem;
		maj();
	}
}
