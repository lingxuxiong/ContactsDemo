package com.neil.contactsdemo.executors;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

final class SingleThreadExecutor implements Executor {

    @Override
    public void execute(@NonNull Runnable command) {
        Executors.newSingleThreadExecutor().execute(command);
    }

}
