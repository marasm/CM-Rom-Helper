package com.marasm.cm_romhelper.worker;

import android.content.Context;
import android.util.Log;

import com.marasm.cm_romhelper.constants.Constants;
import com.marasm.cm_romhelper.dataaccess.LedNotificationSettingsDAO;
import com.marasm.cm_romhelper.valueobjects.TaskResultsVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by mkorotkovas on 4/30/16.
 */
public class CmLedSettingsBackupTask extends AbstractTask
{
  private static final String GET_CURRENT_LED_NOTIFICATION_SETTINGS =
          "sqlite3 " + Constants.CM_SETTINGS_DB_FILE +" \"select name||'>'||value from system where name like 'notification_light%'\"";

  private final String TAG = this.getClass().getSimpleName();

  private Context context;

  public CmLedSettingsBackupTask(Context inContext)
  {
    context = inContext;
  }



  @Override
  public TaskResultsVO executeTask()
  {
    TaskResultsVO res = new TaskResultsVO();

    try
    {
      List<String> currentSettings = Shell.SU.run(GET_CURRENT_LED_NOTIFICATION_SETTINGS);
      Log.d(TAG, "Current settings: " + currentSettings);
      if (currentSettings != null && !currentSettings.isEmpty())
      {
        Map<String, String> settingsMap = new HashMap<String, String>();
        for (String settingLine : currentSettings)
        {
          String[] splitSetting = settingLine.split(">", 2);
          //only add valid lines (key and value)
          if (splitSetting.length == 2)
          {
            settingsMap.put(splitSetting[0], splitSetting[1]);
          }
          else
          {
            Log.d(TAG, "invalid setting line: " + settingLine + ". Will skip.");
          }
        }
        if (!settingsMap.isEmpty())
        {
          LedNotificationSettingsDAO dao = getLedNotificationsSettingsDAO();
          dao.storeLedNotificationSettings(settingsMap);
          res.setIsSuccessful(true);
        }
      }
      else
      {
        throw new RuntimeException("Unable to retrieve current LED Notification settings");
      }

    }
    catch (Exception e)
    {
      Log.d(TAG, "Error while backing up settings: " + e.getMessage());
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
