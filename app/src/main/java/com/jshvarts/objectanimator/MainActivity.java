package com.jshvarts.objectanimator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int BASE_DURATION = 1500;

    @BindView(R.id.droid_blue)
    protected ImageView droidBlue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.object_animator)
    protected void handleObjectAnimatorClick(Button button) {
        //Toast.makeText(MainActivity.this, button.getText(), Toast.LENGTH_SHORT).show();
        // TODO AnimatorInflater.loadAnimator does not work
        //ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.object_animator);
        //anim.setTarget(button);
        //anim.start();

        /*
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("rotationY", 0.0f, 360f);
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("rotationX", 0.0f, 360f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(droidBlue, pvhY, pvhX);
        animator.setDuration(BASE_DURATION * 2);
        animator.start();
        */

        ObjectAnimator rotationX = ObjectAnimator.ofFloat(droidBlue, "rotationX", 0.0f, 360f);
        rotationX.setDuration(BASE_DURATION);
        rotationX.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator rotationY = ObjectAnimator.ofFloat(droidBlue, "rotationY", 0.0f, 360f);
        rotationY.setDuration(BASE_DURATION);
        rotationY.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet sequenceAnimator = new AnimatorSet();
        sequenceAnimator.playSequentially(rotationX, rotationY);
        sequenceAnimator.start();
    }
}
