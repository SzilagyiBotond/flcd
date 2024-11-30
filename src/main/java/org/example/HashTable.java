package org.example;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class HashTable<T> {
    private ArrayList<ArrayList<T>> table;
    private Integer size;

    public HashTable(Integer _size){
        size = _size;
        table = new ArrayList<>();
        for (int i=0;i<size;i++){
            table.add(new ArrayList<>());
        }
    }

    private Integer hash(T term){
        Integer fPos = term.hashCode();
        return fPos % size;
    }

    public Integer getSize(){
        return size;
    }

    public Position findPosition(T term){
        int firstPos = abs(hash(term));
        ArrayList<T> bucket = table.get(firstPos);
        for (int i=0;i<bucket.size();i++){
            if (bucket.get(i).equals(term)){
                return new Position(firstPos,i);
            }
        }
        return null;
    }

    public boolean add(T term){
        if (findPosition(term)!=null){
            return false;
        }
        int firstPos = abs(hash(term));
        table.get(firstPos).add(term);
        return true;
    }

    public T findByPosition(Position position){
        if(position.getFirst()>=table.size() || position.getSecond()>=table.get(position.getFirst()).size()){
            throw new ArrayIndexOutOfBoundsException("Invalid Position");
        }
        return table.get(position.getFirst()).get(position.getSecond());
    }

    public boolean containsTerm(T term){
        return findPosition(term)!=null;
    }

    public String getBucketString(int index) {
        ArrayList<T> bucket = table.get(index);
        if (bucket.isEmpty()) {
            return "empty";
        }

        StringBuilder sb = new StringBuilder();
        for (T item : bucket) {
            sb.append(item).append(" ");
        }

        return sb.toString().trim();
    }

}
