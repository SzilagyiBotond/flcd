package org.example.parser.lrTable;

import org.example.parser.lr0.CanonicalCollection;
import org.example.parser.state.Action;
import org.example.parser.state.Item;
import org.example.parser.state.State;
import org.example.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LrRow {
    private State state;
    private Action stateAction;
    private List<Pair<String,State>> gotoList;
    private Pair<List<String>,List<String>> reduceProduction = null;

    public LrRow(State state,CanonicalCollection collection) {
        this.state = state;
        createTable(collection);
    }

    private void createTable(CanonicalCollection canonicalCollection){
        gotoList = new ArrayList<Pair<String,State>>();
        stateAction = state.getStateAction();
        switch(stateAction){

            case SHIFT -> {
                for (Map.Entry<Pair<State,String>,State> entry: canonicalCollection.getLrTable().entrySet()){
                    Pair<State,String> pair = entry.getKey();
                    if(pair.getFirst()==state){
                        gotoList.add(new Pair<>(pair.getSecond(),entry.getValue()));
                    }
                }
            }
            case REDUCE -> {
                Item item = state.getItems().stream().findAny().orElse(null);
                reduceProduction = new Pair<>(List.of(item.getLhs()),item.getRhs());
            }
            case ACCEPT -> {
                return;
            }
            default -> {
                for (Map.Entry<Pair<State,String>,State> entry: canonicalCollection.getLrTable().entrySet()){
                    Pair<State,String> pair = entry.getKey();
                    //ezt szebben is meg lehet oldani, de ugy dontottem, hogy nincs kedvem feldobni az lrbe egy exceptiont és ott kezelni
                    //ha te megcsinálod, mghívlk egy sörre
                    //btw fájlba kell írni, szóval ha az megvan sörre haver leszel
                    if(pair.getFirst()==state){
                        System.out.println("Error");
                        System.out.println("State is " + stateAction);
                        System.out.println("State: " + state);
                        throw new RuntimeException("Error");
                    }
                }
            }

        }
    }
}
