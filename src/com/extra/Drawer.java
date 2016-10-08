package com.extra;

import com.objects.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by igor on 04.05.16.
 */
public class Drawer {
  final public static int CANVAS_WIDTH = 480;
  final public static int CANVAS_HEIGHT = 480;
  final public static int CANVAS_POINT_RADIUS = 4;
  final public static double CANVAS_LINE_WIDTH = 2.0;

  private GraphicsContext GC;
  private double SCALE_X, SCALE_Y, MIN_X, MIN_Y, MAX_X, MAX_Y;

  public Drawer(GraphicsContext gc, double minX, double maxX, double minY, double maxY) {
    this.GC = gc;
    this.MIN_X = minX;
    this.MIN_Y = minY;
    this.MAX_X = maxX;
    this.MAX_Y = maxY;
    this.SCALE_X = CANVAS_WIDTH / (maxX - minX + 2);
    this.SCALE_Y = CANVAS_HEIGHT / (maxY - minY + 2);
    this.SCALE_X = Math.min(this.SCALE_X, this.SCALE_Y);
    this.SCALE_Y = this.SCALE_X;

    GC.setStroke((new Color(0.05,0.45,0.67,1.)));
    GC.setLineWidth(CANVAS_LINE_WIDTH);
    GC.setFill((new Color(0.05,0.45,0.67,1.)));
  }

  public void point(Point p) {
    GC.fillOval(getScaleX(p.X()) - CANVAS_POINT_RADIUS, getScaleY(p.Y()) - CANVAS_POINT_RADIUS, CANVAS_POINT_RADIUS * 2, CANVAS_POINT_RADIUS * 2);
  }

  public void line(Point p1, Point p2) {
    GC.strokeLine(getScaleX(p1.X()), getScaleY(p1.Y()), getScaleX(p2.X()), getScaleY(p2.Y()));
  }

  private double getScaleX(double x) {
    return (x - MIN_X + 1) * SCALE_X;
  }
  private double getScaleY(double y) {
    return CANVAS_HEIGHT - (y - MIN_Y + 1) * SCALE_Y;
  }

  public void clearCanvas() {
    GC.setFill(Color.WHITE);
    GC.setStroke(new Color(0.53, 0.53, 0.53, 1.));
    GC.clearRect(0,0,CANVAS_WIDTH, CANVAS_HEIGHT);
    GC.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    GC.strokeLine(0, 0, 0, CANVAS_HEIGHT);
    GC.strokeLine(0, CANVAS_HEIGHT, 480, CANVAS_HEIGHT);
    GC.strokeLine(CANVAS_WIDTH, CANVAS_HEIGHT, CANVAS_WIDTH, 0);
    GC.strokeLine(CANVAS_WIDTH, 0, 0, 0);
  }
}
