package Physics;

public class Vector {
    public float x;
    public float y;


    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }


    /**
     * copies this vector
     * @return a copy of this vector
     */
    public Vector copy() {
        return new Vector(this.x,this.y);
    }

    public void add(Vector vec) {
        this.x += vec.x;
        this.y += vec.y;
    }

    public void sub(Vector vec) {
        this.x -= vec.x;
        this.y -= vec.y;
    }

    public void scale(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    /**
     * computes an orthogonal vector using the dot-product
     * @return  a vector that is orthogonal to this vector
     */
    public Vector getOrthogonal () {
        return new Vector(this.y * -1, this.x);
    }

    /**
     * Scales this vector to the length of 1
     */
    public void normalize() {
        float length = this.length();
        this.x /= length;
        this.y /= length;
    }


    /**
     * returns the length of the vector
     * @return length
     */
    public float length () {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }




    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector ) ) {
            return false;
        }
        Vector vec = (Vector) obj;

        return (this.x == vec.x && this.y == vec.y);
    }

    public boolean isZero () {
        return (this.x == 0 && this.y == 0);
    }

    public void flip () {
        this.x *= -1;
        this.y *= -1;
    }
}
