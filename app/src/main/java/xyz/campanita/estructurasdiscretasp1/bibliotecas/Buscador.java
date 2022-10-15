package xyz.campanita.estructurasdiscretasp1.bibliotecas;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * @author: @emi-cruz
 */
public class Buscador {
    public static final int ALPHABET_SIZE = 256;
    private int[] bmBC;
    private int[] bmGs;
    public int comparisons;

    private void preBmBc(char[] x){
        int m = x.length;
        bmBC = new int[ALPHABET_SIZE];
        Arrays.fill(bmBC, m);
        for (int i = 0; i < m - 1; i++) {
            bmBC[x[i]] = m - i - 1;
        }
    }
    private int[] suffixes(char[] x){
        int m = x.length;
        int[] suff = new int[m];
        int g = m - 1;
        int f = m - 1;
        suff[m - 1] = m;
        for (int i = m - 2; i >= 0; --i) {
            if (i > g && (i + m - 1 - f) < m && suff[i + m - 1 - f] < i - g) {
                suff[i] = suff[i + m - 1 - f];
            } else {
                g = i;
                f = g;
                while (g >= 0 && x[g] == x[g + m - 1 - f]) {
                    --g;
                }
                suff[i] = f - g;
            }
        }
        return suff;
    }
    private void preBmGs(char[] x) {
        int m = x.length;
        bmGs = new int[m];
        int[] suff = suffixes(x);
        Arrays.fill(bmGs, m);
        int j = 0;
        for (int i = m - 1; i >= -1; --i) {
            if (i == -1 || suff[i] == i + 1) {
                for (; j < m - 1 - i; ++j) {
                    if (bmGs[j] == m) {
                        bmGs[j] = m - 1 - i;
                    }
                }
            }
        }
        for (int i = 0; i < m - 1; i++) {
            bmGs[m - 1 - suff[i]] = m - 1 - i;
        }
    }
    public ArrayList<Integer> buscar(String text, String pattern) {
        char[] y = text.toCharArray();
        char[] x = pattern.toCharArray();
        int n = y.length; // string length
        int m = x.length; // pattern length
        ArrayList<Integer> resultado = new ArrayList<>();
        int j = 0;
        int i = 0;
        comparisons = 0;
        /* Precompute */
        preBmBc(x);
        preBmGs(x);
        /* Searching */
        while (j <= n - m) {
            for (i = m - 1; i >= 0 && x[i] == y[i + j]; i--) {
                comparisons++;
            }
            if (i < 0) {
                resultado.add(j);
                j += bmGs[0];
            } else {
                j += Math.max(bmGs[i], bmBC[y[i + j]] - m + 1 + i);
            }
        }
        return resultado;
    }
}
