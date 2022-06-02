package xyz.asmallmuseum.android.presentation.General

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import xyz.asmallmuseum.android.R

class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }


}