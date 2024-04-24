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
     *
     * @param wall
     */
    public void collision(Wall wall) {
        //build the trajectory line of the ball
        Line traj_ball = new Line(center, velocity);

        //check if and where the ball hits the wall
        Vector isec_ball_wall;
        if (this.velocity.isZero()) {
            isec_ball_wall = super.intersects(wall);
        } else {
            isec_ball_wall = wall.intersects(traj_ball);
        }

        if (isec_ball_wall == null) {
            return;
        }

        //construct the normal (a line, that is orthogonal to the wall, between the center of the ball and the wall.)
        Vector normal_dir = wall.getDirection_vec().getOrthogonal();
        Line normal = new Line(this.center, normal_dir);

        //get the intersection point of the normal and the wall (lot point)
        Vector isec_normal_wall = normal.intersects(wall);

        //calculate the distance-vector between the lot_point and the center
        Vector dist_lot_center = isec_normal_wall.copy();
        dist_lot_center.sub(center);

        if (dist_lot_center.length() > this.getRadius()) {
            //distance between ball and the wall is greater than the radius -> ball will hit the wall eventually,
            //but is not colliding with it at this point of time
            return;
        }

        //calculate the distance-vector between the lot_point and the intersection-point of the ball_trajectory/wall
        Vector dist_lot_btw = isec_ball_wall.copy();
        dist_lot_btw.sub(isec_normal_wall);

        //mirror velocity vector on the origin
        dist_lot_center.flip();
        dist_lot_center.add(dist_lot_btw);
        dist_lot_center.normalize();
        dist_lot_center.scale(this.velocity.length());

        this.velocity = dist_lot_center;

    }

    public void updatePos() {
        float breaks = 0.0027F;
        this.center.add(this.velocity);
        this.velocity.x -= (this.velocity.x * breaks);
        this.velocity.y -= (this.velocity.y * breaks);
    }


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
}

