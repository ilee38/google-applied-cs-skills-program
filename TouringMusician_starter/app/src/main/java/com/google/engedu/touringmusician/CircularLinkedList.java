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

package com.google.engedu.touringmusician;


import android.graphics.Point;

import java.util.Iterator;

public class CircularLinkedList implements Iterable<Point> {

    public CircularLinkedList() {
    }

    private class Node {
        Point point;
        Node prev, next;
    }

    Node head, tail;
    int list_size = 0;

    /*
    *  Add a point to the beginning of the list
    * */
    public void insertBeginning(Point p) {
        Node newNode = new Node();
        newNode.point = p;
        if(this.list_size == 0){
            newNode.next = newNode;
            newNode.prev = newNode;
            this.head = newNode;
            this.tail = newNode;
        }else{
            newNode.next = this.head;
            this.head.prev = newNode;
            this.tail.next = newNode;
            newNode.prev = this.tail;
            this.head = newNode;
        }
        this.list_size++;
    }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y-to.y, 2) + Math.pow(from.x-to.x, 2));
    }

    public float totalDistance() {
        float total = 0;
        Iterator<Point> it = this.iterator();
        Point start, stop;
        if(it.hasNext()){
            start = it.next();
            while(true){
                if(!it.hasNext())break;
                stop = it.next();
                total += this.distanceBetween(start, stop);
                start = stop;
            }
        }
        return total;
    }

    public void insertNearest(Point p) {
       if(this.list_size == 0 || this.list_size == 1){
           this.insertBeginning(p);
       }else{
           Node newNode = new Node();
           newNode.point = p;
           Point nearestPoint = this.findNearest(p);
           Node current = this.head;
           if(current.point == nearestPoint){
               this.insertAfter(newNode, current);
           }else{
               current = current.next;
               while(current != this.head){
                   if(current.point == nearestPoint){
                       this.insertAfter(newNode, current);
                       return;
                   }
                   current = current.next;
               }
           }
       }
    }


    public void insertSmallest(Point p) {
        if(this.list_size == 0 || this.list_size == 1){
            this.insertBeginning(p);
        }else{
            Node newNode = new Node();
            newNode.point = p;
            Node after, current = this.head;
            this.insertAfter(newNode, current);
            float currTotalDist = this.totalDistance();
            after = current;
            this.removeNode(newNode);
            while(current.next != this.head){
                current = current.next;
                this.insertAfter(newNode, current);
                if(currTotalDist > this.totalDistance()){
                    currTotalDist = this.totalDistance();
                    after = current;
                }
                this.removeNode(newNode);
            }
            this.insertAfter(newNode, after);
        }
    }

    private void removeNode(Node toRemove){
        toRemove.prev.next = toRemove.next;
        toRemove.next.prev = toRemove.prev;
        if(this.tail == toRemove){
            this.tail = toRemove.prev;
        }
        if(this.head == toRemove){
            this.head = toRemove.next;
        }
        toRemove = null;
    }

    private void insertAfter(Node toInsert, Node after){
        toInsert.next = after.next;
        after.next = toInsert;
        toInsert.next.prev = toInsert;
        toInsert.prev = after;
        if(after == this.tail){
            this.tail = toInsert;
        }
    }

    private Point findNearest(Point p){
        float nearestDist = Float.POSITIVE_INFINITY;
        Iterator<Point> it = this.iterator();
        Point stop, nearestPoint = p;
        if(it.hasNext()){
            while(true){
                if(!it.hasNext())break;
                stop = it.next();
                float currDist = this.distanceBetween(p, stop);
                if(currDist < nearestDist && p != stop){
                    nearestDist = currDist;
                    nearestPoint = stop;
                }
            }
        }
        return nearestPoint;
    }

    public void reset() {
        head = null;
        this.list_size = 0;
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}
