package com.example.lottery

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lottery.databinding.FragmentMyTicketBinding
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MyTicketFragment : Fragment() {

    private lateinit var binding: FragmentMyTicketBinding
    private  var latestTickets: ArrayList<LattestTicketModel> = arrayListOf()
    private  var oldTickets: ArrayList<OlderTicketModel> = arrayListOf()
    private var todayFormattedDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMyTicketBinding.inflate(inflater,container,false)
        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val todayDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        todayFormattedDate = todayDateFormat.format(Date())

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getOlderTicketResult(Constants.customer_id)
            }

            if (response.isSuccessful) {
                val lotteryResponse = response.body()
                lotteryResponse?.let { handleOlderTicketResponse(it.result)
                oldTickets.addAll(it.result)
                }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getLatestTicketResult(Constants.customer_id)
            }

            if (response.isSuccessful) {
                val lotteryResponse = response.body()
                if (lotteryResponse == null){
                    binding.tvnoData.visibility = View.VISIBLE
                }
                lotteryResponse?.let {
                    handleLatestResponse(it.result)
                    latestTickets.addAll(it.result)
                    updateTickets()
                    Log.i("TAG", "API Call: ${it.result}")

                }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response}")
            }
        }

        binding.tvDatePicker.setOnClickListener {
            showDatePickerDialog()
        }
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

            val formattedSelectedDate = formatDateForComparison(selectedDate)

            val filteredOldTickets = oldTickets.filter { ticket ->
                val ticketDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val playDate = ticketDateFormat.parse(ticket.play_date)
                val formattedPlayDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(
                    playDate!!
                )
                formattedPlayDate == formattedSelectedDate
            }
            handleOlderTicketResponse(filteredOldTickets)

            if (filteredOldTickets.isEmpty()) {
                binding.tvNoOldData.visibility = View.VISIBLE
            } else {
                binding.tvNoOldData.visibility = View.GONE
            }

            val filteredLatestTickets = latestTickets.filter { ticket ->
                val ticketDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val playDate1 = ticketDateFormat.parse(ticket.play_date)
                val formattedPlayDate1 = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(playDate1!!)
                formattedPlayDate1 == formattedSelectedDate
            }

            handleLatestResponse(filteredLatestTickets)

            if (filteredLatestTickets.isEmpty()) {
                binding.tvnoData.visibility = View.VISIBLE
            } else {
                binding.tvnoData.visibility = View.GONE
            }

//            val oldTicketsWithoutToday = filteredOldTickets.filterNot { ticket ->
//                val ticketDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//                val playDate = ticketDateFormat.parse(ticket.play_date)
//                val formattedPlayDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(playDate)
//                formattedPlayDate == todayFormattedDate
//            }
//            handleOlderTicketResponse(oldTicketsWithoutToday)

        }
    }

    private fun handleLatestResponse(result: List<LattestTicketModel>) {
        val latestTicketCardAdapter = LatestTicketCardAdapter(result)
        binding.rvLatestTicket.adapter = latestTicketCardAdapter
        binding.rvLatestTicket.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun handleOlderTicketResponse(result: List<OlderTicketModel>) {
        val oldTicketCardAdapter = OldTicketCardAdapter(result)
        binding.rvOldTicket.adapter = oldTicketCardAdapter
        binding.rvOldTicket.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun formatDateForComparison(date: String): String {
        val inputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val parsedDate = inputFormat.parse(date)
        return outputFormat.format(parsedDate!!)
    }

    private fun updateTickets() {
        val filteredLatestTickets = latestTickets.filter { ticket ->
            val ticketDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val playDate = ticketDateFormat.parse(ticket.play_date)
            val formattedPlayDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(playDate)
            Constants.notificationTitle= formattedPlayDate
            formattedPlayDate == todayFormattedDate
        }

        handleLatestResponse(filteredLatestTickets)

        if (filteredLatestTickets.isEmpty()) {
            binding.tvnoData.visibility = View.VISIBLE
        } else {
            binding.tvnoData.visibility = View.GONE
        }

    }
}