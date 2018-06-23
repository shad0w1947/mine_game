package com.example.shad0w.mines;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    int Msize;
    int count = 0;
    int[] index_x = {-1, -1, -1, 0, 1, 1, 1, 0};
    int[] index_y = {-1, 0, 1, 1, 1, 0, -1, -1};

    LinearLayout rootlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Rsize = intent.getIntExtra("row", 5);
        Csize = intent.getIntExtra("col", 5);
        int temp=0;
        if(Csize+Rsize<Csize*Rsize)
            temp=Csize+Rsize;
       size= rand.nextInt(Rsize * Csize - (temp))+1;
        Msize=size;
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
        first = true;
        int temp=0;
        if(Csize+Rsize<Csize*Rsize)
            temp=Csize+Rsize;
        size = rand.nextInt(Rsize * Csize - (temp)) + 1;
        Msize = size;
        count = 0;
        setupboard();

    }


    private void setupboard() {
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
        //Mine_button b2=new Mine_button(this);
        //b2.setEnabled(false);

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
