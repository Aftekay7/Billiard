package GUI;

import Game.Structures.Ball;
import Game.Structures.Table;
import Game.Structures.Wall;
import Physics.Collidable;
import Physics.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Renders the used Objects
 * TODO: Rewrite renderer: Determine coordinates of all used objects and then (!) transform into coordinate-system of
 * TODO the frame-library ("flip y coordinate") -> more compact, easier to read, no mixture of coordinate-systems.
 * TODO: instantiate one "database" for all static objects that need to be drawn.
 */
public class Renderer extends JFrame {
    //TODO: move aspects of objects to the according classes
    private Color tableColor;
    private Color wallColor;
    private Color holeColor;

    //TODO: clean up used helper-variables. Most of them are not necessary

    // The value by which the table is downscaled during the rendering process.
    private final int scaler = 2;
    private Window window;
    private JPanel jpanel;
    private Table table;
    private final int gameField_rendered_width = 2240 / scaler;
    private final int gameField_rendered_height = 1120 / scaler;
    private final int table_wall_rendered_width = 100 / scaler;
    private final int table_rendered_width = gameField_rendered_width + 2 * table_wall_rendered_width;
    private final int table_rendered_height = gameField_rendered_height + 2 * table_wall_rendered_width;
    private Vector[] gameField_rendered_edges = new Vector[4];
    private Vector[][] table_rendered_edges;
    private Vector[] table_marks = new Vector[4];       // { headline_top, headline_bot, headpoint, footpoint }
    private static final int FPS = 100;

    public Renderer(Table table) {

        this.tableColor = table.getColor_playfield();
        this.wallColor = table.getColor_walls();
        this.holeColor = table.getColor_holes();
        this.table = table;
        this.window = new Window();


        getTableCoords();
        getGameFieldEdges();
        getMarks();


        SwingUtilities.invokeLater(() -> {

            this.jpanel = new JPanel() {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    renderTable(g);
                    renderDynamicObjects(g);
                }
            };
            this.window.setGamePanel(jpanel);

        });
    }


    /**
     * updates the Window every <FPS> seconds
     */
    public void render() {
        Timer timer = new Timer(1000 / FPS, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jpanel.repaint();
            }
        });
        timer.start();
    }


    /**
     * renders the table (game-field + walls)
     * TODO: maybe change casts to rounding operation, animations might look smoother
     *
     * @param g
     */
    private void renderTable(Graphics g) {
        //draw the playing field
        g.setColor(tableColor);
        g.fillRect((int) gameField_rendered_edges[0].x, (int) gameField_rendered_edges[0].y, gameField_rendered_width, gameField_rendered_height);

        //draw headline and headpoint
        g.setColor(Color.white);
        g.drawLine((int) table_marks[0].x, (int) table_marks[0].y, (int) table_marks[1].x, (int) table_marks[1].y);
        g.drawOval((int) table_marks[2].x, (int) table_marks[2].y, 10, 10);

        //draw footpoint
        g.drawOval((int) table_marks[3].x, (int) table_marks[3].y, 10, 10);

        //draw the walls
        g.setColor(wallColor);
        float height = 0;
        float width = 0;
        for (Vector[] wall : table_rendered_edges) {
            g.drawLine((int) wall[0].x, (int) wall[0].y, (int) wall[1].x, (int) wall[1].y);
            //g.drawRect((int) wall[0].x, (int) wall[0].y, (int) width, (int) height);
        }

        //TODO: Add the holes
    }

    private void renderDynamicObjects(Graphics g) {
        Collidable[] gameObj = this.table.getGameObjects();

        Collidable c = gameObj[0];
        Ball b;
        int index = 0;
        while (c instanceof Ball) {
            b = (Ball) c;

            if (!b.isInGame()) {
                index++;
                c = gameObj[index];
                continue;
            }

            Vector vec = b.getCenterCopy();
            scaleCoords(vec);
            vec.add(gameField_rendered_edges[3]);
            getDrawingCoords(vec, b.getRadius());


            g.setColor(b.getColor());

            //works as we scale down by 2 at the moment and radius * 2 = diameter
            g.fillOval((int) vec.x, (int) vec.y, b.getRadius(), b.getRadius());

            if (b.isHalf()) {
                g.setColor(Color.white);
                g.fillOval((int) (vec.x + b.getRadius() / 4), (int) vec.y, b.getRadius() / 2, b.getRadius());
            }

            g.setColor(Color.black);
            g.drawOval((int) vec.x, (int) vec.y, b.getRadius(), b.getRadius());

            index++;
            c = gameObj[index];
        }
    }

    /**
     * TODO: Sum up in one matrix multiplication.
     * converts the coordinates of the ball of our coordinate system into coords that are used in the coordinate system
     * of the drawing library and scales down the size of the table, so it fits into our screen.
     * We divide x and y by two as every block of 2 * 2 millimeters is represented by one pixel
     *
     * @param vec position vector of our ball
     */
    private void scaleCoords(Vector vec) {
        vec.x = vec.x / scaler;
        vec.y = vec.y / scaler;
        vec.y *= -1;

    }

    private void getDrawingCoords(Vector vec, float radius) {
        vec.x -= radius / scaler;
        vec.y -= radius / scaler;
    }


    /**
     * initializes a little database with the drawing information of the table
     */
    private void getTableCoords() {
        //determine distances between table and window edges
        int x = (this.window.getWidth() - (gameField_rendered_width + table_wall_rendered_width * 2 + 1)) / 2;
        int y = (this.window.getHeight() - (gameField_rendered_height + table_wall_rendered_width * 2 + 1)) / 2;

        Vector offset = new Vector(x + table_wall_rendered_width, y + table_wall_rendered_width);

        Collidable[] objects = table.getGameObjects();

        Collidable c = objects[0];
        int index = 0;

        while (c instanceof Ball) {
            index++;
            c = objects[index];
        }
        int w = 0;
        Vector[][] wallEdges = new Vector[objects.length - index][2];   //e1,e2

        for (int i = index; i < objects.length; i++) {
            Wall wall = (Wall) objects[i];
            Vector e1 = wall.getEdges()[0].copy();
            Vector e2 = wall.getEdges()[1].copy();

            scaleCoords(e1);
            scaleCoords(e2);

            e1.y *= -1;
            e2.y *= -1;

            e1.add(offset);
            e2.add(offset);

            wallEdges[w] = new Vector[]{e1, e2};
            w++;
        }

        this.table_rendered_edges = wallEdges;


    }


    //TODO: Offset is bottom left corner of gamefield, not table. might adjust for computing the wall coordinates
    private void getGameFieldEdges() {
        int x = (this.window.getWidth() - (gameField_rendered_width + table_wall_rendered_width * 2)) / 2 - 1;
        int y = (this.window.getHeight() - (gameField_rendered_height + table_wall_rendered_width * 2)) / 2 - 1;

        x += table_wall_rendered_width;
        y += table_wall_rendered_width;

        Vector top_left = new Vector(x, y);
        this.gameField_rendered_edges[0] = top_left;

        Vector top_right = top_left.copy();
        top_right.x += gameField_rendered_width;
        this.gameField_rendered_edges[1] = top_right;

        Vector bot_right = top_right.copy();
        bot_right.y += gameField_rendered_height;
        this.gameField_rendered_edges[2] = bot_right;

        Vector bot_left = top_left.copy();
        bot_left.y += gameField_rendered_height;
        this.gameField_rendered_edges[3] = bot_left;
    }

    private void getMarks() {
        //radius of the head and foot point
        int radius = 5;

        int x = (int) (gameField_rendered_edges[0].x + gameField_rendered_width / 4);
        int y = (int) (gameField_rendered_edges[0].y);

        //headline
        table_marks[0] = new Vector(x, y);
        table_marks[1] = new Vector(x, y + gameField_rendered_height);

        //headpoint
        table_marks[2] = new Vector(x - radius, y + (gameField_rendered_height / 2F) - radius);

        //footpoint
        table_marks[3] = new Vector(table_marks[2].x + (gameField_rendered_width / 2F), table_marks[2].y);
    }


}
