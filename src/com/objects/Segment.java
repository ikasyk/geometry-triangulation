package com.objects;

/**
 * Created by igor on 26.04.16.
 */

import java.io.IOException;

import com.objects.Point;

public class Segment extends Straight {
  public static final double EPS = 0.001;

  // Input coordinates
  Point a, b;

  // Equation coefficients
  double A = 0, B = 0, C = 0;

  public Segment(Point a, Point b)  {
    this.a = a;
    this.b = b;
    makeEquationCoefficients();
  }

  public void makeEquationCoefficients() {
    double dx = b.X() - a.X();
    double dy = b.Y() - a.Y();
    A = dy;
    B = - dx;
    C = a.Y() * dx - a.X() * dy;
  }

  public double getA() { return A; }
  public double getB() { return B; }
  public double getC() { return C; }
  public Point getP1() { return a; }
  public Point getP2() { return b; }

  public boolean inLimit(Point p) {
    double _x1 = a.X(), _x2 = b.X(), _y1 = a.Y(), _y2 = b.Y(), x = p.X(), y = p.Y();
    return Double.compare(x, Math.min(_x1, _x2)) >= 0 && Double.compare(x, Math.max(_x1, _x2)) <= 0 &&
            Double.compare(y, Math.min(_y1, _y2)) >= 0 && Double.compare(y, Math.max(_y1, _y2)) <= 0;
  }

  public double abs() {
    double x = b.X() - a.X(), y = b.Y() - a.Y();
    return Math.sqrt(x*x + y*y);
  }

  // Angle between OX and segment 0-360 deg
  public double angleOX() {
    double dx = b.X() - a.X();
    double dy = b.Y() - a.Y();
    double fi = Math.acos(dx / Math.sqrt(dx*dx + dy*dy)) * 180 / Math.PI;
    return b.Y() < a.Y() ? 360.0 - fi : fi;
  }

  public String toString() {
    return "Segment{" + a + "; " + b + "}";
  }

  public double solveY(double x) {
    return -(C + A*x)/B;
  }
  public boolean hasPoint(Point p) {
    boolean b = false;
    if (Math.abs(A * p.X() + B * p.Y() + C) <= EPS && inLimit(p)) b = true;
    return b;
  }

  public Point getAnother(Point p) {
    return a == p ? b : a;
  }

  public double distance(Point p) {
    double M = Math.sqrt(A*A + B*B);
    return Math.abs(A*p.X() + B*p.Y() + C) / M;
  }
}