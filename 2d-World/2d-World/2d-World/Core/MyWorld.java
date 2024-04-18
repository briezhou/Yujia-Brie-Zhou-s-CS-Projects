package byow.Core;

public interface MyWorld {

    /**
     * use a int[][] world to represent the world
     * 0 for NOTHING
     * 1 for floor
     * 2 for wall
     * 3 for mark of room (test only)
     */
    public int[][] getWorld();
    public int whatBlock(int x, int y);
    public int getWidth();
    public int getHeight();
}
