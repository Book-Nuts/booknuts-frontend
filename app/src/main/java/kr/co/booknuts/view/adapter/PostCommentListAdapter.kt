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
import kr.co.booknuts.data.remote.BookItemKakao
import kr.co.booknuts.data.remote.BookItemNaver
import kr.co.booknuts.databinding.ItemBookSearchBinding

class PostCommentListAdapter(private val dataList: List<BookItemKakao>?) : RecyclerView.Adapter<PostCommentListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // 책 개수
    override fun getItemCount(): Int {
        if (dataList != null) {
            //Log.d("Adapter", "dataListSize " + dataList.size)
            return dataList.size
        }
        //Log.d("Adapter", "dataListSize zero")
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


inner class ViewHolder(val binding: ItemBookSearchBinding): RecyclerView.ViewHolder(binding.root) {
        private val bookImg: ImageView = binding.imgBook
        private val bookTitle: TextView = binding.bookTitle
        private val bookAuthor: TextView = binding.bookAuthor

        fun bind(item: BookItemKakao) {
            bookTitle.text = item.title
            val authorSize = item.authors?.size?.minus(1)
            if (authorSize != null && authorSize > 0) bookAuthor.text = item.authors?.get(0) + " 외 " + authorSize + "명"
            else bookAuthor.text = item.authors?.get(0)

            Glide.with(bookImg.context)
                .load(item.thumbnail)
                .placeholder(R.drawable.img_book_cover_default)
                .error(R.drawable.img_book_cover_default)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .fitCenter()
                .into(bookImg)
        }
    }
}
