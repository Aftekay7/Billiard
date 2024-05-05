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
            long start = System.currentTimeMillis();
            updatePositions();
            long end = System.currentTimeMillis();

            System.out.println(end - start);

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
}
