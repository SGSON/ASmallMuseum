package xyz.asmallmuseum.android.presentation.General

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import xyz.asmallmuseum.android.R
import xyz.asmallmuseum.android.customview.ASMActionBar

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var actionBar : ASMActionBar
    private lateinit var mMainMenuFragment : Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        mMainMenuFragment = MainMenuFragment()

        actionBar = findViewById(R.id.asm_action_bar)
        actionBar.setActionBarBackClickListener (object: ASMActionBar.ActionBarBackListener {
            override fun onBackClick() {
                onBackPressed()
            }
        })
        actionBar.setActionBarMenuClickListener (object: ASMActionBar.ActionBarMenuListener {
            override fun onMenuClick() {
                var fragmentManager = supportFragmentManager
                var fragmentTransaction = fragmentManager.beginTransaction()

                fragmentTransaction.replace(R.id.fragment_main_container, mMainMenuFragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        })
    }


}