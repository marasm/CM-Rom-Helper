package com.marasm.cm_romhelper.valueobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mkorotkovas on 4/7/16.
 */
public class WorkerResultsVO
{
  private List<TaskResultsVO> taskResultsList = new ArrayList<TaskResultsVO>();

  public void addTaskResults(TaskResultsVO inTaskRes)
  {
    taskResultsList.add(inTaskRes);
  }

  public boolean allTasksSuccessfull()
  {
    for (TaskResultsVO taskRes : taskResultsList)
    {
      if(!taskRes.getIsSuccessful()) return false;
    }
    return true;
  }
}
