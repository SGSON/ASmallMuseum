package xyz.asmallmuseum.android.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import xyz.asmallmuseum.android.R;

/**
 * TODO: document your custom view class.
 */
public class SnsLoginButtonView extends ConstraintLayout implements View.OnClickListener {
    public enum SnsLoginType {EMAIL, GOOGLE, NAVER, KAKAO}

    private ConstraintLayout layout;
    private ImageView imageView;
    private TextView textView;
    private SnsLoginType snsType;

    private OnClickListener mListener;

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

        layout.setOnClickListener(this);
        imageView.setOnClickListener(this);
        textView.setOnClickListener(this);
    }

    private void setAttributes(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SnsLoginButtonView);
        int snsType = typedArray.getInt(R.styleable.SnsLoginButtonView_sns_type, 0);
        this.snsType = SnsLoginType.values()[snsType];

        LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.login_border);
        GradientDrawable gradientDrawable = (GradientDrawable) drawable.findDrawableByLayerId(R.id.login_border_line);

        switch (snsType) {
            case 0:
                setButtonView(R.string.email_login, R.drawable.image_email,
                        R.color.main_theme, R.color.black, 0);
                break;
            case 1:
                setButtonView(R.string.google_login, R.drawable.google_logo,
                        R.color.google, R.color.google_label, R.font.roboto_medium);
                break;
            case 2:
                setButtonView(R.string.naver_login, R.drawable.naver_logo,
                        R.color.naver, R.color.naver_label, 0);
                break;
            case 3:
                setButtonView(R.string.kakao_login, R.drawable.kakao_logo,
                        R.color.kakao, R.color.kakao_label, 0);
                break;
        }

        typedArray.recycle();
    }

    private void setButtonView(int text, int logo, int color, int textColor, int font) {
        LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.login_border);
        GradientDrawable gradientDrawable = (GradientDrawable) drawable.findDrawableByLayerId(R.id.login_border_line);

        if (font != 0) {
            Typeface typeface = getResources().getFont(font);
            textView.setTypeface(typeface);
        }

        imageView.setImageResource(logo);
        textView.setText(text);
        textView.setTextColor(getContext().getColor(textColor));
        gradientDrawable.setColor(getContext().getColor(color));
        layout.setBackground(drawable);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onClick(snsType);
        }
    }

    public static interface OnClickListener {
        void onClick(SnsLoginType snsType);
    }
}

