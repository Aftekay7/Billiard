package Game.Structures;

import Physics.*;

import java.awt.*;

/**
 * implements a billiard-table
 * the coordinate-system we're using places the origin in the bottom left corner as opposed to the coordinate system
 * the JFrame class places the origin in the top(!) left corner. Thus, we have to convert our position-vectors when we
 * want to draw the corresponding objects (see TODO: scaling function)
 */
public class Table {

    private final int playfield_HEIGHT = 1120;
    private final int playfield_WIDTH = 2240;

    //contains all (collision relevant) objects that are used in the game
    private Collidable[] gameObjects;
    private Color color_playfield;
    private Color color_walls;
    private Color color_holes;

    public Table() {
        Collidable[] ballSet = createBallSet();
        Collidable[] walls = createWalls();

        this.gameObjects = new Collidable[ballSet.length + walls.length];
        System.arraycopy(ballSet, 0, gameObjects, 0, ballSet.length);
        System.arraycopy(walls, 0, gameObjects, ballSet.length, walls.length);

        //set colors for the table-components
        this.color_playfield = new Color(0, 150, 0);
        this.color_walls = new Color(120, 80, 0);
        this.color_holes = new Color(0, 0, 0);


    }


    /**
     * Aligns the Balls in the starting-triangle/sets the coordinates of the balls accordingly
     */
    public void useTriangle() {
        /*      0 (white Ball)
                1
               2 3
             4  5  6
           7  8  9  10
        11 12  13 14  15
           */
        Ball b = (Ball) gameObjects[0];
        int radius = b.getRadius();
        int diameter = radius * 2;

        Vector P_0 = new Vector(playfield_WIDTH / 4F, playfield_HEIGHT / 2F);
        Vector P_1 = new Vector((playfield_WIDTH * 3) / 4F, playfield_HEIGHT / 2F);
        Vector P_2 = new Vector(P_1.x + diameter, P_1.y - radius - 10);
        Vector P_3 = new Vector(P_2.x, P_2.y + diameter + 10);
        Vector P_4 = new Vector(P_2.x + diameter, P_1.y - diameter - 10);
        Vector P_5 = new Vector(P_4.x, P_1.y);
        Vector P_6 = new Vector(P_5.x, P_1.y + diameter + 10);
        Vector P_7 = new Vector(P_4.x + diameter, P_4.y - radius - 10);
        Vector P_8 = new Vector(P_7.x, P_2.y);
        Vector P_9 = new Vector(P_7.x, P_3.y);
        Vector P_10 = new Vector(P_7.x, P_9.y + diameter + 10);
        Vector P_11 = new Vector(P_7.x + diameter, P_7.y - radius - 10);
        Vector P_12 = new Vector(P_11.x, P_4.y);
        Vector P_13 = new Vector(P_11.x, P_5.y);
        Vector P_14 = new Vector(P_11.x, P_6.y);
        Vector P_15 = new Vector(P_11.x, P_14.y + diameter + 10);

        //P
        Vector[] coords = {P_0, P_11, P_5, P_15, P_1, P_2, P_3, P_4, P_6, P_7, P_8, P_9, P_10, P_12, P_13, P_14};

        //white Ball
        b.setCenter((int) P_0.x, (int) P_0.y);
        b.setInGame(true);

        //full 1 (yellow)
        b = (Ball) gameObjects[1];
        b.setCenter(P_11);
        b.setInGame(true);

        //half 11 (red)
        b = (Ball) gameObjects[11];
        b.setCenter(P_15);
        b.setInGame(true);

        b = (Ball) gameObjects[8];
        b.setCenter(P_5);
        b.setInGame(true);

        for (int i = 4; i < coords.length; i++) {
            int index = (int) (Math.random() * (coords.length - 1));


            while (index < 16) {
                b = (Ball) gameObjects[index];

                if (!b.isInGame()) {
                    b.setCenter(coords[i]);
                    b.setInGame(true);
                    break;
                }
                index++;
                if (index == 16) {
                    index = 0;
                }
            }
        }
    }


    /**
     * initializes a set of billiard-balls with set colours and the default position (0,0)
     *
     * @return Array of balls
     */
    private Collidable[] createBallSet() {
        Collidable[] ballSet = new Collidable[16];

        //define the used colours
        Color white = new Color(255, 255, 255);
        Color yellow = new Color(200, 200, 0);
        Color blue = new Color(0, 0, 255);
        Color red = new Color(0xFF, 0, 0);
        Color purple = new Color(200, 0, 255);
        Color orange = new Color(0xFF, 127, 0);
        Color green = new Color(0, 180, 0);
        Color brown = new Color(150, 90, 0);
        Color black = new Color(0, 0, 0);


        //white Ball
        Ball wb = new Ball(BallNumber.WHITE, white);
        wb.setCenter(new Vector(100, 100));
        ballSet[0] = wb;

        //full 1 (yellow)
        Ball ybf = new Ball(BallNumber.ONE, yellow);
        ybf.setCenter(new Vector(200, 100));
        ballSet[1] = ybf;

        //full 2 (blue)
        Ball bbf = new Ball(BallNumber.TWO, blue);
        bbf.setCenter(new Vector(300, 100));
        ballSet[2] = bbf;

        //full 3 (red)
        Ball rbf = new Ball(BallNumber.THREE, red);
        rbf.setCenter(new Vector(400, 100));
        ballSet[3] = rbf;

        //full 4 (purple)
        Ball pbf = new Ball(BallNumber.FOUR, purple);
        pbf.setCenter(new Vector(500, 100));
        ballSet[4] = pbf;

        //full 5 (orange)
        Ball obf = new Ball(BallNumber.FIVE, orange);
        obf.setCenter(new Vector(600, 100));
        ballSet[5] = obf;

        //full 6 (green)
        Ball gbf = new Ball(BallNumber.SIX, green);
        gbf.setCenter(new Vector(700, 100));
        ballSet[6] = gbf;

        //full 7 (brown)
        Ball brbf = new Ball(BallNumber.SEVEN, brown);
        brbf.setCenter(new Vector(800, 100));
        ballSet[7] = brbf;

        // 8 (black)
        Ball bb = new Ball(BallNumber.EIGHT, black);
        bb.setCenter(new Vector(100, 800));
        ballSet[8] = bb;

        //half 9 (yellow)
        Ball ybh = new Ball(BallNumber.HALF_NINE, yellow);
        ybh.setCenter(new Vector(200, 800));
        ballSet[9] = ybh;

        //half 10 (blue)
        Ball bbh = new Ball(BallNumber.HALF_TEN, blue);
        bbh.setCenter(new Vector(300, 800));
        ballSet[10] = bbh;

        //half 11 (red)
        Ball rbh = new Ball(BallNumber.HALF_ELEVEN, red);
        rbh.setCenter(new Vector(400, 800));
        ballSet[11] = rbh;

        //half 12 (purple)
        Ball pbh = new Ball(BallNumber.HALF_TWELVE, purple);
        pbh.setCenter(new Vector(500, 800));
        ballSet[12] = pbh;

        //half 13 (orange)
        Ball obh = new Ball(BallNumber.HALF_THIRTEEN, orange);
        obh.setCenter(new Vector(600, 800));
        ballSet[13] = obh;

        //half 14 (green)
        Ball gbh = new Ball(BallNumber.HALF_FOURTEEN, green);
        gbh.setCenter(new Vector(700, 800));
        ballSet[14] = gbh;

        //half 15 (brown)
        Ball brbh = new Ball(BallNumber.HALF_FIFTEEN, brown);
        brbh.setCenter(new Vector(800, 800));
        ballSet[15] = brbh;

        return ballSet;
    }


    /**
     * initializes the walls that are used
     * TODO: Coordinates for the single walls have to be set
     *
     * @return Array of walls
     */
    private Collidable[] createWalls() {
        Collidable[] wallSet = new Collidable[6];
        Vector e1;
        Vector e2;

        //init all 6 walls, dependent on the Coordinates

        //wall 1 (left vertical side)
        e1 = new Vector(0, 95);
        e2 = new Vector(0, 1025);
        Vector[] edges_w1 = {e1, e2};
        wallSet[0] = new Wall(edges_w1);

        //wall 2 (right vertical side)
        e1 = new Vector(2240, 95);
        e2 = new Vector(2240, 1025);
        Vector[] edges_w2 = {e1, e2};
        wallSet[1] = new Wall(edges_w2);

        /*
        //wall 3 (lower horizontal side)
        e1 = new Vector(0, 0);
        e2 = new Vector(2240, 0);
        Vector[] edges_w3 = {e1, e2};
        wallSet[2] = new Wall(edges_w3);
         */

        /*
        //wall 4 (upper horizontal side)
        e1 = new Vector(0, 1120);
        e2 = new Vector(2240, 1120);
        Vector[] edges_w4 = {e1,e2};
        wallSet[3] = new Wall(edges_w4);
         */

        //wall 5 (split horizontal side top left)
        e1 = new Vector(95, 1120);
        e2 = new Vector(1048, 1120);
        Vector[] edges_w4_1 = {e1, e2};
        wallSet[2] = new Wall(edges_w4_1);

        //wall 6 (split horizontal side top right)
        e1 = new Vector(1193, 1120);
        e2 = new Vector(2145, 1120);
        Vector[] edges_w4_2 = {e1, e2};
        wallSet[3] = new Wall(edges_w4_2);

        //wall 7 (split horizontal side bottom left)
        e1 = new Vector(95, 0);
        e2 = new Vector(1048, 0);
        Vector[] edges_w3_1 = {e1, e2};
        wallSet[4] = new Wall(edges_w3_1);

        //wall 8 (split horizontal side bottom right)
        e1 = new Vector(1193, 0);
        e2 = new Vector(2145, 0);
        Vector[] edges_w3_2 = {e1, e2};
        wallSet[5] = new Wall(edges_w3_2);

        return wallSet;
    }

    public Color getColor_holes() {
        return color_holes;
    }

    public Color getColor_playfield() {
        return color_playfield;
    }

    public Color getColor_walls() {
        return color_walls;
    }

    public Collidable[] getGameObjects() {
        return gameObjects;
    }


    private Collidable[] createTestBall() {
        Collidable[] c = new Collidable[1];
        Ball b = new Ball(BallNumber.WHITE, Color.WHITE);
        b.setCenter(new Vector(30, 30));
        b.setInGame(true);
        b.setVelocity(new Vector(10, 10));
        c[0] = b;
        return c;
    }
}
