package kr.co.booknuts.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.booknuts.R
import kr.co.booknuts.data.remote.Comment
import kr.co.booknuts.databinding.FragmentPostCommentBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostCommentFragment : Fragment() {
    var accessToken: String? = null
    var boardId: Long? = null

    lateinit var recyclerview: RecyclerView

    private var mBinding: FragmentPostCommentBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        boardId = arguments?.getLong("boardId")
        Log.d("BoarId In Comment Frag", "" + boardId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentPostCommentBinding.inflate(inflater, container, false)
        recyclerview = binding.rvComment
        recyclerview.layoutManager = LinearLayoutManager(requireContext())

        binding.imgClose.setOnClickListener{


        }

        // 로컬에 저장된 토큰
        val pref = this.activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        accessToken = pref?.getString("accessToken", null)

        RetrofitBuilder.commentApi.getCommentList(accessToken, boardId).enqueue(object:
            Callback<Array<Comment>> {
            override fun onResponse(call: Call<Array<Comment>>, response: Response<Array<Comment>>) {
                if(response.isSuccessful) {
                    var commentData = response.body()
                    Log.d("API Success", "Comment Data is " + commentData.toString())
                } else {
                    if(response.errorBody() != null) {
                        when(response.code()) {
                            403 -> {
                                Log.d("Token Error", "Wrong token")
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<Array<Comment>>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                //Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })

        return inflater.inflate(R.layout.fragment_post_comment, container, false)
    }
}