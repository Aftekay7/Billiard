package Game.Logic;

import Game.Structures.Ball;
import Game.Structures.Table;
import Game.Structures.Wall;
import Physics.Collidable;
import Physics.Vector;

public class GameLogic {
    //ticks per second
    private final int ticksPerSecond = 100;
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
        b.setVelocity(new Vector(30,0));

        while (running) {
            updatePositions();
        }
    }


    /**
     *  Updates the position/velocities of the balls and checks for collisions.
     */
    public void updatePositions() {
        Collidable[] gameObj = this.table.getGameObjects();

        for (Collidable c : gameObj) {
            if (c instanceof Ball) {
                Ball b = (Ball) c;
                if (b.isInGame()) {
                    b.updatePos();
                    checkCollisions(b);
                }
            }
        }

        try {
            Thread.sleep(tickSpeed); // Verzögerung von 1000 Millisekunden (1 Sekunde)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private void checkCollisions(Ball b) {
        Collidable[] gameObj = table.getGameObjects();

        for (Collidable c : gameObj) {
            if (c instanceof Wall) {
                b.collision((Wall) c);

            } else if (!b.equals(c) && ((Ball) c).isInGame()) {
                b.collision((Ball) c);
            }
        }


    }


}
