package com.example.lottery

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lottery.databinding.FragmentHomeBinding
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val bannerImages = listOf(
        R.drawable.bannerimage,
        R.drawable.bannerimage,
        R.drawable.bannerimage
    )

    private lateinit var timer: Timer
    private var currentPage = 0
    private val DELAY_MS: Long = 2000
    private val PERIOD_MS: Long = 2000
    val adapter = TicketCardSlideAdapter()
    val firstwinnerTicketNoAdapter = FirstwinnerTicketNoAdapter()
    val secondWinnerAdapter = SecondWinnerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val homeBannerAdapter = HomeBannerAdapter(bannerImages)
        binding.viewPager.adapter = homeBannerAdapter

        startAutoSlide()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvFirstWinnerTicket.adapter = firstwinnerTicketNoAdapter
        binding.rvFirstWinnerTicket.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvSecondWinnerTicket.adapter = secondWinnerAdapter
        binding.rvSecondWinnerTicket.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        startAutoSlider()
    }
    private fun startAutoSlide() {
        val handler = Handler()
        val update = Runnable {
            if (currentPage == bannerImages.size) {
                currentPage = 0
            }
            binding.viewPager.setCurrentItem(currentPage++, true)
        }

        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, DELAY_MS, PERIOD_MS)
    }
//
//    private fun startAutoSlider() {
//        timer = Timer()
//
//        timer.schedule(object : TimerTask() {
//            override fun run() {
//                requireActivity().runOnUiThread {
//                    val nextPage = (currentPage + 1) % adapter.itemCount
//
//                    binding.recyclerView.smoothScrollToPosition(nextPage)
//                    currentPage = nextPage
//                }
//            }
//        }, 3000, 3000)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
    }
}
