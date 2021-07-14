package com.byteme.cilent_app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Index {
    private int row;
    private int cell;

    public Index(int row, int cell) {
        this.row = row;
        this.cell = cell;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public static Index GetInstance(String board){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(board, Index.class);
    }
}
