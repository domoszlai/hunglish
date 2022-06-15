package org.dlacko.hunglish.query.providers;

import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import org.dlacko.hunglish.query.Genre;
import org.dlacko.hunglish.query.Query;
import org.dlacko.hunglish.query.Sentence;
import org.dlacko.hunglish.query.SentencePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HunglishProvider implements Provider {

    private String getURL(Query query, Integer cursor){
        //https://hunglish.hu/search?huSentence=&enSentence=Sear&doc.genre=7
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("hunglish.hu")
                .appendPath("search")
                .appendQueryParameter("huSentence", query.getFilterHuSentence())
                .appendQueryParameter("enSentence", query.getFilterEnSentence())
                .appendQueryParameter("doc.genre", Integer.toString(query.getFilterGenre().getNumVal()));

        if(cursor != null){
            builder.appendQueryParameter("page", cursor.toString());
        }

        return builder.build().toString();
    }

    private Sentence parseText(Element root){
        Sentence ret = new Sentence();
        for (Node child: root.childNodes()){
            if(child instanceof TextNode){
                ret.addSpan(((TextNode)child).text(), false);
            }
            else // Element: <b>...</b>
            {
                ret.addSpan(((TextNode)child.childNode(0)).text(), true);
            }
        }
        return ret;
    }

    @Override
    public Pair<List<SentencePair>, Integer> makeRequest(Query query, Integer cursor) throws IOException {
        List<SentencePair> sentencePairs = new ArrayList<>();
        if(cursor != null && cursor == -1){
            return Pair.create(sentencePairs, -1);
        }

        URL url = new URL(getURL(query, cursor));

        Document doc = Jsoup.connect(url.toString()).get();
        Elements rows = doc.select("div#results>table>tbody>*");
        for (Element result : rows) {
            TextNode genreNode = (TextNode) result.child(0).childNode(0);
            Genre genre = Genre.byName(genreNode.text());
            Sentence huSentence = parseText(result.child(1));
            Sentence enSentence = parseText(result.child(2));
            sentencePairs.add(new SentencePair(genre, huSentence, enSentence));
        }

        Elements lastPager = doc.select("div#pager>p>*:last-child");
        if(lastPager != null){
            if(lastPager.text().equals(">>")){
                return Pair.create(sentencePairs, Optional.ofNullable(cursor).orElse(1) + 1);
            }
        }

        return Pair.create(sentencePairs,-1);
    }
}
