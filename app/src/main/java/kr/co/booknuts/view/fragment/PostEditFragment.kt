package kr.co.booknuts.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.co.booknuts.R
import kr.co.booknuts.data.remote.PostDetail
import kr.co.booknuts.data.remote.PostEditRequestDTO
import kr.co.booknuts.databinding.FragmentPostEditBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import kr.co.booknuts.view.CommonMethod
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostEditFragment : Fragment() {
    private var mBinding: FragmentPostEditBinding? = null
    private val binding get() = mBinding!!

    //var postData: PostDetail? = null
    var bookGenre: String? = null
    var accessToken: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentPostEditBinding.inflate(inflater, container, false)
        val prevTitle = arguments?.getString("title")
        val prevContent = arguments?.getString("content")
        val boardId = arguments?.getLong("boardId")
        val bookImgUrl = arguments?.getString("bookImgUrl")

        Log.d("Post Info", ""+prevTitle)
        binding.editTitle.setText(prevTitle)
        binding.editContent.setText(prevContent)

        // 책 이미지 출력
        Glide.with(binding.imgBook)
            .load(bookImgUrl)
            .placeholder(R.drawable.img_book_cover_default)
            .error(R.drawable.img_book_cover_default)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .fitCenter()
            .into(binding.imgBook)

        // 로컬에 저장된 토큰
        val pref = requireContext().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        accessToken = pref?.getString("accessToken", null)

        // 수정 완료 버튼
        binding.imgPosting.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val content = binding.editContent.text.toString()

            if (!title?.isEmpty()!!) {
                sendModifiedPost(title, content, boardId)
                closeFragment()
            } else {
                Toast.makeText(requireContext(), "게시글 정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                //Log.d("Post Info", postInfo.toString())
            }
        }

        binding.imgClose.setOnClickListener {
            closeFragment()
        }

        return binding.root
    }

    private fun sendModifiedPost(title: String, content: String, boardId: Long?) {
        var postInfo = PostEditRequestDTO(title, content)

        RetrofitBuilder.boardApi.editPost(accessToken, boardId, postInfo).enqueue(object :
            Callback<PostDetail> {
            override fun onResponse(
                call: Call<PostDetail>,
                response: Response<PostDetail>
            ) {
                if (response.isSuccessful) {
                    Log.d("API Success", "Post Successfully modified" + response.body().toString())
                } else {
                    // 토큰 에러 처리
                    if(CommonMethod.controlTokenError(
                            response.errorBody(),
                            response.code(),
                            requireContext(),
                            activity,
                            requireActivity()
                        )
                    ) sendModifiedPost(title, content, boardId)
                }
            }
            override fun onFailure(call: Call<PostDetail>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
            }
        })
    }

    // 프래그먼트 종료
    private fun closeFragment() {
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }
}