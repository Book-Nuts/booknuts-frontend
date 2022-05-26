package kr.co.booknuts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kr.co.booknuts.databinding.ActivityChatDetailBinding
import kr.co.booknuts.fragment.DebateDetail1Fragment
import kr.co.booknuts.fragment.DebateDetail2Fragment
import kr.co.booknuts.fragment.MyFragment


class ChatDetailActivity : AppCompatActivity() {

    private val fragmentDetail1 by lazy { DebateDetail1Fragment() }
    private val fragmentDetail2 by lazy { DebateDetail2Fragment() }

    val binding by lazy { ActivityChatDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 토론장 id 넘겨받기
        val roomId = intent.getIntExtra("roomId", -1)
        val topic = intent.getStringExtra("topic")
        val title = intent.getStringExtra("title")
        val author = intent.getStringExtra("author")

        // 토론장 id 넘겨주기
        val bundle = Bundle(4) // 파라미터의 숫자는 전달하려는 값의 갯수
        bundle.putInt("roomId", roomId)
        bundle.putString("topic", topic)
        bundle.putString("title", title)
        bundle.putString("author", author)
        fragmentDetail1.arguments = bundle

        // X 버튼 클릭 시 액티비티 종료
        binding.btnExit.setOnClickListener { finish() }

        changeFragment(1)
    }

    fun changeFragment(index: Int) {
        when (index) {
            1 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragmentDetail1)
                    .commit()
            }
            2 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragmentDetail2)
                    .commit()
            }
            3 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragmentDetail2) // 3으로 수정
                    .commit()
            }
        }
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
}