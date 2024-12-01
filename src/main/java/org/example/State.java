package org.example;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class State {
    private Set<Item> items;


    public State(Set<Item> states){
        this.items = states;
    }

    public Set<Item> getItems(){
        return items;
    }

    @Override
    public String toString(){
        return items.toString();
    }

    public List<String> getSymbolsSucceedingTheDot(){
        Set<String> symbols = new LinkedHashSet<>();

        for(Item i: items){
            if(i.getPositionForDot() < i.getRhs().size())
                symbols.add(i.getRhs().get(i.getPositionForDot()));
        }

        return new ArrayList<>(symbols);
    }

    @Override
    public boolean equals(Object item){
        if(item instanceof  State){
            return ((State) item).getItems().equals(this.getItems());
        }

        return false;
    }
}
