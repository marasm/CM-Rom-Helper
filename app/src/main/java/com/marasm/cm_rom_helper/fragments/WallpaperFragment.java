package com.marasm.cm_rom_helper.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.marasm.cm_rom_helper.valueobjects.WorkerResultsVO;
import com.marasm.cm_rom_helper.worker.AsyncWorker;
import com.marasm.cm_rom_helper.worker.SuShellCommandTask;
import com.marasm.cm_rom_helper.worker.WorkerProgressListenerToastImpl;
import com.marasm.cm_romhelper.R;

public class WallpaperFragment extends AbstractFragmentWithCallback<WallpaperFragment.OnWallpaperFragmentActionListener>
{
  private static final String CM_WALLPAPER_LOCATION = "/data/system/users/0/";

  private final String TAG = this.getClass().getSimpleName();

  private OnWallpaperFragmentActionListener callbackActionListener;

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
    Button removeWpBtn = (Button)wpFragmentView.findViewById(R.id.wallpaper_remove_btn);
    removeWpBtn.setOnClickListener(new RemoveButtonOnClickListener());

    Button resetWpBtn = (Button)wpFragmentView.findViewById(R.id.wallpaper_reset_btn);
    resetWpBtn.setOnClickListener(new ResetButtonOnClickListener());

    Button newWpBtn = (Button)wpFragmentView.findViewById(R.id.wallpaper_new_btn);
    newWpBtn.setOnClickListener(new NewButtonOnClickListener());

    return wpFragmentView;
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
    }
  }

}
