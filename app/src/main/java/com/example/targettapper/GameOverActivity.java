package com.example.targettapper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity implements MyGLGame {
    private TextView scoreText;
    private GLSurfaceView mGLView;
    private Square background;
    ListView listView;

    public static final String MY_PREFS_FILE = "myPrefsFile";
    public final String DEFAULT_LOAD = "No file found";
    public final String SAVE_ERROR = "You cannot save without a proper answer for your last valid input.";

    public String[] scores = {"0","0","0","0","0"};

    public void goToTitle(View view){
        Intent game = new Intent(this, MainActivity.class);
        startActivity(game);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new MyGLSurfaceView(this, this);
        setContentView(R.layout.activity_game_over);

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.activity_game_over);

        layout.addView(mGLView);

        int score = getIntent().getIntExtra(GameActivity.EXTRA_INT, 0);
        scoreText = (TextView)findViewById(R.id.score);
        scoreText.setTextColor(Color.WHITE);
        scoreText.setTextSize(26);
        scoreText.setText("Your Score was: " + score);
        scoreText.bringToFront();

        loadData();

        int movingScore = score;
        String nextscore;
        for (int i = 0; i < scores.length; i++) {

            if(movingScore > Integer.parseInt(scores[i])){
                nextscore = scores[i];
                scores[i] = String.valueOf(movingScore);
                movingScore = Integer.parseInt(nextscore);
            }

        }



        listView = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.score_view, R.id.score, scores);
        listView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

        listView.bringToFront();

        ((TextView)findViewById(R.id.title)).bringToFront();

        scoreToString();

    }



    public void initialize(){
        int bgImageId = BitmapLoader.loadBitmap(getResources(), R.drawable.scoresbackground);

        background  = new Square();
        background.setScale(2.0f);
        background.setPos(0.0f, 0.0f, -0.1f);
        background.setTexID(bgImageId);
        background.setIgnoreClicks(true);


    }

    public void scoreToString(){
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < scores.length; i++) {
            strBuilder.append(scores[i] + ",");
        }
        strBuilder.deleteCharAt(strBuilder.length() - 1).toString();

        String newString = strBuilder.toString();
        saveData(newString);
    }

    public void saveData(String info) {
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_FILE, MODE_PRIVATE).edit();
        editor.putString("calculated", info);
        editor.apply();
    }

    public void loadData() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_FILE, MODE_PRIVATE);
        String value = prefs.getString("calculated", DEFAULT_LOAD);//"No value defined" is the default value.
        saveToScores(value);
    }

    public void saveToScores(String scoreString){
        if(scoreString != DEFAULT_LOAD) {
            String[] rawScores = scoreString.split(",");

            for (int i = 0; i < rawScores.length; i++) {
                scores[i] = rawScores[i];
            }
        }

    }

    public void update(float dt){
        /* do nothing */
    }

    public void draw(MyGLRenderer renderer){
        renderer.drawObjectWithShader(background);
    }

    public void shutdown(){ /* do nothing */ }

    public void onTouch(float ndcX, float ndcY, float widthP){ /* do nothing */ }

}
