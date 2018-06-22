package com.example.shad0w.mines;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

import static android.os.Build.VERSION_CODES.N;

public class start extends AppCompatActivity implements View.OnClickListener{
int v1,v2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void value(View view) {
        Button b=(Button)view;
        if(b.getId()==R.id.one){
            v1=5;
            v2=5;
        }
        if(b.getId()==R.id.two){
            v1=10;
            v2=9;
        }
        if(b.getId()==R.id.three){
            v1=15;
            v2=10;
        }
        enter();
    }

    public void custom(View view) {
        EditText t1=new EditText(this);
        EditText t2=new EditText(this);
        ViewGroup.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,0);
        t1.setLayoutParams(lp);
        t2.setLayoutParams(lp);
       // t1.setInputType();
        t1.setHint("enter row");
        t2.setHint("entr column");
        if(t1.getText().toString().isEmpty()||t2.getText().toString().isEmpty())
            Toast.makeText(this,"enter proper values",Toast.LENGTH_LONG).show();
        else
        {
            v1=Integer.parseInt(t1.getText().toString());
            v2=Integer.parseInt(t2.getText().toString());
        }
        Button b=new Button(this);
        b.setLayoutParams(lp);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        enter();
    }

    private void enter() {

        Intent i=new Intent(this,MainActivity.class);
        i.putExtra("row",v1);
        i.putExtra("col",v2);
        startActivity(i);
    }
}
