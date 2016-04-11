package com.marasm.cm_rom_helper.worker;

import com.marasm.cm_rom_helper.enums.TaskType;

/**
 * Created by mkorotkovas on 3/9/16.
 */
public class TaskFactory
{
  public static AbstractTask getWorkerTask(TaskType inTaskType, int inUpdateComponentId)
  {
    switch (inTaskType)
    {
      case SU_CHECKER:
        return new SuCheckerTask();

      default:
        return null;
    }
  }
}
