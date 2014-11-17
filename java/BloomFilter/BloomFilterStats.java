import javafx.scene.effect.Bloom;

import java.security.SecureRandom;
import java.util.*;

/**
 * Created by nichlos on 9/24/14.
 */


public class BloomFilterStats {


    public static void main(String[] args) {
        BloomFilterStats stats = new BloomFilterStats();
        for (int i = 1; i < 10; i ++) {
            stats.test(1000000, 4000000, 10000, i);
        }
    }

    public void test(int m, int n, int testNum, int hashNum) {
        Integer[] subSet = createSubset(m);
        int k;
        BloomFilter bloom;
        List<Integer> sublist = Arrays.asList(subSet);
        if (hashNum == -1) {
            bloom = new BloomFilter(subSet, n);
            k = (int) Math.ceil(((double) n / (double) m) * Math.log(2));

        } else {
            bloom = new BloomFilter(subSet, n, hashNum);
            k = hashNum;
        }
        Integer[] testSet = createSubset(testNum);

        double percentage = 0.0;
        int total = 0;
        int count = 0;

        for (int i = 0; i < testSet.length; i++) {
            if (bloom.contains(testSet[i])) {
                total++;
                if (!sublist.contains(testSet[i])) {
                    count++;
                }
            } else {
                if (sublist.contains(testSet[i])) {
                    System.out.println("something wrong");
                }
            }
        }

        percentage = (double) count/(double) total;
        double theory = Math.pow(1-(Math.pow((1-1/(double) n), k*m)),k);
        System.out.println(m + "," + n + "," + k + "," + count + "," + total + "," + percentage + "," + theory + "," + testNum);
    }

    public Integer[] createSubset(int n) {
        Random r = new SecureRandom();
        Integer[] subset = new Integer[n];
        for (int i = 0; i < n; i++) {
            subset[i] = Math.abs(r.nextInt(n * 5));
        }
        return subset;
    }

}
