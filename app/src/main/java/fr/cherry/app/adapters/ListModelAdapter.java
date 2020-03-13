package fr.cherry.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fr.cherry.app.Cherry;
import fr.cherry.app.R;
import fr.cherry.app.models.ListModel;

public class ListModelAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;

    public ListModelAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return Cherry.getInstance().getNotes().size();
    }

    @Override
    public ListModel getItem(int position) {
        return Cherry.getInstance().getNotes().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.list_item, null);

        TextView title = view.findViewById(R.id.titleNote);
        title.setText(getItem(position).getTitle());

        TextView owner = view.findViewById(R.id.ownerName);
        if(Cherry.getInstance().getAuthenticated().getId() == getItem(position).getOwner().getId())
            owner.setText("Créer par vous");
        else
            owner.setText("Créer par " + getItem(position).getOwner().getLastname() + " " + getItem(position).getOwner().getSurname());
        ImageView img = view.findViewById(R.id.accessNote);

        if(getItem(position).getType() == 0)
            img.setImageResource(R.drawable.ic_note_24px);
        else
            img.setImageResource(R.drawable.ic_view_list_24px);
        return view;
    }
}
