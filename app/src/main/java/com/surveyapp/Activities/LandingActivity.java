package com.surveyapp.Activities;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.surveyapp.Fragments.FragmentCreateNewSurvey;
import com.surveyapp.R;

public class LandingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private NavigationView navigationDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private int drawerItemSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        navigationDrawer = (NavigationView) findViewById(R.id.navigationView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(LandingActivity.this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);

        drawerItemSelected = savedInstanceState == null ? R.id.navHome : savedInstanceState.getInt(getString(R.string.nav_selected_id));

        drawerLayout.setDrawerListener(drawerToggle);
        navigationDrawer.setNavigationItemSelectedListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,new FragmentCreateNewSurvey())
                .commit();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        drawerItemSelected = item.getItemId();
        doCorrespondingEvent(drawerItemSelected);
        return true;
    }

    private void doCorrespondingEvent(int itemId){

        //Here The Click Of Navigation Drawer Items Will Be Handled
        switch (itemId){
            case R.id.navHome:
                break;
            case R.id.navAbout:
                break;
            case R.id.navFeedBack:
                break;
            case R.id.navMySurvey:
                break;
            case R.id.navUpgrade:
                break;
            case R.id.navHelp:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_landing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(getString(R.string.nav_selected_id), drawerItemSelected);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


}
