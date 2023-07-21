package com.example.lottery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lottery.databinding.FragmentMyTicketBinding

class MyTicketFragment : Fragment() {

    private lateinit var binding: FragmentMyTicketBinding
    val latestTicketCardAdapter = LatestTicketCardAdapter()
    val oldTicketCardAdapter = OldTicketCardAdapter()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvLatestTicket.adapter = latestTicketCardAdapter
        binding.rvLatestTicket.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvOldTicket.adapter = oldTicketCardAdapter
        binding.rvOldTicket.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}