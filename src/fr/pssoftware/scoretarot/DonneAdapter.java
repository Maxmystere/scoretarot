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
		  if (convertView == null) {
		    layoutItem = (LinearLayout) mInflater.inflate(R.layout.table_donne_line, parent, false);
		  } else {
		  	layoutItem = (LinearLayout) convertView;
		  }
		  return layoutItem;
	}

}
