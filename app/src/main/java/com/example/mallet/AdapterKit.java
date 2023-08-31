package com.example.mallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdapterKit extends BaseAdapter {

    private Context context;
    private List<ModelKit> kitList;

    public AdapterKit(Context context, List<ModelKit> kitList) {
        this.context = context;
        this.kitList = kitList;
    }

    @Override
    public int getCount() {
        return kitList.size();
    }

    @Override
    public Object getItem(int position) {
        return kitList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            itemView = inflater.inflate(R.layout.model_kit, parent, false);
        }

        ModelKit kit = kitList.get(position);

        TextView kitNameTv = itemView.findViewById(R.id.kit_model_name_tv);
        TextView kitCreatorTv = itemView.findViewById(R.id.kit_model_creator_tv);
        TextView kitTermsTv = itemView.findViewById(R.id.kit_model_terms_tv);

        kitNameTv.setText(kit.getKitName());
        kitCreatorTv.setText("Created by: " + kit.getKitCreator());
        kitTermsTv.setText("Terms: " + kit.getKitTerms());

        return itemView;
    }
}
