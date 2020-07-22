package com.example.waterremainderapp.sync;

import android.app.job.JobParameters;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.firebase.jobdispatcher.JobService;

public class WaterRemainderFirebaseJobService extends JobService {
    private AsyncTask mBackgroundTask;

    /**
     * entry point to your job and you should only use it on async or background thread
     *
     * @return
     */
    @Override
    public boolean onStartJob(@NonNull final com.firebase.jobdispatcher.JobParameters jobParameters) {
        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                // send action using context using service
                Context context = WaterRemainderFirebaseJobService.this;
                RemainderTask.executeTask(context, RemainderTask.ACTION_CHARGING_REMAINDER);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(jobParameters, false);
            }
        };

        mBackgroundTask.execute();
        return true;
    }

    /**
     * called when the schedule job is interrupt it can be addressed
     */
    @Override
    public boolean onStopJob(@NonNull com.firebase.jobdispatcher.JobParameters job) {
        // cancel out any background task so that if there is any interrupt it can be addressed
        if (mBackgroundTask != null) {
            mBackgroundTask.cancel(true);
        }
        return true;
    }
}
