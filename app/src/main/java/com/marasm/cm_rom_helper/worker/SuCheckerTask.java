package com.marasm.cm_rom_helper.worker;

import android.util.Log;

import com.marasm.cm_rom_helper.valueobjects.TaskResultsVO;

import eu.chainfire.libsuperuser.Shell;
import marasm.com.cm_romhelper.R;

/**
 * Created by mkorotkovas on 3/9/16.
 */
public class SuCheckerTask extends AbstractTask
{

  public SuCheckerTask(int inUpdateComponentId)
  {
    super(inUpdateComponentId);
  }

  @Override
  public TaskResultsVO executeTask()
  {
    boolean rootAvailable = Shell.SU.available();

    int resultMsgId = R.string.root_available_msg;
    if (!rootAvailable)
    {
      resultMsgId = R.string.root_not_available_msg;
    }

    Log.d("SU_CHECKER", "root available=" + rootAvailable);

    return new TaskResultsVO(rootAvailable, resultMsgId, getTargetTextCompoinentId());
  }
}
