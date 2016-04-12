package com.marasm.cm_rom_helper.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marasm.cm_romhelper.R;

public class WallpaperFragment extends AbstractFragmentWithCallback<WallpaperFragment.OnWallpaperFragmentActionListener>
{
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
    return inflater.inflate(R.layout.fragment_wallpaper, container, false);
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
}
