package team6.photoball;

import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.amlcurran.showcaseview.targets.Target;

import team6.photoball.widgets.ShowcaseView;


public class Tutorial extends Fragment {

    public static Tutorial create() {
        Tutorial fragment = new Tutorial();
        return fragment;
    }
    public Tutorial() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        appTutorial();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        MainActivity.tutorialChanged = true;
        ((MainActivity)getActivity()).moveToTutorial();
    }

    public void appTutorial () {
        ShowcaseView.Builder v0;

        if (MainActivity.mViewCounter == 0) {

            Target homeTarget = new Target() {
                @Override
                public Point getPoint() {
                    Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
                    int actionBarSize = toolbar.getHeight();
                    int x = 40;
                    int y = actionBarSize / 2;
                    return new Point(x, y);
                }
            };

            ShowcaseView.viewCounter = 0;
            v0 = new ShowcaseView.Builder(getActivity());
            v0.blockAllTouches();
            v0.setContentTitle("Navigation Drawer");
            v0.setContentText("Tap Here to See Your Saved PicMaps, Make Updates and Info.");
            v0.setTarget(homeTarget);
            v0.setStyle(R.style.CustomShowcaseTheme);
            v0.build();
        }

        if (MainActivity.mViewCounter == 1) {
            Target settingsTarget = new Target() {
                @Override
                public Point getPoint() {
                    Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
                    int actionBarHeight = toolbar.getHeight();
                    int actionBarWidth = toolbar.getWidth();
                    int x = actionBarWidth - 40;
                    int y = actionBarHeight / 2;
                    return new Point(x, y);
                }
            };

            ShowcaseView.viewCounter = 1;
            v0 = new ShowcaseView.Builder(getActivity());
            v0.blockAllTouches();
            v0.setContentTitle("Settings");
            v0.setContentText("Tap Here to Change App Settings or Reset to Default.");
            v0.setTarget(settingsTarget);
            v0.setStyle(R.style.CustomShowcaseTheme1);
            v0.build();
        }

        if (MainActivity.mViewCounter == 2) {
            Target addButtonTarget = new Target() {
                @Override
                public Point getPoint() {
                    FloatingActionButton addButton = (FloatingActionButton) ((MainActivity)getActivity()).mHome.getView().findViewById(R.id.addButton);
                    View content = getActivity().findViewById(R.id.content);
                    int addButtonX = (int) (addButton.getX() + 80);
                    int addButtonY = content.getHeight();
                    int x = addButtonX;
                    int y = addButtonY;
                    return new Point(x, y);
                }
            };

            ShowcaseView.viewCounter = 2;
            v0 = new ShowcaseView.Builder(getActivity());
            v0.blockAllTouches();
            v0.setContentTitle("Add From Gallery");
            v0.setContentText("Tap Here to Add a PicMap from your Device Gallery and Start the Simulation.");
            v0.setTarget(addButtonTarget);
            v0.setStyle(R.style.CustomShowcaseTheme2);
            v0.build();
        }

        if (MainActivity.mViewCounter == 3) {
            Target addButtonTarget = new Target() {
                @Override
                public Point getPoint() {
                    View content = getActivity().findViewById(R.id.content);
                    int cameraButtonX = content.getWidth() / 2;
                    int cameraButtonY = content.getHeight();
                    int x = cameraButtonX;
                    int y = cameraButtonY;
                    return new Point(x, y);
                }
            };

            ShowcaseView.viewCounter = 3;
            v0 = new ShowcaseView.Builder(getActivity());
            v0.blockAllTouches();
            v0.setContentTitle("Add From Camera");
            v0.setContentText("Tap Here to Add a PicMap from your Device Camera and Start the Simulation.");
            v0.setTarget(addButtonTarget);
            v0.setStyle(R.style.CustomShowcaseTheme3);
            v0.build();
        }

        if (MainActivity.mViewCounter == 4) {
            Target addButtonTarget = new Target() {
                @Override
                public Point getPoint() {
                    FloatingActionButton playButton = (FloatingActionButton) ((MainActivity)getActivity()).mHome.getView().findViewById(R.id.playButton);
                    View content = getActivity().findViewById(R.id.content);
                    int playButtonX = (int) (playButton.getX() + 80);
                    int playButtonY = content.getHeight();
                    int x = playButtonX;
                    int y = playButtonY;
                    return new Point(x, y);
                }
            };

            ShowcaseView.viewCounter = 4;
            v0 = new ShowcaseView.Builder(getActivity());
            v0.blockAllTouches();
            v0.setContentTitle("Your Picture Maps");
            v0.setContentText("Tap Here to See a List of your Saved Picture Maps and Start the Simulation.");
            v0.setTarget(addButtonTarget);
            v0.setStyle(R.style.CustomShowcaseTheme4);
            v0.build();
        }
    }

}
