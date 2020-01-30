package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String KEY_CHEAT = "cheat";

    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.example.myapplication.geoquiz.answer_is_true";

    private static final String EXTRA_ANSWER_SHOWN =
            "com.example.myapplication.geoquiz.answer_shown";

    private boolean mAnswerIsTrue;
    private boolean isShownResult;
    private TextView mAnswerTextView;
    private TextView mVersionTextView;
    private Button mAnswerButton;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_CHEAT, isShownResult);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            isShownResult = savedInstanceState.getBoolean(KEY_CHEAT, false);
            Log.d(QuizActivity.TAG, "onCreate() called:" + isShownResult);
        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        String appVersion = "API LEVEL ";
        appVersion = appVersion.concat(String.valueOf(Build.VERSION_CODES.LOLLIPOP));

        mVersionTextView = (TextView) findViewById(R.id.version_text_view);
        mVersionTextView.setText(appVersion);

        mAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                isShownResult = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mAnswerButton.getWidth() / 2;
                    int cy = mAnswerButton.getHeight() / 2;
                    float radius = mAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils
                            .createCircularReveal(mAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();

                } else {
                    mAnswerButton.setVisibility(View.INVISIBLE);
                }


            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isShownResult);
        setResult(RESULT_OK, data);

        super.onBackPressed();
    }


    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }
}
