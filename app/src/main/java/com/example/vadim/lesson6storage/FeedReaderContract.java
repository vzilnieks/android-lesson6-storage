package com.example.vadim.lesson6storage;

import android.provider.BaseColumns;

/**
 * Created by vadim on 23.04.2018
 */

public final class FeedReaderContract {
  // To prevent someone from accidentally instantiating the contract class,
  // make the constructor private.
  private FeedReaderContract() {}
  /* Inner class that defines the table contents */
  public static class FeedEntry implements BaseColumns {
    public static final String TABLE_NAME = "entry";
    public static final String COLUMN_NAME_KEY = "key";
    public static final String COLUMN_NAME_VALUE = "value";
  }
}
