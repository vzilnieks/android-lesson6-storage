package com.example.vadim.lesson6storage;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
//    writeToFile("это фиаско братан");
//    readFromFile();
//    getPrivateAlbumStorageDir("test");
//    isExternalStorageReadable();
//    differentDirectories();
//    removeFile("config.txt");
//    differentDirectories();
//    writeSharedPreferences();
//    readToSharedPreferences();
  }

  private void testWriteSQLLite(String data) {
    FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);

    SQLiteDatabase db = mDbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
    ContentValues values = new ContentValues();
    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_KEY, "key1");
    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE, data);

// Insert the new row, returning the primary key value of the new row
    long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
  }

  private void writeToFile(String data) {
    try {
      OutputStreamWriter outputStreamWriter =
              new OutputStreamWriter(
                      openFileOutput("config.txt", Context.MODE_PRIVATE)
              );
      outputStreamWriter.write(data);
      outputStreamWriter.close();
    }
    catch (IOException e) {
      Log.e("Exception","File write failed" + e.toString());
    }
  }

  private String testReadSQLite() {

    FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
    SQLiteDatabase db = mDbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
    String[] projection = {
            BaseColumns._ID,
            FeedReaderContract.FeedEntry.COLUMN_NAME_KEY,
            FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE
    };

// Filter results WHERE "title" = 'My Title'
    String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_KEY + " = ?";
    String[] selectionArgs = { "key1" };

// How you want the results sorted in the resulting Cursor
    String sortOrder =
            FeedReaderContract.FeedEntry.COLUMN_NAME_VALUE + " DESC";

    Cursor cursor = db.query(
            FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
    );

    List itemIds = new ArrayList<>();
    while(cursor.moveToNext()) {

      String s = cursor.getString(1);
      String s2 = cursor.getString(2);
      long itemId = cursor.getLong(
              cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
      itemIds.add(itemId);
    }
    cursor.close();
    return itemIds.toString();
  }

  private void differentDirectories() {
    File directory = getFilesDir();
    File directoryCache = getCacheDir();
    directory.list();
  }

  private void writeSharedPreferences() {
    SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt("user_score",10);
    editor.commit();
  }

  private void readToSharedPreferences() {
    SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
    int defaultValue = -1;
    int highScore = sharedPreferences.getInt("user_score",defaultValue);
    int a = 0;
  }

  private String readFromFile() {

    String ret = "";

    try {
      InputStream inputStream = openFileInput("config.txt");

      if ( inputStream != null ) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String receiveString = "";
        StringBuilder stringBuilder = new StringBuilder();

        while ( (receiveString = bufferedReader.readLine()) != null ) {
          stringBuilder.append(receiveString);
        }

        inputStream.close();
        ret = stringBuilder.toString();
      }
    }
    catch (FileNotFoundException e) {
      Log.e("login activity", "File not found: " + e.toString());
    } catch (IOException e) {
      Log.e("login activity", "Can not read file: " + e.toString());
    }

    return ret;
  }

  private boolean removeFile(String fileName) {
    return deleteFile(fileName);
  }


  public boolean isExternalStorageReadable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state) ||
            Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
      return true;
    }
    return false;
  }

  public File getPrivateAlbumStorageDir(String albumName) {
    File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),albumName);
    if (!file.mkdirs()) {
      Log.e("","Directory not created");
    }
    File target = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    return file;
  }

  public void onWriteClick(View view) {
    EditText editText = findViewById(R.id.editText);
    String a = editText.getText().toString();
    testWriteSQLLite(editText.getText().toString());
  }

  public void onReadClick(View view) {
    TextView textView = findViewById(R.id.readView);
    textView.setText(testReadSQLite());
  }
}
