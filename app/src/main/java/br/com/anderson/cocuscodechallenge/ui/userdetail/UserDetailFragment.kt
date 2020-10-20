package br.com.anderson.cocuscodechallenge.ui.userdetail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.di.Injectable
import br.com.anderson.cocuscodechallenge.ui.listauthored.ListAuthoredChallengeFragment
import br.com.anderson.cocuscodechallenge.ui.listcompleted.ListCompletedChallengeFragment
import kotlinx.android.synthetic.main.fragment_user_detail.*

class UserDetailFragment : Fragment(R.layout.fragment_user_detail), Injectable {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initNavigationBottom()
    }

    fun initFragments() = fragments.apply {
        add(listCompletedChallengeFragment)
        add(listAuthoredChallengeFragment)
    }

    val listAuthoredChallengeFragment: ListAuthoredChallengeFragment by lazy {
        ListAuthoredChallengeFragment.newInstance(arguments)
    }

    val listCompletedChallengeFragment: ListCompletedChallengeFragment by lazy {
        ListCompletedChallengeFragment.newInstance(arguments)
    }

    fun onNavigationItemSelected(item: MenuItem): Boolean {
        viewpager.setCurrentItem(
            when (item.itemId) {
                R.id.page_2 -> 1
                else -> 0
            },
            true
        )
        return true
    }

    fun initViewPager() {
        viewpager.adapter = ScreenSlidePagerAdapter(this)
    }

    fun initNavigationBottom() {
        bottomnavigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected)
    }

    fun navigateToChallenge(id: String) {
        navController().navigate(
            UserDetailFragmentDirections.actionUserDetailFragmentToChallengeFragment(
                id
            )
        )
    }

    fun navController() = findNavController()

    var fragments: ArrayList<Fragment> = arrayListOf()

    private inner class ScreenSlidePagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }
}
