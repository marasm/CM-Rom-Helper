package com.marasm.cm_romhelper.worker;

import android.content.Context;
import android.util.Log;

import com.marasm.cm_romhelper.constants.Constants;
import com.marasm.cm_romhelper.dataaccess.LedNotificationSettingsDAO;
import com.marasm.cm_romhelper.valueobjects.TaskResultsVO;

import java.util.List;
import java.util.Map;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by mkorotkovas on 5/1/16.
 */
public class CmLedSettingsRestoreTask extends AbstractTask
{
  private final String TAG = this.getClass().getSimpleName();

  private Context context;

  public CmLedSettingsRestoreTask(Context inContext)
  {
    context = inContext;
  }

  @Override
  public TaskResultsVO executeTask()
  {
    TaskResultsVO res = new TaskResultsVO();

    try
    {
      Map<String, String> settingsMap = getLedNotificationsSettingsDAO().getLedNotificationSettings();

      if (!settingsMap.isEmpty())
      {
        Log.d(TAG, "Found settings backed up earlier");
        for (Map.Entry<String, String> entry : settingsMap.entrySet())
        {
          //delete setting first
          Log.d(TAG, "clearing setting " + entry.getKey());
          List<String> out = Shell.SU.run("sqlite3 " + Constants.CM_SETTINGS_DB_FILE +
                  " \"DELETE FROM system WHERE name = '" + entry.getKey() +"'\"");
          Log.d(TAG, "DELETE cmd output: " + out);

          //insert new value
          Log.d(TAG, "inserting property " + entry.getKey() + "=" + entry.getValue());
          out = Shell.SU.run("sqlite3 " + Constants.CM_SETTINGS_DB_FILE +
                  " \"INSERT INTO system (name, value) VALUES ('" + entry.getKey() +"', '"+ entry.getValue() +"') \"");
          Log.d(TAG, "INSERT cmd output: " + out);
        }
        res.setIsSuccessful(true);
      }
      else
      {
        throw new RuntimeException("Settings backup was not found");
      }
    }
    catch (Exception e)
    {
      Log.e(TAG, "Error while restoring LED settings" + e.getMessage(), e);
      res.setIsSuccessful(false);
      res.setErrorMsg(e.getMessage());
    }

    return res;
  }

  protected LedNotificationSettingsDAO getLedNotificationsSettingsDAO()
  {
    return new LedNotificationSettingsDAO(context);
  }
}
