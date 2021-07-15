package com.byteme.cilent_app.models;

public class GameSave {
    private Sudoku startingBoard;
    private Sudoku currentBoard;

    public GameSave(Sudoku startingBoard) {
        this.startingBoard = startingBoard;
        this.currentBoard = startingBoard;
    }

    public GameSave(Sudoku startingBoard,Sudoku currentBoard) {
        this.startingBoard = startingBoard;
        this.currentBoard = currentBoard;
    }

    public GameSave() {

    }

    public Sudoku getStartingBoard() {
        return startingBoard;
    }

    public void setStartingBoard(Sudoku startingBoard) {
        this.startingBoard = startingBoard;
    }

    public Sudoku getCurrentBoard() {
        return currentBoard;
    }

    public void setCurrentBoard(Sudoku currentBoard) {
        this.currentBoard = currentBoard;
    }
}
