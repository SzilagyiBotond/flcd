package org.example;

public class Position {
    private Integer first;
    private Integer second;

    public Position(Integer _first, Integer _second){
        first = _first;
        second = _second;
    }

    public Integer getFirst(){
        return first;
    }

    public Integer getSecond(){
        return second;
    }

    @Override
    public String toString() {
        return "Position: " +
                "bucket: " + first +
                ", position in bucket: " + second ;
    }
}
