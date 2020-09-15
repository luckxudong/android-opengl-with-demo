package ice.util;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ice
 * Date: 11-12-9
 * Time: 下午6:05
 */
public class MathUtil {
    public static final int NOT_FOUND = Integer.MAX_VALUE;


    /**
     * 找连N (You must sort data by yourself before this !!!)
     *
     * @param list
     * @param n
     * @return 如果存在将返回首先找到的一个连N的最小值（不是index）,没找到返回Integer.MIN_VALUE
     */
    public static boolean existIncreasingSequence(List<Integer> list, int n) {
        int successTimes = 0;

        for (int i = 1, size = list.size(); i < size; i++) {
            int preId = list.get(i - 1);
            int nextId = list.get(i);

            if ((nextId - preId) == 1) {
                successTimes++;
                if (successTimes >= n - 1)
                    return true;
            }
            else {
                successTimes = 0;
            }
        }

        return false;
    }

    /**
     * 找连N (You must sort data by yourself before this !!!)
     *
     * @param list
     * @param n
     * @return 如果存在将返回首先找到的一个连N的最小值（不是index）,没找到返回Integer.MAX_VALUE
     */
    public static int findIncreasingSequence(List<Integer> list, int n) {
        int successTimes = 0;

        for (int i = 1, size = list.size(); i < size; i++) {
            int preId = list.get(i - 1);
            int nextId = list.get(i);

            int sub = nextId - preId;
            if (sub == 1) {
                successTimes++;
                if (successTimes >= n - 1)
                    return nextId - n + 1;
            }
            else if (sub == 0) {
                continue;
            }
            else {
                successTimes = 0;

                if (sub < 0)
                    throw new IllegalStateException("please sort members first !");
            }
        }

        return NOT_FOUND;
    }

    /**
     * 找连N (You must sort data by yourself before this !!!)
     *
     * @param list
     * @return 第一个最长的连N的长度
     */
    public static int findLongestIncreasingSequence(List<Integer> list) {
        int successTimes = 0;

        for (int i = 1, size = list.size(); i < size; i++) {
            int preId = list.get(i - 1);
            int nextId = list.get(i);

            int sub = nextId - preId;
            if (sub == 1) {
                successTimes++;

            }
            else if (sub == 0) {
                successTimes = 0;
                continue;
            }
            else {
                successTimes = 0;
            }
        }

        return successTimes;
    }

    /**
     * n选r.
     * <p/>
     * 注意：测试结果：
     * (30,7) num = 2035800 time = 1750
     * (30,8) OutOfMemoryError: Java heap space
     * (20, 10)    num = 184756 time = 320
     * (25, 12)   OutOfMemoryError: Java heap space
     * (25, 7)  num = 480700 time = 442
     *
     * @param n
     * @param r
     * @return
     */
    public static List<int[]> combination(int n, int r) {
        if (n < r) throw new IllegalArgumentException("n should not < r !");
        if (n <= 0 || r < 0) throw new IllegalArgumentException("n <= 0 or r < 0 ");

        List<Integer> fromWhich = new ArrayList<Integer>(n);
        for (int i = 0; i < n; i++) {
            fromWhich.add(i);
        }
        return combination(fromWhich, r);
    }

    /**
     * n选r.
     * 注意：测试结果：
     * (30,8)num = 5852925 time = 6036
     * (25, 12)  num = 5200300 time = 7761
     *
     * @param n
     * @param r
     * @return
     */
    public static List<byte[]> combination(byte n, byte r) {
        if (n < r) throw new IllegalArgumentException("n should not < r !");
        if (n <= 0 || r < 0) throw new IllegalArgumentException("n <= 0 or r < 0 ");

        List<Byte> fromWhich = new ArrayList<Byte>(n);
        for (byte i = 0; i < n; i++) {
            fromWhich.add(i);
        }
        return combination(fromWhich, r);
    }

    public static List<byte[]> combination(List<Byte> fromWhich, Byte r) {

        int size = fromWhich.size();

        if (r == 0) {
            byte[] data = new byte[size];

            for (int i = 0; i < size; i++) {
                data[i] = fromWhich.get(i);
            }

            List<byte[]> combination = new ArrayList<byte[]>();
            combination.add(data);

            return combination;
        }
        else if (r == 1) {
            List<byte[]> combination = new ArrayList<byte[]>();

            for (int i = 0; i < size; i++) {
                combination.add(new byte[]{fromWhich.get(i)});
            }

            return combination;
        }
        else {
            List<byte[]> combination = new ArrayList<byte[]>();

            for (int i = 0; i < size; i++) {
                Byte firstElement = fromWhich.get(i);
                List<Byte> subFromWhich = fromWhich.subList(i + 1, size);

                List<byte[]> subCombination = combination(subFromWhich, (byte) (r - 1));

                for (byte[] subSelection : subCombination) {
                    byte[] data = new byte[subSelection.length + 1];
                    data[0] = firstElement;
                    System.arraycopy(subSelection, 0, data, 1, subSelection.length);
                    combination.add(data);
                }

            }

            return combination;
        }

    }

    private static List<int[]> combination(List<Integer> fromWhich, int r) {

        int size = fromWhich.size();

        if (r == 0) {
            int[] data = new int[size];

            for (int i = 0; i < size; i++) {
                data[i] = fromWhich.get(i);
            }

            List<int[]> combination = new ArrayList<int[]>();
            combination.add(data);

            return combination;
        }
        else if (r == 1) {
            List<int[]> combination = new ArrayList<int[]>();

            for (int i = 0; i < size; i++) {
                combination.add(new int[]{fromWhich.get(i)});
            }

            return combination;
        }
        else {
            List<int[]> combination = new ArrayList<int[]>();

            for (int i = 0; i < size; i++) {
                Integer firstElement = fromWhich.remove(0);
                List<Integer> subFromWhich = new ArrayList<Integer>(fromWhich);

                List<int[]> subCombination = combination(subFromWhich, r - 1);

                for (int[] subSelection : subCombination) {
                    int[] data = new int[subSelection.length + 1];
                    data[0] = firstElement;
                    System.arraycopy(subSelection, 0, data, 1, subSelection.length);
                    combination.add(data);
                }

            }

            return combination;
        }

    }


}
