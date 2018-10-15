package tankwar;

import java.awt.*;

/**
 * @Author 李新栋 [lxd3808@163.com]
 * @Date 2018/10/15 9:30
 */
public class Explode {
    private int[] diameter = {4, 7, 12, 18, 26, 32, 49, 30, 14, 6, 1};
    private boolean live = true;
    private int x, y;
    private int step = 0;

    public Explode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics graphics){
        if (!live) return;
        if (step == diameter.length) {
            live = false;
            step = 0;
            return;
        }
        Color c = graphics.getColor();
        graphics.setColor(Color.ORANGE);
        graphics.fillOval(x, y, diameter[step], diameter[step]);
        step++;
        graphics.setColor(c);
    }


    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }
}
