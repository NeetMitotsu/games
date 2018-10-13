package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 贪吃蛇
 * ↑, ↓, ←, → 控制方向,
 * F2 重新开始
 *
 * @Author 李新栋 [lxd3808@163.com]
 * @Date 2018/10/12 9:52
 */
public class Yard extends JFrame {
    private static final long serialVersionUID = -3784412514707913132L;

    private boolean flag = true;

    Snake snake = new Snake(this);
    Egg e = new Egg();
    /**
     * 行
     */
    public static final int ROWS = 50;
    /**
     * 列
     */
    public static final int COLS = 50;
    /**
     * 每小格边长(像素)
     */
    public static final int BLOCK_SIZE = 15;

    /**
     * 分数
     */
    private int score = 0;

    Image offScreenImage = null;

    public void launch() {
        this.setLocation(300, 300);
        this.setSize(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        new Thread(new PaintThread()).start();
        this.addKeyListener(new KeyMonitor());
    }


    public static void main(String[] args) {
        new Yard().launch();

    }

    public void stop() {
        flag = false;
    }

    @Override
    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.gray);
        g.fillRect(0, 0, COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
        g.setColor(Color.darkGray);
        //画横线
        for (int i = 1; i < ROWS; i++) {
            g.drawLine(0, BLOCK_SIZE * i, COLS * BLOCK_SIZE, BLOCK_SIZE * i);
        }
        // 画竖线
        for (int i = 1; i < COLS; i++) {
            g.drawLine(BLOCK_SIZE * i, 0, BLOCK_SIZE * i, ROWS * BLOCK_SIZE);
        }
        // 显示分数
        g.setColor(Color.YELLOW);
        g.drawString("Score: " + score, 10, 60);

        snake.eat(e);
        e.draw(g);
        snake.draw(g);
        if (!flag) {
            g.setFont(new Font("Verdana", Font.BOLD | Font.HANGING_BASELINE, 50));
            g.drawString("GAME OVER", ROWS * BLOCK_SIZE / 4, COLS * BLOCK_SIZE / 4);
            g.drawString("Press F2 to Restart", ROWS * BLOCK_SIZE / 4 - 60, COLS * BLOCK_SIZE / 4 + 50);
        }
        g.setColor(c);

    }

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
        }
        Graphics gOff = offScreenImage.getGraphics();
        paint(gOff);
        g.drawImage(offScreenImage, 0, 0, null);
    }


    private class PaintThread implements Runnable {
        public void run() {
            while (flag) {
                repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            snake.keyPressed(e);
            /**
             * F2 重新开始
             */
            if (e.getKeyCode() == KeyEvent.VK_F2 && !flag) {
                flag = true;
                score = 0;
                snake = new Snake(Yard.this);
                new Thread(new PaintThread()).start();
            }
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
