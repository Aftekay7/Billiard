package Game.Structures;

import Physics.*;
import java.awt.*;

/**
 * Implements a Ball
 */
public class Ball extends Circle {
    private int weight = 170; //in gramm
    private Vector velocity = new Vector(0,0);
    private BallNumber number;
    private Color color;
    private boolean inGame = false;

    /**
     * simulates a (central) hit on the ball and updates its position
     *
     * @param hit defines the direction and the velocity (length of the vector)
     */
    public void hitBall(Vector hit) {
    }




    /**
     * Simulates a collision with a wall
     *  TODO: Fix collision Bug. -> if ball collides and is "inside" the wall, reset position to the realistic (line/circumference) collision point.
     * @param wall
     */
    public void collision(Wall wall) {

        //check if the ball hits the wall. The return is the closest point of the wall to the center of the ball
        Vector isec_ball_wall = super.intersects(wall);
        if (isec_ball_wall == null) {
            return;
        }

        //resetPosition(wall);

        //construct new base for updating the velocity
        isec_ball_wall.sub(center);
        Vector b2 = isec_ball_wall;
        b2.normalize();

        Vector b1 = b2.getOrthogonal();

        //calculate the lc to determine how ball is reflected
        Vector lc_velocity = Physics.solveLS(b1,b2,this.velocity);

        Vector velocity_new = b1.copy();
        velocity_new.scale(lc_velocity.x);
        Vector buf = b2.copy();

        //scale with (-1) because no velocity is absorbed by the wall.
        buf.scale(lc_velocity.y * -1);

        velocity_new.add(buf);

        this.velocity = velocity_new;

    }

    public void updatePos() {
        float brakes = 0.0027F;
        this.center.add(this.velocity);
        this.velocity.x -= (this.velocity.x * brakes);
        this.velocity.y -= (this.velocity.y * brakes);
    }


    /**
     * simulates the collision with another ball.
     *  TODO: Fix collision Bug. -> if ball collides and is "inside" the other ball, reset positions to the realistic (insersection of balls) collision point.
     * @param ball
     */
    public void collision(Ball ball) {
        // We want to build a base with the collision-tangent as the first base vector and
        // the collision-normal as the second base vector to calculate exchanged velocities.

        //determine the collision normal.
        Vector b2 = ball.center.copy();
        b2.sub(this.center);
        // b2 => tc + v = to => to-tc = v

        //check if balls collide.
        if (b2.length() <= (ball.getRadius() + this.getRadius())) {

            //normalize, so the vectors are not scaled.
            b2.normalize();

            //construct an orthonormal vector (on the right-hand-side) of the normal vector.
            Vector b1 = new Vector(0, 0);
            b1.x = b2.y;
            b1.y = b2.x * -1;

            //determine the linear-combination of the velocity vectors using the normal/orthogonal vectors as the base
            Vector lc_vt = Physics.solveLS(b1, b2,this.velocity);
            Vector lc_vo = Physics.solveLS(b1, b2, ball.velocity);


            /*
                if two spheres collide, the parts of the velocity-vectors, that are aligned with the normal vector,
                are exchanged between the spheres. The parts of the velocity-vectors that are aligned with the orthogonal
                vector are not exchanged.
             */

            //update the velocity accordingly
            Vector vt_new = b1.copy();
            vt_new.scale(lc_vt.x);
            Vector buf = b2.copy();
            buf.scale(lc_vo.y);
            vt_new.add(buf);

            this.velocity = vt_new;

            Vector vo_new = b1.copy();
            vo_new.scale(lc_vo.x);
            buf = b2.copy();
            buf.scale(lc_vt.y);
            vo_new.add(buf);

            ball.setVelocity(vo_new);
        }
    }


    public Ball(BallNumber bn, Color color) {
        this.number = bn;
        this.color = color;
        super.setCenter(new Vector(0, 0));
        super.setRadius(29);
    }

    public Color getColor() {
        return color;
    }

    public boolean isHalf() {
        return number.isHalf();
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setCenter(int x, int y) {
        this.center.x = x;
        this.center.y = y;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }


    public Vector getVelocity() {
        return velocity;
    }


    /**
     * resets the position of the Ball after a collision with a wall
     * new position is the exact position where the ball would've hit the wall (compensating the inaccuracy of the ticks)
     */
    private void resetPosition(Line line) {

        //construct the line that is orthonormal to the wall with a distance of (radius - (center/intersec(ball/wall))
        Vector orth = line.getDirection_vec().getOrthogonal();
        orth.normalize();
        orth.scale(getRadius() + 1);

        Vector s = line.getSupport_vec().copy();
        s.add(orth);

        Line wall_reset = new Line(s,line.getDirection_vec());
        Line center_reset = new Line(this.center, this.velocity);

        this.center = center_reset.intersects(wall_reset);
    }
}

