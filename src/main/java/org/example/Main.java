package org.example;

import java.io.FileNotFoundException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        SymbolTable<String> identifiers = new SymbolTable<>(10);
//        SymbolTable<Integer> intConstants = new SymbolTable<>(10);
//        SymbolTable<String> stringConstants = new SymbolTable<>(10);
//
//        identifiers.add("peter");
//        System.out.println(identifiers.containsTerm("peter"));
//        System.out.println(identifiers.add("peter"));
//        System.out.println(identifiers.findPosition("peter"));
//
//        identifiers.add("maria");
//        System.out.println(identifiers.containsTerm("maria"));
//        System.out.println(identifiers.add("maria"));
//        System.out.println(identifiers.findPosition("maria"));
//
//        intConstants.add(1);
//        System.out.println(intConstants.containsTerm(1));
//        System.out.println(intConstants.add(1));
//        System.out.println(intConstants.findPosition(1));
//
//        stringConstants.add("peter has an apple");
//        System.out.println(stringConstants.containsTerm("peter has an apple"));
//        System.out.println(stringConstants.add("peter has an apple"));
//        System.out.println(stringConstants.findPosition("peter has an apple"));
//        Scanner scanner = new Scanner("p2.txt","tokens.in.txt");
//        try{
//            String content = scanner.readFile();
//            System.out.println(content);
//            scanner.scan();
//            scanner.writeIdentifierSTtoFile();
//            scanner.writeStringsSTtoFile();
//            scanner.writeIntegersSTtoFile();
//            scanner.writePIFtoFile();
//            System.out.println(scanner.getPifList());
//            System.out.println(scanner.getIdentifiers());
//            System.out.println(scanner.getIntegerConstants());
//            System.out.println(scanner.getStringConstants());
//        }catch (FileNotFoundException f){
//
//        }

        Grammar g1 = new Grammar("G2.txt");
        System.out.println(g1.getNonTerminals());
        System.out.println(g1.getTerminals());
        System.out.println(g1.getStartingSymbol());
        System.out.println(g1.isCFG());
    }
}