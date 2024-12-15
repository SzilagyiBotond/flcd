package org.example.parser.lr0;

import org.example.utils.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Grammar {
    private Set<String> nonTerminals;
    private Set<String> terminals;
    private Map<List<String>, List<List<String>>> productions;
    private String startingSymbol;
    private boolean isCFG;
    private final String EPSILON = "EPSILON";
    private boolean isEnriched;
    public static String enrichedStartingGrammarSymbol = "S0";

    private void processProduction(String production) {
        String[] leftAndRightHandSide = production.split("->");
        List<String> splitLHS = List.of(leftAndRightHandSide[0].split(" "));
        String[] splitRHS = leftAndRightHandSide[1].split("\\|");

        this.productions.putIfAbsent(splitLHS, new ArrayList<>() {
        });
        for (String splitRH : splitRHS) {
            this.productions.get(splitLHS).add(Arrays.stream(splitRH.split(" ")).collect(Collectors.toList()));
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
            this.isEnriched = false;
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

    public List<Pair<String, List<String>>> getOrderedProductions() {

        List<Pair<String, List<String>>> result = new ArrayList<>();

        this.productions.forEach(
                (lhs, rhs) -> rhs.forEach(
                        (prod) -> result.add(new Pair<>(lhs.get(0), prod))
                )
        );

        return result;
    }

    public Grammar(String filePath) {
        this.loadFromFile(filePath);
    }

    public Grammar(Set<String> nonTerminals, Set<String> terminals, Map<List<String>, List<List<String>>> productions, String startingSymbol) {
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.productions = productions;
        this.startingSymbol = startingSymbol;
        this.isCFG = true;
        this.isEnriched = true;
    }

    public Grammar createEnrichedGrammar(){
        Grammar enrichedGrammar = new Grammar(nonTerminals, terminals, productions, enrichedStartingGrammarSymbol);

        enrichedGrammar.nonTerminals.add(enrichedStartingGrammarSymbol);
        enrichedGrammar.productions.putIfAbsent(List.of(enrichedStartingGrammarSymbol), new ArrayList<>());
        enrichedGrammar.productions.get(List.of(enrichedStartingGrammarSymbol)).add(List.of(startingSymbol));

        return enrichedGrammar;
    }

    public List<List<String>> getProductions(String nonTerminal){
        return this.productions.get(List.of(nonTerminal));
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

    public Map<List<String>, List<List<String>>> getProductions() {
        return productions;
    }

    public void setProductions(Map<List<String>, List<List<String>>> productions) {
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
