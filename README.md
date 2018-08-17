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
 3-Chained 
