package com.kbstar.test3_androidx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.kbstar.test3_androidx.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    //Viewpager를 위한 adapter...
    class MyFragmentAdapter(activity: FragmentActivity): FragmentStateAdapter(activity){
        val fragments: List<Fragment>
        init {
            fragments = listOf(OneFragment(), TwoFragment(), ThreeFragment())
        }
        override fun getItemCount(): Int {
            return fragments.size
        }
        // 화면을 구성하기 위한  fragment 결정위해 호출..
        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //actionbar 의 내용이 개발자  view 인 toolbar 에 적용되라..
        setSupportActionBar(binding.toolbar)

        //fragment....
        /**
        val fragment = OneFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.add(R.id.fragment_content, fragment)
        transaction.commit()**/
        binding.viewpager.adapter = MyFragmentAdapter(this)

        //tablayout ~~~~~
        //viewPager연동
        TabLayoutMediator(binding.tabs, binding.viewpager){
            tab, position ->
            tab.text = "Tab${position}"
        }.attach()

        // toggle ~~~~~
        toggle = ActionBarDrawerToggle(this, binding.drawer,
            R.string.drawer_open, R.string.drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //유저가 손으로 끌어서 열거나 닫거나.. toggle 로 열거나 닫거나..
        //둘이 상호 연동..
        toggle.syncState()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // 메뉴 이벤트 처리하는 함수..
    // toggle이 개발자가 메뉴가 추가한 것은 아니지만..
    // actionbar에 아이콘으로 나오는 것이다. 내부적으로 메뉴 취급이 된다.
    // 유저 클릭시.. 메뉴 이벤트를 탄다.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //toggle이 눌렸다면.. 일반적인 메뉴 이벤트를 타지 않게..
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}