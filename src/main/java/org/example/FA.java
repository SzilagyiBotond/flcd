package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Scanner;

public class FA {
    private String initialState;
    private List<String> finalState;
    private List<String> alphabet;
    private List<String> states;
    private Map<Pair<String,String>, Set<String>> transitions = new HashMap<>();

    private boolean isDeterministic;

    private void readFA(String filepath){
        try(Scanner sc = new java.util.Scanner(new File(filepath))){
            states = new ArrayList<>(List.of(sc.nextLine().split(" ")));
            initialState = sc.nextLine();
            alphabet = new ArrayList<>(List.of(sc.nextLine().split(" ")));
            finalState = new ArrayList<>(List.of(sc.nextLine().split(" ")));
            while(sc.hasNextLine()){
                String[] elems = sc.nextLine().split(" ");
                if (states.contains(elems[0]) && alphabet.contains(elems[1]) && states.contains(elems[2])){
                    if(!transitions.containsKey(new Pair<>(elems[0], elems[1]))){
                        transitions.put(new Pair<>(elems[0], elems[1]), new HashSet<>(List.of(elems[2])));
                    }else{
                        transitions.get(new Pair<>(elems[0], elems[1])).add(elems[2]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        isDeterministic = checkDeterministic();
    }

    private boolean checkDeterministic() {
        return transitions.values().stream().allMatch(list -> list.size() <=1);
    }

    public FA(String filepath){
        readFA(filepath);
    }

    public boolean acceptsSequence(String sequence){
        if (!isDeterministic){
            return false;
        }

        String currentState = initialState;

        for (int i = 0; i < sequence.length(); i++){
            String symbol = sequence.substring(i, i + 1);
            if (transitions.containsKey(new Pair<>(currentState, symbol))){
                currentState = transitions.get(new Pair<>(currentState, symbol)).iterator().next();
            }else {
                return false;
            }
        }
        return finalState.contains(currentState);
    }
    public String writeTransitions(){
        StringBuilder builder = new StringBuilder();
        builder.append("Transitions: \n");
        transitions.forEach((K, V) -> {
            builder.append("<").append(K.getFirst()).append(",").append(K.getSecond()).append("> -> ").append(V).append("\n");
        });

        return builder.toString();
    }

}
