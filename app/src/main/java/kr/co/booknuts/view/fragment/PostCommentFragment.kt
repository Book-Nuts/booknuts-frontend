package kr.co.booknuts.view.fragment

import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.booknuts.data.remote.Comment
import kr.co.booknuts.data.remote.CommentRequestDTO
import kr.co.booknuts.data.remote.DeleteResult
import kr.co.booknuts.databinding.FragmentPostCommentBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import kr.co.booknuts.view.CommonMethod.controlTokenError
import kr.co.booknuts.view.CommonMethod.hideKeyboard
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
    var commentCnt: Int? = 0
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
        mBinding = FragmentPostCommentBinding.inflate(inflater, container, false)
        recyclerview = binding.rvComment
        recyclerview.layoutManager = LinearLayoutManager(requireContext())

        getPostCommentList()

        // 리스트 스크롤 시 키보드 내리기
        binding.rvComment.setOnScrollChangeListener { _, _, i2, _, i4 -> if(i2.minus(i4) >= 10) hideKeyboard(binding.editComment, requireContext()) }

        // 게시글 상세로 돌아가기
        binding.imgClose.setOnClickListener{ closeFragment() }

        // 댓글 작성
        binding.imgBtnCommentSend.setOnClickListener{
            hideKeyboard(binding.editComment, requireContext())
            sendComment()
            fragmentRefresh()
            binding.editComment.text = null
        }

        return binding.root
    }
    
    // 새 댓글 API 전송
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
                        // 토큰 에러 처리
                        if(controlTokenError(response.errorBody(), response.code(), requireContext(), activity, requireActivity())) sendComment()
                    }
                }

                override fun onFailure(call: Call<Comment>, t: Throwable) {
                    Log.d("Approach Fail", "wrong server approach")
                }
            })
        }
    }

    // 해당 게시글의 댓글 리스트 가져오기
    private fun getPostCommentList() {
        RetrofitBuilder.commentApi.getCommentList(accessToken, boardId).enqueue(object:
            Callback<Array<Comment>> {
            override fun onResponse(call: Call<Array<Comment>>, response: Response<Array<Comment>>) {
                if(response.isSuccessful) {
                    Log.d("API Success", "Get Comment List")

                    commentList = response.body()
                    if(commentList?.size != 0) commentCnt = commentList?.size
                    binding.textCommentTitle.text = "댓글 " + commentCnt + "개"

                    // 댓글 리스트 RecyclerView Adapter 연결
                    val adapter = PostCommentListAdapter(commentList)
                    recyclerview.adapter = adapter
                    adapter.setDeleteClickListener(object: PostCommentListAdapter.OnDeleteClickListener{
                        override fun onClick(v: View, position: Int) {
                            
                            // 삭제 확인 Dialog 띄우기
                            val dialogBuilder = AlertDialog.Builder(requireContext())
                            dialogBuilder.setMessage("삭제하시겠습니까?").setCancelable(false)
                                .setPositiveButton("삭제", DialogInterface.OnClickListener { _, _ -> deleteComment(commentList?.get(position)?.commentId); fragmentRefresh()})
                                .setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.cancel() })

                            dialogBuilder.create().show()
                        }
                    })
                } else {
                    // 토큰 에러 처리
                    if(controlTokenError(response.errorBody(), response.code(), requireContext(), activity, requireActivity())) getPostCommentList()
                }
            }
            override fun onFailure(call: Call<Array<Comment>>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                //Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 댓글 삭제
    private fun deleteComment(commentId: Long?) {
        RetrofitBuilder.commentApi.deleteComment(accessToken, commentId).enqueue(object:
            Callback<DeleteResult> {
            override fun onResponse(call: Call<DeleteResult>, response: Response<DeleteResult>) {
                if(response.isSuccessful) {
                    Log.d("COMMENT DELETE SUCCESS", "" + response.body()?.result.toString())
                } else {
                    // 토큰 에러 처리
                    if(controlTokenError(response.errorBody(), response.code(), requireContext(), activity, requireActivity())) deleteComment(commentId)
                }
            }
            override fun onFailure(call: Call<DeleteResult>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                //Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 프래그먼트 종료
    private fun closeFragment() {
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

    // 프래그먼트 새로고침
    private fun fragmentRefresh(){
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
}