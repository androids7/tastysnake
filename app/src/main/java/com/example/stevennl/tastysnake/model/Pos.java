package com.example.stevennl.tastysnake.model;

import java.io.Serializable;

/**
 * Coordinate(position) in 2D plane.
 */
public class Pos implements Cloneable, Serializable {
    private int x;
    private int y;
    private static final int dx[] = {-1, 0, 1, 0, 0};
    private static final int dy[] = {0, 1, 0, -1, 0};

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Return the position at a given direction(relative to self).
     */
    public Pos getPosAt(Direction k) {
        return new Pos(x + dx[k.ordinal()], y + dy[k.ordinal()]);
    }

    /**
     * Return the direction of current position relative to a given position.
     *
     * @param p The given position
     */
    public Direction getDirectionRelativeTo(Pos p) {
        for (int i = 0; i < 4; i ++) {
            Direction d = Direction.values()[i];
            if (p.getPosAt(d).equals(this))
                return d;
        }
        return Direction.NONE;
    }

    public boolean equals(Pos a) {
        return x == a.getX() && y == a.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public Pos clone() {
        return new Pos(x, y);
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}
