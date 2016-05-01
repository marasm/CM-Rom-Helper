package com.marasm.cm_romhelper.worker;

import android.util.Log;

import com.marasm.cm_romhelper.valueobjects.TaskResultsVO;

import java.util.List;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by mkorotkovas on 4/30/16.
 */
public class CmLedSettingsBackupTask extends AbstractTask
{
  private static final String CM_SETTINGS_DB_FILE = "/data/data/org.cyanogenmod.cmsettings/databases/cmsettings.db";

  private static final String GET_CURRENT_LED_NOTIFICATION_SETTINGS =
          "sqlite3 " + CM_SETTINGS_DB_FILE +" \"select name||'>'||value from system where name like 'notification_light%'\"";

  private final String TAG = this.getClass().getSimpleName();


  @Override
  public TaskResultsVO executeTask()
  {
    TaskResultsVO res = new TaskResultsVO();

    try
    {
      List<String> currentSettings = Shell.SU.run(GET_CURRENT_LED_NOTIFICATION_SETTINGS);

      Log.d(TAG, "Current settings: " + currentSettings);
      res.setIsSuccessful(true);
    }
    catch (Exception e)
    {
      Log.d(TAG, "Error while backing up settings: " + e.getMessage());
      res.setIsSuccessful(false);
      res.setErrorMsg(e.getMessage());
    }


    return res;
  }
}
