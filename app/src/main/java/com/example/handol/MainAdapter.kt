package com.example.handol

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MainAdapter(fm:FragmentManager):FragmentStatePagerAdapter(fm){

    private val fragmentTitleList = mutableListOf("All","거실","침실","주방", "화장실")
   // var currentFragment : Fragment?=null


    override fun getItem(position: Int): Fragment {

        when(position){
            0->currentFragment = AFragment()
            1->currentFragmentB = BFragment()
            2->currentFragmentC = CFragment()
            3->currentFragmentD = DFragment()
            4->currentFragmentE = EFragment()
            else -> currentFragment = AFragment()
        }
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

//        if (currentFragment !== `object`) {
//            currentFragment = `object` as AFragment
//        }

        if(currentFragment == AFragment()){
            currentFragment = `object` as AFragment
        }
        else if(currentFragmentB == BFragment()){
            currentFragmentB = `object` as BFragment
        }
        else if(currentFragmentC == CFragment()){
            currentFragmentC = `object` as CFragment
        }
        else if(currentFragmentD == DFragment()){
            currentFragmentD = `object` as DFragment
        }
        else if(currentFragmentE == EFragment()){
            currentFragmentE = `object` as EFragment
        }
        else if(currentFragment !== `object`){
            currentFragment = `object` as AFragment
        }


        super.setPrimaryItem(container!!, position, `object`)
    }

}