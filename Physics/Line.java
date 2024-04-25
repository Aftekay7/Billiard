package Physics;

public class Line extends Collidable {
    private Vector support_vec;
    private Vector direction_vec;

    public Line(Vector sup, Vector dir) {
        this.support_vec = sup;
        this.direction_vec = dir;
    }

    public void setDirection_vec(Vector direction_vec) {
        this.direction_vec = direction_vec;
    }

    public Vector getDirection_vec() {
        return direction_vec;
    }

    public void setAnkerPoint(Vector vec) {
        this.support_vec = vec;
    }


    /**
     * calculates the intersection point of the two lines. returns null if lines don't intersect.
     * @param lineOpp
     * @return
     */
    public Vector intersects(Line lineOpp) {
        float dotProduct = Physics.dotProduct(direction_vec, lineOpp.getDirection_vec());

        //direction vectors are parallel -> lines dont intersect
        if (dotProduct == -1 || dotProduct == 1) {
            return null;
        }

        //build the LS to solve for the parameters that are used for the linear combination of the intersection point

        //first 2 transformation steps, so we can use the LS-function
        Vector solutions = new Vector(lineOpp.support_vec.x - this.support_vec.x, lineOpp.support_vec.y - this.support_vec.y);
        Vector col2 = lineOpp.direction_vec.copy();
        col2.flip();

        //calculate lambdas
        Vector lambdas = Physics.solveLS(this.direction_vec,col2,solutions);
        float L_2 = lambdas.y;

        Vector p = lineOpp.getDirection_vec().copy();
        p.scale(L_2);
        p.add(lineOpp.support_vec);
        return p;
    }
}
