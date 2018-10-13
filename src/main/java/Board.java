/**
 * 画布类
 *
 * @Author 李新栋 [lxd3808@163.com]
 * @Date 2018/10/11 14:11
 */
public class Board {
    /**
     * 边框宽度
     */
    public static final int BOARD_LINE_WIDTH = 6;
    /**
     * 每个方块的边长
     */
    public static final int BLOCK_SIZE = 16;
    /**
     * 距离屏幕左侧的位置
     */
    public static final int BOARD_POSITION = 320;
    /**
     * 游戏画布宽度
     */
    public static final int BOARD_WIDTH = 10;
    /**
     * 游戏画布高度
     */
    public static final int BOARD_HEIGHT = 20;
    /**
     * 最小垂直距离
     */
    public static final int MIN_VERTICAL_MARGIN = 20;
    /**
     * 最小水平距离
     */
    public static final int MIN_HORIZONTAL_MARGIN = 20;
    /**
     * 矩阵块的水平和垂直块的数量
     */
    public static final int PIECE_BLOCKS = 5;

    /**
     * 块状态
     */
    private enum status {
        /**
         * 空白的位置
         */
        POS_FREE,
        /**
         * 填充过的位置
         */
        POS_FILLED
    }

    /**
     * 画布
     */
    private status mBoard[][] = new status[BOARD_WIDTH][BOARD_HEIGHT];
    /**
     * 当前形状
     */
    private Pieces mPieces;
    /**
     * 画布高度
     */
    private int mScreenHeight;

    /**
     * 初始化画布, 填充空白
     */
    private void initBoard() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                mBoard[i][j] = status.POS_FREE;
            }
        }
    }

    /**
     * 消除一行, 并将上一行下移
     *
     * @param pY
     */
    private void deleteLine(int pY) {
        for (int j = pY; j > 0; j--) {
            for (int i = 0; i < BOARD_WIDTH; i++) {
                mBoard[i][j] = mBoard[i][j - 1];
            }
        }
    }

    /**
     * 删除符合条件的行
     */
    public void deletePossibleLines() {
        for (int j = 0; j < BOARD_HEIGHT; j++) {
            int i = 0;
            while (i < BOARD_WIDTH) {
                if (mBoard[i][j] != status.POS_FILLED) break;
                i++;
            }
            if (i == BOARD_WIDTH) deleteLine(j);
        }
    }

    /**
     * 检查块是否为空
     *
     * @param pX 横坐标
     * @param pY 纵坐标
     * @return
     */
    public boolean isFreeBlock(int pX, int pY) {
        return mBoard[pX][pY] == status.POS_FREE;
    }

    /**
     * 检查块是否发生碰撞(是否可以移动), 可以返回true, 否则false
     *
     * @param pX        横坐标
     * @param pY        纵坐标
     * @param pPiece    块形状
     * @param pRotation 块旋转
     * @return
     */
    public boolean isPossibleMovement(int pX, int pY, int pPiece, int pRotation) {
        /* 检查与边框的碰撞 */
        for (int i1 = pX, i2 = 0; i1 < pX + PIECE_BLOCKS; i1++, i2++) {
            for (int j1 = pY, j2 = 0; j1 < pY + PIECE_BLOCKS; j1++, j2++) {
                /*检查是否超出画布*/
                if (i1 < 0 || i1 > BOARD_WIDTH - 1 || j1 > BOARD_HEIGHT - 1) {
                    if (mPieces.getBlockType(pPiece, pRotation, j2, i2) != 0) return false;
                }
                /*检查是否与已经存在的块发生碰撞*/
                if (j1 >= 0) {
                    if ((mPieces.getBlockType(pPiece, pRotation, j2, i2) != 0) && (!isFreeBlock(i1, j1))) {
                        return false;
                    }
                }
            }

        }
        return true;
    }

    /**
     * 填充块
     *
     * @param pX        横坐标
     * @param pY        纵坐标
     * @param pPiece    形状
     * @param pRotation 4种旋转
     */
    public void storePiece(int pX, int pY, int pPiece, int pRotation) {
        for (int i1 = pX, i2 = 0; i1 < pX + PIECE_BLOCKS; i1++, i2++) {
            for (int j1 = pY, j2 = 0; j1 < pY + PIECE_BLOCKS; j1++, j2++) {
                if (mPieces.getBlockType(pPiece, pRotation, j2, i1) != 0) {
                    mBoard[i1][j1] = status.POS_FILLED;
                }
            }
        }
    }

    /**
     * 判断游戏结束
     *
     * @return
     */
    public boolean isGameOver() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            if (mBoard[i][0] == status.POS_FILLED) return true;
        }
        return false;
    }

    /**
     * 返回指定块的水平像素位置
     *
     * @param pPos 水平位置
     * @return
     */
    public int getXPosInPixels(int pPos) {
        return BOARD_POSITION - BLOCK_SIZE * (BOARD_WIDTH / 2) + pPos * BLOCK_SIZE;
    }

    /**
     * 返回指定块的垂直像素位置
     *
     * @param pPos 水平位置
     * @return
     */
    public int getYPosInPixels(int pPos) {
        return (mScreenHeight - BLOCK_SIZE * BOARD_HEIGHT) + pPos * BLOCK_SIZE;
    }


}
