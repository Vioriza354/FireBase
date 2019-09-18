package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String ARTIST_NAME = "artistname";
    public static final String ARTIST_ID = "artistid";
    EditText editTextName;
    Button buttonAdd;
    Spinner spinnerGenres;
    DatabaseReference databaseArtist;
    ListView listViewArtist;
    List<Artist> artislist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseArtist = FirebaseDatabase.getInstance().getReference("artist");

        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonAdd = (Button) findViewById(R.id.buttonAddArtist);
        spinnerGenres = (Spinner) findViewById(R.id.spinnerGenres);
        listViewArtist = (ListView) findViewById(R.id.listViewArtist);
        artislist = new ArrayList<>();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            addArtist();
            }
        });
        listViewArtist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Artist artist = artislist.get(i);

                Intent  intent = new Intent(getApplicationContext(), AddTrackActivity.class);
                intent.putExtra(ARTIST_ID, artist.getArtistid());
                intent.putExtra(ARTIST_NAME, artist.getArtistName());

                startActivity(intent);

            }
        });
        listViewArtist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Artist artist = artislist.get(i);
                showUpdateDialog(artist.getArtistid(), artist.getArtistName());

                return false;
            }
        });
        }

    @Override
    protected void onStart() {
        super.onStart();

        databaseArtist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                artislist.clear();
                for(DataSnapshot artistSnapshot : dataSnapshot.getChildren()){
                    Artist artist = artistSnapshot.getValue(Artist.class);
                    artislist.add(artist);
                }
                ArtistList adapter = new ArtistList(MainActivity.this, artislist);
                listViewArtist.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDialog(final String artistid, String artistName){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View  dialogview =  inflater.inflate(R.layout.update_dialog,null);
        dialogBuilder.setView(dialogview);


        final Button buttondelete = (Button) dialogview.findViewById(R.id.buttonDelete);
        final EditText edittextname = (EditText) dialogview.findViewById(R.id.edittextname);
        final Button buttonupdate = (Button) dialogview.findViewById(R.id.buttonUpdate);
        final Spinner spinnegenre = (Spinner) dialogview.findViewById(R.id.spinneredit);
        dialogBuilder.setTitle("Updating Artist "+artistName);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edittextname.getText().toString().trim();
                String genre = spinnegenre.getSelectedItem().toString();

                if (TextUtils.isEmpty(name)){
                    edittextname.setError("Name Requirerd");
                    return;
                }
                updateArtist(artistid, name, genre);
                alertDialog.dismiss();
            }

        });
        buttondelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteArtist(artistid);
            }
        });
    }
    private void deleteArtist(String artisid){
        DatabaseReference drArtist = FirebaseDatabase.getInstance().getReference("artist").child(artisid);
        DatabaseReference drTrack = FirebaseDatabase.getInstance().getReference("track").child(artisid);

        drArtist.removeValue();
        drTrack.removeValue();

        Toast.makeText(this, "Terhapus", Toast.LENGTH_LONG).show();
    }
    private boolean updateArtist(String id, String name, String genre){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("artist").child(id);
        Artist artist = new Artist(id, name, genre);
        databaseReference.setValue(artist);
        Toast.makeText(this, "Artist Telah Diupdate", Toast.LENGTH_LONG).show();
        return true;

    }

    private void addArtist(){
        String name = editTextName.getText().toString().trim();
        String genre = spinnerGenres.getSelectedItem().toString();

        if (!TextUtils.isEmpty(name)){
           String id =  databaseArtist.push().getKey();

           Artist artist = new Artist(id, name, genre);
           databaseArtist.child(id).setValue(artist);
           Toast.makeText(this, "Artist telah ditambahkan", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "you should enter a name", Toast.LENGTH_LONG).show();
        }
    }

}
