package kr.co.booknuts.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kr.co.booknuts.R
import kr.co.booknuts.data.Chat
import kr.co.booknuts.databinding.ListitemDebateChatBinding

class ChatAdapter: RecyclerView.Adapter<ChatHolder>() {
    var listData = mutableListOf<Chat>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val binding = ListitemDebateChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        val chat = listData.get(position)
        holder.setChat(chat)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}

class ChatHolder(val binding: ListitemDebateChatBinding): RecyclerView.ViewHolder(binding.root) {
    fun setChat(chat: Chat) {
        if (chat.state) { // 찬성인 경우
            binding.imgProfileBackground.setImageResource(R.drawable.img_chat_corner_pros)
            binding.imgPointer.setImageResource(R.drawable.img_chat_pointer_pros)
            binding.textMsg.setBackgroundColor(Color.parseColor("#FFEAEFFC"))
            binding.imgProfile.setImageResource(R.drawable.img_user3)
        }
        binding.textUserID.text = "${chat.username}"
        binding.textMsg.text = "${chat.message}"
    }
}