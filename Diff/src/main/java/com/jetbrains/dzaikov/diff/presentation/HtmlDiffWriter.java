package com.jetbrains.dzaikov.diff.presentation;

import static com.jetbrains.dzaikov.diff.analyzer.DiffType.*;

import com.jetbrains.dzaikov.diff.analyzer.DiffStringsNumbersPair;
import java.io.*;
import java.util.List;

/** Class which helps to represent difference in the html format. */
public class HtmlDiffWriter {
  private final List<DiffStringsNumbersPair> diff;
  private final String filePath1;
  private final String filePath2;

  /**
   * Constructor.
   *
   * @param diff Diff list from DiffAnalyzer
   * @param filePath1 Path to the first file
   * @param filePath2 Path to the first file
   */
  public HtmlDiffWriter(List<DiffStringsNumbersPair> diff, String filePath1, String filePath2) {
    this.diff = diff;
    this.filePath1 = filePath1;
    this.filePath2 = filePath2;
  }

  /**
   * Write the files difference to HTML format.
   *
   * @param outPath Location path for the out file.
   * @throws IOException If something wrong with IO.
   */
  public void writeHtml(String outPath) throws IOException {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(outPath))) {
      BufferedReader br1 = new BufferedReader(new FileReader(filePath1));
      BufferedReader br2 = new BufferedReader(new FileReader(filePath2));
      bw.write(
          "<!DOCTYPE html>\n"
              + "<html>\n"
              + "<head>\n"
              + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n"
              + "<style>");
      bw.flush();
      bw.write(
          "* {\n"
              + "  font-family: Courier;\n"
              + "  color: black;\n"
              + "  line-height: 8px;\n"
              + "}\n");
      bw.flush();
      bw.write(
          ".container {\n"
              + "  display: flex;\n"
              + "  width: 100%;\n"
              + "  flex-direction: row;\n"
              + "}\n");
      bw.flush();
      bw.write(
          ".column {\n"
              + "  width: 100%;\n"
              + "  padding: 10px;\n"
              + "  border: solid 1px black;\n"
              + "  background-color: #ffffff;\n"
              + "}\n");
      bw.flush();
      bw.write(
          ".column1 {\n"
              + "  width: auto;\n"
              + "  padding: 10px;\n"
              + "  border: solid 1px black;\n"
              + "  background-color: #aaaaaa;\n"
              + "}\n");
      bw.write(
          ".green {\n"
              + "  background-color: #5cd65c;\n"
              + "}\n"
              + ".gray {\n"
              + "  background-color: #777777;\n"
              + "}\n"
              + ".blue {\n"
              + "  background-color: #80bfff;\n"
              + "}\n"
              + ".line {\n"
              + "  vertical-align: middle;\n"
              + "  height: 20px;\n"
              + "  width: 100%;\n"
              + "}\n");
      bw.write("</style>\n" + "</head>\n" + "<body>\n" + "\n" + "  <div class=\"container\">\n");
      bw.flush();
      bw.write("    <div class=\"column1\">\n");
      for (DiffStringsNumbersPair d : diff) {
        if (d.getStringId1() == -1) {
          bw.write("      <div class=\"line\"><br></br></div>\n");
        } else {
          bw.write("      <div class=\"line\"><br>" + d.getStringId1() + ".</br></div>\n");
        }
      }
      bw.flush();
      bw.write("  </div>\n" + "    <div class=\"column\">\n");
      for (DiffStringsNumbersPair d : diff) {
        if (d.getStringId1() == -1 && d.getDiffType() == ADDED) {
          bw.write("      <div class=\"green line\"><br></br></div>\n");
        } else if (d.getDiffType() == REMOVED) {
          bw.write("      <div class=\"gray line\"><br>" + br1.readLine() + "</br></div>\n");
        } else {
          bw.write("      <div class=\"line\"><br>" + br1.readLine() + "</br></div>\n");
        }
      }
      bw.flush();

      br1.close();
      br1 = new BufferedReader(new FileReader(filePath1));
      bw.write("  </div>\n" + "    <div class=\"column1\">\n");
      for (DiffStringsNumbersPair d : diff) {
        if (d.getStringId2() == -1) {
          bw.write("      <div class=\"line\"><br></br></div>\n");
        } else {
          bw.write("      <div class=\"line\"><br>" + d.getStringId2() + ".</br></div>\n");
        }
      }
      bw.flush();
      bw.write("  </div>\n" + "    <div class=\"column\">\n");
      for (DiffStringsNumbersPair d : diff) {
        if (d.getStringId2() == -1 && d.getDiffType() == REMOVED) {
          bw.write("      <div class=\"gray line\"><br>" + br1.readLine() + "</br></div>\n");
        } else {
          if (d.getDiffType() == EDITED) {
            br1.readLine();
            bw.write("      <div class=\"blue line\"><br>" + br2.readLine() + "</br></div>\n");
          } else if (d.getDiffType() == ADDED) {
            bw.write("      <div class=\"green line\"><br>" + br2.readLine() + "</br></div>\n");
          } else {
            br1.readLine();
            bw.write("      <div class=\"line\"><br>" + br2.readLine() + "</br></div>\n");
          }
        }
      }

      bw.write("    </div>\n" + "  </div>\n" + "</body>\n" + "</html>");
      bw.flush();
      br1.close();
      br2.close();
    }
  }
}
