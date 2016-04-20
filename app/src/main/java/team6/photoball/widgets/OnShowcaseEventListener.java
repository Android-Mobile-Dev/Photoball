package team6.photoball.widgets;

import android.view.MotionEvent;

/**
* @author Alex
*/
public interface OnShowcaseEventListener {

    /**
     * Called when the ShowcaseView has been told to hide. Use {@link #onShowcaseViewDidHide(ShowcaseView)}
     * if you want to know when the ShowcaseView has been fully hidden.
     */
    void onShowcaseViewHide(ShowcaseView showcaseView);

    /**
     * Called when the animation hiding the ShowcaseView has finished, and it is no longer visible on the screen.
     */
    void onShowcaseViewDidHide(ShowcaseView showcaseView);

    /**
     * Called when the ShowcaseView is shown.
     */
    void onShowcaseViewShow(ShowcaseView showcaseView);

    /**
     * Called when the user has touched on the ShowcaseView, but the touch was blocked
     * @param motionEvent the blocked event
     */
    void onShowcaseViewTouchBlocked(MotionEvent motionEvent);

    /**
     * Empty implementation of OnShowcaseViewEventListener such that null
     * checks aren't needed
     */
    OnShowcaseEventListener NONE = new OnShowcaseEventListener() {
        @Override
        public void onShowcaseViewHide(ShowcaseView showcaseView) {

        }

        @Override
        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

        }

        @Override
        public void onShowcaseViewShow(ShowcaseView showcaseView) {

        }

        @Override
        public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

        }
    };
}
