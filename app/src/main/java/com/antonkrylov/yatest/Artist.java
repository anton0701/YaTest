package com.antonkrylov.yatest;

import java.net.URL;
import java.util.ArrayList;

/*
    Класс, содержащий поля для хранения информации об артисте.
*/
public class Artist {

    String name;
    ArrayList<String> genres;
    String genresString;
    Integer tracks;
    Integer albums;
    URL link;
    String description;
    URL coverSmallLink;
    URL coverBigLink;

    Artist() {
        this.name = "";
        this.genres = null;
        this.genresString = null;
        this.tracks = null;
        this.albums = null;
        this.link = null;
        this.description = null;
        this.coverSmallLink = null;
        this.coverBigLink = null;
    }
}
