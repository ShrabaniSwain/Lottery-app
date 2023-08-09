package com.example.lottery

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lottery.databinding.FragmentMyTicketBinding
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class MyTicketFragment : Fragment() {

    private lateinit var binding: FragmentMyTicketBinding
    private lateinit var latestTickets: List<OlderTicketModel>
    private lateinit var oldTickets: List<OlderTicketModel>

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

        latestTickets = emptyList()
        oldTickets = emptyList()

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getOlderTicketResult(Constants.customer_id)
            }

            if (response.isSuccessful) {
                val lotteryResponse = response.body()
                lotteryResponse?.let { handleOlderTicketResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getOlderTicketResult(Constants.customer_id)
            }

            if (response.isSuccessful) {
                val lotteryResponse = response.body()
                lotteryResponse?.let { handleLatestResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }

        updateDateText(System.currentTimeMillis())
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

            val filteredLatestTickets = latestTickets.filter {
                "04 August 2023" == formattedSelectedDate
            }
            val filteredOldTickets = oldTickets.filter { ticket ->
                ticket.play_date == formattedSelectedDate
            }

            handleLatestResponse(filteredLatestTickets)
            handleOlderTicketResponse(filteredOldTickets)

//            // Show a text message if no matching tickets found
//            if (filteredLatestTickets.isEmpty() && filteredOldTickets.isEmpty()) {
//                binding.tvNoMatchingTickets.visibility = View.VISIBLE
//            } else {
//                binding.tvNoMatchingTickets.visibility = View.GONE
//            }
        }
    }

    private fun handleLatestResponse(result: List<OlderTicketModel>) {
        latestTickets = result
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
}