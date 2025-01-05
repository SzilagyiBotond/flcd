package org.example.parser.tree;

import org.example.parser.lr0.Grammar;
import org.example.utils.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tree {
    public TreeRow getRoot() {
        return root;
    }

    private TreeRow root;
    private Grammar grammar;
    private Integer currentIndex = 1;
    private Integer index = 1;
    private int nextLevel;
    private List<TreeRow> tree;

    public Tree(final Grammar grammar) {
        this.grammar = grammar;
    }

    public TreeRow generateTree(List<Integer> input){
        int prodIndex = input.get(0);
        Pair<String, List<String>> productionString = this.grammar.getOrderedProductions().get(prodIndex);

        root = new TreeRow(productionString.getFirst());

        root.setLevel(0);
        root.setIndex(0);

        root.setlChild(buildFromParent(0,root,productionString.getSecond(),input));
        return root;
    }

    public TreeRow buildFromParent(int level, TreeRow parent, List<String> rhs, List<Integer> input){
        if(rhs.isEmpty() || this.index >= input.size() + 1){
            return null;
        }

        String symbol = rhs.get(0);

        if(this.grammar.getTerminals().contains(symbol)){
            TreeRow node = new TreeRow(symbol);
            node.setIndex(this.currentIndex);
            this.currentIndex++;

            node.setLevel(level);
            node.setParent(parent);

            List<String> newList = new ArrayList<>(rhs);
            newList.remove(0);

            node.setrSibling(buildFromParent(level, parent, newList, input));

            return node;
        }else if(this.grammar.getNonTerminals().contains(symbol)){

            int productionIndex = input.get(this.index);

            Pair<String, List<String>> productionString = this.grammar.getOrderedProductions().get(productionIndex);

            TreeRow node = new TreeRow(symbol);

            node.setIndex(this.currentIndex);
            node.setLevel(level);
            node.setParent(parent);

            int newLevel = level + 1;
            if(newLevel > this.nextLevel)
                this.nextLevel = newLevel;

            this.currentIndex++;
            this.index++;

            node.setlChild(buildFromParent(newLevel, node, productionString.getSecond(), input));

            List<String> newList = new ArrayList<>(rhs);
            newList.remove(0);

            node.setrSibling(buildFromParent(level, parent, newList, input));

            return node;
        }
        else {
            System.out.println("ERROR");
            return null;
        }
    }

    public void printTree(TreeRow node, String filePath) throws IOException {
        this.tree = new ArrayList<>();
        createList(node);

        for(int i = 0; i <= this.nextLevel; i++){
            for(TreeRow n: this.tree){
                if(n.getLevel() == i){
                    System.out.println(n);
                    writeToFile(filePath, n.toString());
                }
            }
        }
    }

    public void writeToFile(String file, String line) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(line);
        bw.newLine();
        bw.close();
    }

    public void createList(TreeRow node){
        if(node == null)
            return;

        while(node != null){

            this.tree.add(node);
            if(node.getlChild() != null){
                createList(node.getlChild());
            }

            node = node.getrSibling();
        }

    }
}
