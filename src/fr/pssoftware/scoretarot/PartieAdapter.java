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
			layoutItem = (LinearLayout) mInflater.inflate(
					R.layout.list_parties, parent, false);
		} else {
			layoutItem = (LinearLayout) convertView;
		}
		try{
			Partie p=listPartie.get(position);
			TextView name = (TextView) layoutItem.findViewById(R.id.partie_nom);
			TextView descr = (TextView) layoutItem.findViewById(R.id.partie_description);
			name.setText(p.getDescription());
			List<String> lj = p.getListJoueurs();
			String d = p.getNbJoueurs() + " joueurs : ";
			for (String j : lj) {
				d += j + ", ";
			}
			descr.setText(d);
		}catch (Exception e){
			Log.e("PartieAdapter",e.getMessage());
		}
		return layoutItem;
	}
}
