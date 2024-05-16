package Game.Structures;

import Physics.*;
import java.awt.*;

/**
 * Implements a Ball
 */
public class Ball extends Circle {
    private int weight = 170; //in gramm
    private Vector velocity = new Vector(0, 0);
    private BallNumber number;
    private Color color;
    private boolean inGame = false;

    private float brakes = 0.01F;


    /**
     * simulates a (central) hit on the ball and updates its position
     *  TODO: implement
     * @param hit defines the direction and the velocity (length of the vector)
     */
    public void hitBall(Vector hit) {
    }


    /**
     * Simulates a collision with a wall
     *
     * @param wall
     */
    public void collision(Wall wall) {

        //TODO fix this stupid shit. If the ball is colliding with the ball, the collisionpoint cant be null.
        //checks if the ball is currently colliding with the wall
        if (super.intersects(wall) == null) {
            return;
        }

        //calculates the realistic collision-point of the ball with the wall.
        Vector isec_ball_wall = wall.intersects(this);


        if (isec_ball_wall == null) {
            return;
        }

        isec_ball_wall.sub(center);
        //construct new base for updating the velocity

        Vector b2 = isec_ball_wall;
        b2.normalize();

        Vector b1 = b2.getOrthogonal();

        //calculate the lc to determine how ball is reflected
        Vector lc_velocity = Physics.solveLS(b1, b2, this.velocity);

        Vector velocity_new = b1.copy();
        velocity_new.scale(lc_velocity.x);
        Vector buf = b2.copy();

        //scale with (-1) because no velocity is absorbed by the wall.
        buf.scale(lc_velocity.y * -1);

        velocity_new.add(buf);

        this.velocity = velocity_new;

    }

    public void updatePos() {
        brakes = 0;
        this.center.add(this.velocity);
        this.velocity.x -= (this.velocity.x * brakes);
        this.velocity.y -= (this.velocity.y * brakes);
    }


    /**
     * simulates the collision with another ball.
     *  TODO: Fix collision Bug. -> if ball collides and is "inside" the other ball, reset positions to the realistic (insersection of balls) collision point.
     *  TODO: calculate backwards where the spheres start to intersect and reset centers depending on their velocity
     *
     * @param ball
     */
    public void collision(Ball ball) {
        //break if balls don't intersect
        if (!super.intersects(ball)) {
            return;
        }

        resetPosition(ball);

        // We want to build a base with the collision-tangent as the first base vector and
        // the collision-normal as the second base vector to calculate exchanged velocities.

        //determine the collision normal.
        Vector b2 = ball.center.copy();
        b2.sub(this.center);
        // b2 => tc + v = to => to-tc = v


        //normalize, so the vectors are not scaled.
        b2.normalize();

        //construct an orthonormal vector (on the right-hand-side) of the normal vector.
        Vector b1 = new Vector(0, 0);
        b1.x = b2.y;
        b1.y = b2.x * -1;

        //determine the linear-combination of the velocity vectors using the normal/orthogonal vectors as the base
        Vector lc_vt = Physics.solveLS(b1, b2, this.velocity);
        Vector lc_vo = Physics.solveLS(b1, b2, ball.velocity);


            /*
                if two spheres collide, the parts of the velocity-vectors, that are aligned with the collision-normal,
                are exchanged between the spheres. The parts of the velocity-vectors that are aligned with
                the collision tangent are not exchanged.
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

    public void setCenter(float x, float y) {
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
    public void resetPosition(Line line) {

        //construct the line that is parallel to the wall with a distance of radius + 1

        //determine the support vector (-> line.support + radius)
        Vector orth = line.getDirection_vec().getOrthogonal();
        orth.normalize();
        orth.scale(getRadius());

        //decide if we are on the left or right side of the wall
        Vector vel = this.velocity.copy();

        //we have to go "backwards" on the trajectory of the ball
        vel.flip();

        //check if the vectors point in the same direction and flip if not (so we reset the position onto the correct side of the wall)
        float dotProd = Physics.dotProduct(vel, orth);
        if (dotProd < 0) {
            orth.flip();
        }

        //construct the support vector of the line parallel to the wall
        Vector sup = line.getSupport_vec().copy();
        //add the offset (radius +1)
        sup.add(orth);

        Line wall_reset = new Line(sup, line.getDirection_vec());
        Line center_reset = new Line(this.center, vel);

        this.center = center_reset.intersects(wall_reset);
    }


    /**
     * returns the scalar of the direction vectors of the earliest touching-point of the given balls
     *
     * @param ball
     */
    public void resetPosition(Ball ball) {
        //concept: follow the trajectory of both balls until the distance between both centers is equal to sum(radi)
        // -> length (traj_ball_1 - traj_ball_2) = ball_1.radius + ball_2.radius

        Vector S_1 = this.getCenter();
        Vector S_2 = ball.getCenter();
        Vector V_1 = this.velocity.copy();
        V_1.flip();
        Vector V_2 = ball.getVelocity().copy();
        V_2.flip();

        float A = (float) (Math.pow(V_1.x - V_2.x, 2) + Math.pow(V_1.y - V_2.y, 2));
        float B = 2 * ((V_1.x - V_2.x) * (S_1.x - S_2.x) + (V_1.y - V_2.y) * (S_1.y - S_2.y));
        float C = (float) (Math.pow(S_1.x - S_2.x, 2) + Math.pow(S_1.y - S_2.y, 2));

        float radii = (float) (Math.pow(ball.getRadius() + this.getRadius(), 2));

        float[] lds1 = Physics.ABCformula(A, B, C - radii);
        float[] lds2 = Physics.ABCformula(A, B, C + radii);
        float[][] lds = {lds1, lds2};

        float min = 2;
        for (int i = 0; i < 2; i++) {

            if (lds[i] != null) {
                for (float f : lds[i]) {
                    if (f < min && f > 0) {
                        min = f;
                    }
                }
            }

            if (lds1 == null && lds2 == null) {
                throw new IllegalStateException("dunno weird case look at it");
            }
        }

        V_1.scale(min);
        V_2.scale(min);
        this.center.add(V_1);
        ball.center.add(V_2);

    }


    public float getEarliestCollision(Wall wall) {
        if (Physics.dotProduct(this.velocity, wall.getDirection_vec()) == 0) {
            //trajectory is parallel to wall -> won't collide as we cant spawn inside the wall
            return 2;
        }
        Vector offset = wall.getDirection_vec().getOrthogonal();
        offset.normalize();
        offset.scale(super.getRadius());
        Vector sup = this.center.copy();
        sup.add(offset);

        Vector col1 = sup;
        Vector col2 = wall.getDirection_vec().copy();
        col2.flip();

        Vector sup_wall = wall.getSupport_vec();
        Vector sol = new Vector(sup_wall.x - sup.x, sup_wall.y - sup.y);

        Vector lds = Physics.solveLS(col1, col2, sol);

        return lds.x;
    }


    @Override
    public String toString() {
        return "Ball{" +
                ", center=" + super.getCenter() +
                ", velocity=" + velocity +
                '}';
    }
}

