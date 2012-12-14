/**
 * 
 */
package fr.pssoftware.scoretarot;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * @author seraphin
 *
 */
public class DonneAdapter extends BaseAdapter {
	private List<Donne> listDonne;
	private Context context;
	private LayoutInflater mInflater;
	
	public DonneAdapter(Context ctx, List<Donne> lDonne){
		context=ctx;
		listDonne=lDonne;
		mInflater = LayoutInflater.from(context);
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
		 Donne prec=null;
		  if (convertView == null) {
		    layoutItem = (LinearLayout) mInflater.inflate(R.layout.table_donne_line, parent, false);
		  } else {
		  	layoutItem = (LinearLayout) convertView;
		  }
		  Donne donne=listDonne.get(position);
		  int nbj=donne.getPartie().getNbJoueurs();
		  if (position>0)  prec=listDonne.get(position-1);
		  for (int i=0;i<nbj;i++){
			  TableDonneCell cell=(TableDonneCell)layoutItem.getChildAt(i);
			  cell.setVisibility(View.VISIBLE);
			  cell.setRole(0);
			  cell.setPoints("");
			  cell.setPetit(0);
			  cell.setPoignee(0);
			  cell.setChelem(0);
			  if (nbj==6 && donne.getMort()==i) cell.setRole(-1);
			  if (nbj==5 && donne.getAppele()==i) cell.setRole(1);
			  if (donne.getPreneur()==i){
				  cell.setRole(2);
				  cell.setPetit(donne.getPetit());
				  cell.setPoignee(donne.getPoignee());
				  cell.setChelem(donne.getChelem());
				  cell.setPoints(donne.getStringContrat()+donne.getPasse());
			  }
			  cell.setContrat(donne.getContrat());
			  int pj=donne.getPointJoueur(i);
			  cell.setTotal_Points(pj);
			  if (donne.getPetit()==0 && donne.getPoignee()==0 && donne.getChelem()==0) cell.setFooterVisibility(View.GONE);
			  else  cell.setFooterVisibility(View.VISIBLE);
			  if (prec != null ) donne.setScore(i,prec.getScore(i)+pj);
			  else donne.setScore(i,pj);
			  cell.setScore(donne.getScore(i));
			  cell.refresh();	
		  }
		  for (int i=nbj;i<6;i++){
			  layoutItem.getChildAt(i).setVisibility(View.GONE);
		  }
		  
		  return layoutItem;
	}

}
