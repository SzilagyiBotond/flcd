package org.example.scanner;

import org.example.FA.FA;
import org.example.utils.Pair;
import org.example.utils.Position;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {
    private String programPath;
    private String symbolPath;
    private SymbolTable<String> stringConstants;
    private SymbolTable<Integer> integerConstants;
    private SymbolTable<String> identifiers;
    private PIFList pifList;

    private static final String DELIMITER_PATTERNS = "\"([^\"]*)\"|'([^']*)'|\\+|\\-|\\*|\\/|==|!=|=>|\\|\\||=<|&&|=|<|>|%|!|\\(|\\)|\\[|\\]|,|;|:|\\s+";

    private final ArrayList<String> reservedWords = new ArrayList<>(List.of("declare","integer","char","array","loop","while","if","else","to","string","print","input","ret","void","func","by","break"));
    private final ArrayList<String> operators = new ArrayList<>(List.of("+","-","*","/","==","<",">","=<","=>","!=","!","=","||","&&","%"));
    private final ArrayList<String> separators = new ArrayList<>(List.of("(",")","[","]",",",";",":","\n","\t"));

    public Scanner(String _programPath, String _symbolPath){
        programPath = _programPath;
        symbolPath = _symbolPath;
        stringConstants = new SymbolTable<>(10);
        integerConstants = new SymbolTable<>(10);
        identifiers = new SymbolTable<>(10);
        pifList = new PIFList();
    }

    public void writePIFtoFile(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("PIF.OUT"))){
            writer.write(pifList.toString());
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public void writeIdentifierSTtoFile(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("IdST.OUT"))){
            writer.write(identifiers.toString());
        }catch (IOException e){
            System.out.println(e);
        }
    }
    public void writeIntegersSTtoFile(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("IntST.OUT"))){
            writer.write(integerConstants.toString());
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public void writeStringsSTtoFile(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("StringST.OUT"))){
            writer.write(stringConstants.toString());
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public SymbolTable<String> getStringConstants() {
        return stringConstants;
    }

    public SymbolTable<Integer> getIntegerConstants() {
        return integerConstants;
    }

    public SymbolTable<String> getIdentifiers() {
        return identifiers;
    }

    public PIFList getPifList() {
        return pifList;
    }

    public String readFile() throws FileNotFoundException{
        StringBuilder content = new StringBuilder();
        java.util.Scanner scanner = new java.util.Scanner(new File(programPath));
        while (scanner.hasNextLine()){
            content.append(scanner.nextLine()).append('\n');
        }
        return content.toString();
    }

    public List<Pair<String,Integer>> tokenize(String content){
        int lineNumber = 1;
        Pattern pattern = Pattern.compile(DELIMITER_PATTERNS);
        Matcher matcher = pattern.matcher(content);
        List<Pair<String,Integer>> tokens = new ArrayList<>();
        int lastEnd = 0;

        while (matcher.find()){
            if (lastEnd != matcher.start()) {
                String between = content.substring(lastEnd, matcher.start()).trim();
                if (!between.isEmpty()) {
                    tokens.add(new Pair<>(between.trim(),lineNumber));
                }
            }

            String token = matcher.group();
            if (matcher.group(1) != null) {
                tokens.add(new Pair<>("\"" + matcher.group(1) + "\"",lineNumber));
            } else if (matcher.group(2) != null) {
                tokens.add(new Pair<>("'" + matcher.group(2) + "'",lineNumber));
            } else if (!token.trim().isEmpty()) {
                tokens.add(new Pair<>(token,lineNumber));
            }
            for (int i = lastEnd; i <= matcher.start(); i++) {
                if (content.charAt(i) == '\n') {
                    lineNumber++;
                }
            }
            lastEnd = matcher.end();

        }

        for (int i = lastEnd; i < content.length(); i++) {
            if (content.charAt(i) == '\n') {
                lineNumber++;
            }
        }
        String remaining = content.substring(lastEnd).trim();
        if (!remaining.isEmpty()) {
            tokens.add(new Pair<>(remaining, lineNumber));
        }

        return tokens;
    }

    public void scan() throws LexicalError,FileNotFoundException {
        List<Pair<String,Integer>> tokens = tokenize(readFile());

        if (tokens==null){
            return;
        }

        tokens.forEach(t->{
            String token = t.getFirst();
            if (reservedWords.contains(token)){
                pifList.add(new Pair<>(token,new Position(-1,-1)), Types.RESERVED_WORD);
            }
            else if(operators.contains(token)){
                pifList.add(new Pair<>(token,new Position(-1,-1)),Types.OPERATOR);
            }
            else if(separators.contains(token)){
                pifList.add(new Pair<>(token, new Position(-1,-1)),Types.SEPARATOR);
            } else if (isStringConstant(token)) {
                stringConstants.add(token);
                pifList.add(new Pair<>(Types.STRING_CONSTANT.toString(), stringConstants.findPosition(token)), Types.STRING_CONSTANT);
//            } else if (isIntegerConstant(token)) {
            }else if(new FA("FA_integer_constant.txt").acceptsSequence(token)) {
                integerConstants.add(Integer.valueOf(token));
                pifList.add(new Pair<>(Types.INTEGER_CONSTANT.toString(), integerConstants.findPosition(Integer.valueOf(token))), Types.INTEGER_CONSTANT);
//            } else if (isIdentifier(token)) {
            }else if(new FA("FA_identifier.txt").acceptsSequence(token)) {
                identifiers.add(token);
                pifList.add(new Pair<>(Types.IDENTIFIER.toString(),identifiers.findPosition(token)), Types.IDENTIFIER);
            }else{
                throw new LexicalError("Lexical error at line " + t.getSecond() + " with token " + token);
            }
        });
        System.out.println("Lexically correct");
    }

    private boolean isStringConstant(String token){
        return (token.startsWith("\"") && token.endsWith("\"")) ||
                (token.startsWith("'") && token.endsWith("'"));
    }
    private boolean isIntegerConstant(String token){
        Pattern integerPattern = Pattern.compile("^[+-]?\\d+$");
        return integerPattern.matcher(token).matches();
    }

    private boolean isIdentifier(String token){
        Pattern identifierPattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]*$");
        return identifierPattern.matcher(token).matches();
    }
}
