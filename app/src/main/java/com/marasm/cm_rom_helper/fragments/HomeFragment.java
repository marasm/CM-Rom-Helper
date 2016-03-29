package com.marasm.cm_rom_helper.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marasm.cm_rom_helper.constants.Constants;
import com.marasm.cm_romhelper.BuildConfig;
import com.marasm.cm_romhelper.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment
{
  private boolean rootAvailable;

  private OnFragmentInteractionListener mListener;
  private boolean isRootAvailable;

  public HomeFragment()
  {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   */
  public static HomeFragment newInstance(boolean inIsRootAvailable)
  {
    HomeFragment fragment = new HomeFragment();
    Bundle args = new Bundle();
    args.putBoolean(Constants.IS_ROOT_AVAILABLE_PARAM, inIsRootAvailable);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    if (getArguments() != null)
    {
      isRootAvailable = getArguments().getBoolean(Constants.IS_ROOT_AVAILABLE_PARAM);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState)
  {
    View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
    TextView versionTxt = (TextView)fragmentView.findViewById(R.id.txt_app_version);

    versionTxt.setText(BuildConfig.VERSION_NAME);

    return fragmentView;
  }


  @Override
  public void onAttach(Context context)
  {
    super.onAttach(context);


  }

  @Override
  public void onDetach()
  {
    super.onDetach();
    mListener = null;
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p/>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener
  {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }
}
