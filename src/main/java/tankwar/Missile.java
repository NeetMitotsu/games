package tankwar;

import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author 李新栋 [lxd3808@163.com]
 * @Date 2018/10/13 13:49
 */
public class Missile {
    /**
     * 炮弹位置
     */
    private int x, y;
    /**
     * 炮弹方向
     */
    private Tank.Direction direction;
    /**
     * 炮弹速度
     */
    private int speed = 30;
    /**
     * 炮弹直径
     */
    private int size = 5;

    private boolean live = true;

    private boolean player;

    public Missile(int x, int y, Tank.Direction direction, boolean player) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.player = player;
    }

    public void draw(Graphics graphics) {
        Color c = graphics.getColor();
        graphics.setColor(Color.BLACK);
        graphics.fillOval(x, y, size, size);
        graphics.setColor(c);
        move();

    }

    private void move() {
        switch (direction) {
            case UP:
                y -= speed;
                break;
            case LEFT_UP:
                x -= speed * 1.414 / 2;
                y -= speed * 1.414 / 2;
                break;
            case RIGHT_UP:
                x += speed * 1.414 / 2;
                y -= speed * 1.414 / 2;
                break;
            case LEFT:
                x -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
            case LEFT_DOWN:
                x -= speed * 1.414 / 2;
                y += speed * 1.414 / 2;
                break;
            case RIGHT_DOWN:
                x += speed * 1.414 / 2;
                y += speed * 1.414 / 2;
                break;
            case DOWN:
                y += speed;
                break;
        }
        if (x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
            live = false;
        }
    }

    public boolean hitTank(Tank tank) {
        if (this.getRect().intersects(tank.getRect()) && tank.isLive() && this.player != tank.isPlayer()) {
            this.setLive(false);
            tank.setLive(false);
            tank.setExplode(new Explode(x, y));
            return true;
        }
        return false;
    }

    public boolean hitTanks(List<Tank> tankList) {
        AtomicBoolean flag = new AtomicBoolean(false);
        tankList.forEach(tank -> {
            if (hitTank(tank)) {
                flag.set(true);
                return;
            }
        });
        return flag.get();
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    /**
     * 获取外围方块
     *
     * @return
     */
    public Rectangle getRect() {
        return new Rectangle(x, y, size, size);
    }
}
