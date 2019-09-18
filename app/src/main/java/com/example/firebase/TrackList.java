package com.example.firebase;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TrackList extends ArrayAdapter<Track> {

    private Activity context;
    private List<Track> trackList;

    public TrackList(Activity context, List<Track> trackList){
        super(context, R.layout.list_track, trackList);
        this.context = context;
        this.trackList = trackList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_track, null, true);
        TextView textviewname = (TextView) listViewItem.findViewById(R.id.textviewname);
        TextView  textviewrating = (TextView) listViewItem.findViewById(R.id.textviewrating);

        Track track = trackList.get(position);
        textviewname.setText(track.getTrackname());
        textviewrating.setText(String.valueOf(track.getTrackrating()));
        return listViewItem;
    }
}
