package org.example.scanner;

import org.example.utils.HashTable;
import org.example.utils.Position;

public class SymbolTable<T> {
    private HashTable<T> table;
    private Integer size;
    public SymbolTable(Integer _size){
        size = _size;
        table = new HashTable(size);
    }
    public boolean add(T term){
        return table.add(term);
    }
    public T findByPosition(Position position){
        return table.findByPosition(position);
    }

    public HashTable getTable() {
        return table;
    }

    public Integer getSize() {
        return size;
    }

    public Position findPosition(T term){
        return table.findPosition(term);
    }

    public boolean containsTerm(T term){
        return table.containsTerm(term);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SymbolTable:\n");

        for (int i = 0; i < table.getSize(); i++) {  // Assuming getSize() returns the number of buckets
            sb.append("Bucket ").append(i).append(": ").append(table.getBucketString(i)).append("\n");
        }

        return sb.toString();
    }
}
