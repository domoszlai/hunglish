package org.dlacko.hunglish.query;

public class SentencePair {

    private Sentence huSentence;
    private Sentence enSentence;
    private Genre genre;

    public Genre getGenre(){
        return genre;
    }

    public Sentence getHuSentence(){
        return huSentence;
    }

    public Sentence getEnSentence(){
        return enSentence;
    }

    public SentencePair(Genre genre, Sentence huSentence, Sentence enSentence){
        this.genre = genre;
        this.huSentence = huSentence;
        this.enSentence = enSentence;
    }
}
