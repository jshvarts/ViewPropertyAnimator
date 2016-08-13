package com.jshvarts.objectanimator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * https://developer.android.com/reference/android/view/ViewPropertyAnimator.html
 *
 * http://android-developers.blogspot.com/2011/05/introducing-viewpropertyanimator.html
 */
public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int BASE_DURATION_MILLIS = 2000;

    @BindView(R.id.droid_blue_imageview)
    protected ImageView droidBlue;

    @BindView(R.id.animator_button)
    protected Button animatorButton;

    private String animatorButtonOrigText;

    private enum AnimatorType {
        ROTATION,
        TRANSLATION,
        SCALE,
        ALPHA,
        COMBO
    }

    private final List<AnimatorType> animatorTypes = new ArrayList<AnimatorType>() {{
        add(AnimatorType.ROTATION);
        add(AnimatorType.TRANSLATION);
        add(AnimatorType.SCALE);
        add(AnimatorType.ALPHA);
        add(AnimatorType.COMBO);
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
    protected void handleAnimatorButtonClick() {
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
            case COMBO:
                performComboAnimations();
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

        updateButtonText("rotation");

        droidBlue.animate().setDuration(BASE_DURATION_MILLIS).rotationX(360f)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        droidBlue.animate().setDuration(BASE_DURATION_MILLIS).rotationY(360f)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        droidBlue.animate().setDuration(BASE_DURATION_MILLIS).rotation(360f)
                                                .withEndAction(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        droidBlue.animate().setDuration(BASE_DURATION_MILLIS).rotation(0f)
                                                                .withEndAction(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        processNextAnimatorType();
                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    /**
     * translationX and translationY: These properties control where the View is located as a delta
     * from its left and top coordinates which are set by its layout container.
     */
    private void performTranslatorAnimations() {
        Log.d(LOG_TAG, "running performTranslatorAnimations");

        currentAnimatorType = AnimatorType.TRANSLATION;

        updateButtonText("translation");

        droidBlue.animate().setDuration(BASE_DURATION_MILLIS).translationX(200f)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        droidBlue.animate().setDuration(BASE_DURATION_MILLIS).translationY(200f)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        droidBlue.animate().setDuration(BASE_DURATION_MILLIS).translationX(0f).translationY(0f)
                                                .withEndAction(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        processNextAnimatorType();
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    /**
     * scaleX and scaleY: These properties control the 2D scaling of a View around its pivot point.
     */
    private void performScaleAnimations() {
        Log.d(LOG_TAG, "running performScaleAnimations");

        currentAnimatorType = AnimatorType.SCALE;

        updateButtonText("scale");

        droidBlue.animate().setDuration(BASE_DURATION_MILLIS).scaleX(1.5f)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        droidBlue.animate().setDuration(BASE_DURATION_MILLIS).scaleX(1.5f)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        droidBlue.animate().setDuration(BASE_DURATION_MILLIS).scaleX(1f).scaleY(1f)
                                                .withEndAction(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        processNextAnimatorType();
                                                    }
                                                });
                                    }
                                });
                    }
                });

    }

    /**
     * alpha: Represents the alpha transparency on the View. This value is 1 (opaque) by default,
     * with a value of 0 representing full transparency (not visible).
     */
    private void performAlphaAnimations() {
        Log.d(LOG_TAG, "running performAlphaAnimations");

        currentAnimatorType = AnimatorType.ALPHA;

        updateButtonText("alpha");

        droidBlue.animate().setDuration(BASE_DURATION_MILLIS).alpha(0)
                .withEndAction(new Runnable() {
            @Override
            public void run() {
                droidBlue.animate().setDuration(BASE_DURATION_MILLIS).alpha(1)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                processNextAnimatorType();
                            }
                        });
            }
        });
    }

    private void performComboAnimations() {
        Log.d(LOG_TAG, "running performComboAnimations");

        currentAnimatorType = AnimatorType.COMBO;

        updateButtonText("combo");

        droidBlue.animate().setDuration(BASE_DURATION_MILLIS * 2)
                .translationY(50f)
                .translationY(50f)
                .scaleX(1.5f)
                .scaleY(1.5f)
                .rotation(360f)
                .alpha(0f)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        processNextAnimatorType();
                    }
                });
    }

    private void updateButtonText(String newText) {
        Preconditions.checkNotNull(newText, "newText must not be null");
        animatorButton.setText(newText);
    }
}
