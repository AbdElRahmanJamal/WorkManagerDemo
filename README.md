# WorkManagerDemo
Schedule tasks with WorkManager
The WorkManager API makes it easy to specify deferrable, asynchronous tasks and when they should run. 
These APIs let you create a task and hand it off to WorkManager to run immediately or at an appropriate time



WorkManager chooses the appropriate way to run your task based on such factors as the device API level and the app state

WorkManager chooses an appropriate way to schedule a background task--depending on the device API level and included dependencies, 
WorkManager might use JobScheduler, Firebase JobDispatcher, or AlarmManager.

work manager advantages 
 1-not need to write device logic
 2-not need google play services 
 3-cancelled 
 4-Chained 
 
 ---------------------------------------------------------------------------------------------------------------------------------------
 
  basic steps to use work manager 
        1- add Adding Components to your Project go to build.gradle and add dependency
         /*
         
         def work_version = "1.0.0-alpha06"
         implementation "android.arch.work:work-runtime:$work_version" // use -ktx for Kotlin
         
         */
        2- create class that extend Worker
        3- create a work request with/without constrains (OneTimeWorkRequest ,PeriodicWorkRequest,Chained ,WorkContinuation )
        4- enqueue the work
        
        
---------------------------------------------------------------------------------------------------------------------------------------

to Canceling a Task
UUID urworkmanagerID = urWorkManager.getId();
WorkManager.getInstance().cancelWorkById(urworkmanagerID);

--------------------------------------------------------------------------------------------------------------------------------------
 If you need to check on the task status, you can get a WorkStatus object by getting a handle to the appropriate LiveData<WorkStatus>
 
 sample code  WorkManager.getInstance().getStatusById(urWorkManager.getId())
    .observe(lifecycleOwner, workStatus -> {
        if (workStatus != null && workStatus.getState().isFinished()) {
            // ...
            //update ui 
            //do anything with result
        }
    });
 
