package com.example.myapplication;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class QuizActivity extends AppCompatActivity {

    public static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;

    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;

    private boolean mIsCheated;

    private Question[] mQuestionsBank = new Question[]{
            new Question(R.string.question_asia, false),
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, true),
            new Question(R.string.question_americas, true),
    };

    private Boolean[] mIsAnsweredBank = new Boolean[]{
            false,
            false,
            false,
            false,
            false,
            false
    };

    private Boolean[] mAnswersBank = new Boolean[]{
            false,
            false,
            false,
            false,
            false,
            false
    };


    private int mCurrentIndex = 0;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult called");

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheated = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");

        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheated = savedInstanceState.getBoolean(KEY_INDEX, false);
        }


        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuestionToNext();
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateQuestionToNext();
            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateQuestionToPrev();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAnswerTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, isAnswerTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        updateQuestion();
    }

    private void updateQuestion() {
        int question = mQuestionsBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(Boolean userPressedTrue) {

        mIsAnsweredBank[mCurrentIndex] = true;

        mAnswersBank[mCurrentIndex] = userPressedTrue;


        showResult();

//        checkButton();

//        if (isAllAnswered()) {
//            showResult();
//        }
    }

    private void showResult() {

//        String scoreStr = getResources().getString(R.string.score);

//        String message = String.format(scoreStr, score());

        int msgId = R.string.incorrect_toast;

        if (mIsAnsweredBank[mCurrentIndex]  == mAnswersBank[mCurrentIndex] ) {
            msgId = R.string.correct_toast;
        }

        if (mIsCheated) {
            msgId = R.string.judgement_toast;
        }


        Toast.makeText(this, msgId, Toast.LENGTH_SHORT).show();
    }


    private Integer score() {
        Integer result = 0;

        for (int i = 0; i < 6; i++) {
            if (mQuestionsBank[i].isAnswerTrue() == mAnswersBank[i]) {
                result = result + 1;
            }
        }

        return result;
    }

    private Boolean isAllAnswered() {
        Boolean result = true;

        for (Boolean isAnswered : mIsAnsweredBank) {
            result = result && isAnswered;
        }

        return result;
    }


    private void checkButton() {
        if (mIsAnsweredBank[mCurrentIndex]) {
            mTrueButton.setClickable(false);
            mFalseButton.setClickable(false);
            return;
        }

        mTrueButton.setClickable(true);
        mFalseButton.setClickable(true);
    }


    private void updateQuestionToNext() {
        mIsCheated = false;
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
        updateQuestion();
        checkButton();
    }

    private void updateQuestionToPrev() {
        mCurrentIndex = (mCurrentIndex - 1);
        if (mCurrentIndex < 0) {
            mCurrentIndex = mQuestionsBank.length - 1;
        }
        updateQuestion();
        checkButton();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(KEY_INDEX, mIsCheated);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
