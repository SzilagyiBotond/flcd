package org.example.parser.lr0;

import org.example.parser.state.State;

import java.util.ArrayList;
import java.util.List;

public class CanonicalCollection {
    private List<State> states;

    public CanonicalCollection() {
        this.states = new ArrayList<>();
    }

    public CanonicalCollection(List<State> states) {
        this.states = states;
    }

    public void addState(State state) {
        this.states.add(states.size(), state);
    }

    public List<State> getStates() {
        return this.states;
    }

    public CanonicalCollection copy() {
        // Shallow copy of the states list
        List<State> copiedStates = new ArrayList<>(this.states);

        // Return a new CanonicalCollection with the copied states
        return new CanonicalCollection(copiedStates); // Pass null for adjacencyList if it's not relevant
    }
}
