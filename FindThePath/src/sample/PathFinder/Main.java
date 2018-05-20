package sample.PathFinder;//import Elements.CustomComparator.CustomComparator;
import javafx.application.Application;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.Elements.Element;
import sample.Elements.ElementEnd;
import sample.Elements.ElementStart;

import java.util.*;

public class Main extends Application {
    private final int MAP_WIDTH = 600;
    private final int MAP_HEIGHT = 600;
    private final int ROWS = MAP_HEIGHT / (Element.radius * 2);
    private final int COLS = MAP_WIDTH / (Element.radius * 2);
    private Element[][] elementTable = new Element[ROWS][COLS];
    public Pane root = new Pane();
    // boolean pathFounded = false; // index


    /* It retun the Map and it's contatent  */
    private Parent createMap() {


        root.setPrefSize(MAP_WIDTH, MAP_HEIGHT);


        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLS; ++j) {
                if (i == ROWS - 1 && j == COLS - 1 || (i == 0 && j == 0))
                    continue;
                Element element = new Element(Element.radius * 2 * j, Element.radius * 2 * i, j,i);
                elementTable[i][j] = element;
                root.getChildren().add(element); // placing the Elements in the Map
            }
        }


        elementTable[0][0] = new ElementStart(0, 0, 0, 0);
        invokeChild(elementTable[0][0]);
        root.getChildren().add(elementTable[0][0]);
        elementTable[ROWS - 1][COLS - 1] = new ElementEnd((COLS - 1) * Element.radius * 2, (ROWS - 1) * Element.radius * 2, COLS - 1, ROWS - 1);
        invokeChild(elementTable[ROWS-1][COLS-1]);
        root.getChildren().add(elementTable[ROWS - 1][COLS - 1]);


        for ( int i=0 ; i<ROWS;++i){
            for (int j=0; j <COLS; ++j){
                if ((i== 0 & j==0) || (i== ROWS-1 && j==COLS-1) )
                    continue;

                invokeChild(elementTable[i][j]);
            }


        }




        return root;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createMap()));
        primaryStage.setTitle("Find the Path");
        pathFinder(elementTable[0][0]);
        primaryStage.getScene().setOnMouseClicked(e->{
            initElement();
            pathFinder(elementTable[0][0]);

        });


        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }




    private void pathFinder(Element currantElement) {
        List<Element> visited = new LinkedList<>();

        initElement();

        currantElement.setLocalGoal(0);
        currantElement.setGlobalGoal(heuristic(currantElement,elementTable[ROWS-1][COLS-1]));


        visited.add(currantElement);


        while (!visited.isEmpty() && currantElement != elementTable[ROWS-1][COLS-1]){


            /*  List sorting */


            /*
            Collections.sort(visited, new Comparator<Element>() {
                @Override
                public int compare(Element e1, Element e2) {
                    return  e1.getGlobalGoal() > e2.getGlobalGoal() ? 1:( e1.getGlobalGoal() < e2.getGlobalGoal() ? -1 : 0)   ;
                }
            });

            */

            visited.sort((Element e1 , Element e2)->

                    e1.getGlobalGoal() > e2.getGlobalGoal() ? 1 : (e1.getGlobalGoal() < e2.getGlobalGoal() ? -1 : 0)

            );


            /* removing visited element*/
            while (!visited.isEmpty() && ((LinkedList<Element>) visited).getFirst().isVisited())
                visited.remove(((LinkedList<Element>) visited).getFirst());



            if (visited.isEmpty())
                break;


            currantElement = ((LinkedList<Element>) visited).getFirst();
            currantElement.setVisited(true); //We flag it visited ==> if we return back in loop we will remove it
            currantElement.setColor(Color.AQUA);

            for ( Element children: currantElement.getVector()){

                if (!children.isObstacle() && !children.isVisited())
                    ((LinkedList<Element>) visited).addLast(children);

                double testLocalGoal = heuristic(children,currantElement) + currantElement.getLocalGoal();

                if (testLocalGoal < children.getLocalGoal()){

                    children.setPrt(currantElement);
                    children.setLocalGoal(testLocalGoal);
                    children.setGlobalGoal(children.getLocalGoal()+ heuristic(children,elementTable[ROWS-1][COLS-1]));
                    children.setProcessed(true);

                }

            }



        }//end while








        paintPath(elementTable[ROWS-1][COLS-1]);














    }





    private void invokeChild(Element element) {


        if (element.getPositionX() != 0 && (element.getPositionX() != (COLS- 1)) && element.getPositionY() != 0 && (element.getPositionY() != (ROWS - 1) )) { //elemnt not in the edge


            element.setVector( elementTable[(element.getPositionY() + 1)][(element.getPositionX() - 1)]  );
            element.setVector( elementTable[element.getPositionY() + 1][element.getPositionX()] );
            element.setVector( elementTable[element.getPositionY() + 1][element.getPositionX() + 1] );
            element.setVector( elementTable[element.getPositionY()][element.getPositionX() - 1] );
            element.setVector( elementTable[element.getPositionY()][element.getPositionX() + 1] );
            element.setVector( elementTable[element.getPositionY()-1][element.getPositionX() -1] );
            element.setVector( elementTable[element.getPositionY()-1][element.getPositionX() ] );
            element.setVector( elementTable[element.getPositionY()-1][element.getPositionX() + 1] );
        } else if (element.getPositionX() == 0 && element.getPositionY() != 0 && element.getPositionY() != ROWS-1)  {


            element.getVector().add( elementTable[element.getPositionY()][1] );
            element.getVector().add( elementTable[element.getPositionY() - 1][0] );
            element.getVector().add( elementTable[element.getPositionY() + 1][0] );
            element.getVector().add( elementTable[element.getPositionY() - 1][1] );
            element.getVector().add( elementTable[element.getPositionY() + 1][1] );

        } else if (element.getPositionY() == 0 && element.getPositionX() != 0 && element.getPositionX() != COLS-1) {


            element.getVector().add( elementTable[0][element.getPositionX() + 1] );
            element.getVector().add( elementTable[1][element.getPositionX()] );
            element.getVector().add( elementTable[1][element.getPositionX() + 1] );
            element.getVector().add( elementTable[0][element.getPositionX() - 1] );
            element.getVector().add( elementTable[1][element.getPositionX() - 1] );

        } else if (element.getPositionY() == ROWS - 1 && element.getPositionX() > 0 && element.getPositionX()<COLS-1 ) {


            element.setVector( elementTable[ROWS - 2][element.getPositionX()] );
            element.setVector( elementTable[ROWS - 2][element.getPositionX() + 1] );
            element.setVector( elementTable[ROWS - 1][element.getPositionX() + 1] );
            element.setVector( elementTable[ROWS - 1][element.getPositionX() - 1] );
            element.setVector( elementTable[ROWS - 2][element.getPositionX() - 1] );
        } else if (element.getPositionY() != 0 && element.getPositionY() != ROWS-1 && element.getPositionX() == COLS - 1) {


            element.setVector( elementTable[element.getPositionY() - 1][COLS - 1] );
            element.setVector( elementTable[element.getPositionY() + 1][COLS - 1] );
            element.setVector( elementTable[element.getPositionY()][COLS - 2] );
            element.setVector( elementTable[element.getPositionY() + 1][COLS - 2] );
            element.setVector( elementTable[element.getPositionY() - 1][COLS - 2] );

        } else if (element.getPositionY() == 0 && element.getPositionX() == COLS - 1) {


            element.setVector( elementTable[0][COLS - 2] );
            element.setVector( elementTable[1][COLS - 2] );
            element.setVector( elementTable[1][COLS - 1] );


        } else if (element.getPositionY() == ROWS - 1 && element.getPositionX() == COLS - 1) {


            element.setVector( elementTable[ROWS - 1][COLS - 2] );
            element.setVector( elementTable[ROWS - 2][COLS - 1] );
            element.setVector( elementTable[ROWS - 2][COLS - 2] );

        } else if (element.getPositionY() == ROWS - 1 && element.getPositionX() == 0) {


            element.setVector( elementTable[ROWS - 2][0] );
            element.setVector( elementTable[ROWS - 2][1] );
            element.setVector( elementTable[ROWS - 1][1] );
        } else if (element.getPositionX() == 0 && element.getPositionY() == 0) {

            element.setVector( elementTable[1][0] );
            element.setVector( elementTable[1][1] );
            element.setVector( elementTable[0][1] );

        }
    }


    public void initElement(){

        for (int i=0 ; i< ROWS ; i++){
            for (int j=0 ; j< COLS; j++){

                if (elementTable[i][j].isProcessed() && !elementTable[i][j].isObstacle())
                    elementTable[i][j].setColor(Color.GREY);



                elementTable[i][j].setGlobalGoal(Double.POSITIVE_INFINITY);
                elementTable[i][j].setLocalGoal(Double.POSITIVE_INFINITY);
                elementTable[i][j].setPrt(null);
                elementTable[i][j].setVisited(false);
                elementTable[i][j].setProcessed(false);

                // elementTable[i][j].setObstacle(false);
            }

        }

    }

    private double heuristic(Element e1 , Element e2){

        return Math.sqrt(
                ( e1.getPositionX()-e2.getPositionX() )*( e1.getPositionX()-e2.getPositionX() ) +
                        ( e1.getPositionY()-e2.getPositionY() )*( e1.getPositionY()-e2.getPositionY() )

        );

    }



    private void paintPath(Element element){

        if(element.equals(elementTable[0][0]))
            return ;
        if(element.getPrt() == null)
            return;
        else {
            element.setColor(Color.GREEN);
            paintPath(element.getPrt());
        }


    }

 /*






    // add the children of each elmt in the queue and remove it /
    private void search(){
        queue.add(elementTable[0][0]);      //add an elt in the queue
        addChild(invokeChild(elementTable[0][0]));  //add the children of the first elt added in the queue

        Iterator itr = queue.iterator();
        while (itr.hasNext() && !pathFounded){              //iterate the element of the queue
            queue.remove(); // remove the first elment in the queue ==> children already added
            addChild(invokeChild(queue.getFirst()));//add the children of the first elt in the queue to the queue


        }


    }



    //return the children of each Node giving , the children are obtained by the position of the node
    private Element [] invokeChild(Element element){

//       Arrays.fill(childTable,null);

       if (element.getPositionX() !=0 && element.getPositionX() != COLS-1 && element.getPositionY() != 0 && element.getPositionY() != ROWS-1){ //elemnt not in the edge
           childTable=new Element[5];

           childTable[0] = elementTable[element.getPositionY()+1][element.getPositionX()-1];
           childTable[1] = elementTable[element.getPositionY()+1][element.getPositionX()];
           childTable[2] = elementTable[element.getPositionY()+1][element.getPositionX()+1];
           childTable[3] = elementTable[element.getPositionY()][element.getPositionX()-1];
           childTable[4] = elementTable[element.getPositionY()][element.getPositionX()+1];



       } else if(element.getPositionX() == 0 && element.getPositionY() != 0 ) {
            childTable=new Element[5];

            childTable[0] = elementTable[element.getPositionY()][1];
            childTable[1]= elementTable[element.getPositionY() - 1][0];
            childTable[2]= elementTable[element.getPositionY() + 1][0];
            childTable[3]= elementTable[element.getPositionY() - 1][1];
            childTable[4] =elementTable[element.getPositionY() + 1][1];

        }else if (element.getPositionX() == 0 && element.getPositionY() !=0){
            childTable=new Element[5];

            childTable[0] = elementTable[0][element.getPositionX()+1];
            childTable[1]= elementTable[1][element.getPositionX()];
            childTable[2]= elementTable[1][element.getPositionX()+1];
            childTable[3]= elementTable[0][element.getPositionX()-1];
            childTable[4] =elementTable[1][element.getPositionX()-1];

        }else if (element.getPositionY() == ROWS-1 && element.getPositionX() != 0){
            childTable = new Element[5];

            childTable[0] = elementTable[ROWS-2][element.getPositionX()];
            childTable[1]= elementTable[ROWS-2][element.getPositionX()+1];
            childTable[2]= elementTable[ROWS-1][element.getPositionX()+1];
            childTable[3]= elementTable[ROWS-1][element.getPositionX()-1];
            childTable[4]= elementTable[ROWS-2][element.getPositionX()-1];
        }else if(element.getPositionY() !=0 && element.getPositionX() == COLS-1){
            childTable=new Element[5];

            childTable[0]= elementTable[element.getPositionY()-1][COLS-1];
            childTable[1]= elementTable[element.getPositionY()+1][COLS-1];
            childTable[2]= elementTable[element.getPositionY()][COLS-2];
            childTable[3]= elementTable[element.getPositionY()-1][COLS-2];
            childTable[4]= elementTable[element.getPositionY()+1][COLS-2];

        }else if(element.getPositionY() == 0 && element.getPositionX() == COLS-1){
           childTable=new Element[3];

           childTable[0]= elementTable[0][COLS-2];
           childTable[1]= elementTable[1][COLS-2];
           childTable[2]= elementTable[1][COLS-1];


       }else if (element.getPositionY() == ROWS-1 && element.getPositionX() == COLS-1){
           childTable=new Element[3];

           childTable[0]= elementTable[ROWS-1][COLS-2];
           childTable[1]= elementTable[ROWS-2][COLS-1];
           childTable[2]= elementTable[ROWS-2][COLS-2];

       }else if (element.getPositionY() == ROWS-1 && element.getPositionX() == 0){
           childTable=new Element[3];

           childTable[0]= elementTable[ROWS-2][0];
           childTable[1]= elementTable[ROWS-2][1];
           childTable[2]= elementTable[ROWS-1][1];
       }else if (element.getPositionX() == 0 && element.getPositionY() == 0){
           childTable=new Element[3];

           childTable[0]= elementTable[1][0];
           childTable[1]= elementTable[1][1];
           childTable[2]= elementTable[0][1];

       }


        for (int i=0 ; i<childTable.length; ++i){ // configue Nodes characteristics ==> obtain the optimum path
                if (element.getH() > childTable[i].getH()+1 || childTable[i].getH() == 0){
                    childTable[i].setPrt(element);
                    childTable[i].setH(element.getH()+1);

                }

       }



        System.gc();
    return childTable;  // return children even if there are obstacle in them
    }



    private void addChild(Element []T){

        for (Element element : T){

            if(element.isObstacle()) //check obstacle state
                continue;
            compaireQueue(element); // add children to the queue if there are not already in it
            if (element.equals(elementTable[COLS-1][ROWS-1])){ // if we found the Target
                pathFounded=true;
                paintPath(elementTable[COLS-1][ROWS-1]);

            }


        }


    }

    private void compaireQueue(Element element){
        if(queue.contains(element))
            return;
        queue.add(element);


    }







*/



}