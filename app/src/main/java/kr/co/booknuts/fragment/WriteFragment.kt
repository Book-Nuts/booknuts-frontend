package kr.co.booknuts.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_write.*
import kr.co.booknuts.MainActivity
import kr.co.booknuts.R
import kr.co.booknuts.databinding.FragmentWriteBinding

class WriteFragment : Fragment() {

    // val binding by lazy { FragmentWriteBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        /*val view: ViewGroup = inflater.inflate(R.layout.fragment_write, container, false)

        view.findViewById(R.id.search_btn_bookTitle).setOnClickListener{
            val bookTitle = search_edit_bookTitle.text.toString()
            //Log.d("Login", "Title: " + bookTitle)
            //Toast.makeText(activity, "Title: " + bookTitle, Toast.LENGTH_SHORT).show()
        }*/

        return view
    }
}