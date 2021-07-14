package com.company;

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

    public String send() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
}
