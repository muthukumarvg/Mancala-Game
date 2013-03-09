/**
FILENAME: Board.java 
WHAT: Defines a class,Game, to store information about the
 Mancala board
@author Sydney Steward and Polina Soshnin
*/


import java.io.*;    
import java.util.*;  
import java.net.*; 
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game extends JApplet implements ActionListener{
    /**
       Description of instance variables
       player1: boolean that denotes whether player1 or player2 is allowed 
       to click on a certain pile on the board
       turn1: boolean denoting whose turn it is
    */
    public LinkedList<Pile> piles;
    private boolean player1;
    private boolean isLandedMancala;
    public Board aBoard;

   
    /**
       Constructs a Board with a 12 piles and 2 mancalas in a linked list.
       The 12 piles are populated with 4 pebbles each.
    */
    public Game() {
      //add to our linkedlist
      aBoard= new Board();
      player1=true;
piles= new LinkedList<Pile>();
piles.add(new Pile(4));
piles.add(new Pile(4));
piles.add(new Pile(4));
piles.add(new Pile(4));
piles.add(new Pile(4));
piles.add(new Pile(4));
piles.add(new Pile(0));
piles.get(6).toggleMancala(); //this pile becomes a mancala
piles.add(new Pile(4));
piles.add(new Pile(4));
piles.add(new Pile(4));
piles.add(new Pile(4));
piles.add(new Pile(4));
piles.add(new Pile(4));
piles.add(new Pile(0));
piles.get(13).toggleMancala(); //this pile becomes a mancala
isLandedMancala=false;
    }
    
    public String toString(){
      return piles.toString();
      
    }
    
    /**
       Determines whether the game has reached its end, by using 
       isSideEmpty to check the state of each side of the board
    */
    public boolean isGameOver() {
 return isSideEmpty();
    }

    /**
       Determines whether a side of the board is empty
    */
    public boolean isSideEmpty() {
      //one of two cases needs to be true:
      //either 0-5 are empty or 7-12 are empty
      if (isBottomSideEmpty()||isTopSideEmpty()){
        return true;
      } else{
 return false;
    }
    }
    
    //checks if labels 0-5 are empty 
      public boolean isBottomSideEmpty() {
        int counter=0;
        for (int i=0; i<6; i++){
          if(piles.get(i).isEmpty()){
                     counter++;
          } }
       return (counter==6); //is empty is all 6 are empty 
      }
      
       //checks if labels 7-12 are empty 
      public boolean isTopSideEmpty() {
        int counter=0;
        for (int i=7; i<13; i++){
          if(piles.get(i).isEmpty()){
                     counter++;
          } }
       return (counter==6); //is empty is all 6 are empty 
      }
      
    /**
       Takes a turn by distributing the contents of a chosen
       pile across the other piles on the board. Traverses the
       linked list, popping from the pile given as a parameter 
       and pushing the popped pebble onto the currrent pile.
    */
      //I think the parameter should be a number corresponding to it's place 
      //in the linkedlist 
    public void makeSingleMove(Integer currentPile) {
     //sets isLandedMancala to false just in case
      
      if((player1==false && currentPile<6)||(player1==true && currentPile>6)){
        //do nothing
      } else{
      
      
      isLandedMancala=false;
      int i=currentPile;
//I want to take the pile at piles.get(currentPile) and dump it 
      //into all of the corresponding piles 
      //if player1==true, dump it into mancala 6
      //if player1==false, dump it into mancala 13
      //now I need to add pebbles to all of the rest piles
     i++; //increment to get to next pile 
      while(!piles.get(currentPile).isEmpty()){
        
        //now, need to check if it's the person's mancala or not 
        
        //we need a TON of conditionals for these guys, I'm just getting started...
        if((player1==true)&&(i==6)){
          piles.get(6).addPebble(); //adds pebble to player 1's mancala
         
          //UPDATE GUI HERE 
           updateImage(6); //updates the image 
          
          if(piles.get(currentPile).checkIfLastPebble()==true){
            isLandedMancala=true;    
          }
            //we want player1 to go again 
          piles.get(currentPile).removePebble(); //need to take into consideration if it's 
          updateImage(currentPile);
          //the last one that landed in it 
          i++;
        } else if ((player1==false)&&(i==13)){
          piles.get(13).addPebble(); //adds pebble to player 2's mancala
          updateImage(13);
           if(piles.get(currentPile).checkIfLastPebble()==true){
            isLandedMancala=true;    
          }
         piles.get(currentPile).removePebble();
         updateImage(currentPile);
          //if the pebble is dropped into an empty pile,
         i++;
        } else if ((player1==true && i==13) || (player1==false && i==6)) {
          //do nothing
          i++;
        } else if (piles.get(currentPile).checkIfLastPebble()==true &&
        piles.get(i).isEmpty() && isAcrossFull(i) && isOnYourSide(i)) {
         piles.get(currentPile).removePebble();
         updateImage(currentPile);
         if (player1==true) {
           piles.get(6).addPebble();
           updateImage(6);
         } else {
           piles.get(13).addPebble();
           updateImage(13);
         }
        //fill more stuff in...
         allotPebbles(i);
         //do not need to update i
        } else {
         piles.get(currentPile).removePebble();
         updateImage(currentPile);
        
         if (!piles.get(i).isEmpty()) {
            piles.get(i).addPebble();
            updateImage(i);
         } else {
           piles.get(i).addPebble();
           updateImage(i);
           piles.get(i).peek().toggleLastTrue();
        }
         i++;
        }
        if (i==14) {
          i=0;
        }
      
    }
      if (isLandedMancala==false) {
        if (player1==true) {
          player1=false;
        } else {
         player1=true; 
        }
      }
    }
    }
     //Parses the description of the gif, updates the number, then finds the gif matching that number
    //and resets the icon
    public void updateImage(Integer i){
    //first we want to get the number of pebbles in the pile
      
      Integer numPebbles= piles.get(i).getNumPebbles();
      
      //if it's not a mancala pile, update the pile buttons
      if ((i!=6)||(i!=13)){
    //then, we want to find the image that matches the number of pebbles
      aBoard.pileJButtons[i].setIcon(aBoard.pileImages[numPebbles]);
      //then we want to reset that image to button i
      
      //if it's a mancala, update the mancala
      } else  {
        
        aBoard.pileLabelArray[i].setIcon(aBoard.pileImages[numPebbles]);
      
    }
    }
    
    
    public boolean isOnYourSide(Integer nationWide) {
      if (player1==true && nationWide<=5) {
        return true;
      } else if (player1==false && nationWide>5) {
        return true;
      } else {
       return false; 
      }
    }

    /**
       During a move, checks whether the last pebble to be
       distributed landed in an empty pile with pebbles
       across from it. Will call findAcross() method in order
       to find the Pile to check.
     */
    public boolean isAcrossFull(Integer thisOne) {
     Integer pileAcross= 12-thisOne; //BAM
     if (piles.get(pileAcross).isEmpty()) {
       return false;
     } else {
       return true;
     }
    }


    /**
       Redistributes pebbles from a given pile to the mancala of
       the current player.
    */
    public void allotPebbles(Integer i) {
      while (!piles.get(12-i).isEmpty()) {
        if (player1==true) {
          piles.get(12-i).removePebble(); 
          piles.get(6).addPebble();
          updateImage(6);
        } else {
          piles.get(12-i).removePebble(); 
          piles.get(13).addPebble();
          updateImage(13);
        }
      }
    }
    
     //initialize GUI display to start a new game
    //all piles will automatically have 4 pieces in them
    /**
     *Calls constructor in Game class to 
     *initialize a Game that will populate 
     */
    public void startNewGame(){

    }
    
        public void actionPerformed(ActionEvent event){
 //handles events generated by Button clicks
 Object source= event.getSource();
 if(source.equals(aBoard.startButton)){
  // aBoard.pileLabelArray[0].setIcon(aBoard.pileImages[6]);
  // validate();
  // repaint();
    startNewGame();
 }else if (source.equals(aBoard.quitButton)){
     System.exit(0);
        } else if (source.equals(aBoard.pileJButtons[0])){
          makeSingleMove(0);
          aBoard.pileJButtons[0].setIcon(aBoard.pileImages[9]); 
          aBoard.pileJButtons[0].revalidate();
          aBoard.pileJButtons[0].repaint();
        } else if (source.equals(aBoard.pileJButtons[1])){
          makeSingleMove(1);
         // revalidate();
          repaint();
        } else if (source.equals(aBoard.pileJButtons[2])){
          makeSingleMove(2);
          //revalidate();
          repaint();
        } else if (source.equals(aBoard.pileJButtons[3])){
          makeSingleMove(3);
          //revalidate();
          repaint();
        } else if (source.equals(aBoard.pileJButtons[4])){
          makeSingleMove(4);
         // revalidate();
          repaint();
        } else if (source.equals(aBoard.pileJButtons[5])){
          makeSingleMove(5);
         // revalidate();
          repaint();
        } else if (source.equals(aBoard.pileJButtons[7])){
          makeSingleMove(7);
         // revalidate();
          repaint();
        } else if (source.equals(aBoard.pileJButtons[8])){
          makeSingleMove(8);
         // revalidate();
          repaint();
        } else if (source.equals(aBoard.pileJButtons[9])){
          makeSingleMove(9);
          //revalidate();
          repaint();
        } else if (source.equals(aBoard.pileJButtons[10])){
          makeSingleMove(10);
         // revalidate();
          repaint();
        } else if (source.equals(aBoard.pileJButtons[11])){
          makeSingleMove(11);
         // revalidate();
          repaint();
        } else if (source.equals(aBoard.pileJButtons[12])){
          makeSingleMove(12);
          //revalidate();
          repaint();
        }
        }
        
         public void init() {

 //adds panels to the Applet display
 setLayout(new GridLayout(1,5,8,4));
 //add JPanels here;
      //setLayout(new BorderLayout(4,4));
      //add(p1, BorderLayout.WEST);
 //add(borderPanel);
 add(aBoard.p9);
 add(aBoard.p1);
 add(aBoard.p2);
 add(aBoard.p3);
 add(aBoard.p4);
 add(aBoard.p5);
 add(aBoard.p6);
 add(aBoard.p7);
 add(aBoard.p8);
 
 //add(buttonPanel);
 //add(rulePane);
//add the rest of the JPanels 
    }
        
           private static void createAndShowGUI(){

 //enable window decorations
 JFrame.setDefaultLookAndFeelDecorated(true);
 
 //Create and set up the window
 JFrame frame = new JFrame("Mancala Board Game");

 frame.setSize(800, 500);
 
 //specify that the close button exits the application
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
 Game aGame= new Game();
 aGame.init();
 
 frame.add(aGame, BorderLayout.CENTER);

 //Display the window
 frame.setVisible(true);
    }

    /**
       Main method to test Board class instance methods.
    */
    public static void main(String[] args) {
 /**Game aGame = new Game();
 System.out.println(aGame);
 System.out.println(aGame.player1);
 aGame.makeSingleMove(2);
 System.out.println();
 System.out.println(aGame);
  System.out.println(aGame.player1);*/
 /**System.out.println(aGame.isGameOver());
 System.out.println(aGame.isAcrossFull(5));
 aGame.piles.get(7).clearPile();
 aGame.piles.get(8).clearPile();
 aGame.piles.get(9).clearPile();
 aGame.piles.get(10).clearPile();
 aGame.piles.get(11).clearPile();
 aGame.piles.get(12).clearPile();
 System.out.println(aGame);
 System.out.println(aGame.isBottomSideEmpty());
  System.out.println(aGame.isTopSideEmpty());
  //System.out.println(aGame);
  System.out.println(aGame.isAcrossFull(0));
  System.out.println(aGame.isGameOver());*/
  
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
    }
}