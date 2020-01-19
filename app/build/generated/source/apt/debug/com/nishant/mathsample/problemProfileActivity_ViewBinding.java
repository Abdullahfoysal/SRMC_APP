// Generated code from Butter Knife. Do not modify!
package com.nishant.mathsample;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.nishant.math.MathView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class problemProfileActivity_ViewBinding implements Unbinder {
  private problemProfileActivity target;

  @UiThread
  public problemProfileActivity_ViewBinding(problemProfileActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public problemProfileActivity_ViewBinding(problemProfileActivity target, View source) {
    this.target = target;

    target.mathView = Utils.findRequiredViewAsType(source, R.id.math_view, "field 'mathView'", MathView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    problemProfileActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mathView = null;
  }
}
