package xyz.asmallmuseum.android.presentation.General

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import xyz.asmallmuseum.android.R
import xyz.asmallmuseum.android.customview.BaseIconView
import xyz.asmallmuseum.android.customview.BaseIconView.OnMainButtonClickListener

class MainButtonListFragment : Fragment(), OnMainButtonClickListener {

    private lateinit var item1 : BaseIconView
    private lateinit var item2 : BaseIconView
    private lateinit var item3 : BaseIconView
    private lateinit var item4 : BaseIconView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_main_button_list, container, false)

        item1 = view.findViewById(R.id.id1)
        item2 = view.findViewById(R.id.id2)
        item3 = view.findViewById(R.id.id3)
        item4 = view.findViewById(R.id.id4)

        item1.setOnMaiButtonClickListener(this)
        item2.setOnMaiButtonClickListener(this)
        item3.setOnMaiButtonClickListener(this)
        item4.setOnMaiButtonClickListener(this)

        return view
    }

    companion object {
        fun newInstance() = MainButtonListFragment()
    }

    override fun onMainButtonClick(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}