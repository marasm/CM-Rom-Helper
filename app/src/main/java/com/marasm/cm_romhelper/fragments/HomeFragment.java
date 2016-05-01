package com.marasm.cm_romhelper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marasm.cm_romhelper.valueobjects.WorkerResultsVO;
import com.marasm.cm_romhelper.worker.AbstractTask;
import com.marasm.cm_romhelper.worker.AsyncWorker;
import com.marasm.cm_romhelper.worker.SuCheckerTask;
import com.marasm.cm_romhelper.BuildConfig;
import com.marasm.cm_romhelper.R;

public class HomeFragment extends AbstractFragmentWithCallback<HomeFragment.OnHomeFragmentActionListener>
  implements AsyncWorker.WorkerProgressListener
{
  private View fragmentView;
  private OnHomeFragmentActionListener callbackActionListener;
  private boolean isRootAvailable;

  public HomeFragment()
  {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   */
  public static HomeFragment newInstance()
  {
    HomeFragment fragment = new HomeFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    //see if superuser is available
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState)
  {
    fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

    AbstractTask task = new SuCheckerTask();
    AsyncWorker suCheckWorker = new AsyncWorker(this);
    suCheckWorker.execute(task);


    TextView versionTxt = (TextView)fragmentView.findViewById(R.id.txt_app_version);
    versionTxt.setText(BuildConfig.VERSION_NAME);

    return fragmentView;
  }



  @Override
  public void onCallbackHandlerAssigned(OnHomeFragmentActionListener inCallbackHandler)
  {
    callbackActionListener = inCallbackHandler;
  }

  @Override
  public boolean getNeedsRoot()
  {
    return false;
  }

  @Override
  public void onDetach()
  {
    super.onDetach();
    callbackActionListener = null;
  }

  @Override
  public void onProgressChange(int percentDone)
  {

  }

  @Override
  public void onWorkComplete(WorkerResultsVO inWorkerResults)
  {
    isRootAvailable = inWorkerResults.allTasksSuccessfull();
    TextView rootAvailableTxt = (TextView)fragmentView.findViewById(R.id.txt_root_available);
    if (isRootAvailable)
    {
      rootAvailableTxt.setText(R.string.txt_root_available);
    }
    else
    {
      rootAvailableTxt.setText(R.string.txt_root_not_available);
    }
    callbackActionListener.onRootCheckComplete(isRootAvailable);
  }

  @Override
  public void onError(WorkerResultsVO inWorkerResultsVO)
  {
    isRootAvailable = false;
    TextView rootAvailableTxt = (TextView)fragmentView.findViewById(R.id.txt_root_available);
    rootAvailableTxt.setText(R.string.txt_root_not_available);

    callbackActionListener.onRootCheckComplete(false);
  }





  public interface OnHomeFragmentActionListener
  {
    void onRootCheckComplete(boolean inRootAvailable);
  }
}
