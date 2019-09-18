package com.example.firebase;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ArtistList extends ArrayAdapter {
    private Activity context;
    private List<Artist> artistList;

    public ArtistList(Activity context, List<Artist> artistList){
        super(context, R.layout.list_layout, artistList);
        this.context = context;
        this.artistList = artistList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        TextView  textviewname = (TextView) listViewItem.findViewById(R.id.textviewname);
        TextView  textviewgenre = (TextView) listViewItem.findViewById(R.id.textviewgenre);

        Artist artist = artistList.get(position);
        textviewname.setText(artist.getArtistName());
        textviewgenre.setText(artist.getArtistGenre());
        return listViewItem;
    }
}
