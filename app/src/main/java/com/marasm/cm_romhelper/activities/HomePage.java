package com.marasm.cm_romhelper.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.marasm.cm_romhelper.fragments.AbstractFragmentWithCallback;
import com.marasm.cm_romhelper.fragments.HomeFragment;
import com.marasm.cm_romhelper.fragments.LedNotificationsFragment;
import com.marasm.cm_romhelper.fragments.WallpaperFragment;
import com.marasm.cm_romhelper.R;


public class HomePage extends AppCompatActivity implements HomeFragment.OnHomeFragmentActionListener,
        WallpaperFragment.OnWallpaperFragmentActionListener, LedNotificationsFragment.OnLedNotificationAcionListener
{

  private boolean rootAvailable = false;
  private DrawerLayout drawerLayout;
  private Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null)
    {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setHomeButtonEnabled(true);
      actionBar.setHomeAsUpIndicator(R.drawable.ic_action_menu);
    }

    drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

    NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
    // Setup drawer view
    setupDrawerContent(navView);

    MenuItem homeMI = navView.getMenu().getItem(0);
    selectDrawerItem(homeMI);

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
    AbstractFragmentWithCallback fragment = null;
    Class fragmentClass;
    switch(menuItem.getItemId()) {
      case R.id.nav_home_fragment:
        fragmentClass = HomeFragment.class;
        break;
      case R.id.nav_wallppr_fragment:
        fragmentClass = WallpaperFragment.class;
        break;
      case R.id.led_notifications_fragment:
        fragmentClass = LedNotificationsFragment.class;
        break;
      default:
        fragmentClass = HomeFragment.class;
    }

    try {
      fragment = (AbstractFragmentWithCallback) fragmentClass.newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    //Check if the fragment needs root
    if(!fragment.getNeedsRoot() || (fragment.getNeedsRoot() && rootAvailable))
    {
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
    else
    {
      Toast.makeText(this, menuItem.getTitle() + getString(R.string.toast_mi_needs_root), Toast.LENGTH_SHORT).show();
    }

  }

  @Override
  public void onRootCheckComplete(boolean inRootAvailable)
  {
    rootAvailable = inRootAvailable;
  }

  @Override
  public void onWallpaperFragmentAction()
  {
    //nothing so far
  }

  @Override
  public void onLedNotificationFragmentAction(Uri uri)
  {
    //nothing so far

  }
}
