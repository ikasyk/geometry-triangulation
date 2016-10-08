package com.objects;

/**
 * Created by igor on 26.04.16.
 */


import com.controls.Angle;

import java.io.IOException;
import java.util.ArrayList;

public class Polygon {
  private ArrayList<Point> points;

  public Polygon(ArrayList<Point> points) throws IOException {
    if (points.size() < 3) throw new IOException("Polygon must consists more than 3 points.");
    else
      this.points = points;
  }

  public Point get(int i) {
    int size = points.size();
    return points.get((i+size) % size);
  }

  public int size() {
    return points.size();
  }

  public int minAngleIndex() {
    Segment s1, s2;
    int size = points.size(), index = -1;
    double minAngle = 360.1, angle;
    for (int i = 0; i < points.size(); i++) {
      angle = this.getAngle(i);
      if (minAngle > angle) {
        minAngle = angle;
        index = i;
      }
    }
    return index;
  }

  public double getAngle(int k) {
    int size = points.size();
    Point p1 = points.get((size + k - 1) % size),
            p2 = points.get((k + size)%size),
            p3 = points.get((k + 1) % size);
    Segment s1 = new Segment(p2, p3), s2 = new Segment(p2, p1);
    return (new Angle(s1, s2)).getFi();
  }

  public void replace1Point2(int k, Point K1, Point K2) {
    int size = points.size();
    k = (k+size) % size;
    int k1 = (k+1) % size;
    points.remove(k);
    points.add(k, K1);
    points.add(k+1, K2);
  }

  public void replace1Point3(int k, Point K1, Point K2, Point K3) {
    int size = points.size();
    k = (k+size) % size;
    int k1 = (k+1) % size,
            k2 = (k+2) % size;
    points.remove(k);
    points.add(k, K1);
    points.add(k+1, K2);
    points.add(k+2, K3);
  }

  public void remove(int k) {
    this.points.remove((k+points.size())%points.size());
  }

  public String toString() {
    String t = "Polygon{";
    for (int i = 0; i < points.size(); i++) {
      if (i > 0) t += "; ";
      t += points.get(i);
    }
    t += "}";
    return t;
  }
}
