package org.dlacko.hunglish.query;

public class Query {

    private final String filterHuSentence;
    private final String filterEnSentence;
    private final Genre filterGenre;

    public Query(String filterHuSentence, String filterEnSentence){
        this(filterHuSentence, filterEnSentence, Genre.ALL);
    }

    public Query(String filterHuSentence, String filterEnSentence, Genre filterGenre){
        this.filterHuSentence = filterHuSentence;
        this.filterEnSentence = filterEnSentence;
        this.filterGenre = filterGenre;
    }

    public String getFilterHuSentence(){
        return this.filterHuSentence;
    }

    public String getFilterEnSentence(){
        return this.filterEnSentence;
    }

    public Genre getFilterGenre(){
        return this.filterGenre;
    }
}
