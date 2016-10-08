package com.objects;

/**
 * Created by igor on 26.04.16.
 */

public class Point {
  double _x, _y;


  public Point() {
    this(0, 0);
  }
  public Point(double x, double y) {
    this._x = x;
    this._y = y;
  }

  public double X() { return _x; }
  public double Y() { return _y; }

  public void X(double x) { this._x = x; }
  public void Y(double y) { this._y = y; }

  public String toString() {
    return "{" + Math.round(X() * 100.0) / 100.0 + "; " + Math.round(Y() * 100.0) / 100.0 + "}";
  }

  public boolean equals(Point p) {
    return Double.compare(_x, p.X()) == 0 && Double.compare(_y, p.Y()) == 0;
  }
}
