package com.marasm.cm_rom_helper.worker;

import com.marasm.cm_rom_helper.valueobjects.TaskResultsVO;

/**
 * Created by mkorotkovas on 3/9/16.
 */
public abstract class AbstractTask
{
  public abstract TaskResultsVO executeTask();
}
