package com.jshvarts.objectanimator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;
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

    @BindView(R.id.droid_blue_imageview)
    protected ImageView droidBlue;

    @BindView(R.id.animator_button)
    protected Button animatorButton;

    private String animatorButtonOrigText;

    private ScheduledExecutorService scheduler;

    private enum AnimatorType {
        ROTATION,
        TRANSLATION,
        SCALE,
        ALPHA
    }

    private final List<AnimatorType> animatorTypes = new ArrayList<AnimatorType>() {{
        add(AnimatorType.ROTATION);
        add(AnimatorType.TRANSLATION);
        add(AnimatorType.SCALE);
        add(AnimatorType.ALPHA);
    }};

    private AnimatorType currentAnimatorType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        animatorButtonOrigText = animatorButton.getText().toString();
    }

    @OnClick(R.id.animator_button)
    protected void handleObjectAnimatorClick() {
        animatorButton.setEnabled(false);
        processNextAnimatorType();
    }

    private void processNextAnimatorType() {
        Preconditions.checkState((animatorTypes != null && !animatorTypes.isEmpty()), "list of animator types cannot be null or empty");

        AnimatorType animatorType;
        if (currentAnimatorType == null) {
            // run the fist animator set
            animatorType = animatorTypes.get(0);
        } else {
            try {
                // try to locate next animation to run
                animatorType = animatorTypes.get(animatorTypes.indexOf(currentAnimatorType)+1);
            } catch (IndexOutOfBoundsException e) {
                Log.d(LOG_TAG, "no more animator sets left to run");
                resetState();
                return;
            }
        }
        switch (animatorType) {
            case ROTATION:
                performRotationAnimations();
                break;
            case TRANSLATION:
                performTranslatorAnimations();
                break;
            case SCALE:
                performScaleAnimations();
                break;
            case ALPHA:
                performAlphaAnimations();
                break;
            default:
                throw new IllegalArgumentException("invalid animatorType " + animatorType);
        }
    }

    private void resetState() {
        updateButtonText(animatorButtonOrigText);
        animatorButton.setEnabled(true);
        currentAnimatorType = null;
    }

    /**
     * rotation, rotationX, and rotationY: These properties control the rotation in 2D (rotation property)
     * and 3D around the pivot point.
     */
    private void performRotationAnimations() {
        Log.d(LOG_TAG, "running performRotationAnimations");

        currentAnimatorType = AnimatorType.ROTATION;

        ObjectAnimator rotationX = ObjectAnimator.ofFloat(droidBlue, View.ROTATION_X, 0f, 360f);
        rotationX.setDuration(BASE_DURATION_MILLIS);
        rotationX.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(rotationX, droidBlue);

        ObjectAnimator rotationY = ObjectAnimator.ofFloat(droidBlue, View.ROTATION_Y, 0f, 360f);
        rotationY.setDuration(BASE_DURATION_MILLIS);
        rotationY.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(rotationY, droidBlue);

        ObjectAnimator rotation = ObjectAnimator.ofFloat(droidBlue, View.ROTATION, 0f, 360f);
        rotation.setDuration(BASE_DURATION_MILLIS);
        rotation.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(rotation, droidBlue);

        ObjectAnimator rotationBack = ObjectAnimator.ofFloat(droidBlue, View.ROTATION, 0f);
        rotationBack.setDuration(BASE_DURATION_MILLIS);
        rotationBack.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(rotationBack, droidBlue);

        final AnimatorSet sequenceAnimator = new AnimatorSet();
        sequenceAnimator.playSequentially(rotationX, rotationY, rotation, rotationBack);
        sequenceAnimator.start();

        controlNextAnimatorSetStart(sequenceAnimator);
    }

    /**
     * translationX and translationY: These properties control where the View is located as a delta
     * from its left and top coordinates which are set by its layout container.
     */
    private void performTranslatorAnimations() {
        Log.d(LOG_TAG, "running performTranslatorAnimations");

        currentAnimatorType = AnimatorType.TRANSLATION;

        ObjectAnimator translationX = ObjectAnimator.ofFloat(droidBlue, View.TRANSLATION_X, 0f, 200f);
        translationX.setDuration(BASE_DURATION_MILLIS);
        translationX.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(translationX, droidBlue);

        ObjectAnimator translationY = ObjectAnimator.ofFloat(droidBlue, View.TRANSLATION_Y, 0f, 200f);
        translationY.setDuration(BASE_DURATION_MILLIS);
        translationY.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(translationY, droidBlue);

        /* Api level 21+
        ObjectAnimator translationZ = ObjectAnimator.ofFloat(droidBlue, View.TRANSLATION_Z, 0f, 20f);
        translationZ.setDuration(BASE_DURATION_MILLIS);
        translationZ.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(translationZ, droidBlue);
        */

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f);
        ObjectAnimator backToOriginal = ObjectAnimator.ofPropertyValuesHolder(droidBlue, pvhX, pvhY);
        backToOriginal.setDuration(BASE_DURATION_MILLIS/2);
        addAnimationListener(backToOriginal, droidBlue);

        final AnimatorSet sequenceAnimator = new AnimatorSet();
        sequenceAnimator.playSequentially(translationX, translationY, backToOriginal);
        sequenceAnimator.start();

        controlNextAnimatorSetStart(sequenceAnimator);
    }

    /**
     * scaleX and scaleY: These properties control the 2D scaling of a View around its pivot point.
     */
    private void performScaleAnimations() {
        Log.d(LOG_TAG, "running performScaleAnimations");

        currentAnimatorType = AnimatorType.SCALE;

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(droidBlue, View.SCALE_X, 1f, 1.5f);
        scaleX.setDuration(BASE_DURATION_MILLIS);
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(scaleX, droidBlue);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(droidBlue, View.SCALE_Y, 1f, 1.5f);
        scaleY.setDuration(BASE_DURATION_MILLIS);
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());
        addAnimationListener(scaleY, droidBlue);

        PropertyValuesHolder scaleSmallerX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.5f);
        PropertyValuesHolder scaleSmallerY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f);
        ObjectAnimator scaleSmaller = ObjectAnimator.ofPropertyValuesHolder(droidBlue, scaleSmallerX, scaleSmallerY);
        scaleSmaller.setDuration(BASE_DURATION_MILLIS);
        addAnimationListener(scaleSmaller, droidBlue);

        PropertyValuesHolder scaleOrigX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f);
        PropertyValuesHolder scaleOrigY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f);
        ObjectAnimator backToOriginal = ObjectAnimator.ofPropertyValuesHolder(droidBlue, scaleOrigX, scaleOrigY);
        backToOriginal.setDuration(BASE_DURATION_MILLIS);
        addAnimationListener(backToOriginal, droidBlue);

        final AnimatorSet sequenceAnimator = new AnimatorSet();
        sequenceAnimator.playSequentially(scaleX, scaleY, scaleSmaller, backToOriginal);
        sequenceAnimator.start();

        controlNextAnimatorSetStart(sequenceAnimator);
    }

    /**
     * alpha: Represents the alpha transparency on the View. This value is 1 (opaque) by default,
     * with a value of 0 representing full transparency (not visible).
     */
    private void performAlphaAnimations() {
        Log.d(LOG_TAG, "running performAlphaAnimations");

        currentAnimatorType = AnimatorType.ALPHA;

        ObjectAnimator alphaToTransparent = ObjectAnimator.ofFloat(droidBlue, View.ALPHA, 0f);
        alphaToTransparent.setDuration(BASE_DURATION_MILLIS);
        addAnimationListener(alphaToTransparent, droidBlue);

        ObjectAnimator alphaToOpaque = ObjectAnimator.ofFloat(droidBlue, View.ALPHA, 1f);
        alphaToOpaque.setDuration(BASE_DURATION_MILLIS);
        addAnimationListener(alphaToOpaque, droidBlue);

        final AnimatorSet sequenceAnimator = new AnimatorSet();
        sequenceAnimator.playSequentially(alphaToTransparent, alphaToOpaque);
        sequenceAnimator.start();

        controlNextAnimatorSetStart(sequenceAnimator);
    }

    /**
     * x and y: These are simple utility properties to describe the final location of the View
     * in its container, as a sum of the left and top values and translationX and translationY values.
     */
    private void performXYAnimations() {
        Log.d(LOG_TAG, "running performXYAnimations");

        Log.d(LOG_TAG, "my X before: " + droidBlue.getX());
        Log.d(LOG_TAG, "my Y before: " + droidBlue.getY());
        Log.d(LOG_TAG, "my left before: " + droidBlue.getLeft());
        Log.d(LOG_TAG, "my top before: " + droidBlue.getTop());
        Log.d(LOG_TAG, "my translationX before: " + droidBlue.getTranslationX());
        Log.d(LOG_TAG, "my translationY before: " + droidBlue.getTranslationY());

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.X, 100f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.Y, 100f);
        ObjectAnimator move = ObjectAnimator.ofPropertyValuesHolder(droidBlue, pvhX, pvhY);
        move.setDuration(BASE_DURATION_MILLIS);
        addAnimationListener(move, droidBlue);

        // Note: the X and Y locations do not change until animation completes
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "my X after: " + droidBlue.getX());
                Log.d(LOG_TAG, "my Y after: " + droidBlue.getY());
                Log.d(LOG_TAG, "my left after: " + droidBlue.getLeft());
                Log.d(LOG_TAG, "my top after: " + droidBlue.getTop());
                Log.d(LOG_TAG, "my translationX after: " + droidBlue.getTranslationX());
                Log.d(LOG_TAG, "my translationY after: " + droidBlue.getTranslationY());
            }
        }, BASE_DURATION_MILLIS * 2);

        //ObjectAnimator.ofPropertyValuesHolder(myView, pvhX, pvyY).start();

        final AnimatorSet sequenceAnimator = new AnimatorSet();
        sequenceAnimator.playSequentially(move);
        sequenceAnimator.start();

        //controlNextAnimatorSetStart(sequenceAnimator);
    }

    private void controlNextAnimatorSetStart(final AnimatorSet currentAnimatorSet) {
        Preconditions.checkNotNull(currentAnimatorSet, "currentAnimationSet must not be null");
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (!currentAnimatorSet.isStarted()) {
                    scheduler.shutdownNow();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            processNextAnimatorType();
                        }
                    });
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void addAnimationListener(Animator animator, final View view) {
        Preconditions.checkNotNull(animator, "animator must not be null");
        Preconditions.checkNotNull(view, "view must not be null");
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.clearAnimation();
                updateButtonText(((ObjectAnimator) animation).getPropertyName());
            }
        });
    }

    private void updateButtonText(String newText) {
        Preconditions.checkNotNull(newText, "newText must not be null");
        animatorButton.setText(newText);
    }
}
