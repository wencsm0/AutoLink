package cc.nihilism.app.basic.utils;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class TrieNode<T> {
    Map<String, TrieNode<T>> children;
    boolean isEndOfPath;

    @Getter
    T data;

    public TrieNode(T data) {
        this.children = new HashMap<>();
        this.isEndOfPath = false;

        this.data = data;
    }
}
