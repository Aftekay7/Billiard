package Tests;

import Game.Structures.Ball;
import Game.Structures.BallNumber;
import Game.Structures.Wall;

import Physics.*;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BilliardTests {
    private static final double delta = 0.0001;

    @Test
    public void testIntersects() {
        Vector sup1 = new Vector(-3, 3);
        Vector dir1 = new Vector(1, 0);
        Line line1 = new Line(sup1, dir1);

        Vector sup2 = new Vector(3, -4);
        Vector dir2 = new Vector(0, 1);
        Line line2 = new Line(sup2, dir2);

        Vector intersec = line1.intersects(line2);
        assertEquals(new Vector(3, 3), intersec);
    }

    @Test
    public void testIntersectsParallel() {
        Vector sup1 = new Vector(-3, 3);
        Vector dir1 = new Vector(1, 0);
        Line line1 = new Line(sup1, dir1);

        Vector sup2 = new Vector(3, -4);
        Vector dir2 = new Vector(1, 0);
        Line line2 = new Line(sup2, dir2);

        Vector intersec = line1.intersects(line2);
        assertNull(intersec);
    }

    @Test
    public void testIntersectsDXis0() {
        Vector sup1 = new Vector(-3, 3);
        Vector dir1 = new Vector(0, 1);
        Line line1 = new Line(sup1, dir1);

        Vector sup2 = new Vector(3, -4);
        Vector dir2 = new Vector(1, 0);
        Line line2 = new Line(sup2, dir2);

        Vector intersec = line1.intersects(line2);
        assertEquals(new Vector(-3, -4), intersec);
    }

    @Test
    public void testDotProduct() {
        Vector v_1 = new Vector(1, 0);
        Vector v_2 = new Vector(0, 1);
        Vector v_3 = new Vector(-1, 0);
        Vector v_4 = new Vector(0, 0);

        assertEquals(0, Physics.dotProduct(v_1, v_2), delta);
        assertEquals(-1, Physics.dotProduct(v_1, v_3), delta);
    }

    @Test
    public void testGetOrthogonal() {
        Vector vec = new Vector(2, 1);
        assertEquals(0, Physics.dotProduct(vec, vec.getOrthogonal()), delta);

        Vector vec1 = new Vector(0, 2);
        assertEquals(0, Physics.dotProduct(vec1, vec1.getOrthogonal()), delta);
    }


    @Test
    public void testCollisionBallWall() {
        Ball ball = new Ball(BallNumber.WHITE, Color.WHITE);
        ball.setCenter(0, -5);
        ball.setVelocity(new Vector(-10, 10));

        Vector[] edges = {new Vector(-50, 0), new Vector(50, 0)};
        Wall wall = new Wall(edges);

        ball.collision(wall);
        assertEquals(new Vector(-10, -10), ball.getVelocity());
    }

    @Test
    public void testCollisionBallWall_2() {
        Ball ball = new Ball(BallNumber.WHITE, Color.WHITE);
        ball.setCenter(5, 15);
        ball.setVelocity(new Vector(10, 20));

        Vector[] edges = {new Vector(-50, 25), new Vector(50, 25)};
        Wall wall = new Wall(edges);

        ball.collision(wall);
        assertEquals(new Vector(10, -20), ball.getVelocity());
    }

    @Test
    public void testCollisionBallWall_3() {
        Ball ball = new Ball(BallNumber.WHITE, Color.WHITE);
        ball.setCenter(20, 0);
        ball.setVelocity(new Vector(10, 20));

        Vector[] edges = {new Vector(25, 25), new Vector(25, -25)};
        Wall wall = new Wall(edges);

        ball.collision(wall);
        assertEquals(new Vector(-10, 20), ball.getVelocity());
    }


    @Test
    public void testCollisionBallBall() {
        //both balls are moving
        Ball b1 = new Ball(BallNumber.WHITE, Color.white);
        Ball b2 = new Ball(BallNumber.EIGHT, Color.BLACK);

        b2.setCenter(-20, 20);
        b2.setVelocity(new Vector(2, 0));

        b1.setCenter(20, -20);
        b1.setVelocity(new Vector(-1, 1));

        b1.collision(b2);

        assertEquals(3.41421, b1.getVelocity().length() + b2.getVelocity().length(), 0.01);

    }

    @Test
    public void testCollisionBallBall_1() {
        //one ball is not moving
        Ball b1 = new Ball(BallNumber.WHITE, Color.white);
        Ball b2 = new Ball(BallNumber.EIGHT, Color.BLACK);

        b2.setCenter(-20, 20);
        b2.setVelocity(new Vector(0, 0));

        b1.setCenter(-20, -20);
        b1.setVelocity(new Vector(0, 5));

        b1.collision(b2);

        assertEquals(new Vector(0,5), b2.getVelocity());
    }


    @Test
    public void testSolveLS() {
        Vector vec1 = new Vector(1,1);
        Vector vec2 = new Vector(-1,1);

        Vector sol1 = new Vector(-1,1);
        Vector sol2 = new Vector(2,0);

        assertEquals(new Vector(0,1),Physics.solveLS(vec1,vec2,sol1));
        assertEquals(new Vector(1,-1),Physics.solveLS(vec1,vec2,sol2));

    }


    @Test
    public void testDotProductReturnOrthogonal () {
        Vector vec1 = new Vector(0,1120);
        Vector vec2 = new Vector(0,-25.50764F);

        Vector vec3 = new Vector(2240,0);
        Vector vec4 = new Vector(29.919F,0);

        float lengthSqd = vec1.length() * vec2.length();

        float dotProd = Physics.dotProduct(vec1,vec2);
        assertEquals(-1 * lengthSqd,dotProd,delta);

        dotProd = Physics.dotProduct(vec3,vec4);
        lengthSqd = vec3.length() * vec4.length();
        assertEquals(lengthSqd,dotProd,delta);
    }


    @Test
    public void testCompareCollisionIntersect () {
        Ball b1 = new Ball(BallNumber.WHITE, Color.white);
        b1.setVelocity(new Vector(5,5));
        b1.setCenter(295,100);

        Vector[] edges = {new Vector(300, 0), new Vector(300, 100)};
        Wall wall = new Wall(edges);

        b1.collision(wall);
        assertEquals(new Vector(-5,5),b1.getVelocity());

    }

    @Test
    public void testSolveLS2 () {
        Vector sol = new Vector(5,5);
        Vector v1 = new Vector(0,1);
        Vector v2 = new Vector(1,0);

        Vector lds = Physics.solveLS(v1,v2,sol);

        assertEquals(new Vector(5,5),lds);
    }

}
