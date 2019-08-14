/* Copyright 2016 Google Inc.
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

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            dictionary = new FastDictionary(assetManager.open("words.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
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

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView text = (TextView) findViewById(R.id.ghostText);
        String currWord = String.valueOf(text.getText());
        Log.d("computerTurn: ", "passed currWord: " + currWord);
        String anyWordWith;
        if(currWord.length() >= 4 && dictionary.isWord(currWord)){
            label.setText("Computer wins!");
            return;
        }else{
            anyWordWith = dictionary.getAnyWordStartingWith(currWord);
            Log.d("computerTurn: ", "anyWordWith: " + anyWordWith);
            if(anyWordWith == null){
                label.setText("Computer wins!");
            }else{
                Log.d("computerTurn: ", "currWord length: " + currWord.length());
                String sub = anyWordWith.substring(currWord.length(), currWord.length()+1);
                Log.d("computerTurn: ", "substring " + sub);
                currWord += sub;
                Log.d("computerTurn: ", "currWord: " + currWord);
                text.setText(currWord);
            }
        }
        // Do computer turn stuff then make it the user's turn again
        userTurn = true;
        label.setText(USER_TURN);
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        TextView text = (TextView) findViewById(R.id.ghostText);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        //A-Z keyCodes = 29-54
        if(keyCode >= 29 && keyCode <= 54){
            String letter = String.valueOf(event.getDisplayLabel());
            text.append(letter.toLowerCase());
            if(userTurn){
                userTurn = false;
                label.setText(COMPUTER_TURN);
                computerTurn();
            }else{
                userTurn = true;
                label.setText(USER_TURN);
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * Handler for Challenge button
     * @param view
     * @return void
     * */
    public void challenge(View view){
        TextView text = (TextView) findViewById(R.id.ghostText);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        String currWord = String.valueOf(text.getText());
        if(currWord.length() >= 4 && dictionary.isWord(currWord)){
            label.setText("You win!");
            return;
        }else{
            String otherWord = dictionary.getAnyWordStartingWith(currWord);
            if(otherWord != null){
                label.setText("Computer wins!");
                text.setText(otherWord);
                return;
            }else{
                label.setText("You win!");
                return;
            }
        }
    }

}
