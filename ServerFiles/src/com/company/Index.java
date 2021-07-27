package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

// represents a position on the boardgame

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Index index = (Index) o;
        return row == index.row && cell == index.cell;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, cell);
    }
}
