package byow.Core;

import java.lang.invoke.WrongMethodTypeException;
import java.util.Random;

public class Beijing implements MyWorld {
    private int[][] world;
    private int WIDTH;
    private int HEIGHT;
    private int MAXROOMSIZE;
    private int numOfRoom;
    Random random;

    @Override
    public int[][] getWorld() {
        return world;
    }

    @Override
    public int whatBlock(int x, int y) {
        return world[x][y];
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    Room[] rooms;
    int xCross;
    int yCross;

    private class Room {
        /**
         * x center x coordinate of room
         * y center y coordinate of room
         * a width of room
         * b height of room
         * k quadrant
         * 2|1
         * ---
         * 3|4
         */
        int x, y, a, b, k;


        public Room(int x, int y, int a, int b) {
            this.x = x;
            this.y = y;
            this.a = a;
            this.b = b;
            this.k = getK();
        }

        private int getK() {
            if (x > xCross && y > yCross) {
                return 1;
            }
            if (x < xCross && y > yCross) {
                return 2;
            }
            if (x < xCross && y < yCross) {
                return 3;
            }
            if (x > xCross && y < yCross) {
                return 4;
            }
            return 0;
        }
    }

    public Beijing(long seed, int width, int height, int maxroomsize) {
        this.random = new Random(seed);
        this.HEIGHT = height;
        this.WIDTH = width;
        this.MAXROOMSIZE = maxroomsize;
        //average coverage of the world is 0.25?
        this.numOfRoom = WIDTH * HEIGHT / (maxroomsize * maxroomsize) / 2;
        this.numOfRoom = (int) ((double)numOfRoom * (0.3333 + random.nextDouble() *2 / 3));
        this.rooms = new Room[numOfRoom];
        this.world = new int[WIDTH + 2][HEIGHT + 2];
        generateCross();
        generateRoom();
        generateHallway();
        generateWall();
    }

    private void generateCross() {
        this.xCross = WIDTH / 2 - 5 + random.nextInt(10);
        this.yCross = HEIGHT / 2 - 5 + random.nextInt(10);
        modifyWorldCross();
    }

    private void modifyWorldCross() {
        for (int i = 2; i < WIDTH; i++) {
            world[i][yCross] = 1;
        }
        for (int j = 2; j < HEIGHT; j++) {
            world[xCross][j] = 1;
        }
    }


    private void generateRoom() {
        int count = 0;
        while (count < numOfRoom) {
            int x = 3 + random.nextInt(WIDTH - 3);
            int y = 3 + random.nextInt(HEIGHT - 3);
            int a = 3 + random.nextInt(MAXROOMSIZE - 2);
            int b = 3 + random.nextInt(MAXROOMSIZE - 2);
            if (checkRoomValid(x, y, a, b)) {
                Room room = new Room(x, y, a, b);
                rooms[count] = room;
                modifyWorldRoom(x, y, a, b);
                count += 1;
            }
        }
        //markRooms();
    }

    private void markRooms() {
        for (Room room : rooms) {
            world[room.x][room.y] = 3;
        }
    }

    private void modifyWorldRoom(int x, int y, int a, int b) {
        for (int i = x - (a - 1) / 2; i <= x + a / 2; i++) {
            for (int j = y - (b - 1) / 2; j <= y + b / 2; j++) {
                world[i][j] = 1;
            }
        }
    }

    private boolean checkRoomValid(int x, int y, int a, int b) {
        for (int i = x - (a - 1) / 2 - 1; i <= x + a / 2 + 1; i++) {
            for (int j = y - (b - 1) / 2 - 1; j <= y + b / 2 + 1; j++) {
                if (checkOutOfWorld(i, j) || world[i][j] == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkOutOfWorld(int i, int j) {
        return i <= 0 || i > WIDTH || j <= 0 || j > HEIGHT;
    }


    private void generateHallway() {
        for (Room room : rooms) {
            generateHallwayHelper(room);
        }
    }

    private void generateHallwayHelper(Room room) {
        int xHallway = room.x - (room.a - 1) / 2 + 1 + random.nextInt(room.a - 2);
        int yHallway = room.y - (room.b - 1) / 2 + 1 + random.nextInt(room.b - 2);
        if (room.k == 1) {
            generateHoriHallwaytoLeft(room, yHallway);
            generateVertiHallwaytoDown(room, xHallway);
        }
        if (room.k == 2) {
            generateHoriHallwaytoRight(room, yHallway);
            generateVertiHallwaytoDown(room, xHallway);
        }
        if (room.k == 3) {
            generateHoriHallwaytoRight(room, yHallway);
            generateVertiHallwaytoUp(room, xHallway);
        }
        if (room.k == 4) {
            generateHoriHallwaytoLeft(room, yHallway);
            generateVertiHallwaytoUp(room, xHallway);
        }
        if (room.k == 0) {
            throw new WrongMethodTypeException();
        }
    }

    private void generateVertiHallwaytoUp(Room room, int xHallway) {
        int j = room.y + (room.b) / 2 + 1;
        while (world[xHallway][j] != 1) {
            world[xHallway][j] = 1;
            j += 1;
        }
    }

    private void generateHoriHallwaytoRight(Room room, int yHallway) {
        int i = room.x + (room.a) / 2 + 1;
        while (world[i][yHallway] != 1) {
            world[i][yHallway] = 1;
            i += 1;
        }
    }

    private void generateVertiHallwaytoDown(Room room, int xHallway) {
        int j = room.y - (room.b - 1) / 2 - 1;
        while (world[xHallway][j] != 1) {
            world[xHallway][j] = 1;
            j -= 1;
        }
    }

    private void generateHoriHallwaytoLeft(Room room, int yHallway) {
        int i = room.x - (room.a - 1) / 2 - 1;
        while (world[i][yHallway] != 1) {
            world[i][yHallway] = 1;
            i -= 1;
        }
    }

    private void generateWall() {
        for (int i = 1; i < WIDTH + 1; i++) {
            for (int j = 1; j < HEIGHT + 1; j++) {
                if (isWall(i, j)) {
                    world[i][j] = 2;
                }
            }
        }
    }

    private boolean isWall(int i, int j) {
        if (world[i][j] != 0) {
            return false;
        }
        if (world[i - 1][j - 1] == 1) {
            return true;
        }
        if (world[i - 1][j] == 1) {
            return true;
        }
        if (world[i - 1][j + 1] == 1) {
            return true;
        }
        if (world[i][j - 1] == 1) {
            return true;
        }
        if (world[i][j + 1] == 1) {
            return true;
        }
        if (world[i + 1][j - 1] == 1) {
            return true;
        }
        if (world[i + 1][j] == 1) {
            return true;
        }
        if (world[i + 1][j + 1] == 1) {
            return true;
        }
        return false;
    }

}
