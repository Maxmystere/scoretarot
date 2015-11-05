/**
 * 
 */
package fr.pssoftware.scoretarot;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;

/**
 * @author seraphin
 *
 */
public class DonneAdapter extends BaseAdapter {
	private List<Donne> listDonne;
	private Context context;
	private LayoutInflater mInflater;
    private TextView n;

	public DonneAdapter(Context ctx, List<Donne> lDonne){
		context=ctx;
		listDonne=lDonne;
		mInflater = LayoutInflater.from(context);
	}
	
	public void add(Donne d){
		listDonne.add(d);
	}
	
	@Override
	public int getCount() {
		return listDonne.size();
	}

	@Override
	public Object getItem(int position) {
		return listDonne.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 LinearLayout layoutItem;
		  if (convertView == null) {
		    layoutItem = (LinearLayout) mInflater.inflate(R.layout.table_donne_line, parent, false);
		  } else {
		  	layoutItem = (LinearLayout) convertView;
		  }
            Donne donne=listDonne.get(position);
            TextView n=(TextView) layoutItem.findViewById(R.id.num);
            n.setText(String.valueOf(donne.getId()));
            n.setBackgroundColor(themeUtils.getBackground((Activity) context));
		  int nbj=donne.getPartie().getNbJoueurs();
		  for (int i=0;i<nbj;i++){
			  TableDonneCell cell=(TableDonneCell)layoutItem.getChildAt(i+1);
			  cell.setVisibility(View.VISIBLE);
			  cell.setRole(TableDonneCell.ROLE_DEFENSE);
			  cell.setPoints("");
			  cell.setPetit(0);
			  cell.setPoignee(0);
			  cell.setChelem(0);
			  if (nbj==6 && donne.getMort()==i) cell.setRole(TableDonneCell.ROLE_MORT);
			  if (nbj>4 && donne.getAppele()==i) cell.setRole(TableDonneCell.ROLE_APPELE);
			  if(donne.getMisere()<0) cell.setMisere(0);
			  else if (donne.getMisere()==i) cell.setMisere(1);
			  else if(donne.getMisere()!=-1) cell.setMisere(-1);
			  if (donne.getPreneur()==i){
				  cell.setRole(TableDonneCell.ROLE_PRENEUR);
				  cell.setPetit(donne.getPetit());
				  cell.setPoignee(donne.getPoignee());
				  cell.setChelem(donne.getChelem());
				  cell.setPoints(donne.getStringContrat()+donne.getPasse());
			  }
			  cell.setContrat(donne.getContrat());
			  int pj=donne.getPointJoueur(i);
			  cell.setTotal_Points(pj);
//			  if (donne.getPetit()==0 && donne.getPoignee()==0 && donne.getChelem()==0) cell.setFooterVisibility(View.GONE);
//			  else  cell.setFooterVisibility(View.VISIBLE);
			  cell.setScore(donne.getScore(i));
			  cell.refresh();	
	  }
		  for (int i=nbj+1;i<7;i++){
			  layoutItem.getChildAt(i).setVisibility(View.GONE);
		  }
		  
		  return layoutItem;
	}

}
