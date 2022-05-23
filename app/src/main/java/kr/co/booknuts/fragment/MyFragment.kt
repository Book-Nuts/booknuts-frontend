package kr.co.booknuts.fragment

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.booknuts.R
import kr.co.booknuts.adapter.BoardListAdapter
import kr.co.booknuts.data.Post
import kr.co.booknuts.data.UserInfo
import kr.co.booknuts.databinding.FragmentMyBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFragment : Fragment() {

    var tab: Int = 0
    private var mBinding: FragmentMyBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentMyBinding.inflate(inflater, container, false)

        binding .myImgBg.setColorFilter(Color.parseColor("#bbbbbb"), PorterDuff.Mode.MULTIPLY);

        // 로컬에 저장된 토큰
        val pref = this.activity?.getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
        val savedToken = pref?.getString("Token", null)

        // 서버에서 유저 데이터 받아오기
        RetrofitBuilder.api.getUserInfo(savedToken).enqueue(object:
            Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                var userInfo = response.body()
                Log.d("UserInfo Get Test", "data : " + userInfo?.accessToken)
                Toast.makeText(activity, "통신 성공", Toast.LENGTH_SHORT).show()
                binding.myTextNickname.text = userInfo?.loginId
            }
            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })

        // 서버에서 유저 데이터 받아오기
        RetrofitBuilder.api.getUserInfo(savedToken).enqueue(object:
            Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                var userInfo = response.body()
                Log.d("UserInfo Get Test", "data : " + userInfo?.accessToken)
                Toast.makeText(activity, "통신 성공", Toast.LENGTH_SHORT).show()
                binding.myTextNickname.text = userInfo?.loginId
            }
            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })

        tabListener()

        // Inflate the layout for this fragment
        return binding.root
    }

    fun tabListener() {
        binding.myLinearPost.setOnClickListener{
            tab = 0
            // 탭 밑줄
            binding.myViewPost.visibility = View.VISIBLE
            binding.myViewSeries.visibility = View.GONE
            binding.myViewArchive.visibility = View.GONE

            // 글씨 굵기
            binding.myTextPost.setTypeface(null, Typeface.BOLD)
            binding.myTextSeries.setTypeface(null, Typeface.NORMAL)
            binding.myTextArchive.setTypeface(null, Typeface.NORMAL)
        }

        binding.myLinearSeries.setOnClickListener{
            tab = 0
            // 탭 밑줄
            binding.myViewPost.visibility = View.GONE
            binding.myViewSeries.visibility = View.VISIBLE
            binding.myViewArchive.visibility = View.GONE

            // 글씨 굵기
            binding.myTextPost.setTypeface(null, Typeface.NORMAL)
            binding.myTextSeries.setTypeface(null, Typeface.BOLD)
            binding.myTextArchive.setTypeface(null, Typeface.NORMAL)
        }

        binding.myLinearArchive.setOnClickListener{
            tab = 0
            // 탭 밑줄
            binding.myViewPost.visibility = View.GONE
            binding.myViewSeries.visibility = View.GONE
            binding.myViewArchive.visibility = View.VISIBLE

            // 글씨 굵기
            binding.myTextPost.setTypeface(null, Typeface.NORMAL)
            binding.myTextSeries.setTypeface(null, Typeface.NORMAL)
            binding.myTextArchive.setTypeface(null, Typeface.BOLD)
        }
    }
}