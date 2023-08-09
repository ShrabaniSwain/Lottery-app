package com.example.lottery

import android.app.DatePickerDialog
import android.app.DownloadManager
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lottery.databinding.FragmentHomeBinding
import kotlinx.coroutines.*
import java.lang.Runnable
import android.text.format.DateFormat
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(), OtherBrandAdapterCallback {

    private lateinit var binding: FragmentHomeBinding
    private var selectedBrandIndex: Int = 0

    private val bannerImages = listOf(
        R.drawable.bannerimage,
        R.drawable.bannerimage,
        R.drawable.bannerimage
    )

    private var timer: Timer? = null
    private var currentPage = 0
    private val DELAY_MS: Long = 2000
    private val PERIOD_MS: Long = 2000
    private val buttonDataList = mutableListOf<OtherResultsData>()

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
        updateUI()

        binding.btnDownload.setOnClickListener {
            val selectedBrandData = buttonDataList[selectedBrandIndex]
            downloadPdf(selectedBrandData.lottery_prize)
        }

        updateDateText(System.currentTimeMillis())
        binding.tvDatePicker.setOnClickListener {
            showDatePickerDialog()
        }

        binding.tvTimePicker.setOnClickListener {
            showTimePickerDialog()
        }
        val calendar = Calendar.getInstance()
        val currentHourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        updateTimeText(currentHourOfDay, currentMinute)

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

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getLottery()
            }

            if (response.isSuccessful) {
                val lotteryResponse = response.body()
                lotteryResponse?.let { handleAllLotteryResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getLotteryResult()
            }

            if (response.isSuccessful) {
                val lotteryResponse = response.body()
                lotteryResponse?.let { handleLotteryResultResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getOtherResult()
            }

            if (response.isSuccessful) {
                val otherBrandResponse = response.body()
                otherBrandResponse?.let { handleOtherBrandResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }
    }

    private fun handleOtherBrandResponse(result: List<OtherResultsData>) {
        buttonDataList.clear()
        buttonDataList.addAll(result)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = OtherBrandAdapter(buttonDataList, this)
        binding.rvOtherBrandName.layoutManager = layoutManager
        binding.rvOtherBrandName.adapter = adapter

        if (buttonDataList.isNotEmpty()) {
            onBrandItemClick(buttonDataList[0],selectedBrandIndex)
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                updateTimeText(hourOfDay, minute)
            },
            hour,
            minute,
            DateFormat.is24HourFormat(requireContext())
        )
        timePickerDialog.show()
    }

    private fun updateTimeText(hourOfDay: Int, minute: Int) {
        val timeFormat = "hh:mm a"
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val selectedTime = SimpleDateFormat(timeFormat, Locale.getDefault()).format(calendar.time)
        binding.tvTimePicker.text = selectedTime
    }




    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerListener(),
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun updateDateText(timestamp: Long) {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(timestamp))
        binding.tvDatePicker.text = formattedDate
    }

    private fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    inner class DatePickerListener : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            val selectedDate = formatDate(year, month, dayOfMonth)
            binding.tvDatePicker.text = selectedDate
        }
    }
    private fun handleLotteryResultResponse(result: List<LotteryResult>) {
        val adapter = FirstwinnerTicketNoAdapter(result)
        binding.rvFirstWinnerTicket.adapter = adapter
        binding.rvFirstWinnerTicket.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val firstWinnerPrizeAmount = result.find { it.prize_position == "1st" }?.prize
        binding.tvFirstPrizeAmount.text = firstWinnerPrizeAmount.toString()

        val secondWinnerAdapter = SecondWinnerAdapter(result)
        binding.rvSecondWinnerTicket.adapter = secondWinnerAdapter
        binding.rvSecondWinnerTicket.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val secondWinnerPrizeAmount = result.find { it.prize_position == "2nd" }?.prize
        binding.tvSecondPrizeAmount.text = secondWinnerPrizeAmount.toString()

        val luckyWinnerAdapter = LuckyWinnerAdapter(result)
        binding.rvLuckyWinnerTicket.adapter = luckyWinnerAdapter
        binding.rvLuckyWinnerTicket.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val luckyWinnerPrizeAmount = result.find { it.prize_position == "Lucky Winner" }?.prize
        binding.tvLuckyWinnerTextPrize.text = luckyWinnerPrizeAmount.toString()
    }

    private fun handleAllLotteryResponse(result: List<AllLotteryModel>) {
        val adapter = TicketCardSlideAdapter(result)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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

    override fun onBrandItemClick(otherBrandData: OtherResultsData, position: Int) {
        selectedBrandIndex = position
        binding.rvOtherBrandName.adapter?.notifyDataSetChanged()
        updateUI()
    }

    private fun updateUI() {
        if (selectedBrandIndex in buttonDataList.indices) {
            val selectedBrandData = buttonDataList[selectedBrandIndex]
            binding.apply {
                tvNameLotteryName.text = selectedBrandData.result_title
                tvNameLotteryText.text = selectedBrandData.result_description
                loadPdfWithGlide(selectedBrandData.image_thumbnail, ivLotteryImage)
            }
        }
    }

    private fun loadPdfWithGlide(url: String, imageView: ImageView) {
        context?.let {
            Glide.with(it)
                .asBitmap()
                .load(url)
                .placeholder(R.drawable.bannerimage)
                .error(R.drawable.bannerimage)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        // Display the PDF thumbnail using the loaded Bitmap
                        imageView.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Called when the resource is released and we should clear the image view.
                        imageView.setImageDrawable(placeholder)
                    }
                })
        }
    }


    private fun downloadPdf(imageUrl: String) {
        val fileName = "lottery_prize.pdf"
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val outputFile = File(directory, fileName)

        val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(imageUrl)

        val request = DownloadManager.Request(downloadUri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Downloading PDF")
        request.setDescription("Downloading lottery prize PDF...")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationUri(Uri.fromFile(outputFile))
        request.setMimeType("application/pdf")

        val downloadId = downloadManager.enqueue(request)

        // You can also listen for download completion and show a toast or notification.
        // For simplicity, we won't add it here, but it can be added for a better user experience.
    }


}
