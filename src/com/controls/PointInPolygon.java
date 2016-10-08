package com.controls;

import java.io.IOException;
import java.util.ArrayList;

import com.objects.Point;
import com.objects.Polygon;
import com.objects.Segment;

public class PointInPolygon {
  // Const for double compare
  public static final double EPS = 0.001;
  
  Polygon polygon;
  Point search;
  
  boolean answer;

  public PointInPolygon(Polygon polygon, Point search) {
    this.polygon = polygon;
    this.search = search;
    
    try {
      isInPolygon();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
  
  private void isInPolygon() throws IOException {
    answer = false;
    double leftX = 0, leftY = search.Y();
    for (int i = 0; i < polygon.size(); i++) {
      if (leftX - polygon.get(i).X() > EPS) leftX = polygon.get(i).X();
    }
    Segment parX = new Segment(new Point(leftX - 1, leftY), search);
    Intersection intersectTask;
    
    for (int i = 0; i < polygon.size(); i++) {
      int next = (i + 1) % polygon.size();
      Segment seg = new Segment(polygon.get(i), polygon.get(next));
      if (seg.hasPoint(search)) {
        answer = true;
        break;
      }
      
      intersectTask = new Intersection(parX, seg);
      double ny = polygon.get(next).Y();
      double ty = polygon.get(i).Y();

      if ((ty < search.Y() && ny >= search.Y() ||
          ny < search.Y() && ty >= search.Y()) && intersectTask.getPointA().X() < search.X()) {
        answer = !answer;
      }
    }
  }
  
  public String getResult() {
    if (answer) return "True";
    else return "False";
  }

  public boolean ans() { return answer;}
}
