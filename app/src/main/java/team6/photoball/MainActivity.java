package team6.photoball;

import android.Manifest;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        Home.OnFragmentInteractionListener,
        MyPicMaps.OnFragmentInteractionListener,
        Camera.OnFragmentInteractionListener,
        Gallery.OnFragmentInteractionListener,
        Settings.OnFragmentInteractionListener,
        MyPicMapsDetail.OnFragmentInteractionListener {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 3;

    public SoundPool mSounds;
    public HashMap<Integer, Integer> mSoundIDMap;
    public boolean mSoundOn;
    public Menu mMenu = null;
    public Home mHome;
    public static Tutorial mTutorial;
    public static int mViewCounter = 0;
    public static boolean tutorialChanged = false;
    public static boolean runTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewCounter = 0;

        runTutorial = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("instruction_preference_key",true);

        createView ();

        moveToHome();

        moveToTutorial();

        getPermissions();

        createSoundPool();
    }

    protected void createView () {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        if (menu != null) {
            MenuItem item = menu.findItem(R.id.action_settings);
            item.setEnabled(true);
            ColorFilter filter = new LightingColorFilter(Color.WHITE, Color.WHITE);
            item.getIcon().setColorFilter(filter);
        }
        return true;
    }

    public void updateMenu() {
        onPrepareOptionsMenu (mMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            moveToSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_pic_maps) {
            moveMyToPicMaps();
        } else if (id == R.id.nav_updates) {
            thisShowDialog(R.string.updates_title);
        } else if (id == R.id.nav_about) {
            thisShowDialog(R.string.about_title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void moveToHome() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mHome = Home.create();
        ft.replace(R.id.the_screens, mHome);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public void moveToTutorial() {
        if(runTutorial) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (tutorialChanged) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("tutorial");
                View v = findViewById(R.id.drawer_layout);
                v.invalidate();
                createView();
                tutorialChanged = false;
                moveToHome();
                moveToTutorial();
            } else {
                mTutorial = Tutorial.create();
                ft.add(R.id.the_screens, mTutorial, "tutorial");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        }
    }

    public void moveMyToPicMaps() {
        if (mSoundOn)
            mSounds.play(mSoundIDMap.get(R.raw.click), 1, 1, 1, 0, 1);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MyPicMaps thisMyPicMaps = MyPicMaps.create();
        ft.replace(R.id.the_screens, thisMyPicMaps);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ft.addToBackStack("fragment_my_pic_maps");
        ft.commit();
    }

    public void moveToCamera() {
        if (mSoundOn)
            mSounds.play(mSoundIDMap.get(R.raw.click), 1, 1, 1, 0, 1);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Camera thisCamera = Camera.create();
        ft.replace(R.id.the_screens, thisCamera);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ft.addToBackStack("fragment_camera");
        ft.commit();
    }

    public void moveToGallery() {
        if (mSoundOn)
            mSounds.play(mSoundIDMap.get(R.raw.click), 1, 1, 1, 0, 1);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Gallery thisGallery = Gallery.create();
        ft.replace(R.id.the_screens, thisGallery);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ft.addToBackStack("fragment_gallery");
        ft.commit();
    }

    public void moveToSettings() {
        if (mSoundOn)
            mSounds.play(mSoundIDMap.get(R.raw.click), 1, 1, 1, 0, 1);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.the_screens, new Settings());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack("fragment_settings");
        ft.commit();
    }

    public void moveToMyPicMapsDetail(String viewModel) {
        if (mSoundOn)
            mSounds.play(mSoundIDMap.get(R.raw.click), 1, 1, 1, 0, 1);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MyPicMapsDetail myPicMapsDetail = MyPicMapsDetail.create(viewModel);
        ft.replace(R.id.the_screens, myPicMapsDetail);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack("fragment_my_pic_maps_detail");
        ft.commit();
    }

    void thisShowDialog(int type) {
        DialogFragment newFragment = MenuDialog.newInstance(type);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void doPositiveClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!");
    }

    public void doNegativeClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Negative click!");
    }

    private void createSoundPool()  {
        int[] soundIds = {R.raw.click};
        mSoundIDMap = new HashMap<Integer, Integer>();
        mSounds = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        for (int id : soundIds)
            mSoundIDMap.put(id, mSounds.load(this, id, 1));
    }

    public void soundOn() {
        mSoundOn = true;
    }

    public void soundOff() {
        mSoundOn = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            createSoundPool();
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            Boolean b = prefs.getBoolean("sound_preference_key", true);
            if (b)
                soundOn();
            else
                soundOff();
        } catch (Exception e) {}
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mSounds != null) {
            mSounds.release();
            mSounds = null;
            soundOff();
        }
    }

    @Override
    public void onFragmentInteractionHome(Uri uri) {
    }

    @Override
    public void onFragmentInteractionMyPicMaps(Uri uri) {
    }

    @Override
    public void onFragmentInteractionCamera(Uri uri) {
    }

    @Override
    public void onFragmentInteractionGallery(Uri uri) {
    }

    @Override
    public void onFragmentInteractionSettings(Uri uri) {
    }

    @Override
    public void onFragmentInteractionMyPicMapsDetail(Uri uri) {
    }

    private void getPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            /// Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            /// Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            /// Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }
        return;
    }

}
