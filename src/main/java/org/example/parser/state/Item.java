package org.example.parser.state;

import java.util.List;
import java.util.Objects;

public class Item {
    private final String lhs;
    private final List<String> rhs;
    private final Integer positionForDot;

    public String getLhs(){
        return this.lhs;
    }

    public List<String> getRhs(){
        return this.rhs;
    }

    public boolean dotIsLast(){
        return this.positionForDot == this.rhs.size();
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

        return lhs + "->" + stringRightHandSide1 + "." + stringLeftHandSide2;
    }

    @Override
    public int hashCode(){
        return Objects.hash(lhs, rhs, positionForDot);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Same reference
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Null or different class
        }
        Item other = (Item) obj;
        return Objects.equals(other.toString(), this.toString());
    }
}
