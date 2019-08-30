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

package com.google.engedu.wordladder;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;

public class PathDictionary {
    private static final int MAX_WORD_LENGTH = 4;
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";    //all words are in lowercase
    private static HashSet<String> words = new HashSet<>();
    private static HashMap<String, ArrayList<String> > AdjList = new HashMap<>();

    public PathDictionary(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return;
        }
        Log.i("Word ladder", "Loading dict");
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        Log.i("Word ladder", "Loading dict");
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() > MAX_WORD_LENGTH) {
                continue;
            }
            words.add(word);
        }
        for(String w:words){
            AdjList.put(w, new ArrayList<String>());
            populateNeighbours(w);
        }
        //Log.d("Word ladder", "Neighbours of cold " + neighbours("cold").toString());
        String[] route = findPath("gain", "fire");
        if(route != null){
            String p = "";
            for(String w:route){
                p += " "+w;
            }
            Log.d("Word ladder", "Path from gain to fire: " + p);
        }else{
            Log.d("Word ladder", "No path found");
        }
    }

    /**
     * Populate the neighbours for each word on the words set:
     *  Each word is a node in the graph, represented as an Adjacency List.
     *  For each word:
     *  1. change each letter in the word, trying one character (from a to z) at a time
     *  2. check if it forms a valid word, and add it to the adjacency list.
     *
     * Runtime:
     *      26 (letters in alphabet) * 4 (MAX_WORD_LENGTH) * N (number of words):
     *      O(104*N) = O(N)
     * */
    private void populateNeighbours(String word){
        String suffix, prefix ="";
        for(int i = 0; i < word.length(); i++){
            if(i  > 0){
                prefix = word.substring(0, i);
            }
            if(i == word.length()-1){
                suffix = "";
            }else{
                suffix = word.substring(i+1);
            }
            for(int j = 0; j < ALPHABET.length(); j++){
                String toCheck = prefix + ALPHABET.substring(j,j+1) + suffix;
                if(!toCheck.equals(word) && toCheck.length() == word.length() && words.contains(toCheck)){
                    AdjList.get(word).add(toCheck);
                }
            }
        }
    }

    public boolean isWord(String word) {
        return words.contains(word.toLowerCase());
    }

    private ArrayList<String> neighbours(String word) {
        if(AdjList.containsKey(word.toLowerCase())){
            return AdjList.get(word.toLowerCase());
        }else{
            return new ArrayList<String>();
        }
    }

    public String[] findPath(String start, String end) {
        ArrayDeque<ArrayList<String> > Q = new ArrayDeque();
        HashSet<String > visited = new HashSet<>();
        ArrayList<String> path = new ArrayList<>();
        visited.add(start);
        path.add(start);
        Q.add(path);
        while(!Q.isEmpty()) {
            path = Q.remove();
            for (String element : AdjList.get(path.get(path.size() - 1))) {
                ArrayList<String> nextPath = new ArrayList<>();
                for(String str: path){
                    nextPath.add(str);
                }
                if (!visited.contains(element)) {
                    nextPath.add(element);
                    Q.add(nextPath);
                    if (element.equals(end)) {
                        String[] ret = new String[nextPath.size()];
                        return nextPath.toArray(ret);
                    }
                    visited.add(element);
                }
            }
        }
        return null;
    }
}
