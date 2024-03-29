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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.util.Log;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        String TAG = "getAnyWordStartingWith";
        if(prefix.length() == 0){
            int randIndex = (int)(Math.random() * words.size());
            Log.d(TAG, "random word: " + words.get(randIndex));
            return words.get(randIndex);
        }else{
            return binSearch(prefix, 0, words.size());
        }
    }

    private String binSearch(String prefix, int lo, int hi){
        int mid = (hi + lo) / 2;
        String checkWord = words.get(mid);
        if(checkWord.length() > prefix.length() && checkWord.substring(0, prefix.length()).equals(prefix)){
            Log.d("binSearch: ", checkWord);
            return checkWord;
        }else if(lo > hi){
            return null;
        }
        if(checkWord.compareToIgnoreCase(prefix) < 0){
            return binSearch(prefix, mid+1, hi);
        }else{
            return binSearch(prefix, lo, mid-1);
        }
        //return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        return selected;
    }
}
