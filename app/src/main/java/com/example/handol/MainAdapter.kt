package com.example.handol

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MainAdapter(fm:FragmentManager):FragmentStatePagerAdapter(fm){

    private val fragmentTitleList = mutableListOf("All","거실","침실","주방", "화장실")

    override fun getItem(position: Int): Fragment {

        return when(position){
            0 -> AFragment()
            1 -> BFragment()
            2 -> CFragment()
            3 -> DFragment()
            4 -> EFragment()
            else -> AFragment()
        }
    }

    override fun getCount() = 5

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }
}