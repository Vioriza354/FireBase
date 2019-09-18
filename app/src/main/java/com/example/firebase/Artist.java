package com.example.firebase;

public class Artist {
    String artistName,artistid,artistGenre;

    public Artist(){

    }
    public Artist(String artistid, String artistName, String artistGenre){
        this.artistid = artistid;
        this.artistName = artistName;
        this.artistGenre = artistGenre;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistid() {
        return artistid;
    }

    public String getArtistGenre() {
        return artistGenre;
    }
}
