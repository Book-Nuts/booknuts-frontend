package kr.co.booknuts.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import kr.co.booknuts.data.remote.DeleteResult
import kr.co.booknuts.data.remote.ModifyArchiveRequestDTO
import kr.co.booknuts.data.remote.MyArchive
import kr.co.booknuts.databinding.FragmentArchiveDetailBinding
import kr.co.booknuts.databinding.FragmentArchiveEditBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import kr.co.booknuts.view.activity.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class ArchiveEditFragment : Fragment() {

    private var mBinding: FragmentArchiveEditBinding? = null
    private val binding get() = mBinding!!

    var accessToken: String? = null
    var title: String? = null
    var content: String? = null
    var archiveId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentArchiveEditBinding.inflate(inflater, container, false)
        title = arguments?.getString("title").toString()
        content = arguments?.getString("content").toString()
        archiveId = arguments?.getLong("archiveId")

        binding.editTitle.setText(title)
        binding.editContent.setText(content)

        // 로컬에 저장된 토큰
        val pref = this.activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        accessToken = pref?.getString("accessToken", null).toString()

        binding.textComplete.setOnClickListener { modifyArchive() }

        return binding.root
    }

    private fun modifyArchive() {
        title = binding.editTitle.text.toString()
        content = binding.editContent.text.toString()
        RetrofitBuilder.archiveApi.modifyArchive(accessToken, archiveId, ModifyArchiveRequestDTO(title, content)).enqueue(object:
            Callback<MyArchive> {
            override fun onResponse(call: Call<MyArchive>, response: Response<MyArchive>) {
                if(response.isSuccessful) {
                    Log.d("Archive Delete Success", "${response.body()}")
                    var bundle = Bundle()
                    bundle.putSerializable("archiveDetailData", response.body())
                    var fragment = ArchiveDetailFragment()
                    fragment.arguments = bundle
                    (activity as MainActivity).changeFragment(fragment)
                } else {
                    when(response.code()){
                        400 -> Log.d("Modify Archive Error", "No Permission")
                        404 -> Log.d("Modify Archive Error", "Archive Id is Not Exist")
                    }
                }
            }
            override fun onFailure(call: Call<MyArchive>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                //Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }
}