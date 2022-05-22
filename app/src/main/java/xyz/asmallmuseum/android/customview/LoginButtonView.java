package xyz.asmallmuseum.android.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import xyz.asmallmuseum.android.R;

class LoginButtonView extends View {
   public LoginButtonView(Context context) {
      super(context);
   }

   public LoginButtonView(Context context, @Nullable AttributeSet attrs) {
      super(context, attrs);
//      TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.)
   }

   public LoginButtonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
   }

}
