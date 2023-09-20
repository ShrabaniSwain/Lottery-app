package com.lakhpati.lottery

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.lakhpati.lottery.databinding.ActivityWalletBinding
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WalletActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWalletBinding
    private lateinit var etBankName: EditText
    private lateinit var inputLayout: EditText
    private lateinit var etAccountName: EditText
    private lateinit var etAccNo: EditText
    private lateinit var etIfscCode: EditText
    private lateinit var btnRequest: TextView
    private lateinit var tvTitle: TextView

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvWalletAMount.text = Constants.WalletBalance

        binding.ivBack.setOnClickListener {
           onBackPressed()
        }

        binding.tvAddFund.setOnClickListener {

            val intent = Intent(this, UpiPaymentActivity::class.java)
            startActivity(intent)
        }

        binding.tvWithDraw.setOnClickListener {view ->
                val context = view.context
                val dialogBuilder = AlertDialog.Builder(context)

                val inflater = LayoutInflater.from(context).inflate(R.layout.withdrawal_request, null)
                dialogBuilder.setView(inflater)

            btnRequest = inflater.findViewById(R.id.btnRequest)
            tvTitle = inflater.findViewById(R.id.tvTitle)
            inputLayout = inflater.findViewById(R.id.inputLayout)
            etBankName = inflater.findViewById(R.id.etBankName)
            etAccountName = inflater.findViewById(R.id.etAccountName)
            etAccNo = inflater.findViewById(R.id.etAccNo)
            etIfscCode = inflater.findViewById(R.id.etIfscCode)


            btnRequest.setOnClickListener {
                val amount = inputLayout.text.toString()
                checkUserBankDetails(amount)
            }


            val dialog = dialogBuilder.create()
            dialog.show()

        }

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getWalletHistoryResult(Constants.customer_id)
            }

            if (response.isSuccessful) {
                val lotteryResponse = response.body()
                lotteryResponse?.let { handleWalletHistoryResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }
    }

    private fun handleWalletHistoryResponse(result: List<WalletHistoryModel>) {
        val walletAdapter = WalletAdapter(result)
        binding.rvWalletHistory.adapter = walletAdapter
        binding.rvWalletHistory.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun checkUserBankDetails(amount: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val bankDetailsResponse = withContext(Dispatchers.IO) {
                RetrofitClient.api.getUserBankDetailsResult(Constants.customer_id)
            }

            if (bankDetailsResponse.isSuccessful) {
                val bankDetails = bankDetailsResponse.body()

                if (bankDetails?.isSuccess == true) {
                    // User has bank details, proceed with withdrawal amount check
                    checkWithdrawalAmount(amount)
                } else {
                    // User does not have bank details, show bank details form and hide input layout
                    etBankName.visibility = View.VISIBLE
                    etAccNo.visibility = View.VISIBLE
                    etAccountName.visibility = View.VISIBLE
                    etIfscCode.visibility = View.VISIBLE
                    inputLayout.visibility = View.GONE
                    btnRequest.text = "Submit"
                    tvTitle.text = "Add Bank Details"
                    Toast.makeText(applicationContext, "Please add bank details first", Toast.LENGTH_SHORT).show()


                    btnRequest.setOnClickListener {
                        // Handle bank details submission here
                        val bankName = etBankName.text.toString()
                        val accNo = etAccNo.text.toString()
                        val accountName = etAccountName.text.toString()
                        val ifscCode = etIfscCode.text.toString()

                        if (bankName.isEmpty() || accNo.isEmpty() || accountName.isEmpty() || ifscCode.isEmpty()) {
                            Toast.makeText(applicationContext, "Please fill in all bank details", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                        // Call API to submit bank details
                        submitBankDetails(Constants.customer_id, bankName, accountName, accNo, ifscCode)
                    }

                }
            } else {
                Log.i("TAG", "Bank Details API Call failed with error code: ${bankDetailsResponse.code()}")
            }
        }
    }

    private fun submitBankDetails(customerId:String, bankName: String, accNo: String, accountName: String, ifscCode: String) {
        val addBankDetailsData = AddBankDetailsData(
            customerId, bankName, accountName, accNo, ifscCode
        )

        val call = RetrofitClient.api.addBankDeatils(addBankDetailsData)
        call.enqueue(object : Callback<AddBankDetailsResponse> {
            override fun onResponse(call: Call<AddBankDetailsResponse>, response: Response<AddBankDetailsResponse>) {
                if (response.isSuccessful) {
                    val addBankDetailsResponse = response.body()
                    if (addBankDetailsResponse?.isSuccess == true) {
                        // Bank details added successfully, show a success message or handle as needed
                        Toast.makeText(applicationContext, "Bank details added successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, WalletActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Show an error message or handle the case where the bank details were not added
                        Toast.makeText(applicationContext, "Failed to add bank details", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.i("TAG", "Add Bank Details API Call failed with error code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AddBankDetailsResponse>, t: Throwable) {
                Log.i("TAG", "Add Bank Details API Call failed: ${t.message}")
            }
        })
    }



    @OptIn(DelicateCoroutinesApi::class)
    private fun checkWithdrawalAmount(amount: String) {
        // Call the API to get the minimum withdrawal amount
        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getminimumWithdrawAmount()
            }

            if (response.isSuccessful) {
                val withdrawAmountResponse = response.body()
                val minimumWithdrawAmount = withdrawAmountResponse?.result?.firstOrNull()?.minimum_withdrawl_amount
                val inputAmount = amount

                if (minimumWithdrawAmount != null && inputAmount < minimumWithdrawAmount.toString()) {
                    Toast.makeText(applicationContext, "Minimum withdrawal amount is $minimumWithdrawAmount.", Toast.LENGTH_SHORT).show()
                }
                else if (inputAmount > Constants.WalletBalance){
                    Toast.makeText(applicationContext, "You don't have enough wallet balance", Toast.LENGTH_SHORT).show()
                }
                else if (inputAmount.isEmpty() || inputAmount == "0") {
                    Toast.makeText(applicationContext, "Enter a valid withdrawal amount", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(applicationContext, "Withdraw request sent successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }
    }
}