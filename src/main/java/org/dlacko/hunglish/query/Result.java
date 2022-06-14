package org.dlacko.hunglish.query;

import android.util.Pair;

import java.util.List;

public abstract class Result {
    private Result() {}

    public static final class Success extends Result {
        public final List<SentencePair> sentencePairs;
        public final Integer cursor;

        public Success(Pair<List<SentencePair>, Integer> res){
            this(res.first, res.second);
        }

        public Success(List<SentencePair> sentencePairs, Integer cursor) {
            this.sentencePairs = sentencePairs;
            this.cursor = cursor;
        }
    }

    public static final class Error extends Result {
        public Exception exception;

        public Error(Exception exception) {
            this.exception = exception;
        }
    }
}