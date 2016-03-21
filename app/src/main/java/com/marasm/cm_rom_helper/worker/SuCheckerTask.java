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

  public SuCheckerTask(int inUpdateComponentId)
  {
    super(inUpdateComponentId);
  }

  @Override
  public TaskResultsVO executeTask()
  {
    boolean rootAvailable = Shell.SU.available();

    int resultMsgId = R.string.txt_root_not_available;
    if (rootAvailable)
    {
      resultMsgId = R.string.txt_root_available;
    }

    Log.d("SU_CHECKER", "root available=" + rootAvailable);

    return new TaskResultsVO(rootAvailable, resultMsgId, getTargetTextCompoinentId());
  }
}
