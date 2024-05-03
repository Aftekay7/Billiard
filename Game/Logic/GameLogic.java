package Game.Logic;

import Game.Structures.Ball;
import Game.Structures.Table;
import Game.Structures.Wall;
import Physics.Collidable;
import Physics.Vector;

public class GameLogic {

    //ticks per second
    private final int ticksPerSecond = 1;
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
            checkCollisions();

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
            }

            index++;
            c = gameObj[index];
        }

    }


    /**
     * iterates over all (other) objects that are used in the game and checks for collisions.
     */
    private void checkCollisions() {
        Collidable[] gameObj = table.getGameObjects();

        int index = 0;
        Collidable b = gameObj[0];
        while (b instanceof Ball ball) {

            for (Collidable c : gameObj) {
                if (c instanceof Wall) {
                    ball.collision((Wall) c);

                } else if (!b.equals(c) && ((Ball) c).isInGame()) {
                    ball.collision((Ball) c);
                }
            }


            index++;
            b = gameObj[index];
        }



    }


}
