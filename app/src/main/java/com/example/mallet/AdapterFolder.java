package com.example.mallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mallet.databinding.FragmentProfileBinding;

import java.util.List;

public class AdapterFolder extends BaseAdapter {


    private Context context;
    private List<ModelFolder> folderList;

    public AdapterFolder(Context context, List<ModelFolder> folderList) {
        this.context = context;
        this.folderList = folderList;
    }

    @Override
    public int getCount() {
        return folderList.size();
    }

    @Override
    public Object getItem(int position) {
        return folderList.get(position);
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
            itemView = inflater.inflate(R.layout.model_folder, parent, false);
        }

        ModelFolder folder = folderList.get(position);

        TextView folderOwnerTv = itemView.findViewById(R.id.folder_model_owner_tv);
        TextView folderNameTv = itemView.findViewById(R.id.folder_model_name_tv);

        folderOwnerTv.setText(folder.getFolderOwner());
        folderNameTv.setText(folder.getFolderName());

        return itemView;
    }

    public interface OnFolderClickListener {
        void onFolderClick(ModelFolder folder);
    }
}
