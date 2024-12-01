package org.example;

import java.util.List;
import java.util.Objects;

public class Item {
    private String lhs;
    private List<String> rhs;
    private Integer positionForDot;

    public String getLhs(){
        return this.lhs;
    }

    public List<String> getRhs(){
        return this.rhs;
    }

    public Item(String leftHandSide, List<String> rightHandSide, Integer positionForDot) {
        this.lhs = leftHandSide;
        this.rhs = rightHandSide;
        this.positionForDot = positionForDot;
    }

    public Integer getPositionForDot(){
        return this.positionForDot;
    }

    @Override
    public String toString(){
        List<String> rightHandSide1 = this.rhs.subList(0, positionForDot);

        String stringRightHandSide1 = String.join("", rightHandSide1);

        List<String> rightHandSide2 = this.rhs.subList(positionForDot, this.rhs.size());

        String stringLeftHandSide2 = String.join("", rightHandSide2);

        return lhs.toString() + "->" + stringRightHandSide1 + "." + stringLeftHandSide2;
    }

    @Override
    public int hashCode(){
        return Objects.hash(lhs, rhs, positionForDot);
    }

    @Override
    public boolean equals(Object item) {
        return item instanceof Item && Objects.equals(((Item)item).lhs, this.lhs) && Objects.equals(((Item)item).rhs, this.rhs) && Objects.equals(((Item)item).positionForDot, this.positionForDot);
    }
}
