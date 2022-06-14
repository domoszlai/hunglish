package org.dlacko.hunglish.query;

import android.os.Handler;
import android.util.Pair;

import org.dlacko.hunglish.query.providers.Provider;

import java.util.List;
import java.util.concurrent.Executor;

public class QueryExecutor {

    private final Executor executor;
    private final Handler resultHandler;
    private final Provider provider;

    public QueryExecutor(Executor executor, Handler resultHandler, Provider provider){
        this.executor = executor;
        this.resultHandler = resultHandler;
        this.provider = provider;
    }

    public void run(final QueryCallback callback, Query query, Integer cursor) {
        executor.execute(new Runnable() {
            @Override
            public void run() {

                Result result;

                try {
                    result = new Result.Success(provider.makeRequest(query, cursor));
                } catch (Exception e) {
                    result = new Result.Error(e);
                }

                notifyResult(result, callback);
            }
        });
    }

    private void notifyResult(
            final Result result,
            final QueryCallback callback
            ) {
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(result);
            }
        });
    }
}
