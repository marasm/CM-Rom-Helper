package com.marasm.cm_rom_helper.worker;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.marasm.cm_rom_helper.valueobjects.WorkerResultsVO;

/**
 * Created by mkorotkovas on 4/18/16.
 */
public class WorkerProgressListenerToastImpl implements AsyncWorker.WorkerProgressListener
{
  private final String TAG = this.getClass().getSimpleName();

  private Context context;

  private String successMessage;
  private String failMessage;


  public WorkerProgressListenerToastImpl(Context inContext, String inSuccessMsg, String inFailMsg)
  {
    context = inContext;
    successMessage = inSuccessMsg;
    failMessage = inFailMsg;
  }

  @Override
  public void onProgressChange(int percentDone)
  {
    Log.d(TAG, "onProgressChange: " + percentDone + "% complete");
  }

  @Override
  public void onWorkComplete(WorkerResultsVO inWorkerResults)
  {
    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT);
  }

  @Override
  public void onError(WorkerResultsVO inWorkerResultsVO)
  {
    Toast.makeText(context, failMessage, Toast.LENGTH_SHORT);

  }
}
