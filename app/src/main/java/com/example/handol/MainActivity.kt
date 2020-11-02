package com.example.handol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_test.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {



    private val adapter by lazy{MainAdapter(supportFragmentManager)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        vpMainAcitivty.adapter = MainActivity@adapter

        tab_layout.setupWithViewPager(vpMainAcitivty)

        switch_outing1.setOnCheckedChangeListener{CompoundButton, onSwitch->
            if(onSwitch){
                toast("switch on")

            }
            else{
                toast("switch off")
            }
        }
    }


    fun changeFragment(f: Fragment, cleanStack: Boolean = false){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.rv_fragment_a, f)
        ft.addToBackStack(null)
        ft.commit()
    }
}