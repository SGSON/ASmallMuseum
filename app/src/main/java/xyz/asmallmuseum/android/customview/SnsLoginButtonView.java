package xyz.asmallmuseum.android.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import xyz.asmallmuseum.android.R;

/**
 * TODO: document your custom view class.
 */
public class SnsLoginButtonView extends ConstraintLayout {

    private ConstraintLayout layout;
    private ImageView imageView;
    private TextView textView;

    public SnsLoginButtonView(Context context) {
        super(context);
        init();
    }

    public SnsLoginButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        setAttributes(attrs, 0);
    }

    public SnsLoginButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        setAttributes(attrs, defStyle);
    }

    private void init() {
        String inflateService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(inflateService);
        View view = layoutInflater.inflate(R.layout.sns_login_button_view, this, false);
        addView(view);

        layout = findViewById(R.id.sns_login_layout);
        imageView = findViewById(R.id.sns_image);
        textView = findViewById(R.id.sns_text);
    }

    private void setAttributes(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SnsLoginButtonView);
        int snsType = typedArray.getInt(R.styleable.SnsLoginButtonView_sns_type, 0);

        LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.login_border);
        GradientDrawable gradientDrawable = (GradientDrawable) drawable.findDrawableByLayerId(R.id.login_border_line);

        switch (snsType) {
            case 0:
                imageView.setImageResource(R.drawable.image_email);
                textView.setText(R.string.email_login);
                textView.setTextColor(getContext().getColor(R.color.black));
                gradientDrawable.setColor(getContext().getColor(R.color.white));
                layout.setBackground(drawable);
                break;
            case 1:
                Typeface typeface = getResources().getFont(R.font.roboto_medium);

                imageView.setImageResource(R.drawable.google_logo);
                textView.setText(R.string.google_login);
                textView.setTypeface(typeface);
                textView.setTextColor(getContext().getColor(R.color.google_label));
                gradientDrawable.setColor(getContext().getColor(R.color.google));
                layout.setBackground(drawable);
                break;
            case 2:
                imageView.setImageResource(R.drawable.naver_logo);
                textView.setText(R.string.naver_login);
                textView.setTextColor(getContext().getColor(R.color.naver_label));
                gradientDrawable.setColor(getContext().getColor(R.color.naver));
                layout.setBackground(drawable);
                break;
            case 3:
                imageView.setImageResource(R.drawable.kakao_logo);
                textView.setText(R.string.kakao_login);
                textView.setTextColor(getContext().getColor(R.color.kakao_label));
                gradientDrawable.setColor(getContext().getColor(R.color.kakao));
                layout.setBackground(drawable);
                break;
        }

        typedArray.recycle();
    }

}