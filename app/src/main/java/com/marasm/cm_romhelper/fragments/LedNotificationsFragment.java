package com.marasm.cm_romhelper.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marasm.cm_romhelper.R;

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
  private OnLedNotificationAcionListener callbackListener;

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
    return inflater.inflate(R.layout.fragment_led_notifications, container, false);
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
}
