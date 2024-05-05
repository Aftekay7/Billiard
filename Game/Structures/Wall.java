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

    @Override
    public Vector intersects(Line lineOpp) {
        Vector intersec = super.intersects(lineOpp);

        if (intersec == null) {
            //lines dont intersect
            return null;

        } else if (edges[0].x != edges[1].x) {

            //edges are sorted by the x coordinate
            if (intersec.x >= edges[0].x && intersec.x <= edges[1].x) {
                //lines intersect within the wall-edges
                return intersec;
            }

        } else if (intersec.y >= edges[0].y && intersec.y <= edges[1].y) {
            //line intersect within the wall-edges
            return intersec;

        }
        //line doesn't intersect within the wall-edges
        return null;
    }


    public Vector[] getEdges () {
        return this.edges;
    }
}
