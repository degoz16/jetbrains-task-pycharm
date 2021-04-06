package com.jetbrains.dzaikov.diff.analyzer;

/**
 * Structure to storing information about files difference.
 */
public class DiffStringsNumbersPair {
  //File #1 string num or -1
  private final int stringId1;
  //File #2 string num or -1
  private final int stringId2;
  //Difference type
  private final DiffType diffType;

  /**
   * Constructor.
   *
   * @param stringId1 The first file string number.
   * @param stringId2 The second file string number.
   * @param diffType  Difference type between strings.
   */
  public DiffStringsNumbersPair(int stringId1, int stringId2, DiffType diffType) {
    this.stringId1 = stringId1;
    this.stringId2 = stringId2;
    this.diffType = diffType;
  }

  /**
   * Getter for the first file string number.
   *
   * @return The first file string number.
   */
  public int getStringId1() {
    return stringId1;
  }

  /**
   * Getter for the second file string number.
   *
   * @return The second file string number.
   */
  public int getStringId2() {
    return stringId2;
  }

  /**
   * Getter for the strings difference type.
   *
   * @return Type
   */
  public DiffType getDiffType() {
    return diffType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DiffStringsNumbersPair that = (DiffStringsNumbersPair) o;
    return stringId1 == that.stringId1
        && stringId2 == that.stringId2
        && diffType == that.diffType;
  }
}
