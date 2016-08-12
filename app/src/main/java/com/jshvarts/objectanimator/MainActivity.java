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

/**
 * https://developer.android.com/guide/topics/graphics/prop-animation.html
 */
public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int BASE_DURATION_MILLIS = 2000;

    private static final int ANIMATOR_SET_ROTATION = 1;
    private static final int ANIMATOR_SET_TRANSLATION = 2;
    private static final int ANIMATOR_SET_SCALE = 3;
    private static final int ANIMATOR_SET_ALPHA = 4;

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
        // set start animator set
        performAnimatorSet(ANIMATOR_SET_ALPHA);
    }

    private void performAnimatorSet(final int animatorSetId) {
        switch (animatorSetId) {
            case ANIMATOR_SET_ROTATION:
                performRotationAnimations();
                break;
            case ANIMATOR_SET_TRANSLATION:
                performTranslatorAnimations();
                break;
            case ANIMATOR_SET_SCALE:
                performScaleAnimations();
                break;
            case ANIMATOR_SET_ALPHA:
                performAlphaAnimations();
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
                performScaleAnimations();
                break;
            case ANIMATOR_SET_SCALE:
                performAlphaAnimations();
                break;
            case ANIMATOR_SET_ALPHA:
                break;
            default:
                throw new IllegalArgumentException("invalid animatorSetId " + animatorSetId);
        }
    }

    /**
     * rotation, rotationX, and rotationY: These properties control the rotation in 2D (rotation property)
     * and 3D around the pivot point.
     */
    private void performRotationAnimations() {
        Log.d(LOG_TAG, "running performRotationAnimations");

        ObjectAnimator rotationX = ObjectAnimator.ofFloat(droidBlue, View.ROTATION_X, 0.0f, 360f);
        rotationX.setDuration(BASE_DURATION_MILLIS);
        rotationX.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(rotationX, droidBlue);

        ObjectAnimator rotationY = ObjectAnimator.ofFloat(droidBlue, View.ROTATION_Y, 0.0f, 360f);
        rotationY.setDuration(BASE_DURATION_MILLIS);
        rotationY.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(rotationY, droidBlue);

        ObjectAnimator rotation = ObjectAnimator.ofFloat(droidBlue, View.ROTATION, 0.0f, 360f);
        rotation.setDuration(BASE_DURATION_MILLIS);
        rotation.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(rotation, droidBlue);

        ObjectAnimator rotationBack = ObjectAnimator.ofFloat(droidBlue, View.ROTATION, 0.0f);
        rotationBack.setDuration(BASE_DURATION_MILLIS);
        rotationBack.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(rotationBack, droidBlue);

        final AnimatorSet sequenceAnimator = new AnimatorSet();
        sequenceAnimator.playSequentially(rotationX, rotationY, rotation, rotationBack);
        sequenceAnimator.start();

        controlNextAnimationSetStart(sequenceAnimator, ANIMATOR_SET_ROTATION);
    }

    /**
     * translationX and translationY: These properties control where the View is located as a delta
     * from its left and top coordinates which are set by its layout container.
     */
    private void performTranslatorAnimations() {
        Log.d(LOG_TAG, "running performTranslatorAnimations");

        ObjectAnimator translationX = ObjectAnimator.ofFloat(droidBlue, View.TRANSLATION_X, 0.0f, 200f);
        translationX.setDuration(BASE_DURATION_MILLIS);
        translationX.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(translationX, droidBlue);

        ObjectAnimator translationY = ObjectAnimator.ofFloat(droidBlue, View.TRANSLATION_Y, 0.0f, 200f);
        translationY.setDuration(BASE_DURATION_MILLIS);
        translationY.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(translationY, droidBlue);

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0.0f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0.0f);
        ObjectAnimator backToOriginal = ObjectAnimator.ofPropertyValuesHolder(droidBlue, pvhX, pvhY);
        backToOriginal.setDuration(BASE_DURATION_MILLIS/2);
        addAnimationListener(backToOriginal, droidBlue);

        final AnimatorSet sequenceAnimator = new AnimatorSet();
        sequenceAnimator.playSequentially(translationX, translationY, backToOriginal);
        sequenceAnimator.start();

        controlNextAnimationSetStart(sequenceAnimator, ANIMATOR_SET_TRANSLATION);
    }

    /**
     * scaleX and scaleY: These properties control the 2D scaling of a View around its pivot point.
     */
    private void performScaleAnimations() {
        Log.d(LOG_TAG, "running performScaleAnimations");

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(droidBlue, View.SCALE_X, 1.0f, 1.5f);
        scaleX.setDuration(BASE_DURATION_MILLIS);
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(scaleX, droidBlue);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(droidBlue, View.SCALE_Y, 1.0f, 1.5f);
        scaleY.setDuration(BASE_DURATION_MILLIS);
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(scaleY, droidBlue);

        PropertyValuesHolder scaleSmallerX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.5f);
        PropertyValuesHolder scaleSmallerY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f);
        ObjectAnimator scaleSmaller = ObjectAnimator.ofPropertyValuesHolder(droidBlue, scaleSmallerX, scaleSmallerY);
        scaleSmaller.setDuration(BASE_DURATION_MILLIS);
        addAnimationListener(scaleSmaller, droidBlue);

        PropertyValuesHolder scaleOrigX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f);
        PropertyValuesHolder scaleOrigY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f);
        ObjectAnimator backToOriginal = ObjectAnimator.ofPropertyValuesHolder(droidBlue, scaleOrigX, scaleOrigY);
        backToOriginal.setDuration(BASE_DURATION_MILLIS);
        addAnimationListener(backToOriginal, droidBlue);

        final AnimatorSet sequenceAnimator = new AnimatorSet();
        sequenceAnimator.playSequentially(scaleX, scaleY, scaleSmaller, backToOriginal);
        sequenceAnimator.start();

        controlNextAnimationSetStart(sequenceAnimator, ANIMATOR_SET_SCALE);
    }

    /**
     * alpha: Represents the alpha transparency on the View. This value is 1 (opaque) by default,
     * with a value of 0 representing full transparency (not visible).
     */
    private void performAlphaAnimations() {
        Log.d(LOG_TAG, "running performAlphaAnimations");

        ObjectAnimator alphaToTransparent = ObjectAnimator.ofFloat(droidBlue, View.ALPHA, 0.0f);
        alphaToTransparent.setDuration(BASE_DURATION_MILLIS);
        addAnimationListener(alphaToTransparent, droidBlue);

        ObjectAnimator alphaToOpaque = ObjectAnimator.ofFloat(droidBlue, View.ALPHA, 1.0f);
        alphaToOpaque.setDuration(BASE_DURATION_MILLIS);
        addAnimationListener(alphaToOpaque, droidBlue);

        final AnimatorSet sequenceAnimator = new AnimatorSet();
        sequenceAnimator.playSequentially(alphaToTransparent, alphaToOpaque);
        sequenceAnimator.start();

        controlNextAnimationSetStart(sequenceAnimator, ANIMATOR_SET_ALPHA);
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
        animatorButton.setText(newText);
    }
}
