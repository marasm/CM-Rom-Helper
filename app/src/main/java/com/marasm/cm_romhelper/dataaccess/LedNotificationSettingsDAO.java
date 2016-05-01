package com.marasm.cm_romhelper.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.marasm.cm_romhelper.exceptions.DataStorageException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mkorotkovas on 4/30/16.
 */
public class LedNotificationSettingsDAO extends SQLiteOpenHelper
{
  private static final int DB_VERSION = 1;
  private static final String DB_NAME = "settings";

  private static final String LED_SETTINGS_TABLE = "LED_SETTINGS";
  private static final String DICTIONARY_TABLE_CREATE =
          "CREATE TABLE " + LED_SETTINGS_TABLE + " (" +
                   "NAME TEXT, " +
                   "VALUE TEXT);";

  private final String TAG = this.getClass().getSimpleName();

  public LedNotificationSettingsDAO(Context context)
  {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db)
  {
    db.execSQL(DICTIONARY_TABLE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
  {
    //nothing for now
  }

  public void storeLedNotificationSettings(Map<String,String> inSettingsMap) throws DataStorageException
  {
    if (inSettingsMap != null)
    {
      SQLiteDatabase db = getWritableDatabase();

      try
      {
        db.beginTransaction();

        //clear all previous settings first
        db.delete(LED_SETTINGS_TABLE, null, null);

        for (Map.Entry<String,String> entry : inSettingsMap.entrySet())
        {
          ContentValues values = new ContentValues();
          values.put("NAME", entry.getKey());
          values.put("VALUE", entry.getValue());

          Log.d(TAG, "saving setting " + entry.getKey() + "=" + entry.getValue());

          db.insert(LED_SETTINGS_TABLE, null, values);
        }

        db.setTransactionSuccessful();
      }
      catch (Exception e)
      {
        throw new DataStorageException("Error while saving LED notification settings", e);
      }
      finally
      {
        db.endTransaction();
      }

    }
  }

  public Map<String, String> getLedNotificationSettings()
  {
    Map<String, String> res = new HashMap<String, String>();
    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.query(LED_SETTINGS_TABLE, new String[]{"name", "value"}, null, null, null, null,null);

    while(cursor!= null && cursor.moveToNext())
    {
      res.put(cursor.getString(0), cursor.getString(1));
    }

    return res;
  }


}
