package fraczak.trie;

import org.junit.Assert;
import org.junit.Test;

public class TrieTest {

    @Test
    public void testAdd() throws Exception {
        Trie<String> trie = new Trie<String>();
        trie.add("ala");
        trie.add("ola");
        trie.add("aaa");
        Assert.assertNull(trie.findByKey("zula"));
        Assert.assertNotNull(trie.findByKey("ola"));
        Assert.assertEquals(trie.getNth(0),"aaa");
        Assert.assertEquals(trie.getNth(1),"ala");
    }

    @Test
    public void testDel() throws Exception {
        Trie<String> trie = new Trie<String>();
        trie.add("ala");
        trie.add("ola");
        trie.add("aaa");
        Assert.assertEquals(3,trie.size());
        trie.del("ola");
        Assert.assertEquals(2,trie.size());
    }

    @Test
    public void testFind() throws Exception {
        Trie<String> trie = new Trie<String>();
        trie.add("a");
        trie.add("b");
        trie.add("c");
        Assert.assertEquals(0,trie.find("a"));
    }

    @Test
    public void testFindByKey() throws Exception {
        Object o1 = new Object(){
            @Override
            public String toString() {
                return "a";
            }
        };
        Object o2 = new Object(){
            @Override
            public String toString() {
                return "b";
            }
        };
        Object o3 = new Object(){
            @Override
            public String toString() {
                return "a";
            }
        };
        Trie<Object> trie = new Trie<Object>();
        trie.add(o1);
        trie.add(o2);
        trie.add(o3);
        Assert.assertEquals(2,trie.findByKey("a").res.size());
    }

    @Test
    public void testGetKeys() throws Exception {
        String[] elems = {"a","b","c"};
        Trie<String> trie = new Trie<String>();
        trie.add(elems[0]);
        trie.add(elems[1]);
        trie.add(elems[2]);
        Assert.assertArrayEquals(elems,trie.getKeys().toArray());
        Assert.assertEquals("b",trie.getKeys().get(1));
    }

    @Test
    public void testGetNth() throws Exception {

    }

    @Test
    public void testGetAt() throws Exception {

    }

    @Test
    public void testSize() throws Exception {
        Assert.assertEquals(0,(new Trie<Object>()).size());
    }

    @Test
    public void testTrieGetKeys() throws Exception {

    }

    @Test
    public void testFindInTrie() throws Exception {

    }

    @Test
    public void testGetAtInTrie() throws Exception {

    }
}