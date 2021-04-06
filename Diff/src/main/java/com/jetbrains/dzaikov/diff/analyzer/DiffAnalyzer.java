package com.jetbrains.dzaikov.diff.analyzer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.jetbrains.dzaikov.diff.analyzer.DiffType.*;

/**
 * Main program logic class
 */
public class DiffAnalyzer {

    // To prevent storing big files in the RAM we are storing
    // only strings equality matrix
    private List<List<Boolean>> stringsEqualityMatrix;

    private int len1;
    private int len2;

    /**
     * Initialize the equality matrix
     *
     * @param filePath1 Path to the first file
     * @param filePath2 Path to the first file
     */
    private void initEqMatrix(String filePath1, String filePath2) throws IOException {
        stringsEqualityMatrix = new ArrayList<>();

        try (BufferedReader reader1 = new BufferedReader(new FileReader(filePath1))) {
            BufferedReader reader2;
            String s1;
            while ((s1 = reader1.readLine()) != null) {
                reader2 = new BufferedReader(new FileReader(filePath2));
                List<Boolean> row = new ArrayList<>();
                stringsEqualityMatrix.add(row);
                String s2;
                while ((s2 = reader2.readLine()) != null) {
                    row.add(s1.equals(s2));
                }
                reader2.close();
            }
        }
    }

    /**
     * LCS algorithm.
     *
     * @param list1 Sequence strings numbers for the first file
     * @param list2 Sequence strings numbers for the second file
     */
    private void findLcs(List<Integer> list1, List<Integer> list2) {
        len1 = stringsEqualityMatrix.size();
        len2 = stringsEqualityMatrix.get(0).size();
        List<List<Integer>> lengthMatrix =
                new ArrayList<>(len1 + 1);
        for (int i = 0; i <= len1; i++) {
            List<Integer> row = new ArrayList<>(len2 + 1);
            lengthMatrix.add(row);
            for (int j = 0; j <= len2; j++) {
                row.add(0);
            }
        }

        for (int i = len1 - 1; i >= 0; i--) {
            for (int j = len2 - 1; j >= 0; j--) {
                if (stringsEqualityMatrix.get(i).get(j)) {
                    lengthMatrix.get(i).set(j, 1 + lengthMatrix.get(i + 1).get(j + 1));
                } else {
                    lengthMatrix.get(i).set(j, Math.max(
                            lengthMatrix.get(i + 1).get(j),
                            lengthMatrix.get(i).get(j + 1)));
                }
            }
        }

        int i = 0;
        int j = 0;

        while (i < len1 && j < len2) {
            if (stringsEqualityMatrix.get(i).get(j)) {
                list1.add(i);
                list2.add(j);
                i++;
                j++;
            } else if (lengthMatrix.get(i + 1).get(j) >=
                    lengthMatrix.get(i).get(j + 1)) {
                i++;
            } else {
                j++;
            }
        }
    }

    /**
     * Get files difference in the convenient for HtmlDiffWriter format.
     *
     * {first file string number} {second file string number} {type of difference}
     * (if == -1, string added)   (if == -1, string removed)
     *
     * @param filePath1 Path to the first file
     * @param filePath2 Path to the first file
     * @return Files difference
     * @throws IOException If something wrong with IO
     */
    public List<DiffStringsNumbersPair> getFilesDiff(String filePath1, String filePath2) throws IOException {
        initEqMatrix(filePath1, filePath2);

        List<Integer> lcsList1 = new ArrayList<>();
        List<Integer> lcsList2 = new ArrayList<>();

        findLcs(lcsList1, lcsList2);


        List<Boolean> lcsMarkers1 = new ArrayList<>();
        for (int i = 0; i < len1; i++) {
            lcsMarkers1.add(false);
        }
        lcsList1.forEach(a -> lcsMarkers1.set(a, true));

        List<Boolean> lcsMarkers2 = new ArrayList<>();
        for (int i = 0; i < len2; i++) {
            lcsMarkers2.add(false);
        }
        lcsList2.forEach(a -> lcsMarkers2.set(a, true));

        int i = 0;
        int j = 0;

        List<DiffStringsNumbersPair> pairList = new ArrayList<>();

        while (i < len1 && j < len2) {
            if (lcsMarkers1.get(i) && !lcsMarkers2.get(j)) {
                while (!lcsMarkers2.get(j) && j < len2) {
                    pairList.add(new DiffStringsNumbersPair(-1, j++, ADDED));
                }
            } else if (!lcsMarkers1.get(i) && lcsMarkers2.get(j)) {
                while (!lcsMarkers1.get(i) && i < len1) {
                    pairList.add(new DiffStringsNumbersPair(i++, -1, REMOVED));
                }
            } else if (lcsMarkers1.get(i) && lcsMarkers2.get(j)) {
                pairList.add(new DiffStringsNumbersPair(i++, j++, NONE));
            } else {
                pairList.add(new DiffStringsNumbersPair(i++, j++, EDITED));
            }
        }

        while (j < len2) {
            pairList.add(new DiffStringsNumbersPair(-1, j++, ADDED));
        }
        while (i < len1) {
            pairList.add(new DiffStringsNumbersPair(i++, -1, REMOVED));
        }

        return pairList;
    }
}
