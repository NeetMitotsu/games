package snake;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @Author 李新栋 [lxd3808@163.com]
 * @Date 2018/10/12 9:53
 */
public class Snake {

    private Node head = null;
    private Node tail = null;
    private int size = 0;
    private Dir dir = null;
    private Yard yard;


    public Snake(Node node) {
        head = node;
        tail = node;
        size = 1;
    }

    public Snake(Yard yard) {
        Node n = new Node(20, 20, Dir.LEFT);
        head = n;
        tail = n;
        size = 1;
        this.yard = yard;
    }

    /**
     * 新节加在尾部
     */
    public void addToTail() {
        Node node = null;
        switch (tail.dir) {
            case UP:
                node = new Node(tail.row + 1, tail.col, tail.dir);
                break;
            case DOWN:
                node = new Node(tail.row - 1, tail.col, tail.dir);
                break;
            case LEFT:
                node = new Node(tail.row, tail.col + 1, tail.dir);
                break;
            case RIGHT:
                node = new Node(tail.row, tail.col - 1, tail.dir);
                break;
        }
        tail.next = node;
        node.prev = tail;
        tail = node;
        size++;
    }

    /**
     * 新节加在头
     */
    public void addToHead() {
        Node node = null;
        switch (head.dir) {
            case UP:
                node = new Node(head.row - 1, head.col, head.dir);
                break;
            case DOWN:
                node = new Node(head.row + 1, head.col, head.dir);
                break;
            case LEFT:
                node = new Node(head.row, head.col - 1, head.dir);
                break;
            case RIGHT:
                node = new Node(head.row, head.col + 1, head.dir);
                break;
        }
        node.next = head;
        head.prev = node;
        head = node;
        size++;
    }

    public void eat(Egg e){
        if (this.getRect().intersects(e.getRect())){
            e.reAppear();
            addToHead();
            yard.setScore(yard.getScore() + 5);
        }
    }

    private Rectangle getRect(){
        return new Rectangle(Yard.BLOCK_SIZE * head.col,Yard.BLOCK_SIZE * head.row, head.w, head.h);
    }

    public void draw(Graphics g) {
        if (size <= 0) {
            return;
        }
        move();
        for (Node n = head; n != null; n = n.next) {
            n.draw(g);
        }
    }

    private void move() {
        addToHead();
        deleteFromTail();
        checkDead();
    }

    private void checkDead() {
        if(head.row < 2 || head.col < 0 || head.row > Yard.ROWS || head.col > Yard.COLS){
            yard.stop();
        }
        for(Node n = head.next; n != null; n = n.next){
            if(head.row == n.row && head.col == n.col){
                yard.stop();
            }
        }
    }

    private void deleteFromTail() {
        if (tail == null) {
            return;
        }
        tail = tail.prev;
        tail.next = null;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                if (head.dir != Dir.DOWN)
                    head.dir = Dir.UP;
                break;
            case KeyEvent.VK_DOWN:
                if(head.dir != Dir.UP)
                    head.dir = Dir.DOWN;
                break;
            case KeyEvent.VK_LEFT:
                if(head.dir != Dir.RIGHT)
                    head.dir = Dir.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                if(head.dir != Dir.LEFT)
                    head.dir = Dir.RIGHT;
                break;
        }
    }


    private class Node {
        int w = Yard.BLOCK_SIZE;
        int h = Yard.BLOCK_SIZE;
        /**
         * 所在位置
         */
        int row, col;
        /**
         * 方向
         */
        Dir dir = Dir.LEFT;
        /**
         * 下一节
         */
        Node next = null;
        /**
         * 前一节
         */
        Node prev = null;

        public Node(int row, int col, Dir dir) {
            this.row = row;
            this.col = col;
            this.dir = dir;
        }

        void draw(Graphics g) {
            Color c = g.getColor();
            g.setColor(Color.black);
            g.fillRect(Yard.BLOCK_SIZE * col,Yard.BLOCK_SIZE * row, w, h);
            g.setColor(c);
        }
    }

}
