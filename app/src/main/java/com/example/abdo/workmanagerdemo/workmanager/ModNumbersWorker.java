package com.example.abdo.workmanagerdemo.workmanager;

import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;

public class ModNumbersWorker extends Worker {
    @NonNull
    @Override
    public Result doWork() {
        Log.d("doWork Mod: ", String.valueOf(addTwoNumbers()));
        return Result.SUCCESS;
    }

    private int addTwoNumbers() {
        return 21 % 12;
    }
}
