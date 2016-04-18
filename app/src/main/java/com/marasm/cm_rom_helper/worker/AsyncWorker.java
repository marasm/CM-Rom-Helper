package com.marasm.cm_rom_helper.worker;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.marasm.cm_rom_helper.valueobjects.TaskResultsVO;
import com.marasm.cm_rom_helper.valueobjects.WorkerResultsVO;

/**
 * Created by mkorotkovas on 3/8/16.
 */
public class AsyncWorker extends AsyncTask<AbstractTask, Integer, WorkerResultsVO>
{
  private final String TAG = this.getClass().getSimpleName();
  private WorkerProgressListener progressListener;

  public AsyncWorker(WorkerProgressListener inProgressListener)
  {
    progressListener = inProgressListener;
  }



  @Override
  protected WorkerResultsVO doInBackground(AbstractTask... inTasks)
  {
    if (inTasks == null || inTasks[0] == null)
    {
      throw new IllegalArgumentException("No task supplied for execution");
    }
    publishProgress(Integer.valueOf(0));

    WorkerResultsVO results = new WorkerResultsVO();
    for(int i = 0; i < inTasks.length; i++)
    {
      TaskResultsVO taskRes = inTasks[i].executeTask();
      publishProgress(Integer.valueOf((i/inTasks.length)*100));
      results.addTaskResults(taskRes);
      if (!taskRes.getIsSuccessful())
      {
        Log.e(TAG, "Task" + inTasks.getClass().getName() +
                " execution failed: " + taskRes.getErrorMsg());
        break;
      }
    }
    if (results.allTasksSuccessfull())
    {
      publishProgress(Integer.valueOf(100));
    }

    return results;
  }

  @Override
  protected void onProgressUpdate(Integer... values)
  {
    super.onProgressUpdate(values);
    progressListener.onProgressChange(values[0]);
  }

  @Override
  protected void onPostExecute(WorkerResultsVO inWorkerResultsVO)
  {
    super.onPostExecute(inWorkerResultsVO);
    if (inWorkerResultsVO.allTasksSuccessfull())
    {
      progressListener.onWorkComplete(inWorkerResultsVO);
    }
    else
    {
      progressListener.onError(inWorkerResultsVO);
    }
  }



  public interface WorkerProgressListener
  {
    public void onProgressChange(int percentDone);

    public void onWorkComplete(WorkerResultsVO inWorkerResults);

    public void onError(WorkerResultsVO inWorkerResultsVO);
  }
}
