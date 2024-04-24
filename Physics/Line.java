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
        //this line
        float S_X = this.support_vec.x;
        float S_Y = this.support_vec.y;
        float D_X = this.direction_vec.x;
        float D_Y = this.direction_vec.y;

        //other line
        float C_X = lineOpp.support_vec.x;
        float C_Y = lineOpp.support_vec.y;
        float V_X = lineOpp.direction_vec.x;
        float V_Y = lineOpp.direction_vec.y;

        float dotProduct = Physics.dotProduct(direction_vec, lineOpp.getDirection_vec());

        //direction vectors are parallel -> lines dont intersect
        if (dotProduct == -1 || dotProduct == 1) {
            return null;
        }

        float L_2;
        if (D_X != 0) {

            L_2 = (C_Y - S_Y) - (D_Y * (C_X - S_X) / D_X);

            L_2 = L_2 / ((-1) * V_Y + ( (D_Y*V_X) / D_X ) );


        } else {
            L_2 = (-1) * (C_X - S_X) / V_X;

        }

        Vector p = lineOpp.getDirection_vec().copy();
        p.scale(L_2);
        p.add(lineOpp.support_vec);
        return p;
    }
}
