package com.litao.android.minimalform;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.litao.android.lib.MinimalFormLayout;
import com.litao.android.lib.SubmitListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements SubmitListener {

    private TextView mTextViewTest;
    private MinimalFormLayout mForm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnClick();

        mForm=(MinimalFormLayout)findViewById(R.id.form);
        mForm.build(creatData(),creatInputTypes(),creatRegular(),creatErrorMsg());
        mForm.setOnSubmitListener(this);
        mForm.setSuccessMsg("Thank you! We'll be in touch.");

    }

    @Override
    public void onSubmit(List<String> formdata) {

        String s="";
        for (int i = 0; i < formdata.size(); i++) {
            s+=i+"."+formdata.get(i)+"\n\n";
        }
        mTextViewTest.setText(s);
    }













    private void btnClick(){
        findViewById(R.id.previousBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mForm.previous();

            }
        });
        findViewById(R.id.nextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mForm.next();
            }
        });
        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mForm.reset();
                mTextViewTest.setText("");
            }
        });
        mTextViewTest=(TextView)findViewById(R.id.testview);
    }



    private List<String> creatData(){
        List<String> s=new ArrayList<String>();
        s.add("What's your username?");
        s.add("What's your password?");
        s.add("What's your phone number?");
        s.add("What's your email address?");
        return  s;
    }
    private List<Integer> creatInputTypes(){
        List<Integer> s=new ArrayList<Integer>();
        s.add(InputType.TYPE_CLASS_TEXT);
        s.add(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        s.add(InputType.TYPE_CLASS_PHONE);
        s.add(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        return  s;
    }
    private List<String> creatErrorMsg(){
        List<String> s=new ArrayList<String>();
        s.add("Please enter a valid username");
        s.add("Please enter a valid password");
        s.add("Please enter a valid phone number");
        s.add("Please enter a valid email address");
        return  s;
    }
    private List<String> creatRegular (){
        List<String> s=new ArrayList<String>();
        s.add("^[\\u4E00-\\u9FA5A-Za-z][\\u4E00-\\u9FA5A-Za-z0-9]+$");
        s.add("^[0-9a-zA-Z]\\w{6,18}$");
        s.add(null);
        s.add("^\\w+[\\w-\\.]*\\@\\w+((-\\w+)|(\\w*))\\.[a-z]{2,3}$");
        return  s;
    }


}
