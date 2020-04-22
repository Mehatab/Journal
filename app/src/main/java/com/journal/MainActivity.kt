package com.journal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.journal.home.HomeFragment
import com.journal.utils.setDefaultNightMode
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavController.Companion.TAB1

class MainActivity : AppCompatActivity(), FragNavController.RootFragmentListener {
    lateinit var fragNavController: FragNavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setDefaultNightMode()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragNavController = FragNavController(supportFragmentManager, R.id.container)
        fragNavController.rootFragmentListener = this
        fragNavController.initialize(TAB1, savedInstanceState)
    }

    override val numberOfRootFragments: Int get() = 1

    override fun getRootFragment(index: Int): Fragment {
        return HomeFragment.newInstance()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        fragNavController.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        when {
            fragNavController.isRootFragment.not() -> fragNavController.popFragment()
            else -> super.onBackPressed()
        }
    }
}
