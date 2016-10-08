/**
 * Created by igor on 4.05.16.
 */
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

//import com.controls.Triangulation;
import com.controls.Triangulation;
import com.extra.Drawer;
import com.objects.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TriangulationDrive extends Application {
  final public static int CANVAS_WIDTH = 480;
  final public static int CANVAS_HEIGHT = 480;
  final public static int CANVAS_POINT_RADIUS = 4;
  final public static double CANVAS_LINE_WIDTH = 2.0;

  Canvas canvas;
  TextArea pointsTextArea;
  double minX, maxX, minY, maxY, scaleX, scaleY;

  public static void main(String[] args) {
    launch(args);
  }

  public void clearCanvas() {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    gc.setFill(Color.WHITE);
    gc.setStroke(new Color(0.53, 0.53, 0.53, 1.));
    gc.clearRect(0,0,CANVAS_WIDTH, CANVAS_HEIGHT);
    gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    gc.strokeLine(0, 0, 0, CANVAS_HEIGHT);
    gc.strokeLine(0, CANVAS_HEIGHT, 480, CANVAS_HEIGHT);
    gc.strokeLine(CANVAS_WIDTH, CANVAS_HEIGHT, CANVAS_WIDTH, 0);
    gc.strokeLine(CANVAS_WIDTH, 0, 0, 0);
  }

  @Override
  public void start(Stage primaryStage) {
    GridPane pane = new GridPane();
    pane.setPadding(new Insets(15, 15, 15, 15));
    pane.setVgap(15);
    pane.setHgap(15);

    canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
    clearCanvas();

    pane.add(canvas, 0, 0, 1, 3);

    Button fileBtn = new Button("Файл");
    fileBtn.setMinWidth(175);
    fileBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        FileChooser fc = new FileChooser();
        File f = fc.showOpenDialog(primaryStage);
        if (f != null) {
          try {
            Scanner textScan = new Scanner(f);
            String text = "";
            while (textScan.hasNextLine()) {
              text += textScan.nextLine() + "\n";
            }
            pointsTextArea.setText(text);
            scanText(new Scanner(f));
          } catch (FileNotFoundException e) {
          }
        }
      }
    });
    pane.add(fileBtn, 1, 0);

    pointsTextArea = new TextArea();
    pointsTextArea.setFont(Font.font("Monospace", FontWeight.BLACK, 14));
    pointsTextArea.setMaxWidth(175);
    pointsTextArea.setMinHeight(397);
    pane.add(pointsTextArea, 1, 1, 2, 1);

    Button buildButton = new Button("Построить триангуляцию");
    buildButton.setMinWidth(175);
    buildButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        String text = pointsTextArea.getText();
        Scanner scanner = new Scanner(text);
        scanText(scanner);
      }
    });
    pane.add(buildButton, 1, 2);

    Scene sc = new Scene(pane, 700, 510);

    primaryStage.setTitle("Триангуляция");
    primaryStage.setScene(sc);
    primaryStage.setResizable(true);
    primaryStage.show();
  }
  public void scanText(Scanner scanner) {
    double x, y, d = 1.0;

    ArrayList<Point> points = new ArrayList<>();

    if (scanner.hasNextDouble()) {
      d = scanner.nextDouble();
    }
    while (scanner.hasNextDouble()) {
      x = scanner.nextDouble();
      if (scanner.hasNextDouble()) {
        y = scanner.nextDouble();
        points.add(new Point(x, y));
      }
    }
    if (points.size() == 0) {
      Alert alert = new Alert(Alert.AlertType.ERROR, "Пожалуйста, введите точки.");
      alert.showAndWait();
    } else
      buildTriang(points, d);
  }

  public void buildTriang(ArrayList<Point> points, double d) {
    clearCanvas();
    minX = points.get(0).X(); maxX = points.get(0).X();
    minY = points.get(0).Y(); maxY = points.get(0).Y();
    for (int i = 0; i < points.size(); i++) {
      Point p = points.get(i);
      if (p.X() > maxX) maxX = p.X();
      if (p.X() < minX) minX = p.X();
      if (p.Y() > maxY) maxY = p.Y();
      if (p.Y() < minY) minY = p.Y();
    }
    Drawer drawer = new Drawer(canvas.getGraphicsContext2D(), minX, maxX, minY, maxY);

    try {
      Polygon poly = new Polygon(points);

      int size = poly.size();
      for (int i = 0; i < size; i++) {
        Point p = poly.get(i);
        drawer.point(p);
        drawer.line(p, poly.get((i+1)%size));
        //gc.strokeLine(getScaleX(p.X()), getScaleY(p.Y()), getScaleX(poly.get((i+1) % size).X()), getScaleY(poly.get((i+1) % size).Y()));
      }
      Triangulation tr = new Triangulation(poly, d);

      GraphicsContext gc = canvas.getGraphicsContext2D();
      gc.setStroke((new Color(0.2,0.2,0.2,1.)));
      gc.setLineWidth(1.5);
      for (int i = 0; i < tr.getTriangles().size(); i++) {
        Triangle t = tr.getTriangles().get(i);
        drawer.line(t.pA(), t.pB());
        drawer.line(t.pB(), t.pC());
        drawer.line(t.pC(), t.pA());

      }
    } catch (IOException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR, "Количество точек должно быть не менее 3.");
      alert.showAndWait();
    }
  }

  private double getScaleX(double x) {
    return (x - minX + 1) * scaleX;
  }
  private double getScaleY(double y) {
    return CANVAS_HEIGHT - (y - minY + 1) * scaleY;
  }
}