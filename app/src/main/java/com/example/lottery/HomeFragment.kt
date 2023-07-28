package com.example.lottery

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lottery.databinding.FragmentHomeBinding
import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val bannerImages = listOf(
        R.drawable.bannerimage,
        R.drawable.bannerimage,
        R.drawable.bannerimage
    )

    private var timer: Timer? = null
    private var currentPage = 0
    private val DELAY_MS: Long = 2000
    private val PERIOD_MS: Long = 2000
    val adapter = TicketCardSlideAdapter()
    private val firstwinnerTicketNoAdapter = FirstwinnerTicketNoAdapter()
    private val secondWinnerAdapter = SecondWinnerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvFirstWinnerTicket.adapter = firstwinnerTicketNoAdapter
        binding.rvFirstWinnerTicket.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvSecondWinnerTicket.adapter = secondWinnerAdapter
        binding.rvSecondWinnerTicket.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getBanners()
            }

            if (response.isSuccessful) {
                val bannerResponse = response.body()
                bannerResponse?.let { handleBannerResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getNoticeDetails()
            }

            if (response.isSuccessful) {
                val aboutResponse = response.body()
                aboutResponse?.let { handleNoticeResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }
    }

    private fun handleNoticeResponse(result: List<NoticeModel>) {
        if (result.isNotEmpty()) {
            val aboutPage = result[0]
            binding.tvNoticeDetails.text = aboutPage.notice_details
        }
    }

    private fun handleBannerResponse(banners: List<HomeBannerModel>) {
        val homeBannerAdapter = HomeBannerAdapter(banners)
        binding.viewPager.adapter = homeBannerAdapter
        startAutoSlide()
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
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, DELAY_MS, PERIOD_MS)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
    }
}
