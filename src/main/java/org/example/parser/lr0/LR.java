package org.example.parser.lr0;

import org.example.parser.state.Item;
import org.example.parser.state.State;

import java.util.*;

public class LR {
    private final Grammar grammar;
    private Grammar enrichedGrammar;

    public LR(Grammar grammar) {
        this.grammar = grammar;
        enrichedGrammar = grammar.createEnrichedGrammar();
    }

    public String getNonTerminalBeforeDot(Item item){
        try {
            String term = item.getRhs().get(item.getPositionForDot());
            if(!grammar.getNonTerminals().contains(term)){
                return null;
            }

            return term;
        }
        catch (Exception e){
            return null;
        }
    }

    public State closure(Item item){

        Set<Item> oldClosure;
        Set<Item> currentClosure = Set.of(item);

        do {
            oldClosure = currentClosure;
            Set<Item> newClosure = new LinkedHashSet<>(currentClosure);
            for(Item i: currentClosure){
                String nonTerminal = getNonTerminalBeforeDot(i);
                if(nonTerminal != null){
                    for(List<String> prod:  grammar.getProductions(nonTerminal)){
                        Item currentItem = new Item(nonTerminal, prod, 0);
                        newClosure.add(currentItem);
                    }
                }
            }
            currentClosure = newClosure;
        } while(!oldClosure.equals(currentClosure));

        return new State(currentClosure);
    }

    public State goTo(State state, String elem) {
        Set<Item> result = new LinkedHashSet<>();

        for (Item i : state.getItems()) {
            try {
                String nonTerminal = i.getRhs().get(i.getPositionForDot());
                if (Objects.equals(nonTerminal, elem)) {
                    Item nextItem = new Item(i.getLhs(), i.getRhs(), i.getPositionForDot() + 1);
                    State newState = closure(nextItem);
                    result.addAll(newState.getItems());
                }
            } catch(Exception ignored) {
            }
        }

        return new State(result);
    }

    public CanonicalCollection getCanonicalCollection(){
        CanonicalCollection canonicalCollection = new CanonicalCollection();

        canonicalCollection.addState(
                closure(
                        new Item(
                                enrichedGrammar.getStartingSymbol(),
                                enrichedGrammar.getProductions(enrichedGrammar.getStartingSymbol()).stream().toList().get(0),
                                0
                        )
                )
        );

        boolean modified = false;

        do {
            modified = false;

            List<State> currentStates = new ArrayList<>(canonicalCollection.getStates());

            for (State state : currentStates) {
                for (String symbol : enrichedGrammar.getNonTerminals()) {
                    State newState = goTo(state, symbol);

                    if (!canonicalCollection.getStates().contains(newState) && !newState.getItems().isEmpty()) {
                        canonicalCollection.addState(newState);
                        modified = true;
                    }
                }

                for (String terminal : enrichedGrammar.getTerminals()) {
                    State newState = goTo(state, terminal);

                    if (!canonicalCollection.getStates().contains(newState) && !newState.getItems().isEmpty()) {
                        canonicalCollection.addState(newState);
                        modified = true;
                    }
                }
            }
        } while (modified);

        return  canonicalCollection;
    }
}
