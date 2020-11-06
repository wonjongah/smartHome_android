package com.example.handol

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MainAdapter(fm:FragmentManager):FragmentStatePagerAdapter(fm){

    private val fragmentTitleList = mutableListOf("All","거실","침실","주방", "화장실")
   // var currentFragment : Fragment?=null


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

    var currentFragment : AFragment?=null
    var currentFragmentB : BFragment?=null
    var currentFragmentC : CFragment?=null
    var currentFragmentD : DFragment?=null
    var currentFragmentE : EFragment?=null



    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {

        currentFragment = null
        currentFragmentB = null
        currentFragmentC = null
        currentFragmentD = null
        currentFragmentE = null
        //if(currentFragment !== `object`){
        //    currentFragment = `object` as AFragment
        //}
        if(position == 0){
            currentFragment = `object` as AFragment
        }
        else if(position == 1){
            currentFragmentB = `object` as BFragment
        }
        else if(position == 2){
            currentFragmentC = `object` as CFragment
        }
        else if(position == 3){
            currentFragmentD = `object` as DFragment
        }
        else if(position == 4){
            currentFragmentE = `object` as EFragment
        }



        super.setPrimaryItem(container!!, position, `object`)
    }

}