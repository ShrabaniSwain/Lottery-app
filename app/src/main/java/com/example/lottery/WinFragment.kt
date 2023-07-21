package com.example.lottery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lottery.databinding.FragmentWinBinding

class WinFragment : Fragment() {

    private lateinit var binding: FragmentWinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWinBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val imageUrlList = mutableListOf<String>()
//
//        imageUrlList.add("https://img.freepik.com/premium-vector/winner-banner-win-congratulations-vintage-frame-golden-congratulating-framed-sign-with-gold-confetti-background-illustration_102902-1370.jpg?size=626&ext=jpg&ga=GA1.2.661717373.1689076573&semt=ais")
//        imageUrlList.add("https://img.freepik.com/premium-vector/winner-banner-win-congratulations-vintage-frame-golden-congratulating-framed-sign-with-gold-confetti-background-illustration_102902-1370.jpg?size=626&ext=jpg&ga=GA1.2.661717373.1689076573&semt=ais")
//        imageUrlList.add("https://img.freepik.com/premium-vector/winner-banner-win-congratulations-vintage-frame-golden-congratulating-framed-sign-with-gold-confetti-background-illustration_102902-1370.jpg?size=626&ext=jpg&ga=GA1.2.661717373.1689076573&semt=ais")
//
//        binding.rvWInTicket.layoutManager = LinearLayoutManager(requireContext())
//        val adapter = WInTicketAdapter(imageUrlList)
//        binding.rvWInTicket.adapter = adapter

    }

}