package com.marasm.cm_rom_helper.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.marasm.cm_rom_helper.enums.TaskType;
import com.marasm.cm_rom_helper.fragments.HomeFragment;
import com.marasm.cm_rom_helper.valueobjects.TaskResultsVO;
import com.marasm.cm_rom_helper.worker.AsyncWorker;
import com.marasm.cm_rom_helper.worker.TaskFactory;
import com.marasm.cm_rom_helper.worker.AbstractTask;
import com.marasm.cm_romhelper.BuildConfig;
import com.marasm.cm_romhelper.R;


public class HomePage extends AppCompatActivity
{

  private boolean rootAvailable = false;
  private DrawerLayout drawerLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

    NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
    // Setup drawer view
    setupDrawerContent(navView);

    try
    {
      //see if superuser is available
      AbstractTask task = TaskFactory.getWorkerTask(TaskType.SU_CHECKER, R.id.txt_root_available);
      AsyncWorker suCheckWorker = new AsyncWorker(this);
      suCheckWorker.execute(task);
      TaskResultsVO suCheckResult = suCheckWorker.get();
      rootAvailable = suCheckResult.getIsSuccessful();

      //set the home fragment
      FragmentManager fragmentManager = getSupportFragmentManager();
      fragmentManager.beginTransaction().replace(R.id.fragment_content,
              new HomeFragment()).commit();

    }
    catch (Exception e)
    {
      Log.e(this.getClass().getName(), "Error checking for root access: " + e.getMessage());
    }
    Log.d(this.getClass().getName(), "Root is available=" + rootAvailable);
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the HomeFragment/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    switch (item.getItemId()) {
      case android.R.id.home:
        drawerLayout.openDrawer(GravityCompat.START);
        return true;
    }


    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
  }


  private void setupDrawerContent(NavigationView navigationView) {
    navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
              @Override
              public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
              }
            });
  }

  public void selectDrawerItem(MenuItem menuItem) {
    // Create a new fragment and specify the fragment to show based on nav item clicked
    Fragment fragment = null;
    Class fragmentClass;
    switch(menuItem.getItemId()) {
      case R.id.nav_home_fragment:
        fragmentClass = HomeFragment.class;
        break;
      case R.id.nav_wallppr_fragment:
        fragmentClass = HomeFragment.class;
        break;
      default:
        fragmentClass = HomeFragment.class;
    }

    try {
      fragment = (Fragment) fragmentClass.newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Insert the fragment by replacing any existing fragment
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.fragment_content, fragment).commit();

    // Highlight the selected item has been done by NavigationView
    menuItem.setChecked(true);
    // Set action bar title
    setTitle(menuItem.getTitle());
    // Close the navigation drawer
    drawerLayout.closeDrawers();
  }
}
