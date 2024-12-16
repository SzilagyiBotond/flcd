package org.example.parser.state;

import org.example.parser.lr0.Grammar;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class State {
    private final Set<Item> items;

    public Action getStateAction() {
        return stateAction;
    }

    private Action stateAction;

    public State(Set<Item> states){
        this.items = states;
        setStateAction();
    }

    private void setStateAction(){
        if(items.size()==1 && ((Item)items.toArray()[0]).getLhs() == Grammar.enrichedStartingGrammarSymbol && ((Item)items.toArray()[0]).getRhs().size()== ((Item) items.toArray()[0]).getPositionForDot()){
            stateAction = Action.ACCEPT;
        } else if (items.size()==1 && ((Item)items.toArray()[0]).getRhs().size()== ((Item) items.toArray()[0]).getPositionForDot()) {
            stateAction = Action.REDUCE;            
        } else if (items.stream().allMatch(item -> item.getRhs().size() > item.getPositionForDot())) {
            stateAction = Action.SHIFT;
        } else if (items.size()>1 && items.stream().allMatch(item -> item.getRhs().size() == item.getPositionForDot())) {
            stateAction = Action.REDUCE_REDUCE_CONFLICT;
        }else{
            stateAction = Action.SHIFT_REDUCE_CONFLICT;
        }
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
