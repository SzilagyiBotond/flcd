package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Grammar {
    private Set<String> nonTerminals;
    private Set<String> terminals;
    private Map<List<String>, Set<List<String>>> productions;
    private String startingSymbol;
    private boolean isCFG;
    private final String EPSILON = "EPSILON";

    private void processProduction(String production) {
        String[] leftAndRightHandSide = production.split("->");
        List<String> splitLHS = List.of(leftAndRightHandSide[0].split(" "));
        String[] splitRHS = leftAndRightHandSide[1].split("\\|");

        this.productions.putIfAbsent(splitLHS, new HashSet<>());
        for (String splitRH : splitRHS) {
            this.productions.get(splitLHS).add(Arrays.stream(splitRH.split(" ")).toList());
        }

    }

    private void loadFromFile(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            this.nonTerminals = new HashSet<>(List.of(scanner.nextLine().split(" ")));
            this.terminals = new HashSet<>(List.of(scanner.nextLine().split(" ")));
            this.startingSymbol = scanner.nextLine();

            this.productions = new HashMap<>();
            while (scanner.hasNextLine()) {
                this.processProduction(scanner.nextLine());
            }

            this.isCFG = this.checkIfCFG();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean checkIfCFG() {
        if (!this.nonTerminals.contains(this.startingSymbol)) {
            return false;
        }

        for (List<String> leftHandSide : this.productions.keySet()) {
            if (leftHandSide.size() != 1 || this.terminals.contains(leftHandSide.get(0))) {
                return false;
            }

            for (List<String> possibleRHS : this.productions.get(leftHandSide)) {
                for (String rhs : possibleRHS) {
                    if (!this.nonTerminals.contains(rhs) && !this.terminals.contains(rhs) && !rhs.equals(EPSILON)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public Grammar(String filePath) {
        this.loadFromFile(filePath);
    }

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public void setNonTerminals(Set<String> nonTerminals) {
        this.nonTerminals = nonTerminals;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public void setTerminals(Set<String> terminals) {
        this.terminals = terminals;
    }

    public Map<List<String>, Set<List<String>>> getProductions() {
        return productions;
    }

    public void setProductions(Map<List<String>, Set<List<String>>> productions) {
        this.productions = productions;
    }

    public String getStartingSymbol() {
        return startingSymbol;
    }

    public void setStartingSymbol(String startingSymbol) {
        this.startingSymbol = startingSymbol;
    }

    public boolean isCFG() {
        return isCFG;
    }

    public void setCFG(boolean CFG) {
        isCFG = CFG;
    }
}
