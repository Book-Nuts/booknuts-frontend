package kr.co.booknuts.Adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.co.booknuts.R
import kr.co.booknuts.data.BoardList
import kr.co.booknuts.data.Post
import kr.co.booknuts.databinding.FragmentHomeBinding
import kr.co.booknuts.fragment.HomeFragment
import java.net.URL

class BoardListAdapter(private val context: Context, private val dataList: ArrayList<Post>?) : RecyclerView.Adapter<BoardListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.home_recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    // 게시글 개수
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

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val title: TextView = itemView.findViewById(R.id.board_text_title)
        private val writer: TextView = itemView.findViewById(R.id.board_text_writer)
        private val bookImg: ImageView = itemView.findViewById(R.id.board_img_bookImg)
        private val bookTitle: TextView = itemView.findViewById(R.id.board_text_bookTitle)
        private val content: TextView = itemView.findViewById(R.id.board_text_content)
        fun bind(item: Post) {
            title.text = item.title
            bookTitle.text = item.bookTitle
            writer.text = item.writer
            content.text = item.content
            Glide.with(bookImg.context)
                .load(item.bookImgUrl)
                .placeholder(R.drawable.icon_search)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.img_user3)
                .fallback(R.drawable.img_user2)
                .fitCenter()
                .into(bookImg)
        }
    }
}
