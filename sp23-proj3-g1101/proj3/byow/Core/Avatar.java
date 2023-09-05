package byow.Core;

import java.util.Random;

public class Avatar {
    private int x;
    private int y;

    public Avatar(MyWorld world, long seed) {
        Random rand = new Random(seed);
        x = rand.nextInt(world.getWidth() - 1) + 1;
        y = rand.nextInt(world.getHeight() - 1) + 1;
        while (world.whatBlock(x, y) != 1) {
            x = rand.nextInt(world.getWidth() - 1) + 1;
            y = rand.nextInt(world.getHeight() - 1) + 1;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveRight() {
        x += 1;
    }

    public void moveLeft() {
        x -= 1;
    }

    public void moveUp() {
        y += 1;
    }

    public void moveDown() {
        y -= 1;
    }
}
