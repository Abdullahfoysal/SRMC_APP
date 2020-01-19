// Generated code from Butter Knife. Do not modify!
package com.nishant.mathsample;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.nishant.math.MathView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(MainActivity target, View source) {
    this.target = target;

    target.linearLayout = Utils.findRequiredViewAsType(source, R.id.activity_main, "field 'linearLayout'", LinearLayout.class);
    target.mathView = Utils.findRequiredViewAsType(source, R.id.math_view, "field 'mathView'", MathView.class);
    target.inputView = Utils.findRequiredViewAsType(source, R.id.input_view, "field 'inputView'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.linearLayout = null;
    target.mathView = null;
    target.inputView = null;
  }
}
