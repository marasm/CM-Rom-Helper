package com.marasm.cm_romhelper.exceptions;

/**
 * Created by mkorotkovas on 4/30/16.
 */
public class DataStorageException extends Exception
{
  public DataStorageException()
  {
    super();
  }

  public DataStorageException(String inMsg)
  {
    super(inMsg);
  }

  public DataStorageException(String inMsg, Throwable inThrowable)
  {
    super(inMsg, inThrowable);
  }

}
