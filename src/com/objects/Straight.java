package com.objects;

/**
 * Created by igor on 05.05.16.
 */
public class Straight {
  private double A, B, C;

  public Straight() {}
  public Straight(double A, double B, double C) {
    this.A = A;
    this.B = B;
    this.C = C;
  }

  public double getA() { return A; }
  public double getB() { return B; }
  public double getC() { return C; }

  public double solveY(double x) {
    return -(C + A*x)/B;
  }

  public Straight normalStraight(Point p) {
    return new Straight(-B, A, -A*p.Y() + B*p.X());
  }

  public Point intersect(Straight s) {
    double d_x = det(A, B, s.getA(), s.getB());
    double d_x1 = det(-C, B, -s.getC(), s.getB());
    double d_x2 = det(A, -C, s.getA(), -s.getC());
    return new Point(d_x1 / d_x, d_x2 / d_x);
  }

  private double det(double a, double b, double c, double d) {
    return a*d - b*c;
  }
}
