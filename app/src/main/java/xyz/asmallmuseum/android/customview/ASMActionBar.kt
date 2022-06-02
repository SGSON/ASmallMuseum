package xyz.asmallmuseum.android.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import xyz.asmallmuseum.android.R

class ASMActionBar : ConstraintLayout {

    private lateinit var back_button: Button
    private lateinit var menu_button: Button

    private lateinit var backListener: ActionBarBackListener
    private lateinit var menuListener: ActionBarMenuListener

    constructor(context: Context) : super(context) {
        initView()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
        setAttributes(attrs!!)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
        setAttributes(attrs!!)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView()
        setAttributes(attrs!!)
    }

    private fun initView() {
        val infalteService: String = Context.LAYOUT_INFLATER_SERVICE
        val layoutInflater = getContext().getSystemService(infalteService) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.asm_action_bar_view, this, false)
        addView(view)

        back_button = view.findViewById(R.id.back_button)
        menu_button = view.findViewById(R.id.top_menu_button)

        back_button.setOnClickListener {
            backListener.onBackClick()
        }
        menu_button.setOnClickListener {
            menuListener.onMenuClick()
        }
    }

    private fun setAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ASMActionBar)
        val type = typedArray.getString(R.styleable.ASMActionBar_type)

        typedArray.recycle()
    }

    fun setActionBarBackClickListener(mListener: ActionBarBackListener) {
        backListener = mListener
    }

    fun setActionBarMenuClickListener(mListener: ActionBarMenuListener) {
        menuListener = mListener
    }

    fun setBackVisibility(visible: Int) {
        back_button.visibility = visible
    }

    interface ActionBarBackListener {
        fun onBackClick()
    }

    interface ActionBarMenuListener {
        fun onMenuClick()
    }
}