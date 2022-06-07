package xyz.asmallmuseum.android.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import xyz.asmallmuseum.android.R

/**
 * TODO: document your custom view class.
 */
class BaseIconView : ConstraintLayout {

    private lateinit var iconImage : ImageView
    private lateinit var iconText : TextView
    private lateinit var mListener : OnMainButtonClickListener

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        var layoutInflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = layoutInflator.inflate(R.layout.base_icon_view, this, false)
        addView(view)

        view.setOnClickListener{
            if (mListener != null) {
                mListener.onMainButtonClick(iconText.text.toString())
            }
        }

        iconImage = findViewById(R.id.icon_image)
        iconText = findViewById(R.id.icon_text)

        setAttrs(attrs!!)
    }

    private fun setAttrs(attrs: AttributeSet) {
        var typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseIconView)

        var text = typedArray.getText(R.styleable.BaseIconView_name)
        var image = typedArray.getResourceId(R.styleable.BaseIconView_image_res, R.drawable.image_write)

        iconText.text = text
        iconImage.setImageResource(image)

        typedArray.recycle()
    }

    fun setText(text : String) {
        iconText.text = text
    }

    fun setOnMaiButtonClickListener(mListener : OnMainButtonClickListener) {
        this.mListener = mListener
    }

    interface OnMainButtonClickListener {
        fun onMainButtonClick(item: String)
    }
}