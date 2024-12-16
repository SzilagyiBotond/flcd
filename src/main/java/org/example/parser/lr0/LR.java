package org.example.parser.lr0;

import org.example.parser.lrTable.LrRow;
import org.example.parser.lrTable.LrTable;
import org.example.parser.state.Action;
import org.example.parser.state.Item;
import org.example.parser.state.State;
import org.example.utils.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class LR {
    private final Grammar grammar;
    private final Grammar enrichedGrammar;
    private List<Pair<String, List<String>>> orderedProductions;
    public LR(Grammar grammar) {
        this.grammar = grammar;
        enrichedGrammar = grammar.createEnrichedGrammar();
        orderedProductions = grammar.getOrderedProductions();
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
                    if(!newState.getItems().isEmpty()){
                        canonicalCollection.addLrTable(state,symbol,newState);
                    }
                }

                for (String terminal : enrichedGrammar.getTerminals()) {
                    State newState = goTo(state, terminal);

                    if (!canonicalCollection.getStates().contains(newState) && !newState.getItems().isEmpty()) {
                        canonicalCollection.addState(newState);
                        modified = true;
                    }
                    if(!newState.getItems().isEmpty()){
                        canonicalCollection.addLrTable(state,terminal,newState);
                    }
                }
            }
        } while (modified);

        return  canonicalCollection;
    }

    public void parse(Stack<String> inputStack, LrTable lrTable, String filePath) throws IOException {
        Stack<Pair<String, State>> workingStack = new Stack<>();
        Stack<String> outputStack = new Stack<>();
        Stack<Integer> outputNumberStack = new Stack<>();

        //ez mi?
        String lastSymbol = "";
        State currentState = lrTable.entries.get(0).state;

        boolean sem = true;

        workingStack.push(new Pair<>(lastSymbol, currentState));
        LrRow lastRow = null;
        String onErrorSymbol = null;

        try {
            do{
                if(!inputStack.isEmpty()) {
                    // We keep the symbol before which an error might occur
                    onErrorSymbol = inputStack.peek();
                }
                // We update the last row from the table we worked with
                State finalCurrentState = currentState;
                lastRow = lrTable.entries.stream()
                        .filter(row -> row.state.equals(finalCurrentState))
                        .findFirst().orElse(null);


                // We take a copy of the entry from the table and work on it
                LrRow entry = lrTable.entries.stream()
                        .filter(row -> row.state.equals(finalCurrentState))
                        .findFirst().orElse(null);

                if(entry.stateAction.equals(Action.SHIFT)) {
                    String symbol = inputStack.pop();
                    Pair<String, State> cell = entry.gotoList.stream()
                            .filter(pair -> pair.getFirst().equals(symbol))
                            .findFirst().orElse(null);

                    if (cell != null) {
                        currentState = cell.getSecond();
                        lastSymbol = symbol;
                        workingStack.push(new Pair<>(lastSymbol, currentState));
                    }
                    else {
                        throw new NullPointerException("Action is SHIFT but there are no matching states");
                    }
                } else if(entry.stateAction.equals(Action.REDUCE)) {

                    List<String> reduceProdRhs = new ArrayList<>(entry.reduceProduction.getSecond());
                    String reduceProdLhs = entry.reduceProduction.getFirst().get(0);
                    while(reduceProdRhs.contains(workingStack.peek().getFirst()) && !workingStack.isEmpty()){
                        reduceProdRhs.remove(workingStack.peek().getFirst());
                        workingStack.pop();
                    }
                    //eddig jo


                    LrRow neededStateRow = lrTable.entries.stream()
                            .filter(lrRow -> lrRow.state.equals(workingStack.peek().getSecond()))
                            .findFirst().orElse(null);

                    Optional<Pair<String, State>> matchingPair = neededStateRow.gotoList.stream()
                            .filter(pair -> pair.getFirst().equals(reduceProdLhs)).findFirst();


                    State lastState = new State(currentState.getItems());
                    currentState = matchingPair.get().getSecond();
                    lastSymbol = entry.reduceProduction.getFirst().get(0);
                    workingStack.push(new Pair<>(lastSymbol, currentState));

                    // We "form" the production used for reduction and look for its production number
                    int productionNumber = this.grammar.getOrderedProductions().stream()
                            .filter(prod -> prod.getFirst().equals(entry.reduceProduction.getFirst().get(0)) && prod.getSecond().equals(entry.reduceProduction.getSecond()))
                            .findFirst()
                            .map(prod -> this.grammar.getOrderedProductions().indexOf(prod)) // Get the index of the found production
                            .orElse(-1);

                    outputNumberStack.push(productionNumber);
                } else {
                    List<String> output = new ArrayList<>(outputStack);
                    Collections.reverse(output);
                    List<Integer> numberOutput = new ArrayList<>(outputNumberStack);
                    Collections.reverse(numberOutput);

                    System.out.println("ACCEPTED");
                    writeToFile(filePath, "ACCEPTED");
                    System.out.println("Production strings: " + output);
                    writeToFile(filePath, "Production strings: " + output);
                    System.out.println("Production number: " + numberOutput);
                    writeToFile(filePath, "Production number: " + numberOutput);

                    sem = false;
                }
            } while(sem);
        } catch (NullPointerException ex){

            System.out.println(lastRow);
            System.out.println(ex);
        }
    }

    public void writeToFile(String file, String line) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(line);
        bw.newLine();
        bw.close();
    }

    public void writeLrTableToFile(CanonicalCollection canonicalCollection, String filePath) {
        LrTable lrTable = getLrTable(canonicalCollection);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(lrTable.toString());
        } catch (IOException e) {
            System.err.println("Error writing LrTable to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public LrTable getLrTable(CanonicalCollection canonicalCollection){
        LrTable lrTable = new LrTable();
        for(State state : canonicalCollection.getStates()){
            LrRow row = new LrRow(state,canonicalCollection);
            lrTable.addRow(row);
        }
        return lrTable;
    }
}
