/**
 * BloomFilter implementation for CS 4540 hw 3
 *
 * Created by nichlos incardone on 9/21/14.
 */

public class BloomFilter {

    public static int numOfHashes;
    public static long[] hashTable;
    public static int tableSize;
    public static BitTable bitTable;

    public static void main(String [] args) {
        Integer[] numArray = new Integer[3];
        numArray[0] = 1;
        numArray[1] = 2;
        numArray[2] = 3;
        BloomFilter test = new BloomFilter(numArray, 20, 3);
        System.out.println("bob");

    }

    public BloomFilter(Integer[] m, int n) {
        this(m, n, (int) Math.ceil(((double) n / (double) m.length) * Math.log(2)));
    }

    public BloomFilter(Integer[] m, int n, int c) {
        this.numOfHashes = c;
        this.tableSize = n;
        this.hashTable = createHashTable();
        this.bitTable = new BitTable(n,c);
        for (int i = 0; i < m.length; i++) {
            this.add((int) m[i]);
        }
    }


    /**
     * Hash function implemented with changes from
     * http://www.javamex.com/tutorials/collections/strong_hash_code_implementation.shtml
     */
    private static long[] createHashTable() {
        long[] byteTable = new long[256 * numOfHashes];
        long h = 0x544B2FBACAAF1684L;
        for (int i = 0; i < byteTable.length; i++) {
            for (int j = 0; j < 31; j++)
                h = (h >>> 7) ^ h; h = (h << 11) ^ h; h = (h >>> 10) ^ h;
            byteTable[i] = h;
        }
        return  byteTable;
    }

    private static int[] hashFunction(int i) {
        String s = Integer.toString(i);
        long random = 0xBB40E64DA205B064L;
        long randomMult = 7664345821815920749L;
        int[] hashBits = new int[numOfHashes];
        for (int n = 0; n < numOfHashes; n++) {
            int startIx = 256 * n;
            for (int len = s.length(), j = 0; j < len; j++) {
                char ch = s.charAt(j);
                random = (random * randomMult) ^ hashTable[startIx + (ch & 0xff)];
                random = (random * randomMult) ^ hashTable[startIx + ((ch >>> 8) & 0xff)];
            }
            hashBits[n] = (int) Math.abs(random % tableSize);
        }
        return hashBits;
    }

    public void add(int i) {
        bitTable.setBits(hashFunction(i));
    }

    public boolean contains(int i) {
        return bitTable.contains(hashFunction(i));
    }

    private class BitTable {
        private boolean[] bitTable;
        private int numOfHashes;

        public BitTable(int n, int numOfHashes) {
            this.bitTable = new boolean[n];
            this.numOfHashes = numOfHashes;
        }

        public void setBits(int[] bits) {
            if (bits.length != this.numOfHashes) {
                throw new IllegalArgumentException("Invalid number of hashes");
            }
            for (int i = 0; i < bits.length; i++) {
                bitTable[bits[i]] = true;
            }
        }

        public boolean contains(int[] bits){
            if (bits.length != this.numOfHashes) {
                throw new IllegalArgumentException("Invalid number of hashes");
            }
            for (int i = 0; i < bits.length; i++) {
                if (!this.bitTable[bits[i]]) return false;
            }
            return true;
        }



    }

}
