package Physics;

public class Circle extends Collidable {
    private int radius;     //in millimeter
    protected Vector center;

    public void setCenter(Vector center) {
        this.center = center;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }


    /**
     * returns a point on the line that is within the circle and closest to the center.
     * This function does NOT necessarily return the intersections with the outer line of the circle and the other line.
     * @param line
     * @return
     */
    public Vector intersects (Line line) {

        //construct line through center orthogonal to the line we want to check
        Vector orth_line = line.getDirection_vec().getOrthogonal();
        Line center_line = new Line(center,orth_line);

        //get the intersection
        Vector intersec = center_line.intersects(line);

        //calculate distance
        Vector dist = intersec.copy();
        dist.sub(center);

        if (intersec.length() > this.radius) {
            //Line cuts within the circle
            return intersec;
        }

        return null;
    }


    //returns copy of center
    public Vector getCenterCopy () {
        return new Vector(center.x, center.y);
    }

    public int getRadius() {
        return radius;
    }

    public void setAnkerPoint(Vector vec) {
        this.center = vec;
    }
}
