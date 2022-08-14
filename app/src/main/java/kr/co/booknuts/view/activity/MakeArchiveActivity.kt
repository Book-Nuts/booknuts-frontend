package kr.co.booknuts.view.activity

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.net.toFile
import kr.co.booknuts.data.remote.ArchiveRequestDTO
import kr.co.booknuts.data.remote.MyArchive
import kr.co.booknuts.data.remote.ResultData
import kr.co.booknuts.databinding.ActivityMakeArchiveBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.URL
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList

class MakeArchiveActivity : AppCompatActivity() {

    val binding by lazy { ActivityMakeArchiveBinding.inflate(layoutInflater) }

    private var savedToken: String? = null
    var boardId: Int? = null
    var coverImage: File? = null

    val PERMISSON_Album = 101
    val REQUEST_STORAGE = 201

    /*private var imageList: ArrayList<String> = arrayListOf("https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072821_960_720.jpg",
        "https://cdn.pixabay.com/photo/2016/09/08/22/43/books-1655783_1280.jpg",
        "https://cdn.pixabay.com/photo/2016/03/26/22/21/books-1281581_960_720.jpg",
        "https://cdn.pixabay.com/photo/2017/02/26/21/39/rose-2101475_960_720.jpg",
        "https://cdn.pixabay.com/photo/2018/10/05/14/39/sunset-3726030_960_720.jpg",
        "https://cdn.pixabay.com/photo/2016/10/18/21/28/seljalandsfoss-1751463_960_720.jpg",
        "https://cdn.pixabay.com/photo/2016/11/08/05/20/sunset-1807524__340.jpg")*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        boardId = intent.getIntExtra("boardId", -1)?.toInt()
        binding.imgClose.setOnClickListener {
            finish()
        }

        // 로컬에 저장된 토큰
        val pref = getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        savedToken = pref?.getString("accessToken", null).toString()


        binding.imgCover.setOnClickListener{
            requirePermissions(arrayOf(WRITE_EXTERNAL_STORAGE), PERMISSON_Album)
        }

        binding.textGoNext.setOnClickListener{
            var title = binding.editTitle.text.toString()
            var content = binding.editContent.text.toString()
            //var random = Random().nextInt(imageList.size)
            //var imgUrl = imageList[random]

            if(!title.isEmpty() && !content.isEmpty()){
                var archiveInfo = ArchiveRequestDTO(title, content)


                RetrofitBuilder.archiveApi.postArchive(savedToken, coverImage, archiveInfo).enqueue(object :
                    Callback<MyArchive> {
                    override fun onResponse(
                        call: Call<MyArchive>,
                        response: Response<MyArchive>
                    ) {
                        Log.d("Post Info Sent", archiveInfo.toString())
                        var responseData = response.body()
                        Log.d("Post Success", responseData?.archiveId.toString())
                        //Toast.makeText(this@MakeArchiveActivity, "통신 성공", Toast.LENGTH_SHORT).show()
                        setPostInArchive(responseData?.archiveId)

                        val intent = Intent(this@MakeArchiveActivity, MainActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onFailure(call: Call<MyArchive>, t: Throwable) {
                        Log.d("Approach Fail", "wrong server approach")
                        //Toast.makeText(this@MakeArchiveActivity, "통신 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    fun setPostInArchive(archiveId: Int?) {
        RetrofitBuilder.myApi.addPostToArchive(savedToken, archiveId, boardId).enqueue(object :
            Callback<ResultData> {
            override fun onResponse(
                call: Call<ResultData>,
                response: Response<ResultData>
            ) {
                //Log.d("Post Info Sent", archiveInfo.toString())
                var responseData = response.body()
                Log.d("Post Success", archiveId.toString())
                //Toast.makeText(this@MakeArchiveActivity, "통신 성공", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ResultData>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                //Toast.makeText(this@MakeArchiveActivity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun requirePermissions(permissions: Array<String>, requestCode: Int){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionGranted(requestCode)
        } else {
            val isAllPermissionsGranted = permissions.all { checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }
            if(isAllPermissionsGranted) permissionGranted(requestCode)
            else ActivityCompat.requestPermissions(this, permissions, requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.all { it == PackageManager.PERMISSION_GRANTED })
            permissionGranted(requestCode)
        else permissionDenied(requestCode)
    }
    private fun permissionGranted(requestCode: Int) {
        when(requestCode) {
            PERMISSON_Album -> openGallery()
        }
    }
    private fun permissionDenied(requestCode: Int) {
        when(requestCode) {
            PERMISSON_Album -> Toast.makeText(this, "앨범에 접근하기 위해서 저장소 권한이 필요합니다", Toast.LENGTH_LONG).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQUEST_STORAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("ACTIVITY RESULT", "&&&&&&&")
        if(resultCode == -1) {
            Log.d("RESULT", "&&&&&&&+++++++++++++++++++++")
            when(requestCode) {
                201 -> {
                    Log.d("IN WHEN", "%%%%%%%%%%%%%%%%%%%%%%")
                    data?.data?.let {
                        uri ->
                        {
                            Log.d("COVER IMAGE", "" + uri)
                            binding.imgCover.setImageURI(uri)
                            coverImage = uri.toFile()
                        }
                    }
                }
                REQUEST_STORAGE -> {
                    Log.d("IN WHEN RS", "%%%%%%%%%%%%%%%%%%%%%%")
                    data?.data?.let {
                            uri ->
                        {
                            Log.d("COVER IMAGE", "" + uri)
                            binding.imgCover.setImageURI(uri)
                            coverImage = uri.toFile()
                        }
                    }
                }
                else -> Log.d("REQUEST CODE", "#########" + requestCode)
            }
            Log.d("REQUEST CODE", "#########+++++++++++++++++" + resultCode)
        } else {
            Log.d("RESULT CODE IS NOT OK", "!!!!")
        }
    }
}