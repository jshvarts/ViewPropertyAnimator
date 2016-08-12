package com.jshvarts.objectanimator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.google.common.base.Preconditions;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int BASE_DURATION_MILLIS = 2000;

    private static final int ANIMATOR_SET_ROTATION = 1;
    private static final int ANIMATOR_SET_TRANSLATION = 2;

    @BindView(R.id.droid_blue_imageview)
    protected ImageView droidBlue;

    @BindView(R.id.animator_button)
    protected Button animatorButton;

    private String animatorButtonOrigText;

    private ScheduledExecutorService scheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        animatorButtonOrigText = animatorButton.getText().toString();
    }

    @OnClick(R.id.animator_button)
    protected void handleObjectAnimatorClick() {
        performAnimatorSet(ANIMATOR_SET_ROTATION);
    }

    private void performAnimatorSet(final int animatorSetId) {
        switch (animatorSetId) {
            case ANIMATOR_SET_ROTATION:
                performRotationAnimations();
                break;
            case ANIMATOR_SET_TRANSLATION:
                performTranslatorAnimations();
                break;
            default:
                throw new IllegalArgumentException("invalid animatorSetId " + animatorSetId);
        }
    }


    private void performNextAnimatorSet(final int animatorSetId) {
        switch (animatorSetId) {
            case ANIMATOR_SET_ROTATION:
                performTranslatorAnimations();
                break;
            case ANIMATOR_SET_TRANSLATION:
                break;
            default:
                throw new IllegalArgumentException("invalid animatorSetId " + animatorSetId);
        }
    }

    private void performRotationAnimations() {
        Log.d(LOG_TAG, "running performRotationAnimations");

        /* START ROTATION ANIMATION */
        ObjectAnimator rotationX = ObjectAnimator.ofFloat(droidBlue, "rotationX", 0.0f, 360f);
        rotationX.setDuration(BASE_DURATION_MILLIS);
        rotationX.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(rotationX, droidBlue);

        ObjectAnimator rotationY = ObjectAnimator.ofFloat(droidBlue, "rotationY", 0.0f, 360f);
        rotationY.setDuration(BASE_DURATION_MILLIS);
        rotationY.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(rotationY, droidBlue);

        ObjectAnimator rotation = ObjectAnimator.ofFloat(droidBlue, "rotation", 0.0f, 360f);
        rotation.setDuration(BASE_DURATION_MILLIS);
        rotation.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(rotation, droidBlue);

        final AnimatorSet rotationSequenceAnimator = new AnimatorSet();
        rotationSequenceAnimator.playSequentially(rotationX, rotationY, rotation);
        rotationSequenceAnimator.start();
        /* END ROTATION ANIMATION */

        controlNextAnimationSetStart(rotationSequenceAnimator, ANIMATOR_SET_ROTATION);
    }

    private void performTranslatorAnimations() {
        Log.d(LOG_TAG, "running performTranslatorAnimations");

        /* START TRANSLATION ANIMATION */
        ObjectAnimator translationX = ObjectAnimator.ofFloat(droidBlue, "translationX", 0.0f, 200f);
        translationX.setDuration(BASE_DURATION_MILLIS);
        translationX.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(translationX, droidBlue);

        ObjectAnimator translationY = ObjectAnimator.ofFloat(droidBlue, "translationY", 0.0f, 200f);
        translationY.setDuration(BASE_DURATION_MILLIS);
        translationY.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(translationY, droidBlue);

        // move the image back to its original position
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("translationX", 0.0f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY", 0.0f);
        ObjectAnimator translationToOrigPosition = ObjectAnimator.ofPropertyValuesHolder(droidBlue, pvhX, pvhY);
        translationToOrigPosition.setDuration(BASE_DURATION_MILLIS/2);
        addAnimationListener(translationToOrigPosition, droidBlue);

        final AnimatorSet translationSequenceAnimator = new AnimatorSet();
        translationSequenceAnimator.playSequentially(translationX, translationY, translationToOrigPosition);
        translationSequenceAnimator.start();
        /* END TRANSLATION ANIMATION */

        controlNextAnimationSetStart(translationSequenceAnimator, ANIMATOR_SET_TRANSLATION);
    }

    private void controlNextAnimationSetStart(final AnimatorSet currentAnimationSet, final int currentAnimatorSetId) {
        Preconditions.checkNotNull(currentAnimationSet, "currentAnimationSet must not be null");
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (!currentAnimationSet.isStarted()) {
                    scheduler.shutdownNow();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            performNextAnimatorSet(currentAnimatorSetId);
                        }
                    });
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void addAnimationListener(Animator animator, final View view) {
        Preconditions.checkNotNull(animator, "animator must not be null");
        Preconditions.checkNotNull(view, "view must not be null");
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.clearAnimation();
                updateButtonText(((ObjectAnimator) animation).getPropertyName());
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                updateButtonText(animatorButtonOrigText);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void updateButtonText(String newText) {
        Preconditions.checkNotNull(newText, "newText must not be null");
        Log.d(LOG_TAG, "Updating button text to " + newText);
        animatorButton.setText(newText);
    }
}
