package com.google.huang.leonyj.scarnedice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private enum Turn {
        USER, COMPUTER
    }
    private int userScore = 0;
    private int userTurn = 0;
    private int comScore = 0;
    private int comTurn = 0;
    private Turn turn = Turn.USER;
    private final String scoreFormat = "Your score: %d Computer Score: %d\nYour turn score: %d Computer turn score: %d";
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            rollDice();
            if(turn == Turn.COMPUTER && comTurn < 20) {
                timerHandler.postDelayed(this,750);
            }else if(turn == Turn.COMPUTER){
                handleHoldClick(null);
            }
        }
    };


    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateScore();
    }
    private void userTurn() {
        turn = Turn.USER;
        findViewById(R.id.btRoll).setEnabled(true);
        findViewById(R.id.bthold).setEnabled(true);
    }

    private void computerTurn() {
        turn = Turn.COMPUTER;
        findViewById(R.id.btRoll).setEnabled(false);
        findViewById(R.id.bthold).setEnabled(false);
        timerHandler.postDelayed(timerRunnable,0);
    }
    private void nextTurn(){
        checkWinner();
        userTurn = 0;
        comTurn = 0;
        updateScore();
        if(turn == Turn.USER) {
             computerTurn();
        }else {
            timerHandler.removeCallbacks(timerRunnable);
            userTurn();
        }
    }

    public void handleRollClick(View view) {
        rollDice();
    }

    public void handleHoldClick(View view) {
        if(turn == Turn.USER) {
            userScore += userTurn;
        }else {
            comScore += comTurn;
        }
        nextTurn();
    }
    public void handleResetClick(View view) {
        userTurn();
        userScore = 0;
        comScore = 0;
        userTurn = 0;
        comTurn = 0;
        updateScore();
        findViewById(R.id.btRoll).setEnabled(true);
        findViewById(R.id.bthold).setEnabled(true);
    }
    private void rollDice() {
        Random ran = new Random();
        int num = ran.nextInt(6);
        int resId = 0;
        switch(num) {
            case 0:
                resId = R.drawable.dice1;
                break;
            case 1:
                resId = R.drawable.dice2;
                break;
            case 2:
                resId = R.drawable.dice3;
                break;
            case 3:
                resId = R.drawable.dice4;
                break;
            case 4:
                resId = R.drawable.dice5;
                break;
            case 5:
                resId = R.drawable.dice6;
                break;
        }
        ImageView dices = (ImageView) findViewById(R.id.dice_view);
        dices.setImageDrawable(getResources().getDrawable(resId,null));
        if (num == 0) {
            nextTurn();
        }else {
            if(turn == turn.USER) {
                userTurn += num +1;
            }else {
                comTurn += num +1;
            }
            updateScore();
        }
    }
    private void updateScore() {
        TextView score = (TextView) findViewById(R.id.score);
        score.setText(String.format(scoreFormat,userScore,comScore,userTurn,comTurn));
    }

    private void checkWinner() {
        TextView winner = (TextView) findViewById(R.id.winner);
        if(userScore >= 100 ) {
            winner.setText("You wins!");
            timerHandler.removeCallbacks(timerRunnable);
            findViewById(R.id.btRoll).setEnabled(false);
            findViewById(R.id.bthold).setEnabled(false);
        }
        if(comScore >= 100) {
            winner.setText("Computer wins!");
            findViewById(R.id.btRoll).setEnabled(false);
            findViewById(R.id.bthold).setEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
