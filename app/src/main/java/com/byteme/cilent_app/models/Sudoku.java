package com.byteme.cilent_app.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Sudoku {
    private static final int BOARD_START_INDEX = 0;
    private static final int BOARD_SIZE = 9;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 9;
    private static final int NO_VALUE = 0;
    private static final int SUBSECTION_SIZE = 3;
    private int[][] board;
    boolean completed;


    public Sudoku(){
        board = new int[9][9];
        completed = false;
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
