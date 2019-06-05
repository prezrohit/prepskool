package in.prepskool.prepskoolacademy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.model.Standard;

public class StandardAdapter extends BaseAdapter {

    private static LayoutInflater inflater=null;

    private ArrayList<Standard> list;

    private Context context;

    public StandardAdapter(ArrayList<Standard> list, Context context) {
        this.list = list;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Holder holder;

        if (view == null) {

            holder = new Holder();
            view = inflater.inflate(R.layout.class_gridview_box,null);
            holder.boardName =( TextView) view.findViewById(R.id.txt_class);
            holder.boardName.setText(list.get(i).getRom());
            view.setTag(holder);
        }
        else {

            holder = (Holder) view.getTag();
        }
        return view;
    }

    public class Holder{
        TextView boardName;
    }
}
