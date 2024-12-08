package org.example.parser.lrTable;

import java.util.ArrayList;
import java.util.List;

public class LrTable {
    private List<LrRow> entries;

    public void addRow(LrRow row){
        entries.add(row);
    }

    public LrTable() {
        this.entries = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Parsing Table: \n");

        for (LrRow entry : entries) {
            result.append(entry);
            result.append("\n");
        }

        return result.toString();
    }
}
