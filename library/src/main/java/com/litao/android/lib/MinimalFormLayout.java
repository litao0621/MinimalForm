package com.litao.android.lib;
/*
 * Copyright 2014 litao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class MinimalFormLayout extends RelativeLayout implements TextWatcher ,Animator.AnimatorListener{

    private static final long ANIMATION_DURATION = 500;
    private static final int LABLE_SUCCESS_ID = android.R.id.text1;

    private EditText mEditText;
    private TextView mLabelTitle;
    private TextView mTextViewSuccess;
    private TextView mTextPage;
    private TextView mTextValidator;
    private ProgressBar mProgressBar;
    private ImageButton mImageButton;

    private int currentStep = 0;
    private int progressnumber = 0;
    private int currentnumber = 0;
    private int datasize;

    private List<String> titles = null;
    private List<Integer> inputTypes = null;
    private List<String> regular = null;
    private List<String> errorMsg = null;
    private String[] contents;

    private SubmitListener mSubmitListener;

    public MinimalFormLayout(Context context) {
        this(context, null);
    }

    public MinimalFormLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MinimalFormLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mTextViewSuccess = new TextView(context);
        RelativeLayout.LayoutParams successParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        successParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        mTextViewSuccess.setId(LABLE_SUCCESS_ID);
        addView(mTextViewSuccess, successParams);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof EditText) {
            mEditText = (EditText) child;
            mEditText.addTextChangedListener(this);
        } else if (child instanceof ProgressBar) {
            mProgressBar = (ProgressBar) child;
        } else if (child instanceof ImageButton) {
            mImageButton = (ImageButton) child;
            mImageButton.setVisibility(INVISIBLE);
            mImageButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    next();
                }
            });

        }
        if (child.getTag() != null && child.getTag().equals("page")) {
            mTextPage = (TextView) child;
        } else if (child.getTag() != null && child.getTag().equals("title")) {
            mLabelTitle = (TextView) child;
            mTextViewSuccess.setTextColor(mLabelTitle.getTextColors());
            mTextViewSuccess.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLabelTitle.getTextSize());
        } else if (child.getTag() != null && child.getTag().equals("validator")) {
            mTextValidator = (TextView) child;
        }


        super.addView(child, index, params);
    }

    public void build(List<String> titles) {
        build(titles, null);
    }

    public void build(List<String> titles, List<Integer> inputTypes) {
        build(titles, inputTypes, null, null);
    }

    public void build(List<String> titles, List<Integer> inputTypes, List<String> regular, List<String> errorMsg) {
        this.titles = titles;
        mLabelTitle.setText(this.titles.get(0));
        datasize = titles.size();
        contents = new String[datasize];
        progressnumber = 100 / datasize;
        mTextPage.setText((currentStep + 1) + "/" + datasize);
        mEditText.setText("");
        mTextValidator.setText("");
        if (inputTypes != null) {
            this.inputTypes = inputTypes;
            mEditText.setInputType(inputTypes.get(0));
        }
        if (regular != null) {
            this.regular = regular;
        }
        if (errorMsg != null) {
            this.errorMsg = errorMsg;
        }
    }

    public void next() {
        if (regular!=null&&regular.get(currentStep) != null) {
            Pattern p = Pattern.compile(regular.get(currentStep));
            if (!p.matcher(mEditText.getText().toString()).matches()) {
                mTextValidator.setText(errorMsg.get(currentStep));
                ObjectAnimator.ofFloat(mTextValidator, "translationX", 0, 5f,0,4f,0,3f,2f,0).setDuration(ANIMATION_DURATION).start();
                return;
            } else {
                mTextValidator.setText("");
            }
        }

        if (currentStep < titles.size() - 1) {
            contents[currentStep] = mEditText.getText().toString();
            resetLayout(titles.get(++currentStep));
            animNext();
            currentnumber = currentStep * progressnumber;
        } else if (currentStep == titles.size() - 1) {
            contents[currentStep] = mEditText.getText().toString();
            currentStep++;
            animNext();
            currentStep--;
        }
    }

    public void previous() {
        if (currentStep > 0) {
            contents[currentStep] = mEditText.getText().toString();
            resetLayout(titles.get(--currentStep));
            animPrevious();
            currentnumber = currentStep * progressnumber;
        }
    }

    public void toStep(int step){
        step=step-1;
        if(step<=titles.size()){
            int jump=Math.abs(currentStep-step);
            while (jump-->0){
                Log.e("22","jump:"+jump+"  current:"+currentStep);
                boolean flag=currentStep>step;
                if (flag){
                    previous();
                }else {
                    next();
                }
            }
        }
    }

    public void setSuccessMsg(String msg){
        mTextViewSuccess.setText(msg);
    }
    public void setOnSubmitListener(SubmitListener submitListener){
        this.mSubmitListener=submitListener;
    }


    private void success() {
        if (TextUtils.isEmpty(mTextViewSuccess.getText())) {
            setSuccessMsg("Thank you! We'll be in touch.");
        }
            for (int i = 0; i < this.getChildCount(); i++) {
                View childView = getChildAt(i);
                if (childView.getId() != LABLE_SUCCESS_ID) {
                    childView.setVisibility(INVISIBLE);
                }
            }
            ObjectAnimator.ofFloat(mTextViewSuccess, "alpha", 0, 1).setDuration(ANIMATION_DURATION).start();
        mSubmitListener.onSubmit(Arrays.asList(contents));
    }

    private void animNext() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(currentnumber, currentStep * progressnumber);
        valueAnimator.setDuration(ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int i = (Integer) valueAnimator
                        .getAnimatedValue();
                if (i == datasize * progressnumber) {
                    success();
                } else {
                    mProgressBar.setProgress(i);
                }
            }
        });
        valueAnimator.start();
    }

    private void animPrevious() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(currentnumber, currentStep * progressnumber);
        valueAnimator.setDuration(ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mProgressBar.setProgress((Integer) valueAnimator
                        .getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    private void resetLayout(String title) {
        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(mLabelTitle, "translationY", 0, -mLabelTitle.getHeight()).setDuration(ANIMATION_DURATION/2);
        objectAnimator.addListener(this);
        objectAnimator.start();

        mEditText.setText(contents[currentStep]);
        mEditText.setInputType(inputTypes.get(currentStep));
        mTextPage.setText((currentStep + 1) + "/" + datasize);
        mEditText.setSelection(contents[currentStep] == null ? 0 : contents[currentStep].length());
    }

    public void reset() {
        currentStep = 0;
        currentnumber = 0;
        build(titles);
        mProgressBar.setProgress(0);
        for (int i = 0; i < this.getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView.getId() != LABLE_SUCCESS_ID) {
                childView.setVisibility(VISIBLE);
            }

        }
        mImageButton.setVisibility(GONE);
    }


    private int dipsToPix(float dps) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps,
                getResources().getDisplayMetrics());
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (!TextUtils.isEmpty(charSequence)) {
            mImageButton.setVisibility(VISIBLE);
        } else {
            mImageButton.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mLabelTitle.setText(titles.get(currentStep));
        ObjectAnimator.ofFloat(mLabelTitle, "translationY",  mLabelTitle.getHeight(),0).setDuration(ANIMATION_DURATION/2).start();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
