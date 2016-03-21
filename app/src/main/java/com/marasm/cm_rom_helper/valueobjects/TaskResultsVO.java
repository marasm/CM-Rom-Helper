package com.marasm.cm_rom_helper.valueobjects;

/**
 * Created by mkorotkovas on 3/10/16.
 */
public class TaskResultsVO
{
  private boolean isSuccessful;
  private int resultMessageId;
  private int targetTextComponentId;

  public TaskResultsVO()
  {
    isSuccessful = false;
    resultMessageId = -1;
    targetTextComponentId = -1;
  }

  public TaskResultsVO(boolean inIsSuccessful, int inResultMsgId, int inTargetTextComponentId)
  {
    isSuccessful = inIsSuccessful;
    resultMessageId = inResultMsgId;
    targetTextComponentId = inTargetTextComponentId;
  }



  public boolean getIsSuccessful()
  {
    return isSuccessful;
  }

  public void setIsSuccessful(boolean isSuccessful)
  {
    this.isSuccessful = isSuccessful;
  }

  public int getResultMessageId()
  {
    return resultMessageId;
  }

  public void setResultMessageId(int resultMessageId)
  {
    this.resultMessageId = resultMessageId;
  }

  public int getTargetTextComponentId()
  {
    return targetTextComponentId;
  }

  public void setTargetTextComponentId(int targetTextComponentId)
  {
    this.targetTextComponentId = targetTextComponentId;
  }
}
