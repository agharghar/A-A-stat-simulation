package sample.Elements;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class ElementStart extends Element {




    public ElementStart(int translatex, int translatey, int positionX, int positionY){
        //super(translatex ,  translatey);
        super();
        setPositionX(positionX);
        setPositionY(positionY);
        setPrefSize(10,10);
        setTranslateX(translatex);
        setTranslateY(translatey);
        Circle circle = new Circle(10,Color.RED);
        circle.setCenterX(10);
        circle.setCenterY(10);

        getChildren().addAll(circle);


    }

}
