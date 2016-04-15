package com.surveyapp.Activities;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.surveyapp.Constants;
import com.surveyapp.CustomObjects.User;
import com.surveyapp.Fragments.FragmentCreateNewSurvey;
import com.surveyapp.Fragments.FragmentMySurvey;
import com.surveyapp.Fragments.UserAccountRelated.FragmentUserAccountShow;
import com.surveyapp.R;
import com.surveyapp.SharedPrefUtil;
import com.surveyapp.Utils;

public class LandingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private NavigationView navigationDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private int drawerItemSelected;
    private SharedPrefUtil sharedPrefUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPrefUtil = new SharedPrefUtil(LandingActivity.this);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        navigationDrawer = (NavigationView) findViewById(R.id.navigationView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(LandingActivity.this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);

        drawerItemSelected = savedInstanceState == null ? R.id.navHome : savedInstanceState.getInt(getString(R.string.nav_selected_id));

        drawerLayout.setDrawerListener(drawerToggle);
        navigationDrawer.setNavigationItemSelectedListener(this);

        this.setUserDetailsInDrawer();

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

                drawerLayout.closeDrawer(GravityCompat.START);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new FragmentCreateNewSurvey())

                        .commit();
                break;
            case R.id.navAbout:

                new MaterialDialog.Builder(LandingActivity.this)
                        .cancelable(true)
                        .title("About Participate Me")
                        .content("We Are The Leading Survey Taking Organisation...")
                        .show();

                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.navFeedBack:
                drawerLayout.closeDrawer(GravityCompat.START);
                new MaterialDialog.Builder(LandingActivity.this)
                        .content("Enter Your Feedback")
                        .input(null, null, false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                String feedback = input.toString();
                                new SubmitFeedBackExecutor(feedback).execute();
                            }
                        })
                        .show();
                break;
            case R.id.navMySurvey:

                drawerLayout.closeDrawer(GravityCompat.START);
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.TAG_FRAGMENT_MY_SURVEY);
                if (fragment!=null && fragment.isVisible()){
                    return;

                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new FragmentMySurvey(),Constants.TAG_FRAGMENT_MY_SURVEY)
                        .addToBackStack(null)
                        .commit();
                break;
           /* case R.id.navUpgrade:
                break;*/
            case R.id.navHelp:
                drawerLayout.closeDrawer(GravityCompat.START);

                new MaterialDialog.Builder(LandingActivity.this)
                        .title("Need Help !!")
                        .content("In  Case You Need Any Help You Can Visit Our website www.participateme.com")
                        .show();
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
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

    private void setUserDetailsInDrawer(){
        View headerView = navigationDrawer.getHeaderView(0);

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavigationHeaderClicked();
            }
        });

        TextView userName = (TextView) headerView.findViewById(R.id.navHeaderUserName);
        TextView userEmail = (TextView) headerView.findViewById(R.id.navHeaderUserEmail);
        TextView userPlan = (TextView) headerView.findViewById(R.id.navHeaderUserPlan);

        User user = sharedPrefUtil.getUserInfo();

        userName.setText(user.getName());
        userEmail.setText(user.getEmail());
        userPlan.setText(user.getPlan());
    }

    private void onNavigationHeaderClicked(){

        drawerLayout.closeDrawer(GravityCompat.START);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new FragmentUserAccountShow(), Constants.TAG_FRAGMENT_USER_ACCOUNT)
                .addToBackStack(null)
                .commit();
    }

    protected class SubmitFeedBackExecutor extends AsyncTask<String,Void,Void>{

        MaterialDialog progressDialog;

        String feedback;

        public SubmitFeedBackExecutor(String feedback) {

            this.feedback=feedback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new MaterialDialog.Builder(LandingActivity.this)
                    .progress(true,100)
                    .content("Submitting Your Feedback")
                    .build();

            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            String requestUrl = "";          //Here The Url Will be Added after Service Will be Created And Also The further Code

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog.cancel();

            new MaterialDialog.Builder(LandingActivity.this)
                    .content("Your FeedBack Is Stored")
                    .positiveText("OK")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            dialog.cancel();
                        }
                    })
                    .show();
        }
    }


    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.TAG_FRAGMENT_USER_ACCOUNT);

        if (fragment!=null && fragment.isVisible()){
            setTitle("Home");
        }

        super.onBackPressed();


    }
}
