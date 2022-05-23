package kr.co.booknuts.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.co.booknuts.R
import kr.co.booknuts.data.MySeries
import kr.co.booknuts.databinding.MyRecyclerviewSeriesItemBinding

class MySeriesListAdapter(private val dataList: ArrayList<MySeries>?) : RecyclerView.Adapter<MySeriesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MyRecyclerviewSeriesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // 시리즈 게시글 개수
    override fun getItemCount(): Int {
        if (dataList != null) {
            Log.d("Adapter", "dataListSize " + dataList.size)
            return dataList.size
        }
        Log.d("Adapter", "dataListSize zero")
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataList?.get(position)?.let { holder?.bind(it) }
    }

    inner class ViewHolder(val binding: MyRecyclerviewSeriesItemBinding): RecyclerView.ViewHolder(binding.root) {
        private val title: TextView = binding.seriesTextTitle
        private val content: TextView = binding.seriesTextContent
        private val bookImg: ImageView = binding.imgSeriesBook
        private val totalPost: TextView = binding.seriesTextTotalPost
        private val totalNuts: TextView = binding.seriesTextTotalNuts
        fun bind(item: MySeries) {
            title.text = item.title
            content.text = item.content
            totalPost.text = "총 " + item.totalPost + "화"
            totalNuts.text = item.totalNuts.toString()
            Glide.with(bookImg.context)
                .load(item.imgUrl)
                .placeholder(R.drawable.icon_search)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.img_user3)
                .fallback(R.drawable.img_user2)
                .fitCenter()
                .into(bookImg)
        }
    }
}
