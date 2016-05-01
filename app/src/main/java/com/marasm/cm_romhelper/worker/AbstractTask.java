package com.marasm.cm_romhelper.worker;

import com.marasm.cm_romhelper.valueobjects.TaskResultsVO;

/**
 * Created by mkorotkovas on 3/9/16.
 */
public abstract class AbstractTask
{
  public abstract TaskResultsVO executeTask();
}
