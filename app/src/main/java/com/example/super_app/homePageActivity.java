package com.example.super_app;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class homePageActivity extends AppCompatActivity {

    private ImageView imageView;
    private GestureDetector gestureDetector;

    private int[] images = {
            R.drawable.shufersallogo,
            R.drawable.shufesrsal1,
            R.drawable.shufesrsal2
    };
    private int currentImageIndex = 0;

    private boolean isAnimating = false;
    private float initialY = 0;
    private float previousY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        imageView = findViewById(R.id.imageViewHome);
        gestureDetector = new GestureDetector(this, new SwipeGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            initialY = e.getY();
            previousY = initialY;
            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (isAnimating) {
                return false;
            }

            float currentY = e2.getY();
            float deltaY = currentY - previousY;

            float translationY = imageView.getTranslationY() + deltaY;
            imageView.setTranslationY(translationY);

            previousY = currentY;

            float swipePercentage = getSwipePercentage();
            if (swipePercentage >= 0.5) {
                if (deltaY > 0) {
                    // Swipe down
                    switchToPreviousImageWithAnimation();
                } else {
                    // Swipe up
                    switchToNextImageWithAnimation();
                }
            }

            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;

            try {
                float diffY = e2.getY() - initialY;
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        // Swipe down
                        switchToPreviousImageWithAnimation();
                    } else {
                        // Swipe up
                        switchToNextImageWithAnimation();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return result;
        }
    }

    private void switchToNextImageWithAnimation() {
        if (isAnimating) {
            return;
        }

        isAnimating = true;

        int nextImageIndex = (currentImageIndex + 1) % images.length;
        imageView.setImageDrawable(getResources().getDrawable(images[nextImageIndex]));

        float targetTranslationY = imageView.getHeight() - (imageView.getHeight() * getSwipePercentage());
        imageView.animate()
                .translationY(targetTranslationY)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(500)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        currentImageIndex = nextImageIndex;
                        imageView.setTranslationY(0);
                        isAnimating = false;
                    }
                })
                .start();
    }

    private void switchToPreviousImageWithAnimation() {
        if (isAnimating || currentImageIndex == 0) {
            return;
        }

        isAnimating = true;

        int previousImageIndex = currentImageIndex - 1;
        imageView.setImageDrawable(getResources().getDrawable(images[previousImageIndex]));

        float targetTranslationY = -imageView.getHeight() + (imageView.getHeight() * getSwipePercentage());
        imageView.animate()
                .translationY(targetTranslationY)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(500)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        currentImageIndex = previousImageIndex;
                        imageView.setTranslationY(0);
                        isAnimating = false;
                    }
                })
                .start();
    }

    private float getSwipePercentage() {
        return Math.abs(imageView.getTranslationY()) / imageView.getHeight();
    }
}
