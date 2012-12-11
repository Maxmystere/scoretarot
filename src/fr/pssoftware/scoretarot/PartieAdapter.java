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

/**
 * @author seraphin
 *
 */
public class PartieAdapter extends BaseAdapter {
	private List<Partie> listPartie;
	private Context context;
	private LayoutInflater mInflater;
	
	public PartieAdapter(Context ctx, List<Partie> lPartie){
		context=ctx;
		listPartie=lPartie;
		mInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return listPartie.size();
	}

	@Override
	public Object getItem(int position) {
		return listPartie.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 LinearLayout layoutItem;
		  if (convertView == null) {
		    layoutItem = (LinearLayout) mInflater.inflate(R.layout.list_parties, parent, false);
		  } else {
		  	layoutItem = (LinearLayout) convertView;
		  }
		  TextView name=(TextView) layoutItem.findViewById(R.id.partie_nom);
		  TextView descr=(TextView) layoutItem.findViewById(R.id.partie_description);
		  name.setText(listPartie.get(position).getDescription());
		  List<String> lj=listPartie.get(position).getListJoueurs();
		  String d=listPartie.get(position).getNbJoueurs()+" joueurs : ";
		for (String j:lj){
			d+=j+", ";
		}
		  descr.setText(d);
		  return layoutItem;
	}

}
