package tankwar;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

/**
 * @Author 李新栋 [lxd3808@163.com]
 * @Date 2018/10/13 10:42
 */
public class Tank {
    private static final int X_SPEED = 5;
    private static final int Y_SPEED = 5;

    private static final Random random = new Random();

    TankClient tankClient;
    /**
     * 位置坐标
     */
    private int x, y;
    /**
     * 直径
     */
    private int size = 30;

    private boolean bL = false, bR = false, bU = false, bD = false;
    /**
     * 8个方向, 默认为STOP
     */
    private Direction dir = Direction.STOP;
    /**
     * 上次移动方向
     */
    private Direction barrelDir = Direction.RIGHT;

    /**
     * 玩家标识
     */
    private boolean player = false;

    private int enemyStep = 3;

    /**
     * 坦克是否被打
     */
    private boolean live = true;

    private boolean waitDel = false;

    private List<Missile> missileList = new LinkedList<>();

    private Explode explode;

    public Tank(int x, int y, boolean player) {
        this.x = x;
        this.y = y;
        this.player = player;
//        this.size = size;
    }

    public Tank(int x, int y, boolean player, TankClient tankClient) {
        this(x, y, player);
        this.tankClient = tankClient;
    }

    public static Tank newEnemy(TankClient tankClient) {
        final Tank tank = new Tank(random.nextInt(TankClient.GAME_WIDTH), random.nextInt(TankClient.GAME_HEIGHT), false, tankClient);
        tank.setDir(Direction.values()[random.nextInt(Direction.values().length)]);
        return tank;
    }


    public void draw(Graphics graphics) {
        if (explode != null) {
            explode.draw(graphics);
        }
        if (!isLive()) {
            if(!player && !explode.isLive()){
//                tankClient.getTanks().remove(this);
                setWaitDel(true);
            }
            return;
        }
        Color c = graphics.getColor();
        if (player) {
            graphics.setColor(Color.RED);
        } else {
            graphics.setColor(Color.LIGHT_GRAY);
        }
        graphics.fillOval(x, y, size, size);
        graphics.setColor(c);
        locateDirection();
        drawBarrel(graphics);
        move();
        final ListIterator<Missile> missileListIterator = missileList.listIterator();
        while (missileListIterator.hasNext()) {
            Missile missile = missileListIterator.next();
            missile.hitTanks(tankClient.getTanks());
            missile.hitTank(tankClient.myTank);
            if (missile.isLive()) {
                missile.draw(graphics);
            } else {
                missileListIterator.remove();
            }
        }
    }

    public void drawBarrel(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        switch (barrelDir) {
            case UP:
                graphics.drawLine(x + size / 2, y + size / 2, x + size / 2, y);
                break;
            case LEFT_UP:
                graphics.drawLine(x + size / 2, y + size / 2, (int) (x + (size / 2 - size / 2 * Math.cos(Math.PI / 4))), (int) (y + (size / 2 - size / 2 * Math.cos(Math.PI / 4))));
                break;
            case RIGHT_UP:
                graphics.drawLine(x + size / 2, y + size / 2, (int) (x + (size / 2 + size / 2 * Math.cos(Math.PI / 4))), (int) (y + (size / 2 - size / 2 * Math.cos(Math.PI / 4))));
                break;
            case LEFT:
                graphics.drawLine(x + size / 2, y + size / 2, x, y + size / 2);
                break;
            case RIGHT:
                graphics.drawLine(x + size / 2, y + size / 2, x + size, y + size / 2);
                break;
            case LEFT_DOWN:
                graphics.drawLine(x + size / 2, y + size / 2, (int) (x + (size / 2 - size / 2 * Math.cos(Math.PI / 4))), (int) (y + (size / 2 + size / 2 * Math.cos(Math.PI / 4))));
                break;
            case RIGHT_DOWN:
                graphics.drawLine(x + size / 2, y + size / 2, (int) (x + (size / 2 + size / 2 * Math.cos(Math.PI / 4))), (int) (y + (size / 2 + size / 2 * Math.cos(Math.PI / 4))));
                break;
            case DOWN:
                graphics.drawLine(x + size / 2, y + size / 2, x + size / 2, y + size);
                break;
        }
    }

    public Missile fire() {
        Missile missile = new Missile(x + size / 2, y + size / 2, barrelDir, this.player);
        missileList.add(missile);
        return missile;
    }

    /**
     * 判断当前方向
     */
    public void locateDirection() {
        if (!player) return;
        if (!bU && !bD && !bL && !bR) {
            dir = Direction.STOP;
        } else if (bU && !bD && !bL && !bR) {
            dir = Direction.UP;
        } else if (!bU && bD && !bL && !bR) {
            dir = Direction.DOWN;
        } else if (!bU && !bD && bL && !bR) {
            dir = Direction.LEFT;
        } else if (!bU && !bD && !bL && bR) {
            dir = Direction.RIGHT;
        } else if (bU && !bD && bL && !bR) {
            dir = Direction.LEFT_UP;
        } else if (bU && !bD && !bL && bR) {
            dir = Direction.RIGHT_UP;
        } else if (!bU && bD && bL && !bR) {
            dir = Direction.LEFT_DOWN;
        } else if (!bU && bD && !bL && bR) {
            dir = Direction.RIGHT_DOWN;
        }
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;

        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_SPACE:
                fire();
                break;
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                break;
        }
    }

    public void move() {
        switch (dir) {
            case UP:
                y -= Y_SPEED;
                break;
            case LEFT_UP:
                x -= X_SPEED * 1.414 / 2;
                y -= Y_SPEED * 1.414 / 2;
                break;
            case RIGHT_UP:
                x += X_SPEED * 1.414 / 2;
                y -= Y_SPEED * 1.414 / 2;
                break;
            case LEFT:
                x -= X_SPEED;
                break;
            case RIGHT:
                x += X_SPEED;
                break;
            case LEFT_DOWN:
                x -= X_SPEED * 1.414 / 2;
                y += Y_SPEED * 1.414 / 2;
                break;
            case RIGHT_DOWN:
                x += X_SPEED * 1.414 / 2;
                y += Y_SPEED * 1.414 / 2;
                break;
            case DOWN:
                y += Y_SPEED;
                break;
            case STOP:
                break;
        }
        if (this.dir != Direction.STOP) {
            barrelDir = this.dir;
        }
        if (this.x > TankClient.GAME_WIDTH) {
            x = 0;
        } else if (this.x < 0) {
            x = TankClient.GAME_WIDTH;
        }
        if (this.y > TankClient.GAME_HEIGHT) {
            y = 0;
        } else if (this.y < 0) {
            y = TankClient.GAME_HEIGHT;
        }

        if (!player){
            if (enemyStep <= 0){
                enemyStep = random.nextInt();
                dir = Direction.values()[random.nextInt(Direction.values().length)];
            }
            enemyStep--;
            if (random.nextInt(100) > 95){
                fire();
            }
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isPlayer() {
        return player;
    }

    public void setPlayer(boolean player) {
        this.player = player;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, size, size);
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    public Explode getExplode() {
        return explode;
    }

    public void setExplode(Explode explode) {
        this.explode = explode;
    }

    public boolean isWaitDel() {
        return waitDel;
    }

    public void setWaitDel(boolean waitDel) {
        this.waitDel = waitDel;
    }

    enum Direction {
        UP,
        LEFT_UP,
        RIGHT_UP,
        LEFT,
        RIGHT,
        LEFT_DOWN,
        RIGHT_DOWN,
        DOWN,
        STOP
    }

}
