package br.com.anderson.cocuscodechallenge.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.di.Injectable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_user_detail.*

private const val NUM_PAGES = 2

class UserDetailFragment : Fragment(R.layout.fragment_user_detail), Injectable{

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initNavigationBottom()

    }

    fun onNavigationItemSelected(item: MenuItem): Boolean {
        viewpager.setCurrentItem( when(item.itemId){
            R.id.page_2 -> 1
            else -> 0
        },true)
        return true
    }

    fun initViewPager(){
        viewpager.adapter = ScreenSlidePagerAdapter(this)
    }

    fun initNavigationBottom(){
        bottomnavigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected)
    }

    private inner class ScreenSlidePagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            return when (position){
                1 -> ListAuthoredChallengeFragment.newInstance(arguments)
                else -> ListCompletedChallengeFragment.newInstance(arguments)
            }
        }
    }

}