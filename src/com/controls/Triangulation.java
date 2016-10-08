package com.controls;

import com.objects.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by igor on 04.05.16.
 */
public class Triangulation {
  Polygon poly;
  int x = 19;
  private ArrayList<Triangle> triangles = new ArrayList<>();

  private double D;

  public Triangulation(Polygon p, double d) {
    this.poly = p;
    this.D = d;
//    int minIndex = poly.minAngleIndex();
    this.step();
  }

  private void step() {
//    if (x < 0) return;
//    x--;
    int k = poly.minAngleIndex();
    int size = poly.size();
    Point p1, p2, p3;
    Segment s1, s2;

    p1 = poly.get((size + k - 1) % size);
    p2 = poly.get(k % size);
    p3 = poly.get((k + 1) % size);
    s1 = new Segment(p2, p1);
    s2 = new Segment(p2, p3);
    double angle = poly.getAngle(k);
    if (angle > 75.0) {
      Point K = getPointBetweenSegments(s1, s2, p2, D);
      Segment sb = new Segment(p2, K);
      boolean b = false;
      for (int i = k+1; i < k - 1 + size; i++) {
        Intersection ch = new Intersection(sb, new Segment(poly.get(i), poly.get(i + 1)));
        if (ch.getResultCode() == 1) {
          b = true;
          break;
        }
      }
      if (b) {
        Point p4 = poly.get((size + k - 2) % size), p5 = poly.get((size + k + 2) % size);
        if ((new Segment(p2, p4)).abs() <= D*1.4) {
          Triangle t = new Triangle(p2, p1, p4);
          triangles.add(t);
          poly.replace1Point2(k+1, p2, p4);
        } else if ((new Segment(p2, p5)).abs() <= D*1.4) {
          Triangle t = new Triangle(p2, p3, p5);
          triangles.add(t);
          poly.replace1Point2(k - 1, p2, p5);
        }
      } else {
        Point K1 = getPointOnSegment(s1, p2, D), K2 = getPointOnSegment(s2, p2, D);
        Segment sc = new Segment(p2, K1);
        if (1.4 * sc.abs() >= s1.abs()) {
          K1 = p1;
          poly.remove(k - 1);
          if (k - 1 >= 0) k--;
        }
        sc = new Segment(p2, K2);
        if (1.4 * sc.abs() >= s2.abs()) {
          K2 = p3;
          poly.remove(k + 1);
          if (k >= poly.size()) k = poly.size()-1;
        }
        Triangle t1 = new Triangle(p2, K1, K), t2 = new Triangle(p2, K2, K);
        triangles.add(t1);
        triangles.add(t2);
        poly.replace1Point3(k, K1, K, K2);
      }
    } else {
      Point K1 = getPointOnSegment(s1, p2, D), K2 = getPointOnSegment(s2, p2, D);
      if ((new Segment(p2, K2)).abs()*1.4 >= s2.abs()) { K2 = p3; poly.remove((k + 1)); if(k+1>=size) k--; size--; }
      if ((new Segment(p2, K1)).abs()*1.4 >= s1.abs()) { K1 = p1; poly.remove((k - 1)); if(k-1>=0) k--; size--; }
      Triangle t = new Triangle(p2, K1, K2);
      triangles.add(t);
      poly.replace1Point2(k, K1, K2);
    }
    if (poly.size() > 3) {
      this.step();
    } else {
      p1 = poly.get(0);
      p2 = poly.get(1);
      p3 = poly.get(2);
      if ((new Segment(p1, p2)).abs() <= D * 1.4 &&
              (new Segment(p2, p3)).abs() <= D * 1.4 &&
              (new Segment(p3, p1)).abs() <= D * 1.4) {
        triangles.add(new Triangle(poly.get(0), poly.get(1), poly.get(2)));
      } else {
        this.step();
      }
    }
  }

  private Point getPointBetweenSegments(Segment s1, Segment s2, Point p, double radius) {
    double A1, B1, C1, A2, B2, C2, M1, M2;
    M1 = Math.sqrt(Math.pow(s1.getA(), 2.) + Math.pow(s1.getB(), 2.));
    M2 = Math.sqrt(Math.pow(s2.getA(), 2.) + Math.pow(s2.getB(), 2.));
    A1 = s1.getA()/M1 - s2.getA()/M2;
    A2 = s1.getA()/M1 + s2.getA()/M2;
    B1 = s1.getB()/M1 - s2.getB()/M2;
    B2 = s1.getB()/M1 + s2.getB()/M2;
    C1 = s1.getC()/M1 - s2.getC()/M2;
    C2 = s1.getC()/M1 + s2.getC()/M2;
    Straight st1 = new Straight(A1, B1, C1);
    Straight st2 = new Straight(A2, B2, C2);
    Point[] K1 = getPointOnSegment(st1, p, radius);
    Point[] K2 = getPointOnSegment(st2, p, radius);
    Point as1 = s1.getAnother(p), as2 = s2.getAnother(p);
    Segment sc1 = new Segment(p, K1[0]), sc2 = new Segment(p, K1[1]), sc3 = new Segment(p, K2[0]), sc4 = new Segment(p, K2[1]);
    Angle a11 = new Angle(s1, sc1), a12 = new Angle(sc1, s2),
            a21 = new Angle(s1, sc2), a22 = new Angle(sc2, s2),
            a31 = new Angle(s1, sc3), a32 = new Angle(sc3, s2),
            a41 = new Angle(s1, sc4), a42 = new Angle(sc4, s2);
//    if (detCheckPoints(as1, K1[0], as2) < 0) return K1[0];
//    else if (detCheckPoints(as1, K1[1], as2) < 0) return K1[1];
//    else if (detCheckPoints(as1, K2[0], as2) < 0) return K2[0];
//    else if (detCheckPoints(as1, K2[1], as2) < 0) return K1[1];
    if (Math.abs(a11.getFi() - a12.getFi()) < 10) return K1[0];
    else if (Math.abs(a21.getFi() - a22.getFi()) < 10) return K1[1];
    else if (Math.abs(a31.getFi() - a32.getFi()) < 10) return K2[0];
    else if (Math.abs(a41.getFi() - a42.getFi()) < 10) return K2[1];
    System.out.println("NOTHING");
    return K1[0];
    ///////
//    double E_D = Math.pow(2*A1*C1/Math.pow(B1, 2.0) + 2*A1*p.Y()/B1 - 2*p.X(), 2.0) - 4*(Math.pow(A1/B1, 2.0) + 1)*(Math.pow(C1/B1, 2.0) + 2*C1*p.Y()/B1 - radius + Math.pow(p.X(), 2.) + Math.pow(p.Y(), 2.));
//    double E_X1 = 1/(2*(Math.pow(A1/B1, 2.) + 1))*((-2*A1*C1/Math.pow(B1, 2.) - 2*A1*p.Y()/B1 + 2*p.X()) + Math.sqrt(E_D));
//    double E_X2 = 1/(2*(Math.pow(A1/B1, 2.) + 1))*((-2*A1*C1/Math.pow(B1, 2.) - 2*A1*p.Y()/B1 + 2*p.X()) - Math.sqrt(E_D));
//    Point K1 = new Point(E_X1, s.solveY(E_X1)), K2 = new Point(E_X2, s.solveY(E_X2));
//    return (new PointInPolygon(poly, K1)).ans() ? K1 : K2;
  }

  private Point[] getPointOnSegment(Straight s, Point p, double radius) {
    Point[] r = new Point[2];
    double A1 = s.getA(), B1 = s.getB(), C1 = s.getC();
//    double E_D = Math.pow(2*A1*C1/Math.pow(B1, 2.0) + 2*A1*p.Y()/B1 - 2*p.X(), 2.0) - 4*(Math.pow(A1/B1, 2.0) + 1)*(Math.pow(C1/B1, 2.0) + 2*C1*p.Y()/B1 - radius + Math.pow(p.X(), 2.) + Math.pow(p.Y(), 2.));
//    double E_X1 = 1/(2*(Math.pow(A1/B1, 2.) + 1))*((-2*A1*C1/Math.pow(B1, 2.) - 2*A1*p.Y()/B1 + 2*p.X()) + Math.sqrt(E_D));
//    double E_X2 = 1/(2*(Math.pow(A1/B1, 2.) + 1))*((-2*A1*C1/Math.pow(B1, 2.) - 2*A1*p.Y()/B1 + 2*p.X()) - Math.sqrt(E_D));
    //double E_D = Math.sqrt(Math.pow(A1,2.) * Math.pow(B1,2.) * Math.pow(radius,2.) - Math.pow(A1,2.)*Math.pow(B1,2.)*Math.pow(p.X(),2)-2*A1*Math.pow(B1,3.)*p.X()*p.Y()-2*A1*Math.pow(B1,2.) *C1 *p.X()+Math.pow(B1,4)*Math.pow(radius,2)-Math.pow(B1,4)*Math.pow(p.Y(),2.)-2*Math.pow(B1,3.) *C1*p.Y()-Math.pow(B1,2.)*Math.pow(C1,2.))-A1*B1*p.Y()-A1*C1+Math.pow(B1,2.)*p.X();
    if (B1 == 0) {
      Point K1 = new Point(p.X(), p.Y() - radius), K2 = new Point(p.X(), p.Y() + radius);
      r[0] = K1;
      r[1] = K2;
      return r;
    }
    double E_D = Math.sqrt(-Math.pow(C1, 2.)+Math.pow(A1,2.)*Math.pow(radius,2.)+Math.pow(B1,2.)*Math.pow(radius,2.)-2*A1*C1*p.X()-Math.pow(A1,2)*Math.pow(p.X(),2.)-2*B1*C1*p.Y()-2*A1*B1*p.X()*p.Y()-Math.pow(B1,2.)*Math.pow(p.Y(),2.));
    double E_X1 = (-A1*C1+p.X()*Math.pow(B1,2.)-A1*B1*p.Y()-E_D*B1)/(Math.pow(A1,2.)+Math.pow(B1, 2.));
    double E_X2 = (-A1*C1+p.X()*Math.pow(B1,2.)-A1*B1*p.Y()+E_D*B1)/(Math.pow(A1,2.)+Math.pow(B1, 2.));
    Point K1 = new Point(E_X1, s.solveY(E_X1)), K2 = new Point(E_X2, s.solveY(E_X2));
    r[0] = K1;
    r[1] = K2;
    return r;
  }

  private Point getPointOnSegment(Segment s, Point p, double radius) {
    double A1 = s.getA(), B1 = s.getB(), C1 = s.getC();
    //    double E_D = Math.pow(2*A1*C1/Math.pow(B1, 2.0) + 2*A1*p.Y()/B1 - 2*p.X(), 2.0) - 4*(Math.pow(A1/B1, 2.0) + 1)*(Math.pow(C1/B1, 2.0) + 2*C1*p.Y()/B1 - radius + Math.pow(p.X(), 2.) + Math.pow(p.Y(), 2.));
    //    double E_X1 = 1/(2*(Math.pow(A1/B1, 2.) + 1))*((-2*A1*C1/Math.pow(B1, 2.) - 2*A1*p.Y()/B1 + 2*p.X()) + Math.sqrt(E_D));
    //    double E_X2 = 1/(2*(Math.pow(A1/B1, 2.) + 1))*((-2*A1*C1/Math.pow(B1, 2.) - 2*A1*p.Y()/B1 + 2*p.X()) - Math.sqrt(E_D));
    //double E_D = Math.sqrt(Math.pow(A1,2.) * Math.pow(B1,2.) * Math.pow(radius,2.) - Math.pow(A1,2.)*Math.pow(B1,2.)*Math.pow(p.X(),2)-2*A1*Math.pow(B1,3.)*p.X()*p.Y()-2*A1*Math.pow(B1,2.) *C1 *p.X()+Math.pow(B1,4)*Math.pow(radius,2)-Math.pow(B1,4)*Math.pow(p.Y(),2.)-2*Math.pow(B1,3.) *C1*p.Y()-Math.pow(B1,2.)*Math.pow(C1,2.))-A1*B1*p.Y()-A1*C1+Math.pow(B1,2.)*p.X();
    if (B1 == 0) {
      Point K1 = new Point(p.X(), p.Y() - radius), K2 = new Point(p.X(), p.Y() + radius);
      return s.inLimit(K1) ? K1 : K2;
    }
    double E_D = Math.sqrt(-Math.pow(C1, 2.)+Math.pow(A1,2.)*Math.pow(radius,2.)+Math.pow(B1,2.)*Math.pow(radius,2.)-2*A1*C1*p.X()-Math.pow(A1,2)*Math.pow(p.X(),2.)-2*B1*C1*p.Y()-2*A1*B1*p.X()*p.Y()-Math.pow(B1,2.)*Math.pow(p.Y(),2.));
    double E_X1 = (-A1*C1+p.X()*Math.pow(B1,2.)-A1*B1*p.Y()-E_D*B1)/(Math.pow(A1,2.)+Math.pow(B1, 2.));
    double E_X2 = (-A1*C1+p.X()*Math.pow(B1,2.)-A1*B1*p.Y()+E_D*B1)/(Math.pow(A1,2.)+Math.pow(B1, 2.));
    Point K1 = new Point(E_X1, s.solveY(E_X1)), K2 = new Point(E_X2, s.solveY(E_X2));
    return s.inLimit(K1) ? K1 : K2;
  }

  public ArrayList<Triangle> getTriangles() { return this.triangles; }


  private double detCheck(Segment a, Segment b, Segment c) {
    double Ax = a.getP2().X() - a.getP1().X(),
            Ay = a.getP2().Y() - a.getP1().Y(),
            Bx = b.getP2().X() - b.getP1().X(),
            By = b.getP2().Y() - b.getP1().Y(),
            Cx = c.getP2().X() - c.getP1().X(),
            Cy = c.getP2().Y() - c.getP1().Y();
    return Ax*(By - Cy) - Ay*(Bx - Cx) + Bx*Cy - By*Cx;
  }

  private double detCheckPoints(Point p1, Point p2, Point p3) {
    return (p2.X() - p1.X())*(p3.Y() - p1.Y()) - (p2.Y() - p1.Y())*(p3.X() - p1.X());
  }
}
