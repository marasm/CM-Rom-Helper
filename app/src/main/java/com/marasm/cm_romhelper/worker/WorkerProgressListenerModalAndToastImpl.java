package com.marasm.cm_romhelper.worker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.marasm.cm_romhelper.R;
import com.marasm.cm_romhelper.valueobjects.WorkerResultsVO;

/**
 * Created by mkorotkovas on 4/30/16.
 */
public class WorkerProgressListenerModalAndToastImpl implements AsyncWorker.WorkerProgressListener
{
  private Context context;
  private String successMsg;
  private String failMsg;
  private ProgressDialog progressDialog;


  public WorkerProgressListenerModalAndToastImpl(Context inContext, String inSuccessMsg, String inFailMsg)
  {
    context = inContext;
    successMsg = inSuccessMsg;
    failMsg = inFailMsg;

  }

  @Override
  public void onProgressChange(int inPercentDone)
  {
    if(inPercentDone < 100)
    {
      if (progressDialog == null)
      {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.txt_led_settings_backingup));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
      }
    }
    else
    {
      dismissProgreesDialog();
    }
  }

  private void dismissProgreesDialog()
  {
    if (progressDialog != null)
    {
      progressDialog.dismiss();
    }
  }

  @Override
  public void onWorkComplete(WorkerResultsVO inWorkerResults)
  {
    dismissProgreesDialog();
    Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onError(WorkerResultsVO inWorkerResultsVO)
  {
    dismissProgreesDialog();
    Toast.makeText(context, failMsg, Toast.LENGTH_SHORT).show();
  }
}
