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
        float dotProduct = 0;
        dotProduct = Physics.dotProduct(direction_vec, lineOpp.getDirection_vec());

        //dotProd(v1,v2) == len(v1) * len(v2) <=> vectors are parallel
        // length only returns positive lengths whereas dotProd might return a negative value depending on the orientation of the vectors
        //-> take the absolut value of the dot product

        if (dotProduct < 0) {
            dotProduct *= -1;
        }


        // TODO: Maye add a delta for the check (float might be not precise enough) if parallel Vectors are still passed on
        float lengthProd = direction_vec.length() * lineOpp.direction_vec.length();
        float delta = 0.001F;
        if (dotProduct == lengthProd) {
            //direction vectors are parallel -> lines dont intersect
            return null;
        }


        //build the LS to solve for the parameters that are used for the linear combination of the intersection point

        //first 2 transformation steps, so we can use the LS-function
        Vector solutions = new Vector(lineOpp.support_vec.x - this.support_vec.x, lineOpp.support_vec.y - this.support_vec.y);
        Vector col2 = lineOpp.direction_vec.copy();
        col2.flip();

        float L_2;
        try {

            //calculate lambdas
            Vector lambdas = Physics.solveLS(this.direction_vec,col2,solutions);
            L_2 = lambdas.y;

        } catch (IllegalArgumentException e) {
            System.out.println("this Line: " + this.support_vec + " + L_1 *  " + this.direction_vec);
            System.out.println("opp Line: " + lineOpp.support_vec + " + L_2 * " + lineOpp.direction_vec);
            throw e;
        }



        Vector p = lineOpp.getDirection_vec().copy();
        p.scale(L_2);
        p.add(lineOpp.support_vec);
        return p;
    }
}
