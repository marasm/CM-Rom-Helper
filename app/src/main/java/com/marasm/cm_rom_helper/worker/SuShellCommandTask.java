package com.marasm.cm_rom_helper.worker;

import android.util.Log;

import com.marasm.cm_rom_helper.valueobjects.TaskResultsVO;

import java.util.List;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by mkorotkovas on 4/14/16.
 */
public class SuShellCommandTask extends AbstractTask
{
  private String TAG = this.getClass().getSimpleName();
  private String command;

  public SuShellCommandTask(String inCommand)
  {
    command = inCommand;
  }

  @Override
  public TaskResultsVO executeTask()
  {
    boolean isSuccesfull = false;
    String errMsg = "";

    try
    {
      List<String> out = Shell.SU.run(command);
      if (out != null)
      {
        Log.d(TAG, command + " \n " + out.toString());
        isSuccesfull = true;
      }
      else
      {
        Log.d(TAG, command + " failed or root is not available");
        isSuccesfull = false;
      }
    }
    catch(Exception e)
    {
      Log.e(TAG, "Error executing shell command " + command, e);
    }


    TaskResultsVO res = new TaskResultsVO(isSuccesfull, errMsg);
    return res;
  }
}
