
package com.jetbrains.dzaikov.diff;

import com.jetbrains.dzaikov.diff.analyzer.DiffAnalyzer;
import com.jetbrains.dzaikov.diff.analyzer.DiffStringsNumbersPair;
import com.jetbrains.dzaikov.diff.presentation.HtmlDiffWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * <p>Main class.</p>
 *
 * <p>To get help about this program, run it without parameters.</p>
 */
public class App {

  /**
   * <p>Main method.</p>
   *
   * @param args Arguments
   */
  public static void main(String[] args) {
    // Print help
    if (args.length == 0) {
      System.out.println("Program to compare files. First file is old version, "
          + "second in new.\n");
      System.out.println(
          "Use guide:\n"
              + "  -p1 to specify path to the first file (default = \"./\").");
      System.out.println(
          "  -p2 to specify path to the second file (default = \"./\").");
      System.out.println(
          "  -o to specify location for the \"diff.html\" file (default = \"./\").");
      System.exit(0);
    }
    // Simple options parser
    int i = 0;
    String path1 = "./";
    String path2 = "./";
    String diffPath = "./";
    while (i < args.length) {
      if (args[i].charAt(0) == '-') {
        if (i + 1 >= args.length) {
          continue;
        }
        if (args[i + 1].charAt(0) == '-') {
          continue;
        }
      }
      switch (args[i]) {
        case "-p1":
          path1 = args[++i];
          break;
        case "-p2":
          path2 = args[++i];
          break;
        case "-o":
          diffPath = args[++i];
          break;
        default:
          break;
      }
      i++;
    }

    // Files comparison.
    List<DiffStringsNumbersPair> diff = null;
    try {
      DiffAnalyzer diffAnalyzer = new DiffAnalyzer();
      diff = diffAnalyzer.getFilesDiff(path1, path2);
    } catch (FileNotFoundException e) {
      System.err.println("Input file not found\n");
      e.printStackTrace();
      System.exit(-1);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }

    // Writing HTML
    try {
      HtmlDiffWriter htmlDiffWriter = new HtmlDiffWriter(diff, path1, path2);
      htmlDiffWriter.writeHtml(diffPath + "diff.html");
    } catch (FileNotFoundException e) {
      System.err.println("Output location not found\n");
      e.printStackTrace();
      System.exit(-1);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }
}
