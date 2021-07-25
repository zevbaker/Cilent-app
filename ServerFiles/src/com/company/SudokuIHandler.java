package com.company;

import java.io.*;
import com.google.gson.*;

import java.util.Random;

public class SudokuIHandler implements IHandler {
    private volatile boolean doWork = true;

    @Override
    public void resetMembers() {
        this.doWork = true;
    }

    @Override
    public void handle(InputStream fromClient, OutputStream toClient) throws IOException, ClassNotFoundException {

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(toClient);
        ObjectInputStream objectInputStream = new ObjectInputStream(fromClient);

        boolean doWork = true;

        System.out.println("server:  handle client's tasks");
        while(doWork){

            String command = objectInputStream.readObject().toString();
            System.out.println("server: got "+ command);

            switch (command){
                case "rand":{
                    Random rnd = new Random();
                    int res =rnd.nextInt(3);
                    System.out.println("server rand: "+res);
                    objectOutputStream.writeObject(res);
                    break;
                }
            
                case "Ck":{
                    Sudoku board = Sudoku.GetInstance(objectInputStream.readObject().toString());
                    System.out.println("server ck got: " + board);

                    System.out.println("server cking Sudoku: ");

                    String[] probIndexs = board.checkBoard();
                    if(board.isCompleted()){
                        objectOutputStream.writeObject("EndGame");
                    }

                    objectOutputStream.writeObject("index");
                    objectOutputStream.writeObject(probIndexs);
                    break;
                }

                case "Generate":{
                    String difficulty = objectInputStream.readObject().toString();
                    System.out.println("server ck got: " + difficulty);

                    Sudoku s = new Sudoku();
                    s.generateNewBoard(Difficulty.valueOf(difficulty));
                    objectOutputStream.writeObject(s.send());

                    break;
                }

                case "stop":{
                    doWork = false;
                    break;
                }
            }
        }
    }
}
