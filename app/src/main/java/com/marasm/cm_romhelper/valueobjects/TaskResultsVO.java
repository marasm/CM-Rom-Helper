package com.marasm.cm_romhelper.valueobjects;

/**
 * Created by mkorotkovas on 3/10/16.
 */
public class TaskResultsVO
{
  private boolean isSuccessful;
  private String errorMsg;

  public TaskResultsVO()
  {
    isSuccessful = false;
  }


  public TaskResultsVO(boolean inIsSuccessful, String inErrorMsg)
  {
    isSuccessful = inIsSuccessful;
    errorMsg = inErrorMsg;
  }



  public boolean getIsSuccessful()
  {
    return isSuccessful;
  }

  public void setIsSuccessful(boolean isSuccessful)
  {
    this.isSuccessful = isSuccessful;
  }


  public String getErrorMsg()
  {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg)
  {
    this.errorMsg = errorMsg;
  }
}
