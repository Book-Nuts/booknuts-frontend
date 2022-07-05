package kr.co.booknuts.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.co.booknuts.R
import kr.co.booknuts.data.remote.MyArchive
import kr.co.booknuts.databinding.MyRecyclerviewArchiveItemBinding

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
        holder.binding.linearArchive.setOnClickListener{
            itemClickListener.onClick(it, position)
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener: OnItemClickListener

    inner class ViewHolder(val binding: MyRecyclerviewArchiveItemBinding): RecyclerView.ViewHolder(binding.root) {
        private val title: TextView = binding.myTextArchiveTitle
        private val date: TextView = binding.myTextArchiveDate
        private val cnt: TextView = binding.myTextArchiveCnt
        private val img: ImageView = binding.imgArchive
        fun bind(item: MyArchive) {
            title.text = item.title
            date.text = item.createdAt
            cnt.text = item.archiveCnt.toString() + "개"
            Glide.with(img.context)
                .load(item.imgUrl)
                .placeholder(R.drawable.img_book_cover_default)
                .error(R.drawable.img_book_cover_default)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .fitCenter()
                .into(img)
        }
    }
}
