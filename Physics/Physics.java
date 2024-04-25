package Physics;


/**
 * Util class for the used physical and mathematical functions
 */
public class Physics {


    /**
     * calculates the dot-product for vec1, vec2 != 0.
     *
     * @param vec1
     * @param vec2
     * @return dot-product
     */
    public static float dotProduct(Vector vec1, Vector vec2) {

        if (vec1.isZero() || vec2.isZero()) {
            throw new IllegalArgumentException("dotProduct: One of input vectors is 0");
        }

        return vec1.x * vec2.x + vec1.y * vec2.y;
    }

    /**
     * solves a Linear system with one solution
     * @param vec1 != (0,0)
     * @param vec2 != (0,0)
     * @param sol the solution values of the system
     * @return
     */
    public static Vector solveLS(Vector vec1, Vector vec2, Vector sol) {
        if (vec1.isZero() || vec2.isZero()) {
            throw new IllegalArgumentException("SolveLS: (0,0) vectors are not allowed as input vectors");
        }
        Vector solution = new Vector(0, 0);

        /*
            vec1.x  vec2.x  sol.x   ->  A B | S_X
            vec1.y  vec2.y  sol.y   ->  C D | S_Y
         */

        float S_X = sol.x;
        float S_Y = sol.y;
        float A = vec1.x;
        float B = vec2.x;
        float C = vec1.y;
        float D = vec2.y;
        float L_1;
        float L_2;

        if (A != 0) {
            //first operation on row1
            float step_1 = (C/A) * -1;
            //apply to other row
            float buf1 = D + (step_1*B);
            float buf2 = S_Y + (step_1 * S_X);

            if (buf1 == 0) {
                System.out.println("vec1: " + vec1 + ", vec2: " + vec2 + ", sol: " + sol);
                throw new IllegalArgumentException("SolveLS: Linear system has infinite or no solutions");
            }

            L_2 = buf2 / buf1;
            L_1 = S_X - (B * L_2);
            L_1 /= A;

        } else if (B != 0) {
            L_2 = S_X / B;
            L_1 = (S_Y - C*L_2) / C;

        } else {
            System.out.println("vec1: " + vec1 + ", vec2: " + vec2 + ", sol: " + sol);
            throw new IllegalArgumentException("SolveLS: Linear system has infinite or no solutions");
        }

        solution.x = L_1;
        solution.y = L_2;

        return solution;
    }

}
