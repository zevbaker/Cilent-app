package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Sudoku implements Serializable {
    private int[][] board;
    boolean completed;

    public Sudoku() {
        board = new int[9][9];
        completed = false;
    }

    public Sudoku(int[][] board) {
        this.board = board;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public static Sudoku GetInstance(String board){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(board, Sudoku.class);
    }

    public boolean isCompleted() {
        return completed;
    }

    public String[] checkBoard(){


        Boolean[] rows = new Boolean[9];
        Boolean[] cells = new Boolean[9];

        boolean resRows,resCells;
        boolean totalBoard = true;
        ArrayList<Index> duplicatIndexs = new ArrayList<>();

        for (int row = 0; row < 9; row++) {
            rows[row] = valid_row(row,board,duplicatIndexs);
        }
        for (int coll = 0; coll < 9; coll++) {
            cells[coll] = valid_col(coll,board,duplicatIndexs);
        }

        if(duplicatIndexs.size()==0){
            boolean flag = true;
            for (int i = 0; i < rows.length; i++) {
                if (rows[i] == false || cells[i] == false){
                    flag = false;
                    break;
                }
            }

            completed = flag;
        }

        String[] res = new String[duplicatIndexs.size()];

        for (int i = 0; i < duplicatIndexs.size(); i++) {
            res[i] = duplicatIndexs.get(i).send();
        }

        return res;
    }


    public boolean valid_row(int row, int [][] grid,ArrayList<Index> res){
        int temp[] = grid[row];
        Boolean flag = true;
        HashMap<Integer,Index>set = new HashMap<>();
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != 0){
                if (set.containsKey(temp[i])){
                    res.add(new Index(row+1,i+1));

                    if (!res.contains(set.get(temp[i]))){
                        res.add(set.get(temp[i]));
                    }
                    if (flag)
                        flag = false;
                }else {
                    set.put(temp[i],new Index(row+1,i+1));
                }
            }else if (flag)
                flag = false;
        }
        return flag;
    }

    public boolean valid_col(int col, int [][] grid,ArrayList<Index> res){
        Boolean flag = true;
        HashMap<Integer,Index>set = new HashMap<>();

        for (int i =0 ; i< 9; i++) {
            if (grid[i][col] != 0){
                if (set.containsKey(grid[i][col])){
                    res.add(new Index(i+1,col+1));

                    if (!res.contains(set.get(grid[i][col]))){
                        res.add( set.get(grid[i][col]));
                    }

                    if (flag)
                        flag = false;
                }else {
                    set.put(grid[i][col],new Index(i+1,col+1));
                }
            }else if (flag)
                flag = false;

        }

        return flag;
    }


    @Override
    public String toString() {
        String res = "";
        for (int[] row : board) {
            for (int num : row) {
                res += num + "";
            }
        }
        return res;
    }


}