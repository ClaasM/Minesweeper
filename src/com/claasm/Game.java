package com.claasm;

import java.util.Scanner;

/**
 * Created by claasmeiners on 17/07/17.
 * This class manages the input from the user and the output from the game in a simple game loop
 */
class Game {
    //Used to read the user input
    private Scanner sc;

    //The board holds all the pieces
    private Board board;

    /**
     * Creates a game.
     */
    Game() {
        this.board = new Board(7,7);
        this.sc = new Scanner(System.in);
    }

    /**
     * Starts the game.
     */
    void start(){
        boolean isRunning = true;
        while(isRunning){
            //Start each turn by showing the user the current state of the board.
            System.out.println(board);
            //Get the input for the next turn from the user
            System.out.println("Which action? c for click, f for flag");
            String action = sc.next(); //Flag or click
            System.out.println("Which column?");
            int x = sc.nextInt(board.getWidth()); //x position of the selected cell
            System.out.println("Which row?");
            int y = sc.nextInt(board.getHeight()); //y position of the selected cell

            //Perform the appropriate action
            if("c".equals(action)){
                //The user left-clicked on a cell
                boolean wasMine = board.click(x,y);
                boolean won = board.isCleared();
                if(wasMine){
                    //The user selected a mine, end the game
                    System.out.println("You hit a mine, Game Over!");
                    isRunning = false;
                } else if (won){
                    //The user exposed all empty cells
                    System.out.println("You won!");
                    isRunning = false;
                } //else continue
            } else if ("f".equals(action)){
                //The user right-clicked on a cell
                board.toggleFlag(x,y);
            } else {
                //The user is allowed to mistype the action, the loop will just start from the beginning in that case
                System.out.println("Invalid input!");
            }
            //Don't do anything in the loop beyond here
        }
        System.out.println("Bye Bye!");
    }
}
