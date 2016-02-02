package fraczak.trie;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by wojtek on 2/1/16.
 */
public class TrieTest {

    @Test
    public void testAdd() throws Exception {
        Trie<String> trie = new Trie<String>();
        trie.add("ala");
        trie.add("ola");
        trie.add("aaa");
        Assert.assertNull(trie.find("zula"));
        Assert.assertNotNull(trie.find("ola"));
        Assert.assertEquals(trie.getNth(0),"aaa");
        Assert.assertEquals(trie.getNth(1),"ala");
    }

    @Test
    public void testDel() throws Exception {
        Trie<String> trie = new Trie<String>();
        trie.add("ala");
        trie.add("ola");
        trie.add("aaa");
        Assert.assertNotNull(trie.find("ola"));
        trie.del("ola");
        Assert.assertNull(trie.find("ola"));
    }

    @Test
    public void testFind() throws Exception {

    }

    @Test
    public void testFindByKey() throws Exception {

    }

    @Test
    public void testGetKeys() throws Exception {

    }

    @Test
    public void testGetNth() throws Exception {

    }

    @Test
    public void testGetAt() throws Exception {

    }

    @Test
    public void testSize() throws Exception {

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