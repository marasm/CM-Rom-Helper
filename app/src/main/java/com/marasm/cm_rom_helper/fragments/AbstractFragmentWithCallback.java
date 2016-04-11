package com.marasm.cm_rom_helper.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by mkorotkovas on 3/30/16.
 */
public abstract class AbstractFragmentWithCallback<T> extends Fragment
{

  @Override
  public final void onAttach(Context inContext)
  {
    super.onAttach(inContext);
    try
    {
      onCallbackHandlerAssigned((T)inContext);
    }
    catch (ClassCastException e)
    {
      throw new RuntimeException(inContext.getClass() +
              " must implement callback interface for the " + this.getClass());
    }

  }

  public abstract void onCallbackHandlerAssigned(T inCallbackHandler);
}