package com.marasm.cm_romhelper.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by mkorotkovas on 4/21/16.
 */
public class Utils
{
  public static String getRealPathFromURI(Context inContext, Uri inContentUri) {
    Cursor cursor = null;
    try
    {
      String[] proj = { MediaStore.Images.Media.DATA };
      cursor = inContext.getContentResolver().query(inContentUri,  proj, null, null, null);
      int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
      cursor.moveToFirst();
      return cursor.getString(column_index);
    }
    finally
    {
      if (cursor != null)
      {
        cursor.close();
      }
    }
  }
}
