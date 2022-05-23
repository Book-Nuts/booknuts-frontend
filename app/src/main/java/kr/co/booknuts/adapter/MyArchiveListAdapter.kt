package kr.co.booknuts.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.co.booknuts.data.MyArchive
import kr.co.booknuts.data.MySeries
import kr.co.booknuts.databinding.MyRecyclerviewArchiveItemBinding
import kr.co.booknuts.databinding.MyRecyclerviewSeriesItemBinding

class MyArchiveListAdapter(private val dataList: ArrayList<MyArchive>?) : RecyclerView.Adapter<MyArchiveListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MyRecyclerviewArchiveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // 아카이브 게시글 개수
    override fun getItemCount(): Int {
        if (dataList != null) {
            Log.d("Adapter", "ArchiveDataListSize " + dataList.size)
            return dataList.size
        }
        Log.d("Adapter", "ArchiveDataListSize zero")
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataList?.get(position)?.let { holder?.bind(it) }
    }

    inner class ViewHolder(val binding: MyRecyclerviewArchiveItemBinding): RecyclerView.ViewHolder(binding.root) {
        private val title: TextView = binding.myTextArchiveTitle
        private val date: TextView = binding.myTextArchiveDate
        fun bind(item: MyArchive) {
            title.text = item.title
            //date.text = item.date
            /*Glide.with(bookImg.context)
                .load(item.bookImgUrl)
                .placeholder(R.drawable.icon_search)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.img_user3)
                .fallback(R.drawable.img_user2)
                .fitCenter()
                .into(bookImg)*/
        }
    }
}
