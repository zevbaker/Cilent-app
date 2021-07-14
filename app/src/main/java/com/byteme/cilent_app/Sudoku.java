package com.byteme.cilent_app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;


public class Sudoku implements Serializable {
    private  int[][] board;

    public Sudoku(){
        board = new int[9][9];
    }

    public Sudoku(int[][] board){
        this.board = board;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public void updateBoard(int row, int cell,int val) {

        this.board[row][cell] = val;
     }

    public static Sudoku GetInstance(String board){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(board, Sudoku.class);
    }

    @Override
    public String toString() {
        String res ="";
        for (int[] row: board ) {
            for (int num: row ) {
                res += num+"";
            }
        }
        return res;
    }


    public String send() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
}
