package team6.photoball;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        Home.OnFragmentInteractionListener,
        MyPicMaps.OnFragmentInteractionListener,
        Camera.OnFragmentInteractionListener,
        Gallery.OnFragmentInteractionListener {

    float mAddButtonX = 0;
    float mAddButtonY = 0;
    float mCameraButtonX = 0;
    float mCameraButtonY = 0;
    float mPlayButtonX = 0;
    float mPlayButtonY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        if (savedInstanceState == null) {
            moveToHome();
        }

        final FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addButton);
        final FloatingActionButton cameraButton = (FloatingActionButton) findViewById(R.id.cameraButton);
        final FloatingActionButton playButton = (FloatingActionButton) findViewById(R.id.playButton);

        assert addButton != null;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddButtonX = addButton.getX();
                mAddButtonY = addButton.getY();
                mPlayButtonX = playButton.getX();
                mPlayButtonY = playButton.getY();
                mCameraButtonX = cameraButton.getX();
                mCameraButtonY = cameraButton.getY();
                resetFloatingButtons();
                cameraButton.setX(playButton.getX());
                playButton.setX(addButton.getX());
                moveGallery();
                addButton.setScaleX((float) 1.3);
                addButton.setScaleY((float) 1.3);
                addButton.setY(1400);
                addButton.setX(findViewById(R.id.drawer_layout).getWidth() / 2 - playButton.getMeasuredWidth() / 2);
                addButton.setClickable(false);
            }
        });

        assert cameraButton != null;
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddButtonX = addButton.getX();
                mAddButtonY = addButton.getY();
                mPlayButtonX = playButton.getX();
                mPlayButtonY = playButton.getY();
                mCameraButtonX = cameraButton.getX();
                mCameraButtonY = cameraButton.getY();
                resetFloatingButtons();
                moveCamera();
                cameraButton.setScaleX((float) 1.3);
                cameraButton.setScaleY((float) 1.3);
                cameraButton.setY(1400);
                cameraButton.setClickable(false);
            }
        });

        assert playButton != null;
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddButtonX = addButton.getX();
                mAddButtonY = addButton.getY();
                mPlayButtonX = playButton.getX();
                mPlayButtonY = playButton.getY();
                mCameraButtonX = cameraButton.getX();
                mCameraButtonY = cameraButton.getY();
                resetFloatingButtons();
                cameraButton.setX(addButton.getX());
                addButton.setX(playButton.getX());
                moveMyPicMaps();
                playButton.setScaleX((float) 1.3);
                playButton.setScaleY((float) 1.3);
                playButton.setY(1400);
                playButton.setX(findViewById(R.id.drawer_layout).getWidth() / 2 - playButton.getMeasuredWidth()/2);
                playButton.setClickable(false);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            resetFloatingButtons();
            moveToHome();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
            FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addButton);
            FloatingActionButton cameraButton = (FloatingActionButton) findViewById(R.id.cameraButton);
            FloatingActionButton playButton = (FloatingActionButton) findViewById(R.id.playButton);

            mAddButtonX = addButton.getX();
            mAddButtonY = addButton.getY();
            mPlayButtonX = playButton.getX();
            mPlayButtonY = playButton.getY();
            mCameraButtonX = cameraButton.getX();
            mCameraButtonY = cameraButton.getY();
            resetFloatingButtons();
            cameraButton.setX(addButton.getX());
            addButton.setX(playButton.getX());
            moveMyPicMaps();
            playButton.setScaleX((float) 1.3);
            playButton.setScaleY((float) 1.3);
            playButton.setY(1400);
            playButton.setX(findViewById(R.id.drawer_layout).getWidth() / 2 - playButton.getMeasuredWidth()/2);
            playButton.setClickable(false);

        } else if (id == R.id.nav_updates) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void myPicMaps(View view) {
        Intent intent = new Intent(MainActivity.this, MyPicMaps.class);
        startActivity(intent);
    }

    public void moveToHome() {
        resetFloatingButtons();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.the_screens, new Home());
        // Complete the changes added above
        ft.commit();
    }

    public void moveMyPicMaps() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        // Replace the contents of the container with the new fragment
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.the_screens, new MyPicMaps());
        if(fm.getBackStackEntryCount() == 0) {
            ft.addToBackStack("fragment_my_pic_maps");
        }
        // Complete the changes added above
        ft.commit();
    }

    public void moveCamera() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        // Replace the contents of the container with the new fragment
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.the_screens, new Camera());
        if(fm.getBackStackEntryCount() == 0) {
            ft.addToBackStack("fragment_camera");
        }
        // Complete the changes added above
        ft.commit();
    }

    public void moveGallery() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        // Replace the contents of the container with the new fragment
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.the_screens, new Gallery());
        if(fm.getBackStackEntryCount() == 0) {
            ft.addToBackStack("fragment_gallery");
        }
        // Complete the changes added above
        ft.commit();
    }

    private void resetFloatingButtons() {

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addButton);
        FloatingActionButton cameraButton = (FloatingActionButton) findViewById(R.id.cameraButton);
        FloatingActionButton playButton = (FloatingActionButton) findViewById(R.id.playButton);

        float maxY = 0;
        float minX = 1000000000;
        float maxX = 0;
        float centerX = 0;

        if (mAddButtonY > maxY) maxY = mAddButtonY;
        if (mCameraButtonY > maxY) maxY = mCameraButtonY;
        if (mPlayButtonY > maxY) maxY = mPlayButtonY;

        if (mAddButtonX > maxX) maxX = mAddButtonX;
        if (mCameraButtonX > maxX) maxX = mCameraButtonX;
        if (mPlayButtonX > maxX) maxX = mPlayButtonX;

        if (mAddButtonX < minX) minX = mAddButtonX;
        if (mCameraButtonX < minX) minX = mCameraButtonX;
        if (mPlayButtonX < minX) minX = mPlayButtonX;

        if (mAddButtonX != maxX && mAddButtonX != minX) centerX = mAddButtonX;
        if (mCameraButtonX != maxX && mCameraButtonX != minX) centerX = mCameraButtonX;
        if (mPlayButtonX != maxX && mPlayButtonX != minX) centerX = mPlayButtonX;

        if (mAddButtonX != 0) {
            addButton.setScaleX(1);
            addButton.setScaleY(1);
            addButton.setX(minX);
            addButton.setY(maxY);
            addButton.setClickable(true);

            cameraButton.setScaleX(1);
            cameraButton.setScaleY(1);
            cameraButton.setX(centerX);
            cameraButton.setY(maxY);
            cameraButton.setClickable(true);

            playButton.setScaleX(1);
            playButton.setScaleY(1);
            playButton.setX(maxX);
            playButton.setY(maxY);
            playButton.setClickable(true);
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
}
