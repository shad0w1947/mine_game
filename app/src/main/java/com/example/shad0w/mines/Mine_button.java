package com.example.shad0w.mines;

import android.content.Context;
import android.widget.Button;

public class Mine_button extends android.support.v7.widget.AppCompatButton {
    int r;
    int c;
    boolean open=false;
    int value;
    boolean longp=true;
    public Mine_button(Context context){
        super(context);
    }
    public void setvalues(int t){
        if(t==0)
        {

             setBackgroundDrawable(getResources().getDrawable(R.drawable.blanck,null));

        }
        else if(t==-1){
            setBackgroundDrawable(getResources().getDrawable(R.drawable.bomb,null));
        }
        else if(t==-2)
            setBackgroundDrawable(getResources().getDrawable(R.drawable.download,null));
        else{
        setText(t+"");

        }
    }



}
