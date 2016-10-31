package geert.berkers.cartracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import geert.berkers.cartracker.R;

/**
 * Created by Geert.
 */
public class MenuAdapter extends BaseAdapter {
    private Context context;

    private String[] sort = {"Huidige Tank", "Betaalde Tank"};
    private Integer[] images = {R.mipmap.ic_current, R.mipmap.ic_past};

    public MenuAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return sort.length;
    }

    @Override
    public String getItem(int position) {
        return sort[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.drawer_layout, parent, false);
        } else {
            row = convertView;
        }
        TextView titleMenuItem = (TextView) row.findViewById(R.id.menuItem);
        ImageView titleImageView = (ImageView) row.findViewById(R.id.menuPicture);

        titleMenuItem.setText(sort[position]);
        titleImageView.setImageResource(images[position]);

        return row;
    }
}