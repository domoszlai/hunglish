package org.dlacko.hunglish.query;

import java.util.HashMap;

public enum Genre {
    ALL(-10, "All"),
    DICTIONARY(7, "Dictionary"),
    SUBTITLES(3, "Subtitles"),
    LAW(6, "Law"),
    LITERATURE(2, "Literature"),
    MAGAZINE(4, "Magazine"),
    SCIENCE(8, "Science"),
    SOFTWARE(5, "Software documentation")
    ;

    private int numVal;
    private String title;
    private static HashMap<String, Genre> map;

    public int getNumVal(){
        return numVal;
    }

    public String getTitle(){
        return title;
    }

    private static void putValue(String name, Genre genre) {
        if (map == null)
            map = new HashMap<>();
        map.put(name, genre);
    }

    public static Genre byName(String title){
        return map.get(title);
    }

    Genre(int numVal, String title) {
        this.numVal = numVal;
        this.title = title;
        putValue(title, this);
    }
}
