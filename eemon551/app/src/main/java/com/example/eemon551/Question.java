package com.example.eemon551;
public class Question {
    private int id;
    private String name;
    private String img;
    private String card;
    private String txt;
    private String link;
    private int rare;
    private int loc_id;
    private int genre_id;

    // Getters and Setters
    public int getId() { return id;}
    public String getName() {
        return name;
    }
    public String getImg() {return img;}

    public String getCard(){return card;}

    public String getTxt(){return txt;}

    public String grtLink(){return link;}

    public int getRare() {
        return rare;
    }

    public int getLoc_id(){return loc_id;}
    public int getGenre_id(){return genre_id;}
}
