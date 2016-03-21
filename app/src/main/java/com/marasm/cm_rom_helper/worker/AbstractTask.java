package com.marasm.cm_rom_helper.worker;

import com.marasm.cm_rom_helper.valueobjects.TaskResultsVO;

/**
 * Created by mkorotkovas on 3/9/16.
 */
public abstract class AbstractTask
{
  protected int targetTextCompoinentId;

  public AbstractTask(int inUpdateComponentId)
  {
    targetTextCompoinentId = inUpdateComponentId;
  }


  public int getTargetTextCompoinentId()
  {
    return targetTextCompoinentId;
  }

  public void setTargetTextCompoinentId(int targetTextCompoinentId)
  {
    this.targetTextCompoinentId = targetTextCompoinentId;
  }


  public abstract TaskResultsVO executeTask();
}
