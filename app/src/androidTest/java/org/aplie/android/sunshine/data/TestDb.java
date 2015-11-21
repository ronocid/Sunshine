package org.aplie.android.sunshine.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

public class TestDb extends AndroidTestCase {
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    void deleteTheDatabase() {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
    }

    public void setUp() {
        deleteTheDatabase();
    }

      public void testCreateDb() throws Throwable {
          final HashSet<String> tableNameHashSet = new HashSet<String>();
          tableNameHashSet.add(WeatherContract.LocationEntry.TABLE_NAME);
          tableNameHashSet.add(WeatherContract.WeatherEntry.TABLE_NAME);

          mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
          SQLiteDatabase db = new WeatherDbHelper(
                  this.mContext).getWritableDatabase();
          assertEquals(true, db.isOpen());

          Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

          assertTrue("Error: This means that the database has not been created correctly",
                  c.moveToFirst());

          do {
              tableNameHashSet.remove(c.getString(0));
         } while( c.moveToNext() );

          assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                  tableNameHashSet.isEmpty());

          c = db.rawQuery("PRAGMA table_info(" + WeatherContract.LocationEntry.TABLE_NAME + ")",
                  null);

          assertTrue("Error: This means that we were unable to query the database for table information.",
                  c.moveToFirst());

          final HashSet<String> locationColumnHashSet = new HashSet<String>();
          locationColumnHashSet.add(WeatherContract.LocationEntry._ID);
          locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_CITY_NAME);
          locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LAT);
          locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LONG);
          locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING);

          int columnNameIndex = c.getColumnIndex("name");
          do {
              String columnName = c.getString(columnNameIndex);
              locationColumnHashSet.remove(columnName);
          } while(c.moveToNext());

          assertTrue("Error: The database doesn't contain all of the required location entry columns",
                  locationColumnHashSet.isEmpty());
          db.close();
      }

    public void testLocationTable() {
        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();

        ContentValues testValues = TestUtilities.createNorthPoleLocationValues();
        long locationRowId = insertLocation(db,testValues);
        assertTrue("Error: no Insert de values in the table location",locationRowId !=-1);

        Cursor cursor = db.query(WeatherContract.LocationEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue("Error: No Records returned from location query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: Location Query Validation Failed", cursor, testValues);
        assertFalse("Error: More than one record returned from location query", cursor.moveToNext());
        cursor.close();
        db.close();
    }

    public void testWeatherTable() {
        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();

        ContentValues locationValues = TestUtilities.createNorthPoleLocationValues();
        long locationRowId = insertLocation(db,locationValues);
        assertTrue("Error: no Insert de values in the table location",locationRowId !=-1);

        ContentValues weatherValues = TestUtilities.createWeatherValues(locationRowId);

        long weatherRowId = db.insert(WeatherContract.WeatherEntry.TABLE_NAME,null,weatherValues);
        assertTrue("Error: no Insert de values in the table weather",weatherRowId !=-1);

        Cursor cursor = db.query(WeatherContract.WeatherEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue("Error: No Records returned from weather query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: Location Query Validation Failed", cursor, weatherValues);
        assertFalse("Error: More than one record returned from weather query", cursor.moveToNext());

        cursor.close();
        db.close();
    }

    public long insertLocation(SQLiteDatabase db, ContentValues testValues) {
        long locationRowId;
        locationRowId = db.insert(WeatherContract.LocationEntry.TABLE_NAME,null,testValues);
        return locationRowId;
    }
}