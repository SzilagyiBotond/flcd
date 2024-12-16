package org.example.parser.lr0;

import org.example.parser.state.State;
import org.example.utils.Pair;

import java.util.*;

public class CanonicalCollection {
    private List<State> states;

    public Map<Pair<State, String>, State> getLrTable() {
        return lrTable;
    }

    private Map<Pair<State, String>, State> lrTable;

    public CanonicalCollection() {
        this.states = new ArrayList<>();
        lrTable = new LinkedHashMap<>();
    }

    public CanonicalCollection(List<State> states, Map<Pair<State, String>, State> lrTable) {
        this.states = states; this.lrTable = lrTable;
    }

    public void addState(State state) {
        this.states.add(state);
    }

    public List<State> getStates() {
        return this.states;
    }

    public void addLrTable(State startState, String symbol, State endState){
        lrTable.put(new Pair<>(startState, symbol), endState);
    }

    public CanonicalCollection copy() {
        // Shallow copy of the states list
        List<State> copiedStates = new ArrayList<>(this.states);
        Map<Pair<State, String>, State> copiedLrTable = new LinkedHashMap<>(this.lrTable);
        // Return a new CanonicalCollection with the copied states
        return new CanonicalCollection(copiedStates,copiedLrTable); // Pass null for adjacencyList if it's not relevant
    }
}
