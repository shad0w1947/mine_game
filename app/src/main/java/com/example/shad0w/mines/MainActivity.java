package com.example.shad0w.mines;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    Random rand = new Random();
    Mine_button board[][];
    int ans[][];
    int Rsize = 8;
    int Csize = 6;
    boolean first = true;
    int size;
    private Handler customHandler = new Handler();
    int Msize;
    int count = 0;
    int[] index_x = {-1, -1, -1, 0, 1, 1, 1, 0};
    int[] index_y = {-1, 0, 1, 1, 1, 0, -1, -1};
    long startTime;
    LinearLayout rootlayout;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    TextView high_score;
    String score="99:99:99";
    SharedPreferences sharedPreferences;
    TextView textView;
   boolean win=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        high_score=findViewById(R.id.high_score);
        sharedPreferences=getSharedPreferences("my_data2",MODE_PRIVATE);
        String s=sharedPreferences.getString("my_data2",null);
        if(s!=null)
        { high_score.setText(s);
            score=s;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = findViewById(R.id.tv);
        getSupportActionBar().setTitle("");
        textView.setText("00:00:000");
        Intent intent = getIntent();
        Rsize = intent.getIntExtra("row", 5);
        Csize = intent.getIntExtra("col", 5);
        int temp = 0;
        if (Csize + Rsize < Csize * Rsize)
            temp = Csize + Rsize;
        size = (Rsize*Csize)/5;
        Msize = size;
        Log.i("intent", "" + intent);
        rootlayout = findViewById(R.id.root);
        setupboard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mmmm, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.reset) {
            reset();
        }

        return super.onOptionsItemSelected(item);
    }

    private void reset() {
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
        first = true;
        int temp = 0;
        if (Csize + Rsize < Csize * Rsize)
            temp = Csize + Rsize;
        size = rand.nextInt(Rsize * Csize - (temp)) + 1;
        Msize = size;
        count = 0;
        startTime = 0;
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        updatedTime = 0L;
        setupboard();

    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            String ans="" + mins + ":" + String.format("%02d", secs) + ":" + String.format("%03d", milliseconds);
          //  Log.i("win","nwinn  "+score+" "+ans+"  "+updatedTime);

            textView.setText(ans);
            customHandler.postDelayed(this, 0);
        }
    };


    private void setupboard() {
        if (rootlayout != null)
            rootlayout.removeAllViews();
        board = new Mine_button[Rsize][Csize];
        ans = new int[Rsize][Csize];
        for (int i = 0; i < Rsize; i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            linearLayout.setLayoutParams(layoutParams);
            rootlayout.addView(linearLayout);
            for (int j = 0; j < Csize; j++) {
                Mine_button button = new Mine_button(this);
                ViewGroup.LayoutParams layoutbutton = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                button.setLayoutParams(layoutbutton);
                button.setOnClickListener(MainActivity.this);
                button.setOnLongClickListener(MainActivity.this);
                button.r = i;
                button.c = j;
                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.notopen, null));
                linearLayout.addView(button);
                board[i][j] = button;
                Log.i("value", "this" + button.r + " " + button.c);
                // board[i][j].setvalues(5);
                //Toast.makeText(this, "game over", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(this, "game over", Toast.LENGTH_LONG).show();
        Mine_button button = (Mine_button) view;
        if (!button.longp)
            return;
        if (first) {
            plantbomb(button.r, button.c);
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
            // Log.i("value", "this" + button.r + " " + button.c);
        }

        openvalue(button.r, button.c);
    }

    private void openvalue(int p, int q) {
        if (p < 0 || p >= Rsize || q < 0 || q >= Csize)
            return;
        if (board[p][q].open || (board[p][q].value != ans[p][q]))
            return;
        if (ans[p][q] == -1) {
            openall(board[p][q]);
            Toast.makeText(this, "game over", Toast.LENGTH_LONG).show();
            return;
        }
        // Log.i("ans", ans[p][q] + " " + p + " " + q);
        board[p][q].setvalues(ans[p][q]);
        count++;
        board[p][q].open = true;
        if (ans[p][q] == 0) {

            for (int k = 0; k < index_x.length; k++)
                openvalue(p + index_x[k], q + index_y[k]);
        }
        if (count == (Rsize * Csize) - Msize) {
            win=true;
          if(highScoreComparer(textView.getText().toString(),score)){
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("my_data2",textView.getText().toString());
            high_score.setText(textView.getText().toString());
            editor.commit();}
            Toast.makeText(this, "you win", Toast.LENGTH_LONG).show();
            openall(board[p][q]);
        }


    }


    private void openall(Mine_button b) {

        for (int i = 0; i < Rsize; i++) {

            for (int j = 0; j < Csize; j++) {
                board[i][j].setvalues(ans[i][j]);
                board[i][j].setEnabled(false);
            }
        }
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
        //Mine_button b2=new Mine_button(this);
        //b2.setEnabled(false);

    }
    private boolean highScoreComparer(String s, String p) {


        String y[]=s.split(":");
        String z[]=p.split(":");
        int s1=0;
        for(int i=0;i<y.length;i++){
            s1*=60;
            s1+=Integer.parseInt(y[i]);
        }
        int s2=0;
        for(int i=0;i<y.length;i++){
            s2*=60;
            s2+=Integer.parseInt(z[i]);
        }
        if(s1>s2)
            return false;
        else
            return true;
    }
    void plantbomb(int i, int j) {
        while (size > 0) {
            int x = rand.nextInt(Rsize);
            int y = rand.nextInt(Csize);
            if ((x == i && y == j) || ans[x][y] == -1)
                continue;
            ans[x][y] = -1;
            board[x][y].value = ans[x][y];
            // board[x][y].setvalues(ans[x][y]);
            //Log.i("ans",ans[x][y]+" "+x+" "+y);
            size--;
        }
        assignvalue();
        first = false;
    }

    private void assignvalue() {
        int x = 0, y = 0;
        for (int i = 0; i < Rsize; i++) {

            for (int j = 0; j < Csize; j++) {
                int temp = 0;

                if (ans[i][j] == -1)
                    ;
                else {
                    for (int k = 0; k < index_x.length; k++) {
                        x = i + index_x[k];
                        y = j + index_y[k];
                        if (x >= 0 && x < Rsize && y >= 0 && y < Csize) {
                            if (ans[x][y] == -1)
                                temp++;
                        }
                    }
                    ans[i][j] = temp;
                    board[i][j].value = ans[i][j];
                }
                //Log.i("ans", ans[i][j] + " " + i + " " + j);
                //  board[i][j].setvalues(ans[i][j]);
            }
        }
    }

    @Override
    public boolean onLongClick(View view) {
        Mine_button button = (Mine_button) view;
        if (button.open)
            return true;
        if (button.longp) {
            board[button.r][button.c].value = -2;
            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.download, null));
            button.longp = false;

        } else {

            board[button.r][button.c].value = ans[button.r][button.c];
            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.notopen, null));
            button.longp = true;

        }

        return true;
    }
}
