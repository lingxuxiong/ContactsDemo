package com.neil.contactsdemo.executors;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

public class MainThreadExecutor implements Executor {

    private final Handler mUiHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(@NonNull Runnable command) {
        mUiHandler.post(command);
    }
}
