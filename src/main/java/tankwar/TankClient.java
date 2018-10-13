package tankwar;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @Author 李新栋 [lxd3808@163.com]
 * @Date 2018/10/12 15:46
 */
public class TankClient extends JFrame {

    private static final long serialVersionUID = -7238753179865144234L;

    public static final int GAME_WIDTH = 640;
    public static final int GAME_HEIGHT = 480;

    /**
     * 双缓存
     */
    Image offScreenImage = null;

    public void init() {
        this.setTitle("Tank War v1.0");
        this.setLocation(500, 500);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                myTank.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                myTank.keyReleased(e);
            }
        });
        new Thread(new PaintThread()).start();
    }

    Tank myTank = new Tank(50, 50, true, this);

    Tank enemyTank = new Tank(200, 200, false, this);

    @Override
    public void paint(Graphics g) {
        Color c = g.getColor();
        /* 画背景 */
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        /* 画自己的坦克 */
        myTank.draw(g);
        /* 敌人坦克 */
        enemyTank.draw(g);
        g.setColor(c);
    }

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        final Graphics gOffScreen = offScreenImage.getGraphics();
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    public static void main(String[] args) {
        TankClient tankClient = new TankClient();
        tankClient.init();
    }

    private class PaintThread implements Runnable {

        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(1000 / 50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
