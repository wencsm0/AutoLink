package cc.nihilism.app.autolink.basic.utils;

public class TrieTree<T> {
    private final TrieNode<T> root;

    public TrieTree() {
        this.root = new TrieNode<>(null);
    }

    // 添加路径到前缀树中
    public void addPath(String path, T data) {
        String[] directories = path.split("/");
        TrieNode<T> node = root;
        for (String dir : directories) {
            node.children.putIfAbsent(dir, new TrieNode<>(data));
            node = node.children.get(dir);
        }
        node.isEndOfPath = true;
    }

    // 判断路径是否为已保存路径的子路径
    public boolean isSubPath(String path) {
        String[] directories = path.split("/");
        TrieNode<T> node = root;
        for (String dir : directories) {
            if (!node.children.containsKey(dir)) {
                return false;
            }
            node = node.children.get(dir);
            if (node.isEndOfPath) {
                System.out.println("isSubPath: " + dir);
                return true;
            }
        }
        return false;
    }

    public TrieNode<T> getNode(String path) {
        String[] directories = path.split("/");
        TrieNode<T> node = root;
        for (String dir : directories) {
            if (!node.children.containsKey(dir)) {
                return null;
            }
            node = node.children.get(dir);
            if (node.isEndOfPath) {
                return node;
            }
        }

        return null;
    }

    public String getPath(String path) {
        StringBuilder builder = new StringBuilder();
        String[] directories = path.split("/");
        TrieNode<T> node = root;
        for (String dir : directories) {
            if (!node.children.containsKey(dir)) {
                return null;
            }
            node = node.children.get(dir);
            builder.append(dir).append("/");
            if (node.isEndOfPath) {
                builder.deleteCharAt(builder.length() - 1);
                return builder.toString();
            }
        }

        return null;
    }
}
