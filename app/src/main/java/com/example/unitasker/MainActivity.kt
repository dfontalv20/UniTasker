package com.example.unitasker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private var frameLayout: FrameLayout? = null
    private var navbar: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        frameLayout = findViewById(R.id.main_layout)
        navbar = findViewById(R.id.navbar)
        navbar?.setOnItemSelectedListener { item ->
            handleNavbarItemSelection(item)
        }
    }

    private fun handleNavbarItemSelection(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.calendar -> changeView(CalendarFragment())
            R.id.management -> changeView(ManagementFragment())
            R.id.new_task -> changeView(NewTaskFragment())
        }
        return true
    }

    private fun changeView(fragment: Fragment) {
        val manager = supportFragmentManager
        val transition = manager.beginTransaction()
        transition.replace(R.id.main_layout, fragment)
        transition.commit()
    }
}