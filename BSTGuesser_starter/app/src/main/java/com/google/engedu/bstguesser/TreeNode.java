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

package com.google.engedu.bstguesser;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class TreeNode {
    private static final int SIZE = 60;
    private static final int MARGIN = 20;
    private int value, height;
    protected TreeNode left, right, parent;
    private TreeNode addedNode;
    private boolean showValue;
    private int x, y;
    private int color = Color.rgb(150, 150, 250);

    public TreeNode(int value) {
        this.value = value;
        this.height = 0;
        showValue = false;
        left = null;
        right = null;
        parent = null;
    }

    public void insert(int valueToInsert) {
        insert(this, valueToInsert, this);  //insert a new node with given value
        TreeNode unbalanced = addedNode.parent;
        int leftHeight, rightHeight;
        while(unbalanced != null){
            leftHeight = updateHeight(unbalanced.left);
            rightHeight = updateHeight(unbalanced.right);
            if(!(leftHeight == -1) && !(rightHeight == -1)){
                if(Math.abs(leftHeight - rightHeight) > 1){
                    rebalance(unbalanced);
                }
            }
            unbalanced = unbalanced.parent;
        }
    }

    private TreeNode insert(TreeNode node, int value, TreeNode prev){
        if(node == null){
            node = new TreeNode(value);
            node.parent = prev;
            addedNode = node;
            return node;
        }
        if(value < node.value){
            node.left = insert(node.left, value, node);
            //node.left.parent = node;
        }else if(value > node.value){
            node.right = insert(node.right, value, node);
           // node.right.parent = node;
        }
        node.height = updateHeight(node);
        return node;
    }

    private int updateHeight(TreeNode node){
        if(node == null)return -1;
        int heightLeft = updateHeight(node.left);
        int heightRight = updateHeight(node.right);
        return (1 + Math.max(heightLeft, heightRight));
    }

    private void rebalance(TreeNode N){
        TreeNode p = N.parent;
        int leftHeight, rightHeight;
        leftHeight = updateHeight(N.left);
        rightHeight = updateHeight(N.right);
        if(leftHeight > rightHeight + 1){
            rebalanceRight(N);
        }
        if(rightHeight > leftHeight + 1){
            rebalanceLeft(N);
        }
        updateHeight(N);
        if(p != null){
            rebalance(p);
        }
    }

    private void rebalanceRight(TreeNode N){
        TreeNode M = N.left;
        int leftHeight, rightHeight;
        leftHeight = updateHeight(M.left);
        rightHeight = updateHeight(M.right);
        if(rightHeight > leftHeight){
            rotateLeft(M);
            M.height = updateHeight(M);
            M.parent.height = updateHeight(M.parent);
        }
        rotateRight(N);
        N.height = updateHeight(N);
        N.parent.height = updateHeight(N.parent);
    }

    private void rebalanceLeft(TreeNode N){
        TreeNode M = N.right;
        int leftHeight, rightHeight;
        leftHeight = updateHeight(M.left);
        rightHeight = updateHeight(M.right);
        if(leftHeight > rightHeight){
            rotateRight(M);
            M.height = updateHeight(M);
            M.parent.height = updateHeight(M.parent);
        }
        rotateLeft(N);
        N.height = updateHeight(N);
        N.parent.height = updateHeight(N.parent);
    }

    private void rotateRight(TreeNode X){
        TreeNode p = X.parent;
        TreeNode Y = X.left;
        TreeNode B = Y.right;
        Y.parent = p;
        if(p != null && p.left == X)p.left = Y;
        else if(p != null && p.right == X)p.right = Y;
        X.parent = Y;
        Y.right = X;
        if(B != null)B.parent = X;
        X.left = B;
    }

    private void rotateLeft(TreeNode Y){
        TreeNode p = Y.parent;
        TreeNode X = Y.right;
        TreeNode B = X.left;
        X.parent = p;
        if(p != null && p.right == Y)p.right = X;
        else if(p != null && p.left == Y)p.left = X;
        Y.parent = X;
        X.left = Y;
        if(B != null)B.parent = Y;
        Y.right = B;
    }

    public int getValue() {
        return value;
    }

    public void positionSelf(int x0, int x1, int y) {
        this.y = y;
        x = (x0 + x1) / 2;

        if(left != null) {
            left.positionSelf(x0, right == null ? x1 - 2 * MARGIN : x, y + SIZE + MARGIN);
        }
        if (right != null) {
            right.positionSelf(left == null ? x0 + 2 * MARGIN : x, x1, y + SIZE + MARGIN);
        }
    }

    public void draw(Canvas c) {
        Paint linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3);
        linePaint.setColor(Color.GRAY);
        if (left != null)
            c.drawLine(x, y + SIZE/2, left.x, left.y + SIZE/2, linePaint);
        if (right != null)
            c.drawLine(x, y + SIZE/2, right.x, right.y + SIZE/2, linePaint);

        Paint fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(color);
        c.drawRect(x-SIZE/2, y, x+SIZE/2, y+SIZE, fillPaint);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(SIZE * 2/3);
        paint.setTextAlign(Paint.Align.CENTER);
        c.drawText(showValue ? String.valueOf(value) : "?", x, y + SIZE * 3/4, paint);

        if (height > 0) {
            Paint heightPaint = new Paint();
            heightPaint.setColor(Color.MAGENTA);
            heightPaint.setTextSize(SIZE * 2 / 3);
            heightPaint.setTextAlign(Paint.Align.LEFT);
            c.drawText(String.valueOf(height), x + SIZE / 2 + 10, y + SIZE * 3 / 4, heightPaint);
        }

        if (left != null)
            left.draw(c);
        if (right != null)
            right.draw(c);
    }

    public int click(float clickX, float clickY, int target) {
        int hit = -1;
        if (Math.abs(x - clickX) <= (SIZE / 2) && y <= clickY && clickY <= y + SIZE) {
            if (!showValue) {
                if (target != value) {
                    color = Color.RED;
                } else {
                    color = Color.GREEN;
                }
            }
            showValue = true;
            hit = value;
        }
        if (left != null && hit == -1)
            hit = left.click(clickX, clickY, target);
        if (right != null && hit == -1)
            hit = right.click(clickX, clickY, target);
        return hit;
    }

    public void invalidate() {
        color = Color.CYAN;
        showValue = true;
    }
}
