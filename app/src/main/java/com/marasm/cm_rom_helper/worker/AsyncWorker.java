package com.marasm.cm_rom_helper.worker;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.marasm.cm_rom_helper.valueobjects.TaskResultsVO;

/**
 * Created by mkorotkovas on 3/8/16.
 */
public class AsyncWorker extends AsyncTask<AbstractTask, Integer, TaskResultsVO>
{
  private Context context;

  public AsyncWorker(Context inContext)
  {
    context = inContext;
  }



  @Override
  protected TaskResultsVO doInBackground(AbstractTask... inParams)
  {
    if (inParams == null || inParams[0] == null)
    {
      throw new IllegalArgumentException("No task supplied for execution");
    }
    publishProgress(Integer.valueOf(0));

    //at this point we only care about the first task
    TaskResultsVO results =  inParams[0].executeTask();

    publishProgress(Integer.valueOf(100));
    return results;
  }

  @Override
  protected void onProgressUpdate(Integer... values)
  {
    super.onProgressUpdate(values);
  }

  @Override
  protected void onPostExecute(TaskResultsVO inTaskResultsVO)
  {
    Log.d(this.getClass().getName(), "entering onPostExecute()");

    if (inTaskResultsVO != null && inTaskResultsVO.getResultMessageId() > 0 &&
            inTaskResultsVO.getTargetTextComponentId() > 0 && context != null)
    {
      View view = ((Activity) context).findViewById(inTaskResultsVO.getTargetTextComponentId());
      Log.d(this.getClass().getName(), "checking target component compatibility");
      if (view instanceof TextView)
      {
        Log.d(this.getClass().getName(), "updating target component text");
        ((TextView)view).setText(inTaskResultsVO.getResultMessageId());
      }
    }

  }
}
