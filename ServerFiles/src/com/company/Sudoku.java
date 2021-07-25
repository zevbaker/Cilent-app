package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.*;
import java.util.stream.IntStream;

public class Sudoku {
    private static final int BOARD_START_INDEX = 0;
    private static final int BOARD_SIZE = 9;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 9;
    private static final int NO_VALUE = 0;
    private static final int SUBSECTION_SIZE = 3;
    private int[][] board;
    boolean completed;

    public Sudoku() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        completed = false;
    }

    public static Sudoku GetInstance(String board){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(board, Sudoku.class);
    }

    public boolean isCompleted() {
        return completed;
    }

    public String[] checkBoard(){

        ArrayList<Index> duplicateIndex = new ArrayList<>();

        valid_row(board,duplicateIndex);
        valid_col(board,duplicateIndex);
        valid_squares(board,duplicateIndex);

        String[] res = new String[duplicateIndex.size()];

        if(duplicateIndex.size()==0){
            completed = check_empty_cells();
        }

        for (int i = 0; i < duplicateIndex.size(); i++) {
            res[i] = duplicateIndex.get(i).send();
        }

        return res;
    }

    private boolean check_empty_cells(){
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int cell = 0; cell < BOARD_SIZE; cell++)
                if (board[row][cell] == NO_VALUE){
                    return false;
                }
        }
        return true;
    }

    private void valid_squares(int[][] board, ArrayList<Index> res) {

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                HashMap<Integer,Index>set = new HashMap<>();
                //todo ck that this works
                if (board[row][col] != NO_VALUE && !res.contains(new Index(row,col))){
                    for (int i = 0; i < BOARD_SIZE; i++) {
                        if(((SUBSECTION_SIZE * (row / SUBSECTION_SIZE) + i / SUBSECTION_SIZE != row) && (SUBSECTION_SIZE * (col / SUBSECTION_SIZE) + i % SUBSECTION_SIZE!= col)) && board[SUBSECTION_SIZE * (row / SUBSECTION_SIZE) + i / SUBSECTION_SIZE][ SUBSECTION_SIZE * (col / SUBSECTION_SIZE) + i % SUBSECTION_SIZE] == board[row][col]){

                            if (!res.contains(new Index(row+1,col+1)))
                                res.add(new Index(row+1,col+1));

                            if (!res.contains(new Index(SUBSECTION_SIZE * (row / SUBSECTION_SIZE) + i / SUBSECTION_SIZE + 1,SUBSECTION_SIZE * (col / SUBSECTION_SIZE) + i % SUBSECTION_SIZE+1)))
                                res.add(new Index((SUBSECTION_SIZE * (row / SUBSECTION_SIZE) + i / SUBSECTION_SIZE)+1,(SUBSECTION_SIZE * (col / SUBSECTION_SIZE) + i % SUBSECTION_SIZE)+1));

                        }
                    }

                }
            }

        }
    }

    public void valid_row(int [][] grid,ArrayList<Index> res){

        for (int row = 0; row < BOARD_SIZE; row++) {
            int temp[] = grid[row];
            HashMap<Integer,Index>set = new HashMap<>();
            for (int i = 0; i < temp.length; i++) {
                if (temp[i] != NO_VALUE){
                    if (set.containsKey(temp[i])){
                        if (!res.contains(new Index(row+1,i+1))){
                            res.add(new Index(row+1,i+1));
                        }

                        if (!res.contains(set.get(temp[i]))){
                            res.add(set.get(temp[i]));
                        }

                    }else {
                        set.put(temp[i],new Index(row+1,i+1));
                    }
                }
            }
        }
    }

    public void valid_col( int [][] grid,ArrayList<Index> res){

        for (int col = 0; col < BOARD_SIZE; col++) {
            HashMap<Integer,Index>set = new HashMap<>();
            for (int i =0 ; i< BOARD_SIZE; i++) {
                if (grid[i][col] != NO_VALUE){
                    if (set.containsKey(grid[i][col])){
                        if (!res.contains(new Index(i+1,col+1))){
                            res.add(new Index(i+1,col+1));
                        }

                        if (!res.contains(set.get(grid[i][col]))){
                            res.add( set.get(grid[i][col]));
                        }

                    }else {
                        set.put(grid[i][col],new Index(i+1,col+1));
                    }
                }
            }

        }
    }

    public String send() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
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

    public void generateNewBoard(Difficulty difficulty) {
        Random rand = new Random();
        int[] randCol = new int[BOARD_SIZE];
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            numbers.add(i+1);
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            randCol[i] = numbers.remove(rand.nextInt(numbers.size()));
        }

        int randColIndex = rand.nextInt(BOARD_SIZE);
        for (int row = 0; row < BOARD_SIZE; row++) {
            this.board[row] = new int[BOARD_SIZE];
            this.board[row][randColIndex] = randCol[row];
        }

        solve(board);
        printBoard();
        removeNumbers(difficulty);
        printBoard();
    }

    private void removeNumbers(Difficulty difficulty) {

        int number;
        Random rand = new Random();
        switch (difficulty){
            case easy -> {
                number = 30+ rand.nextInt(10);
                break;
            }
            case medium -> {
                number = 40+ rand.nextInt(10);
                break;
            }
            default -> {
                number = 50+ rand.nextInt(10);
                break;
            }
        }

        for (int i = 0; i < number; i++) {
            removeNumber();
        }
    }

    private void removeNumber() {

        Random rand = new Random();
        int row = rand.nextInt(BOARD_SIZE);
        int cell = rand.nextInt(BOARD_SIZE);

        if (board[row][cell] == NO_VALUE){
            removeNumber();
        }else {
            board[row][cell] = NO_VALUE;
        }
    }

    private boolean solve(int[][] board) {
        for (int row = BOARD_START_INDEX; row < BOARD_SIZE; row++) {
            for (int column = BOARD_START_INDEX; column < BOARD_SIZE; column++) {
                if (board[row][column] == NO_VALUE) {
                    for (int k = MIN_VALUE; k <= MAX_VALUE; k++) {
                        board[row][column] = k;
                        if (isValid(board, row, column) && solve(board)) {
                            return true;
                        }
                        board[row][column] = NO_VALUE;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] board, int row, int column) {
        return (rowConstraint(board, row)
                && columnConstraint(board, column)
                && subsectionConstraint(board, row, column));
    }

    private boolean rowConstraint(int[][] board, int row) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        return IntStream.range(BOARD_START_INDEX, BOARD_SIZE)
                .allMatch(column -> checkConstraint(board, row, constraint, column));
    }

    private boolean columnConstraint(int[][] board, int column) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        return IntStream.range(BOARD_START_INDEX, BOARD_SIZE)
                .allMatch(row -> checkConstraint(board, row, constraint, column));
    }

    private boolean subsectionConstraint(int[][] board, int row, int column) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        int subsectionRowStart = (row / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subsectionRowEnd = subsectionRowStart + SUBSECTION_SIZE;

        int subsectionColumnStart = (column / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subsectionColumnEnd = subsectionColumnStart + SUBSECTION_SIZE;

        for (int r = subsectionRowStart; r < subsectionRowEnd; r++) {
            for (int c = subsectionColumnStart; c < subsectionColumnEnd; c++) {
                if (!checkConstraint(board, r, constraint, c)) return false;
            }
        }
        return true;
    }

    boolean checkConstraint(int[][] board,int row,boolean[] constraint,int column) {
        if (board[row][column] != NO_VALUE) {
            if (!constraint[board[row][column] - 1]) {
                constraint[board[row][column] - 1] = true;
            } else {
                return false;
            }
        }
        return true;
    }

    private void printBoard() {
        for (int row = BOARD_START_INDEX; row < BOARD_SIZE; row++) {
            for (int column = BOARD_START_INDEX; column < BOARD_SIZE; column++) {
                System.out.print(board[row][column] + " ");
            }
            System.out.println();
        }
    }
}