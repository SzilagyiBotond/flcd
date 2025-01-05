package org.example.parser.tree;

import javax.swing.tree.TreeNode;

public class TreeRow {
    private TreeRow parent;
    private TreeRow lChild;
    private TreeRow rSibling;

    private String info;
    private int level;
    private Integer index;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public TreeRow(String _info){
        info = _info;
    }

    public TreeRow getParent() {
        return parent;
    }

    public void setParent(TreeRow parent) {
        this.parent = parent;
    }

    public TreeRow getlChild() {
        return lChild;
    }

    public void setlChild(TreeRow lChild) {
        this.lChild = lChild;
    }

    public TreeRow getrSibling() {
        return rSibling;
    }

    public void setrSibling(TreeRow rSibling) {
        this.rSibling = rSibling;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        result.append("ParsingTree.ParsingTreeRow: ");
        result.append("index = ").append(index);
        result.append(", info = ").append(info);
        result.append(", leftChild = ").append(lChild != null ? lChild.getIndex() : -1);
        result.append(", rightSIbling = ").append(rSibling != null ? rSibling.getIndex() : -1);
        result.append(", parent = ").append(parent != null ? parent.getIndex() : -1);
        result.append(", level = ").append(level);

        return result.toString();
    }
}
