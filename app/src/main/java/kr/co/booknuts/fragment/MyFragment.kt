package kr.co.booknuts.fragment

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.booknuts.R
import kr.co.booknuts.databinding.FragmentMyBinding

class MyFragment : Fragment() {

    var tab: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMyBinding.inflate(inflater, container, false)

        binding.myImgBg.setColorFilter(Color.parseColor("#bbbbbb"), PorterDuff.Mode.MULTIPLY);

        binding.myLinearPost.setOnClickListener{
            tab = 0
            // 탭 밑줄
            binding.myViewPost.visibility = View.VISIBLE
            binding.myViewSeries.visibility = View.GONE
            binding.myViewArchive.visibility = View.GONE

            // 글씨 굵기
            binding.myTextPost.setTypeface(null, Typeface.BOLD)
            binding.myTextSeries.setTypeface(null, Typeface.NORMAL)
            binding.myTextArchive.setTypeface(null, Typeface.NORMAL)
        }

        binding.myLinearSeries.setOnClickListener{
            tab = 0
            // 탭 밑줄
            binding.myViewPost.visibility = View.GONE
            binding.myViewSeries.visibility = View.VISIBLE
            binding.myViewArchive.visibility = View.GONE

            // 글씨 굵기
            binding.myTextPost.setTypeface(null, Typeface.NORMAL)
            binding.myTextSeries.setTypeface(null, Typeface.BOLD)
            binding.myTextArchive.setTypeface(null, Typeface.NORMAL)
        }

        binding.myLinearArchive.setOnClickListener{
            tab = 0
            // 탭 밑줄
            binding.myViewPost.visibility = View.GONE
            binding.myViewSeries.visibility = View.GONE
            binding.myViewArchive.visibility = View.VISIBLE

            // 글씨 굵기
            binding.myTextPost.setTypeface(null, Typeface.NORMAL)
            binding.myTextSeries.setTypeface(null, Typeface.NORMAL)
            binding.myTextArchive.setTypeface(null, Typeface.BOLD)
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}