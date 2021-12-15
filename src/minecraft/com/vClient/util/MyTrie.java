package com.vClient.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MyTrie {
    private TrieNode root;

    private static class TrieNode {
        private TrieNode[] array;
        private boolean isKey;
        TrieNode() {
            array = new TrieNode[27];
            isKey = false;
        }
    }

    public MyTrie(Set<String> cleanedNames) {
        root = new TrieNode();
        for (String s : cleanedNames) {
            add(s);
        }
    }
    private void add(String s) {
        TrieNode p = root;
        for (char c : s.toCharArray()) {
            if (p.array[getIndex(c)] == null) {
                p.array[getIndex(c)] = new TrieNode();
            }
            p = p.array[getIndex(c)];
        }
        p.isKey = true;
    }
    public boolean containsPrefix(String s) {
        TrieNode p = root;
        for (char c : s.toCharArray()) {
            if (p.array[getIndex(c)] == null) {
                return false;
            }
            p = p.array[getIndex(c)];
        }
        return true;
    }
    public List<String> prefixMatches(String s) {
        if (s == "" || !containsPrefix(s))
            return null;
        TrieNode p = root;
        for (char c : s.toCharArray()) {
            p = p.array[getIndex(c)];
        }
        return prefixHelper(s, new ArrayList<>(), p);
    }
    private List<String> prefixHelper(String s, List<String> x, TrieNode n) {
        if (n.isKey) {
            x.add(s);
        }
        for (int i = 0; i < 27; i += 1) {
            if (n.array[i] == null) {
                continue;
            }
            prefixHelper(s + getChar(i), x, n.array[i]);
        }
        return x;
    }
    private int getIndex(char c) {
        if (c == ' ') {
            return 26;
        }
        return c - 'a';
    }
    private char getChar(int i) {
        if (i == 26) {
            return ' ';
        }
        return (char) (i + 97);
    }
    public static void main(String[] args) {
        char a = 32;
        String u = "dwasd" + a + "wdad";
    }
}

