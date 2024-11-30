package org.example;

import java.util.ArrayList;

public class PIFList {
    private ArrayList<Pair<String,Position>> pif;
    private ArrayList<Types> types;

    public PIFList(){
        pif = new ArrayList<>();
        types = new ArrayList<>();
    }

    public void add(Pair<String,Position> symbol, Types type){
        pif.add(symbol);
        types.add(type);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < pif.size(); i++) {
            Pair<String, Position> pair = pif.get(i);
            Types type = types.get(i);

            sb.append("Token: ").append(pair.getFirst())
                    .append(", Position: ").append(pair.getSecond().getFirst().toString()).append(" ").append(pair.getSecond().getSecond().toString())
                    .append(", Type: ").append(type)
                    .append("\n");
        }

        return sb.toString();
    }
}
