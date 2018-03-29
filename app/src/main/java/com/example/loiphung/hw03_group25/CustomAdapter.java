package com.example.loiphung.hw03_group25;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by LoiPhung on 3/26/18.
 */

public class CustomAdapter extends ArrayAdapter<App> {


    public CustomAdapter(@NonNull Context context, int resource, ArrayList<App> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        App app = this.getItem(position);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.app_row, parent, false);
        TextView appname = convertView.findViewById(R.id.textViewApp);
        ImageView appImage = convertView.findViewById(R.id.appImageView);

        appname.setText(app.getName());
        Picasso.get().load(app.getImage()).into(appImage);


        return convertView;
    }


}
