package com.example.abdo.workmanagerdemo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.abdo.workmanagerdemo.workmanager.AddingNumbersWorker;
import com.example.abdo.workmanagerdemo.workmanager.DivideNumberWorker;
import com.example.abdo.workmanagerdemo.workmanager.MathWorker;
import com.example.abdo.workmanagerdemo.workmanager.ModNumbersWorker;
import com.example.abdo.workmanagerdemo.workmanager.MultipleNumberWorker;
import com.example.abdo.workmanagerdemo.workmanager.SubtractionNumbersWorker;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {
    int length, width;

    /*
      there are 4 steps to use work manager component
        1- add Adding Components to your Project go to build.gradle and add dependency
        2- create class that extend Worker
        3- create a work request with/without constrains
        4- enqueue the work
       */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        length = 10;
        width = 13;
    }

    public void OneTimeWorkRequest(View v) {
        OneTimeWorkRequest addingWorker =
                new OneTimeWorkRequest.Builder(AddingNumbersWorker.class)
                        .build();
        WorkManager.getInstance().enqueue(addingWorker);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void OneTimeWorkRequestWithConstraints(View v) {
        Constraints myConstraints = new Constraints.Builder()
                .setRequiresDeviceIdle(true)
                .setRequiresCharging(true)
                .build();
        OneTimeWorkRequest addingWorker =
                new OneTimeWorkRequest.Builder(AddingNumbersWorker.class)
                        .setConstraints(myConstraints)
                        .build();
        WorkManager.getInstance().enqueue(addingWorker);
    }

    /*
    **** HINT : minimum The minimum interval duration for PeriodicWorkRequest (in milliseconds).
             CONST Value = 900000  and it is equal  15 minutes
    also we can add constrains to Periodic Work Request
    * */
    public void PeriodicWorkRequest(View v) {
        PeriodicWorkRequest.Builder addingNumberWorker =
                new PeriodicWorkRequest.Builder(AddingNumbersWorker.class, 16,
                        TimeUnit.MINUTES);

        PeriodicWorkRequest addingWorker = addingNumberWorker.build();

        WorkManager.getInstance().enqueue(addingWorker);
    }

    public void sequenceChainedTask(View v) {
        ArrayList<OneTimeWorkRequest> oneTimeWorkRequests = buildOneTimeWorkRequest();
        //if work addingWorker fail it will not proceed to other works
        WorkManager.getInstance()
                .beginWith(oneTimeWorkRequests.get(0)) //beginWith and then also can take list of OneTimeWorkRequest
                //beginWith(workA,workB,workC) in parallel way
                .then(oneTimeWorkRequests.get(1))
                .then(oneTimeWorkRequests.get(2))
                .then(oneTimeWorkRequests.get(3))
                .enqueue();
    }

    public void parallelChainedTask(View v) {
        ArrayList<OneTimeWorkRequest> oneTimeWorkRequests = buildOneTimeWorkRequest();
        WorkManager.getInstance()
                //all work must be success to proceed to others
                .beginWith(oneTimeWorkRequests.get(0), oneTimeWorkRequests.get(1))
                //if addingWorker fail or subWorker fail it will not proceed to multiWorker or divideWorker
                .then(oneTimeWorkRequests.get(2), oneTimeWorkRequests.get(3))
                .enqueue();
    }

    //for more complex chained tasks
    //like this figure
    // A     C
    // B     D
    //     E
    public void workContinuation(View v) {
        ArrayList<OneTimeWorkRequest> oneTimeWorkRequests = buildOneTimeWorkRequest();
        WorkContinuation chain1 = WorkManager.getInstance()
                .beginWith(oneTimeWorkRequests.get(0))
                .then(oneTimeWorkRequests.get(1));
        WorkContinuation chain2 = WorkManager.getInstance()
                .beginWith(oneTimeWorkRequests.get(2))
                .then(oneTimeWorkRequests.get(3));
        WorkContinuation chain3 = WorkContinuation
                .combine(chain1, chain2)
                .then(oneTimeWorkRequests.get(4));
        chain3.enqueue();
    }

    public void inputParametersAndReturnedValuesUsingWorkManger(View view) {
        Data input = new Data.Builder()
                .putInt(MathWorker.LENGTH_KEY, length)
                .putInt(MathWorker.WIDTH_KEY, width)
                .build();

        OneTimeWorkRequest mathWork = new OneTimeWorkRequest.Builder(MathWorker.class)
                .setInputData(input)
                .build();
        WorkManager.getInstance().enqueue(mathWork);

        WorkManager.getInstance().getStatusById(mathWork.getId())
                .observe(this, status -> {
                    if (status != null && status.getState().isFinished()) {
                        int myResult = status.getOutputData().getInt(MathWorker.AREA_KEY, 1);
                        Log.d("Area  ", String.valueOf(myResult));
                    }
                });

    }

    private ArrayList<OneTimeWorkRequest> buildOneTimeWorkRequest() {
        ArrayList<OneTimeWorkRequest> oneTimeWorkRequests = new ArrayList<>();
        OneTimeWorkRequest addingWorker =
                new OneTimeWorkRequest.Builder(AddingNumbersWorker.class)
                        .build();
        OneTimeWorkRequest subWorker =
                new OneTimeWorkRequest.Builder(SubtractionNumbersWorker.class)
                        .build();
        OneTimeWorkRequest multiWorker =
                new OneTimeWorkRequest.Builder(MultipleNumberWorker.class)
                        .build();
        OneTimeWorkRequest divideWorker =
                new OneTimeWorkRequest.Builder(DivideNumberWorker.class)
                        .build();
        OneTimeWorkRequest modWorker =
                new OneTimeWorkRequest.Builder(ModNumbersWorker.class)
                        .build();
        oneTimeWorkRequests.add(addingWorker);
        oneTimeWorkRequests.add(subWorker);
        oneTimeWorkRequests.add(multiWorker);
        oneTimeWorkRequests.add(divideWorker);
        oneTimeWorkRequests.add(modWorker);
        return oneTimeWorkRequests;
    }

}
