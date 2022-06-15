package org.dlacko.hunglish.query.providers;

import android.util.Pair;

import org.dlacko.hunglish.query.Query;
import org.dlacko.hunglish.query.SentencePair;

import java.util.List;

public interface Provider {
    // It is supposed to load some sentence pairs only and return a cursor,
    // so a consecutive call can fetch some of the remaining sentence pairs
    Pair<List<SentencePair>, Integer> makeRequest(Query query, Integer cursor) throws Exception;
}
