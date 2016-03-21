package com.marasm.cm_rom_helper.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.marasm.cm_rom_helper.enums.TaskType;
import com.marasm.cm_rom_helper.valueobjects.TaskResultsVO;
import com.marasm.cm_rom_helper.worker.AsyncWorker;
import com.marasm.cm_rom_helper.worker.TaskFactory;
import com.marasm.cm_rom_helper.worker.AbstractTask;
import com.marasm.cm_romhelper.BuildConfig;
import com.marasm.cm_romhelper.R;


public class HomePage extends AppCompatActivity
{

  private boolean rootAvailable = false;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home_page);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    TextView versionTxt = (TextView)findViewById(R.id.txt_app_version);
    versionTxt.setText(BuildConfig.VERSION_NAME);


    //see if superuser is available
    AbstractTask task = TaskFactory.getWorkerTask(TaskType.SU_CHECKER, R.id.txt_root_available);
    AsyncWorker suCheckWorker = new AsyncWorker(this);
    suCheckWorker.execute(task);
    try
    {
      TaskResultsVO suCheckResult = suCheckWorker.get();
      rootAvailable = suCheckResult.getIsSuccessful();
    }
    catch (Exception e)
    {
      Log.e(this.getClass().getName(), "Error checking for root access: " + e.getMessage());
    }
    Log.d(this.getClass().getName(), "Root is available=" + rootAvailable);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_home_page, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings)
    {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
