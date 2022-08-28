package kr.co.booknuts.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.booknuts.data.remote.GetProfileResultDTO
import kr.co.booknuts.databinding.ActivityProfileBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import kr.co.booknuts.view.adapter.ProfilePagerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    val binding by lazy { ActivityProfileBinding.inflate(layoutInflater) }

    private val tabTitleArray = arrayOf("포스트 0", "시리즈 0", "아카이브 0")

    var isMine = false
    var isFollow = false
    var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // viewPager와 tabLayout 연결
        binding.viewPager.adapter = this.let { ProfilePagerAdapter(supportFragmentManager, lifecycle) }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()

        // 전 액티비티에서 전달받은 유저 닉네임 반환
        val nickname = intent.getStringExtra("nickname")
        binding.textToolbarTitle.text = "${nickname}님의 프로필"
        binding.myTextNickname.text = nickname

        // 로컬에 저장된 토큰 반환
        val pref = this.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val token = pref?.getString("accessToken", null).toString()

        // ★★★ 유저 프로필 정보 조회 (닉네임 반영 테스트 필요)
        RetrofitBuilder.api.getProfile(token, nickname.toString()).enqueue(object: Callback<GetProfileResultDTO> {
            override fun onResponse(call: Call<GetProfileResultDTO>, response: Response<GetProfileResultDTO>) {
                Log.d("GET_PROFILE", "response : " + response.body().toString())
                userId = response.body()?.userId!!
                binding.textFollower.text = response.body()?.follower.toString()
                binding.textFollowing.text = response.body()?.following.toString()
                // 나의 프로필인 경우
                if (response.body()?.isMyProfile == true) {
                    isMine = true
                    binding.myTextEditProfile.text = "프로필 수정"
                    return
                }

                // 팔로우한 유저인 경우
                if (response.body()?.isFollow == true) {
                    isFollow = true
                    binding.myTextEditProfile.text = "언팔로우"
                }
            }

            override fun onFailure(call: Call<GetProfileResultDTO>, t: Throwable) {
                Log.d("GET_PROFILE", "유저 프로필 정보 조회 실패")
                Log.d("GET_PROFILE", t.message.toString())
            }
        })
    }
}