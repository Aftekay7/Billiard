package Game.Logic;

import Game.Structures.*;
import Physics.*;

public class GameLogic {

    //ticks per second
    private final int ticksPerSecond = 128;
    private final int tickSpeed = 1000 / ticksPerSecond;
    private Table table;
    private boolean running;

    public GameLogic(Table table) {
        this.table = table;
        this.running = true;
    }


    /**
     * starts the game-process
     */
    public void startGame() {
        this.table.useTriangle();

        Ball b = (Ball) table.getGameObjects()[0];
        b.setVelocity(new Vector(30, 0));

        while (running) {
            updatePositions();

            try {
                Thread.sleep(tickSpeed);
            } catch (
                    InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Updates the position/velocities of the balls and checks for collisions.
     */
    public void updatePositions() {
        Collidable[] gameObj = this.table.getGameObjects();
        Ball b;
        int index = 0;
        Collidable c = gameObj[index];
        while (c instanceof Ball) {
            b = (Ball) c;
            if (b.isInGame()) {
                b.updatePos();
                checkCollision(b);
            }

            index++;
            c = gameObj[index];
        }

    }

    private void checkCollision(Ball b) {
        Collidable[] gameObj = this.table.getGameObjects();
        for (Collidable c : gameObj) {
            if (c instanceof Ball && c != b) {
                Ball ball = (Ball) c;

                if (ball.isInGame()) {
                    b.collision(ball);
                }
            } else if (c instanceof Wall){
                Wall wall = (Wall) c;
                b.collision(wall);
            }
        }
    }


    /**
     * iterates over all (other) objects that are used in the game and checks for collisions.
     * TODO
     */
    /*
    private void checkCollisions() {
        Collidable[] gameObj = table.getGameObjects();

        Ball col1 = null;
        Collidable col2 = null;
        float ld;
        float ld_min = 2;

        int index = 0;
        Collidable b = gameObj[0];

        //check for all balls for the first collision that happens
        while (b instanceof Ball ball) {
            ball = (Ball) b;

            for (Collidable c : gameObj) {
                if (c instanceof Wall) {
                    ld = ball.getEarliestCollision((Wall) c);
                    if (ld < ld_min) {
                        ld_min = ld;
                        col1 = ball;
                        col2 = c;
                    }

                } else if (!b.equals(c) && ((Ball) c).isInGame()) {
                    ld = ball.getEarliestCollision((Ball) c);
                    if (ld < ld_min) {
                        ld_min = ld;
                        col1 = ball;
                        col2 = c;
                    }
                }
            }
            index++;
            b = gameObj[index];
        }

        if (col1 != null && ld_min <= 1) {
            if (col2 instanceof Ball) {
                Ball ball = (Ball) col2;
                col1.collision(ball);
            } else {
                Wall wall = (Wall) col2;
                col1.collision(wall);
            }


            index = 0;
            b = gameObj[0];
            while (b instanceof Ball) {

                if (b != col1 && b != col2) {
                    ((Ball) b).updatePos();
                }

                index++;
                b = gameObj[index];
            }
        } else {
            updatePositions();
        }


    }
    */


}
