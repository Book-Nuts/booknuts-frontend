package kr.co.booknuts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_my.*
import kr.co.booknuts.databinding.ActivityMainBinding
import kr.co.booknuts.databinding.ActivitySignUpBinding
import kr.co.booknuts.fragment.*

class MainActivity : AppCompatActivity() {

    private val fragmentHome by lazy {HomeFragment()}
    private val fragmentSearch by lazy { SearchFragment() }
    //private val fragmentWrite by lazy { WriteFragment() }
    private val fragmentDebate by lazy {DebateFragment()}
    private val fragmentMy by lazy { MyFragment() }

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        changeFragment(fragmentDebate)
        initNavigationBar()

        val pref = getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
        val editor = pref.edit()
    }

    private fun initNavigationBar() {
        binding.bnvMain.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.home -> {
                        changeFragment(fragmentHome)
                    }
                    R.id.search -> {
                        changeFragment(fragmentSearch)
                    }
                    R.id.write -> {
                        changeFragment(fragmentHome)
                        var intent = Intent(this@MainActivity, WriteActivity::class.java)
                        startActivity(intent)
                    }
                    R.id.debate -> {
                        changeFragment(fragmentDebate)
                    }
                    R.id.my -> {
                        changeFragment(fragmentMy)
                    }
                }
                true
            }
            selectedItemId = R.id.home
        }
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_container, fragment)
            .commit()
    }

    fun changeFragmentWithData(fragment: Fragment, id: Int) {
        val bundle = Bundle()
        bundle.putInt("id", id);

        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_container, fragment)
            .commit()
    }

    fun changeFragmentWithArrayData(fragment: Fragment, data: Array<String?>) {
        val bundle = Bundle()
        bundle.putStringArray("data", data);

        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_container, fragment)
            .commit()
    }

    /*fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        //fragmentTransaction.replace()
    }*/

}