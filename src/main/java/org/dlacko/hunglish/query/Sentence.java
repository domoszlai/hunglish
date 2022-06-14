package org.dlacko.hunglish.query;

import java.util.ArrayList;

public class Sentence {

    private ArrayList<Span> spans = new ArrayList<>();

    private static class Span {
        private String text;
        private boolean emphasized;

        public Span(String text, boolean emphasized){
            this.text = text;
            this.emphasized = emphasized;
        }
    }

    public void addSpan(String text, boolean emphasized){
        spans.add(new Span(text, emphasized));
    }

    public String asHtml(){
        StringBuilder b = new StringBuilder();
        for(Span span : spans){
            if(span.emphasized){
                b.append("<b>");
                b.append(span.text);
                b.append("</b>");
            }
            else{
                b.append(span.text);
            }
        }
        return b.toString();
    }
}

