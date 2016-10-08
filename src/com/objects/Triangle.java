package com.objects;

/**
 * Created by igor on 04.05.16.
 */
import java.io.IOException;
import java.util.ArrayList;

public class Triangle {
  private Point A, B, C;

  public Triangle(Point A, Point B, Point C) {
    this.A = A;
    this.B = B;
    this.C = C;
  }

  public Point pA() { return A; }
  public Point pB() { return B; }
  public Point pC() { return C; }

  public String toString() {
    return "Triangle{" + A + "; " + B + "; " + C + "}";
  }
}
