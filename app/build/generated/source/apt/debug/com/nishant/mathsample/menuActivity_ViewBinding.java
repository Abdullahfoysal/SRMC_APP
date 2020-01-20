// Generated code from Butter Knife. Do not modify!
package com.nishant.mathsample;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.nishant.math.MathView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class menuActivity_ViewBinding implements Unbinder {
  private menuActivity target;

  @UiThread
  public menuActivity_ViewBinding(menuActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public menuActivity_ViewBinding(menuActivity target, View source) {
    this.target = target;

    target.mathView = Utils.findRequiredViewAsType(source, R.id.math_view, "field 'mathView'", MathView.class);
    target.inputView = Utils.findRequiredViewAsType(source, R.id.input_view, "field 'inputView'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    menuActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mathView = null;
    target.inputView = null;
  }
}