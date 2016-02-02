package fraczak.trie;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Trie<E> {

    public static class Found<E> {
        public int pos;
        public List<E> res;

        public Found(int pos, List<E>  res) {
            this.pos = pos;
            this.res = res;
        }
    }
    public static class Node<E> {
        private int size = 0;
        private String prefix = "";
        private List<E> leaves = new ArrayList<E>();
        private SortedMap<Character,Node<E>> dict = new TreeMap<Character,Node<E>>();
    }

    private final Function<E,String> toStr;
    private Node<E> root = null;

    public Trie() {
         this.toStr = Object::toString;
    }
    public Trie(Function<E,String> toStr) {
        this.toStr = toStr;
    }

    public Trie<E> add(E e){
        if (root == null) {
            root = singleton(e, this.toStr.apply(e));
        } else {
            addToTrie(root, e, this.toStr.apply(e));
        }
        return this;
    }


    public Trie<E> del(E e) {
        root = delInTrie(root, e, toStr.apply(e));
        return this;
    }
//----------------------
// ############### USER INTERFACE #################

    /**
     * Find the index of `e` in trie
     * @param e - element to find
     * @return -1, if element is not in the try, or its position (index, starting from 0)
     */
    public int find(E e) {
        Found<E> searchResult = this.findByKey(this.toStr.apply(e));
        if (searchResult != null) {
            for (int i = 0; i < searchResult.res.size(); i++) {
                if (searchResult.res.get(i) == e)
                    return searchResult.pos + i;
            }
        }
        return -1;
    }

    /**
     * Find all elements with key `key`
     * @param key - string representation of element
     * @return {pos: start_pos, res: List<E><}
     */
    public Found<E> findByKey(String key) {
        return findInTrie(root,key);
    }

    /**
     * get keys
     * @return list of keys in the trie
     */
    public List<String> getKeys() {
        return trieGetKeys(root);
    }

    /**
     * retrive the element at position/index `n`
     * @param n - position/index
     * @return the element at the index `n`
     */
    public E getNth(int n) {
        List<E> aux = getAtInTrie(root, n, 1);
        return aux.get(0);
    }

    /**
     * Get `n` elements starting at index `start`
     * @param start - start index
     * @param n - number of elements to take
     * @return the list of at most `n` elements starting at index `start`
     */
    public List<E> getAt(int start, int n) {
        return getAtInTrie(root, start, n);
    }

    /**
     *
     * @return number of elements in the trie
     */
    public int size() {
        if (root == null) {
            return 0;
        }
        return root.size;
    }


//-----------------------
// ############### Implementation of the user interfaces

    public List<String> trieGetKeys(Node<E> t) {
        List<String> res = new ArrayList<String>();
        if (t.leaves.size() > 0)
            res.add(t.prefix);
        for(char a : t.dict.keySet())
            res.addAll(trieGetKeys(t.dict.get(a)).stream().map(p -> t.prefix + a + p).collect(Collectors.toList()));
        return res;
    }

    public Found<E> findInTrie(Node<E> t, String s) {
        int pos;
        String p = prefix(t.prefix, s);
        if (p.length() < t.prefix.length())
            return null;

        if (p.length() < s.length()) {
            pos = t.leaves.size();
            for (char a : t.dict.keySet()) {
                if (a == s.charAt(p.length())) {
                    Found<E> aux = findInTrie(t.dict.get(a), s.substring(p.length() + 1));
                    if (aux != null) {
                        aux.pos += pos;
                        return aux;
                    }
                    return null;
                }
                pos += t.dict.get(a).size;
            }
            return null;
        }
        return new Found<E>(0,new ArrayList<E>(t.leaves));
    }

    public List<E> getAtInTrie(Node<E> t, int start, int n) {
        List<E> res = new ArrayList<E>();
        if (n == 0 || t.size <= start)
            return res;

        if (start < t.leaves.size()) {
            for (int i = start; i < Math.min(start + n, t.leaves.size()); i++)
                res.add(t.leaves.get(i));
            n -= res.size();
            start = 0;
        } else {
            start -= t.leaves.size();
        }
        for (char a : t.dict.keySet()) {
            if (start < t.dict.get(a).size) {
                List<E> aux = getAtInTrie(t.dict.get(a), start, n);
                n -= aux.size();
                start = 0;
                res.addAll(aux);
            } else {
                start -= t.dict.get(a).size;
            }
        }
        return res;
    }


//-----------------------
// ############### Static helper functions

    private static String prefix(String w1, String w2) {
        int i;
        int len = Math.min(w1.length(),w2.length());
        for(i = 0; i < len; i++) {
            if (w1.charAt(i) != w2.charAt(i))
                break;
        }
        return w1.substring(0,i);
    }

    private static <E> void addToTrie(Node<E> t, E x, String s) {
        String p;
        while (true) {
            t.size++;
            p = prefix(t.prefix,s);
            if (p.length() == s.length()) {
                if (p.length() < t.prefix.length())
                    splitNode(t,p.length());
                t.leaves.add(x);
                return;
            }
            if (p.length() != t.prefix.length()) {
                splitNode(t,p.length());
                t.dict.put(s.charAt(p.length()),
                        singleton(x, s.substring(p.length()+1)));
                return;
            }
            Node<E> q = t.dict.get(s.charAt(p.length()));
            if (q == null) {
                t.dict.put(s.charAt(p.length()),
                        singleton(x, s.substring(p.length()+1)));
                return;
            }
            t = q;
            s = s.substring(p.length()+1);
        }
    }

    private static <E> void splitNode(Node<E> x, int n) {
        Node<E> y = new Node<E>();
        y.size = x.size - 1;
        y.prefix = x.prefix.substring(n+1);
        y.leaves = x.leaves;
        y.dict = x.dict;
        x.dict = new TreeMap<Character,Node<E>>();
        x.dict.put(x.prefix.charAt(n),y);
        x.prefix = x.prefix.substring(0,n);
        x.leaves = new ArrayList<E>();
    }

    private static <E> Node<E> singleton(E x, String s) {
        Node<E> result = new Node<>();
        result.size = 1;
        result.prefix = s;
        result.leaves.add(x);
        return result;
    }

    private static <E> Node<E> mergeUniqueChild(Node<E> n) {
        Node<E> theChild = n.dict.get(n.dict.firstKey());
        n.prefix += n.dict.firstKey() + theChild.prefix;
        n.dict = theChild.dict;
        n.leaves = theChild.leaves;
        return n;
    }

    private static <E> Node<E> delInTrie(Node<E> t, E x, String s) {
        // x has to be in trie !!!!
        if (s.length() == t.prefix.length()) {
            int old_length = t.leaves.size();
            t.leaves.remove(x);
            t.size += t.leaves.size() - old_length;
            if ((t.leaves.size() == 0) && (t.dict.size() == 1))
                mergeUniqueChild(t);
        } else {
            s = s.substring(t.prefix.length());
            char a = s.charAt(0);
            Node<E> theChild = t.dict.get(a);
            int old_size = theChild.size;
            Node<E> t_a = delInTrie(theChild, x, s.substring(1));
            t.size +=  t_a.size - old_size;
            if (t_a.size == 0) {
                t.dict.remove(a);
                if ((t.leaves.size() == 0) && (t.dict.size() == 1))
                    mergeUniqueChild(t);
            }
        }
        return t;
    }
}
