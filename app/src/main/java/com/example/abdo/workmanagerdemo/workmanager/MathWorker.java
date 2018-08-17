package com.example.abdo.workmanagerdemo.workmanager;

import android.support.annotation.NonNull;

import androidx.work.Data;
import androidx.work.Worker;

public class MathWorker extends Worker {
    // Define the  arguments keys:
    public static final String LENGTH_KEY = "LENGTH_KEY";
    public static final String WIDTH_KEY = "WIDTH_KEY";
    // ...and the result key:
    public static final String AREA_KEY = "AREA_KEY";

    @NonNull
    @Override
    public Result doWork() {

        int length = getInputData().getInt(LENGTH_KEY, 1);
        int width = getInputData().getInt(WIDTH_KEY, 1);

        int area = getAreaOfRectangle(length, width);

        Data output = new Data.Builder()
                .putInt(AREA_KEY, area)
                .build();

        setOutputData(output);

        return Result.SUCCESS;
    }

    private int getAreaOfRectangle(int length, int width) {
        return length * width;
    }

}
