package com.marasm.cm_romhelper.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.marasm.cm_romhelper.R;
import com.marasm.cm_romhelper.dataaccess.LedNotificationSettingsDAO;
import com.marasm.cm_romhelper.worker.AsyncWorker;
import com.marasm.cm_romhelper.worker.CmLedSettingsBackupTask;
import com.marasm.cm_romhelper.worker.CmLedSettingsRestoreTask;
import com.marasm.cm_romhelper.worker.WorkerProgressListenerModalAndToastImpl;

import java.util.Map;

import eu.chainfire.libsuperuser.Shell;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLedNotificationAcionListener} interface
 * to handle interaction events.
 * Use the {@link LedNotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LedNotificationsFragment extends AbstractFragmentWithCallback<LedNotificationsFragment.OnLedNotificationAcionListener>
{
  private final String TAG = this.getClass().getSimpleName();

  private OnLedNotificationAcionListener callbackListener;

  private Button backupButton;
  private Button restoreButton;
  private Button restartButton;

  public LedNotificationsFragment()
  {
    // Required empty public constructor
  }

  public static LedNotificationsFragment newInstance()
  {
    LedNotificationsFragment fragment = new LedNotificationsFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle inSavedInstanceState)
  {
    super.onCreate(inSavedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState)
  {
    View fragmentView = inflater.inflate(R.layout.fragment_led_notifications, container, false);

    backupButton = (Button) fragmentView.findViewById(R.id.led_notifications_backup_btn);
    backupButton.setOnClickListener(new BackupButtonOnClickListener());

    restoreButton = (Button) fragmentView.findViewById(R.id.led_notifications_restore_btn);
    restoreButton.setOnClickListener(new RestoreButtonOnClickListener());

    restartButton = (Button) fragmentView.findViewById(R.id.led_notifications_restart_system_btn);
    restartButton.setOnClickListener(new RestartButtonOnClickListener());


    return fragmentView;
  }


  @Override
  public void onCallbackHandlerAssigned(OnLedNotificationAcionListener inCallbackHandler)
  {
    callbackListener = inCallbackHandler;
  }

  @Override
  public boolean getNeedsRoot()
  {
    return true;
  }

  @Override
  public void onDetach()
  {
    super.onDetach();
    callbackListener = null;
  }

  public interface OnLedNotificationAcionListener
  {
    void onLedNotificationFragmentAction(Uri uri);
  }

  private class BackupButtonOnClickListener implements View.OnClickListener
  {
    @Override
    public void onClick(View v)
    {
      Log.d(TAG, "Backup button clicked");
      //perform the backup
      CmLedSettingsBackupTask backupTask = new CmLedSettingsBackupTask(getContext());
      AsyncWorker worker = new AsyncWorker(
              new WorkerProgressListenerModalAndToastImpl(
                      getContext(),
                      getString(R.string.txt_led_settings_backingup),
                      getString(R.string.toast_led_settings_backup_success),
                      getString(R.string.toast_led_settings_backup_fail)));
      worker.execute(backupTask);
    }
  }
  private class RestoreButtonOnClickListener implements View.OnClickListener
  {
    @Override
    public void onClick(View v)
    {
      Log.d(TAG, "Restore button clicked");
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
              .setTitle(R.string.ttl_txt_confirm_settings_restore)
              .setMessage(R.string.txt_led_cur_settings_overwrite)
              .setPositiveButton(R.string.btn_alert_confirm, new RestoreConfirmationOnClickListener())
              .setNegativeButton(R.string.btn_alert_cancel, new RestoreConfirmationOnClickListener());
      builder.create().show();
    }
  }

  private class RestoreConfirmationOnClickListener implements DialogInterface.OnClickListener
  {
    @Override
    public void onClick(DialogInterface inDialog, int inChoice)
    {
      Log.d(TAG, "Restore setting confirmation clicked. Choice: " + inChoice);

      if(DialogInterface.BUTTON_POSITIVE == inChoice)
      {
        Log.d(TAG, "Restoring the LED Notification settings");
        //perform the restore of settings only if backup exist
        LedNotificationSettingsDAO dao = getLedNotificationsSettingsDAO();
        Map<String, String> settingsMap = dao.getLedNotificationSettings();
        if (settingsMap.isEmpty())
        {
          Toast.makeText(getContext(), getString(R.string.toast_led_settings_restore_unavailable),
                  Toast.LENGTH_SHORT).show();
        }
        else
        {
          CmLedSettingsRestoreTask restoreTask = new CmLedSettingsRestoreTask(getContext());
          AsyncWorker worker = new AsyncWorker(
                  new WorkerProgressListenerModalAndToastImpl(
                          getContext(),
                          getString(R.string.txt_led_settings_restoring),
                          getString(R.string.toast_led_settings_restore_success),
                          getString(R.string.toast_led_settings_restore_fail)));
          worker.execute(restoreTask);
        }


      }

    }
  }

  private class RestartButtonOnClickListener implements View.OnClickListener
  {
    @Override
    public void onClick(View v)
    {
      Log.d(TAG, "Restart button clicked. ");
      Log.d(TAG, "Restarting system. ");
      Toast.makeText(getContext(), getString(R.string.toast_restarting_system), Toast.LENGTH_SHORT).show();
      Shell.SU.run("reboot");
    }
  }

  protected LedNotificationSettingsDAO getLedNotificationsSettingsDAO()
  {
    return new LedNotificationSettingsDAO(getContext());
  }

}
