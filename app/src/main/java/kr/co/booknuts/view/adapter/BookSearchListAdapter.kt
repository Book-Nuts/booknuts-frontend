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
import kr.co.booknuts.data.remote.BookItem
import kr.co.booknuts.databinding.BookSearchItemBinding

class BookSearchListAdapter(private val dataList: List<BookItem>?) : RecyclerView.Adapter<BookSearchListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BookSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // 책 개수
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
        holder.binding.linearPostItem.setOnClickListener{
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


inner class ViewHolder(val binding: BookSearchItemBinding): RecyclerView.ViewHolder(binding.root) {
        private val bookImg: ImageView = binding.imgBook
        private val bookTitle: TextView = binding.bookTitle
        private val bookAuthor: TextView = binding.bookAuthor
        fun bind(item: BookItem) {
            bookTitle.text = item.title?.replace("<b>", "")?.replace("</b>", "")
            bookAuthor.text = item.author?.replace("<b>", "")?.replace("</b>", "")
            Glide.with(bookImg.context)
                .load(item.image)
                .placeholder(R.drawable.img_book_cover_default)
                .error(R.drawable.img_book_cover_default)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .fitCenter()
                .into(bookImg)
        }
    }
}
