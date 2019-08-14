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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        if(s.length() == 0)return;
        HashMap<String, TrieNode> currChildren = children;
        String key;
        for(int i = 0; i < s.length(); i++){
            key = s.substring(i, i+1);
            if(!currChildren.containsKey(key)){
                TrieNode newChildren = new TrieNode();
                currChildren.put(key, newChildren);
                //if last character, mark as complete word
                if(i == s.length()-1){
                    currChildren.get(key).isWord = true;
                }
            }
            //the next trieNode becomes the root
            currChildren = currChildren.get(key).children;
        }
    }

    public boolean isWord(String s) {
        if(s.length() == 0)return false;
        HashMap<String, TrieNode> currChildren = children;
        String key;
        for(int i = 0; i < s.length(); i++){
            key = s.substring(i,i+1);
            if(!currChildren.containsKey(key)){
                return false;
            }else if(i == s.length()-1){
                return currChildren.get(key).isWord;
            }
            currChildren = currChildren.get(key).children;
        }
        return false;
    }

    public String getAnyWordStartingWith(String s) {
        HashMap<String, TrieNode> currChildren = children;
        String key;
        if(s.length() == 0){
            return null;
            //TODO: return a random word when the prefix is empty
        }
        for(int i = 0; i < s.length(); i++){
            key = s.substring(i, i+1);
            if(!currChildren.containsKey(key)){
                return null;
            }else if(i == s.length()-1){
                currChildren = currChildren.get(key).children;
                break;
            }
            currChildren = currChildren.get(key).children;
        }
        //find full word with prefix
        String next;
        while(true){
            if(currChildren.size() == 0 || currChildren == null){
                return null;
            }else{
                Set<String> keyList = currChildren.keySet();
                Iterator<String> it = keyList.iterator();
                next = it.next();
                s += next;
                if(currChildren.get(next).isWord){
                    return s;
                }
            }
            currChildren = currChildren.get(next).children;
        }
    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }
}
