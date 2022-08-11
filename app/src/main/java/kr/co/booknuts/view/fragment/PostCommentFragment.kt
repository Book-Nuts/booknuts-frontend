package kr.co.booknuts.view.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.booknuts.R
import kr.co.booknuts.data.remote.Comment
import kr.co.booknuts.data.remote.CommentRequestDTO
import kr.co.booknuts.databinding.FragmentPostCommentBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import kr.co.booknuts.view.adapter.PostCommentListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostCommentFragment : Fragment() {

    private var mBinding: FragmentPostCommentBinding? = null
    private val binding get() = mBinding!!

    var accessToken: String? = null
    var boardId: Long? = null
    var commentList: Array<Comment>? = null
    lateinit var recyclerview: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        boardId = arguments?.getLong("boardId")
        Log.d("BoarId In Comment Frag", "" + boardId)

        // 로컬에 저장된 토큰
        val pref = this.activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        accessToken = pref?.getString("accessToken", null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentPostCommentBinding.inflate(inflater, container, false)
        recyclerview = binding.rvComment
        Log.d("PostCommentFragment onCreateView", "" + boardId)
        //recyclerview.layoutManager = LinearLayoutManager(requireContext())

        getPostCommentList()

        binding.imgClose.setOnClickListener{
            closeFragment()
        }

        binding.imgBtnCommentSend.setOnClickListener{
            hideKeyboard()
            sendComment()
            binding.editComment.text = null
            Handler().postDelayed({
                var ft = this.fragmentManager?.beginTransaction()
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ft?.detach(this)?.commitNow()
                    ft?.attach(this)?.commitNow()
                } else {
                    ft?.detach(this)?.attach(this)?.commit()
                }
            }, 500)
        }

        return binding.root
    }

    private fun sendComment() {
        val comment = CommentRequestDTO(binding.editComment.text.toString())
        if(!comment.content!!.isNullOrEmpty()) {
            RetrofitBuilder.commentApi.writeComment(accessToken, boardId, comment).enqueue(object :
                Callback<Comment> {
                override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                    if (response.isSuccessful) {
                        var commentData = response.body()
                        Log.d("API Success", "Written Comment Data is " + commentData.toString())
                    } else {
                        if (response.errorBody() != null) {
                            when (response.code()) {
                                403 -> {
                                    Log.d("Token Error", "Wrong token")
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<Comment>, t: Throwable) {
                    Log.d("Approach Fail", "wrong server approach")
                }
            })
        }
    }

    private fun getPostCommentList() {
        RetrofitBuilder.commentApi.getCommentList(accessToken, boardId).enqueue(object:
            Callback<Array<Comment>> {
            override fun onResponse(call: Call<Array<Comment>>, response: Response<Array<Comment>>) {
                if(response.isSuccessful) {
                    commentList = response.body()
                    Log.d("API Success", "Get Comment List")
                    recyclerview.layoutManager = LinearLayoutManager(requireContext())
                    recyclerview.adapter = PostCommentListAdapter(commentList)
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
    }

    // 프래그먼트 종료
    private fun closeFragment() {
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

    // 키보드 내리기
    private fun hideKeyboard() {
        val editComment = binding.editComment
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editComment.windowToken, 0)
    }
}