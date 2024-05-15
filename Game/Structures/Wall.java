package Game.Structures;

import Physics.*;

import java.awt.*;

/**
 * Walls of the
 */
public class Wall extends Line {

    /**
     * specifies where the wall ends.
     * edges[0] + direction_vec = edges[2]
     * edges[0].x < edges[1].x. If x-values are equal, edges[0].y < edges[1].y
     */
    private Vector[] edges;

    public Wall(Vector[] edges) {
        super(edges[0], null);

        if (edges[0].equals(edges[1])) {
            throw new IllegalArgumentException("Wall: Edges of wall cant be equal, your wall is a point");
        }

        this.edges = new Vector[2];

        //sorting the vectors like described in the doc
        if (edges[0].x < edges[1].x) {
            this.edges[0] = edges[0];
            this.edges[1] = edges[1];
        } else if (edges[0].x > edges[1].x) {
            this.edges[1] = edges[1];
            this.edges[0] = edges[0];
        } else {
            if (edges[0].y < edges[1].y) {
                this.edges[0] = edges[0];
                this.edges[1] = edges[1];
            } else {
                this.edges[0] = edges[1];
                this.edges[1] = edges[0];
            }
        }
        Vector dir = this.edges[1].copy();
        dir.sub(this.edges[0]);
        super.setDirection_vec(dir);
    }

    /**
     * returns the collision-point of the ball and the wall and null if they don't collide
     * resets the position of the center accordingly
     *
     * @param ball
     * @return
     */
    public Vector intersects(Ball ball) {

        Line traj_center = null;
        Vector intersec = null;

        traj_center = new Line(ball.getCenter(), ball.getVelocity());
        intersec = super.intersects(traj_center);


        Vector orth = this.getDirection_vec().getOrthogonal();
        orth.normalize();
        orth.scale(ball.getRadius());
        orth.flip();

        if (intersec == null) {
            //lines dont intersect
            return null;

        } else if (edges[0].x != edges[1].x) {

            //edges are sorted by the x coordinate
            if (intersec.x >= edges[0].x && intersec.x <= edges[1].x) {
                //center of ball hits the wall
                ball.resetPosition(this);
                orth.add(ball.getCenter());

                return orth;
            }

        } else if (intersec.y >= edges[0].y && intersec.y <= edges[1].y) {
            //center of ball hits the wall
            ball.resetPosition(this);
            orth.add(ball.getCenter());
            return orth;

        } else {

            Vector dist_1 = edges[0].copy();
            Vector dist_2 = edges[1].copy();

            dist_1.sub(intersec);
            dist_2.sub(intersec);

            float d1 = dist_1.length();
            float d2 = dist_2.length();


            Vector vel = ball.getVelocity().copy();
            Vector sup = ball.getCenter().copy();
            vel.flip();

            if (d1 <= ball.getRadius() || d2 <= ball.getRadius()) {
                Vector edge;
                if (d1 < d2) {
                    edge = edges[0];
                } else {
                    edge = edges[1];
                }

                sup.sub(edge);

                float A = (vel.x * vel.x) + (vel.y * vel.y);
                float B = 2 * (vel.x * sup.x + vel.y * sup.x);
                float C = (sup.x * sup.x) + (sup.y * sup.y) - (ball.getRadius() * ball.getRadius());

                float lds1[] = Physics.ABCformula(A, B, C);
                float lds2[] = Physics.ABCformula(A, B, C);
                float max = 0;

                if (lds1 != null) {
                    for (float ld : lds1) {
                        if (ld > 0 && ld > max) {
                            max = ld;
                        }
                    }
                }
                if (lds2 != null) {
                    for (float ld : lds2) {
                        if (ld > 0 && ld > max) {
                            max = ld;
                        }
                    }
                }


                vel.scale(max);
                ball.getCenter().add(vel);

                return edge;
            }
        }
        return null;
    }


    public Vector[] getEdges() {
        return this.edges;
    }
}
