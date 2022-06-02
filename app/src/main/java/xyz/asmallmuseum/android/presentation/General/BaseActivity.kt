package xyz.asmallmuseum.android.presentation.General

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import xyz.asmallmuseum.android.R
import xyz.asmallmuseum.android.customview.ASMActionBar

class BaseActivity : AppCompatActivity() {

    private lateinit var actionBar : ASMActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        actionBar = findViewById(R.id.asm_action_bar)
        actionBar.setActionBarBackClickListener (object: ASMActionBar.ActionBarBackListener {
            override fun onBackClick() {
                onBackPressed()
            }
        })
        actionBar.setActionBarMenuClickListener (object: ASMActionBar.ActionBarMenuListener {
            override fun onMenuClick() {

            }
        })
    }


}