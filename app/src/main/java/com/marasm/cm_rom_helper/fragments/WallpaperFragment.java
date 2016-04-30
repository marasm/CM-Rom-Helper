package com.marasm.cm_rom_helper.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.marasm.cm_rom_helper.util.Utils;
import com.marasm.cm_rom_helper.worker.AbstractTask;
import com.marasm.cm_rom_helper.worker.AsyncWorker;
import com.marasm.cm_rom_helper.worker.SuShellCommandTask;
import com.marasm.cm_rom_helper.worker.WorkerProgressListenerToastImpl;
import com.marasm.cm_romhelper.R;

import java.io.File;

import eu.chainfire.libsuperuser.Shell;

public class WallpaperFragment extends AbstractFragmentWithCallback<WallpaperFragment.OnWallpaperFragmentActionListener>
{
  private static final String CM_WALLPAPER_LOCATION = "/data/system/users/0/";

  private static final String LOCKSCREEN_WP_INFO_FILE_CONTENT =
          "\"<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\n" +
          "<kwp width=\"1080\" height=\"1920\" name=\"\" />\n\"";

  private static final int REQ_CD_INTENT_IMAGE_SELECT = 1;

  private static final int REQ_CD_PERMISSION_READ_EXT_STORAGE = 1;

  private final String TAG = this.getClass().getSimpleName();

  private OnWallpaperFragmentActionListener callbackActionListener;

  private boolean extStoragePermissionGranted = false;
  private Button removeWpBtn;
  private Button resetWpBtn;
  private Button newWpBtn;
  private Button restartSysUiBtn;

  public WallpaperFragment()
  {
    // Required empty public constructor
  }

  public static WallpaperFragment newInstance()
  {
    WallpaperFragment fragment = new WallpaperFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState)
  {
    // Inflate the layout for this fragment
    View wpFragmentView =  inflater.inflate(R.layout.fragment_wallpaper, container, false);

    //setup the button handlers
    removeWpBtn = (Button)wpFragmentView.findViewById(R.id.wallpaper_remove_btn);
    removeWpBtn.setOnClickListener(new RemoveButtonOnClickListener());

    resetWpBtn = (Button)wpFragmentView.findViewById(R.id.wallpaper_reset_btn);
    resetWpBtn.setOnClickListener(new ResetButtonOnClickListener());

    newWpBtn = (Button)wpFragmentView.findViewById(R.id.wallpaper_new_btn);
    newWpBtn.setOnClickListener(new NewButtonOnClickListener());
    newWpBtn.setEnabled(false);

    restartSysUiBtn = (Button)wpFragmentView.findViewById(R.id.wallpaper_restart_ui_btn);
    restartSysUiBtn.setOnClickListener(new RestartSysUiButtonOnClickListener());

    checkExtStoragePermissions();


    return wpFragmentView;
  }


  private void checkExtStoragePermissions()
  {
    if (ContextCompat.checkSelfPermission(getActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)
    {
      requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_CD_PERMISSION_READ_EXT_STORAGE);
    }
    else
    {
      newWpBtn.setEnabled(true);
    }
  }


  @Override
  public void onDetach()
  {
    super.onDetach();
  }




  @Override
  public void onCallbackHandlerAssigned(OnWallpaperFragmentActionListener inCallbackHandler)
  {
    callbackActionListener = inCallbackHandler;
  }

  @Override
  public boolean getNeedsRoot()
  {
    return true;
  }

  @Override
  public void onActivityResult(int inRequestCode, int inResultCode, Intent inIntent)
  {
    Log.d(TAG, "onActivityResult result code: " + inResultCode);
    Log.d(TAG, "onActivityResult request code: " + inRequestCode);
    Uri imageUri = inIntent.getData();

    Log.d(TAG, "selected image uri: " + imageUri.toString());
    String fullFilePath = Utils.getRealPathFromURI(getContext(), imageUri);
    Log.d(TAG, "selected image path: " + fullFilePath);

    AbstractTask[] tasks = {
    //1. backup current keyguard_wallpaper
      new SuShellCommandTask(
            "mv " + CM_WALLPAPER_LOCATION + "keyguard_wallpaper " + CM_WALLPAPER_LOCATION + "keyguard_wallpaper.bak"),
    //2. copy the selected one
      new SuShellCommandTask(
            "cp " + fullFilePath + " " + CM_WALLPAPER_LOCATION + "keyguard_wallpaper"),
    //3. create a keyguard.xml file if needed
      new SuShellCommandTask(
            "echo " + LOCKSCREEN_WP_INFO_FILE_CONTENT + " > " + CM_WALLPAPER_LOCATION + "keyguard_wallpaper_info.xml"),
    //4. set proper owner and permissions
      new SuShellCommandTask(
            "chown -h system:system " + CM_WALLPAPER_LOCATION + "keyguard_wallpaper"),
      new SuShellCommandTask(
            "chown -h system:system " + CM_WALLPAPER_LOCATION + "keyguard_wallpaper_info.xml")
    
    };

    AsyncWorker worker = new AsyncWorker(new WorkerProgressListenerToastImpl(
            getContext(),
            getString(R.string.toast_lock_scrn_wlppr_set_success),
            getString(R.string.toast_lock_scrn_wlppr_set_fail)));
    worker.execute(tasks);
  }

  @Override
  public void onRequestPermissionsResult(int inRequestCode, @NonNull String[] inPermissions, @NonNull int[] inGrantResults)
  {
    Log.d(TAG, "onRequestPermissionsResult: request code" + inRequestCode);

    if (inRequestCode == REQ_CD_PERMISSION_READ_EXT_STORAGE)
    {
      // If request is cancelled, the result arrays are empty.
      if (inGrantResults.length > 0 && inGrantResults[0] == PackageManager.PERMISSION_GRANTED)
      {
        newWpBtn.setEnabled(true);
      }
      else
      {
        newWpBtn.setEnabled(false);
        Toast.makeText(getContext(), R.string.toast_ext_storage_perm_denied, Toast.LENGTH_SHORT).show();
      }
    }
  }

  public interface OnWallpaperFragmentActionListener
  {
    void onWallpaperFragmentAction();
  }

  private class RemoveButtonOnClickListener implements View.OnClickListener
  {
    @Override
    public void onClick(View v)
    {
      Log.d(TAG, "remove button clicked: ");

      SuShellCommandTask cmd = new SuShellCommandTask(
              "mv " + CM_WALLPAPER_LOCATION + "keyguard_wallpaper " + CM_WALLPAPER_LOCATION + "keyguard_wallpaper.bak");

      AsyncWorker worker = new AsyncWorker(new WorkerProgressListenerToastImpl(
        getContext(),
        getString(R.string.toast_lock_scrn_wlppr_remove_success),
        getString(R.string.toast_lock_scrn_wlppr_remove_fail)));

      worker.execute(cmd);
    }
  }
  private class ResetButtonOnClickListener implements View.OnClickListener
  {
    @Override
    public void onClick(View v)
    {
      Log.d(TAG, "reset button clicked: ");

      //make a backup copy if one already set
      SuShellCommandTask cmd1 = new SuShellCommandTask(
              "mv -f " + CM_WALLPAPER_LOCATION + "keyguard_wallpaper " + CM_WALLPAPER_LOCATION + "keyguard_wallpaper.bak 2>/dev/null");
      //create a link pointing to the regular (launcher) wallpaper
      SuShellCommandTask cmd2 = new SuShellCommandTask(
              "ln -s " + CM_WALLPAPER_LOCATION + "wallpaper " + CM_WALLPAPER_LOCATION + "keyguard_wallpaper");
      //set permissions
      SuShellCommandTask cmd3 = new SuShellCommandTask(
              "chown -h system:system " + CM_WALLPAPER_LOCATION + "keyguard_wallpaper");

      AsyncWorker worker = new AsyncWorker(new WorkerProgressListenerToastImpl(
              getContext(),
              getString(R.string.toast_lock_scrn_wlppr_reset_success),
              getString(R.string.toast_lock_scrn_wlppr_reset_fail)));

      worker.execute(cmd1, cmd2, cmd3);
    }
  }
  private class NewButtonOnClickListener implements View.OnClickListener
  {
    @Override
    public void onClick(View v)
    {
      Log.d(TAG, "new button clicked: ");

      Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

      photoPickerIntent.setType("image/*");

      startActivityForResult(photoPickerIntent, REQ_CD_INTENT_IMAGE_SELECT);

    }
  }

  private class RestartSysUiButtonOnClickListener implements View.OnClickListener
  {

    @Override
    public void onClick(View v)
    {
      Log.d(TAG, "restart sys ui button clicked: ");
      Shell.SU.run("pkill -f com.android.systemui");
    }
  }

}
