package sample.Elements;

import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Vector;


public class Element extends Pane {
    public final static int radius = 10;
    private boolean obstacle = false ;
    private boolean visited = false;
    private boolean processed=false;
    private final Circle circle = new Circle(radius);
    //    private int h; // hint of how much Node are traversed
    private Element prt; //parent
    private Vector<Element> vector = new Vector<>() ; //Neighbours
    private int positionX,positionY;
    private double localGoal = Double.POSITIVE_INFINITY;
    private double globalGoal = Double.POSITIVE_INFINITY;


    public Element(){
        //Default constructor

    }

    public Element(int translatex , int translatey,int positionX , int positionY){

        this.positionX=positionX;
        this.positionY=positionY;

        circle.setFill(Color.GREY);
        circle.setStroke(Color.rgb(10,20,53));
        circle.setCenterX(radius);
        circle.setCenterY(radius);

        setPrefSize(10,10);
        setTranslateX(translatex);
        setTranslateY(translatey);

        getChildren().addAll(circle);


        setOnMouseClicked(e-> {
            if (e.getButton() == MouseButton.PRIMARY){
                if (!obstacle) {
                    circle.setFill(Color.DARKMAGENTA);
                    obstacle=true;



                } else {
                    circle.setFill(Color.GREY);
                    obstacle=false;
                }

            }   // first if
        });

    }


    public boolean isObstacle() {

        return obstacle;
    }

    public double getCenterX(){

        return circle.getCenterX();
    }
    public double getCenterY(){

        return circle.getCenterY();
    }

    public void setPrt(Element prt) {
        this.prt = prt;
    }

    public Element getPrt() {
        return prt;
    }

    public void setColor(Color color){
        circle.setFill(color);
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionX(int positionX){
        this.positionX=positionX;

    }

    public void setPositionY(int positionY){
        this.positionY=positionY;

    }

    public double getLocalGoal() {
        return localGoal;
    }

    public void setLocalGoal(double localGoal) {
        this.localGoal = localGoal;
    }

    public double getGlobalGoal(){

        return globalGoal;
    }

    public void setGlobalGoal(double globalGoal) {
        this.globalGoal = globalGoal;
    }

    public Vector<Element> getVector() {
        return vector;
    }

    public void setVector(Element element) {
        this.vector.add(element);
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisited() {
        return visited;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    /*
    public int getH() {
        return h;
    }

    public void setH(int h){
        this.h=h;
    }
    */

}
