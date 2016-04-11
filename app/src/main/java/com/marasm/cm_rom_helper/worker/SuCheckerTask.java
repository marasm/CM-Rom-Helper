package com.marasm.cm_rom_helper.worker;

import android.util.Log;

import com.marasm.cm_rom_helper.valueobjects.TaskResultsVO;
import com.marasm.cm_romhelper.R;

import eu.chainfire.libsuperuser.Shell;


/**
 * Created by mkorotkovas on 3/9/16.
 */
public class SuCheckerTask extends AbstractTask
{


  @Override
  public TaskResultsVO executeTask()
  {

    try
    {
      boolean rootAvailable = Shell.SU.available();
      Log.d("SU_CHECKER", "root available=" + rootAvailable);
      return new TaskResultsVO(rootAvailable, null);
    }
    catch(Exception e)
    {
      Log.e(this.getClass().getName(), "Error while checking root: ", e);
      return new TaskResultsVO(false, e.getMessage());
    }



  }
}
