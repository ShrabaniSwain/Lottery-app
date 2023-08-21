package com.lakhpati.lottery

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lakhpati.lottery.databinding.ActivityMainBinding
import com.lakhpati.lottery.databinding.HeaderLayoutBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var headerBinding: HeaderLayoutBinding

    private var isLoggedOut = false

    private val homeFragment: Fragment = HomeFragment()
    private val ticketFragment: Fragment = MyTicketFragment()
    private val winFragment:Fragment = WinFragment()
    private val historyFragment:Fragment = HistoryFragment()

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!Utility.isNetworkAvailable(this)) {
            Toast.makeText(this, "No internet connection. Please check your network settings.", Toast.LENGTH_SHORT).show()
            return
        }
        val sharedPrefHelper = SharedPreferenceHelper(this)
        if (sharedPrefHelper.isLoggedIn()) {
            Constants.customer_id = sharedPrefHelper.getCustomerId(this)
            Constants.customer_name = sharedPrefHelper.getCustomerName(this)
            Constants.customer_mobilenumber = sharedPrefHelper.getCustomerMobileNumber(this)
            Constants.customer_EmialId = sharedPrefHelper.getCustomerEmailId(this)
            Constants.customer_Mipn = sharedPrefHelper.getCustomerMpin(this)
        }

        if (!sharedPrefHelper.isLoggedIn()) {
            clearSessionData()
        }

        fetchWalletBalance(Constants.customer_id)

        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_home -> replaceFragment(homeFragment)
                R.id.action_my_ticket -> replaceFragment(ticketFragment)
                R.id.action_win3 -> replaceFragment(winFragment)
                R.id.action_history -> replaceFragment(historyFragment)
            }
            true
        }

        binding.toolbar.walletButton.setOnClickListener{
            val intent = Intent(this, WalletActivity::class.java)
            startActivity(intent)
        }

        binding.toolbar.notificationButton.setOnClickListener{
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.toolbar.shareButton.setOnClickListener {
            share()
        }

        // Set the initial fragment
        replaceFragment(homeFragment)

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            for (i in 0 until binding.navView.menu.size()) {
                val item = binding.navView.menu.getItem(i)
                item.isChecked = false
            }
            menuItem.isChecked = true
            binding.drawerLayout.closeDrawers()

            // Handle navigation view item clicks here.
            when (menuItem.itemId) {

                R.id.nav_transaction_history -> {
                    val intent = Intent(this, HistoryActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_support -> {
                    val intent = Intent(this, SupportActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_recharge_wallet -> {
                    val intent = Intent(this, UpiPaymentActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_about -> {
                    val intent = Intent(this, com.lakhpati.lottery.AboutUsActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_contact -> {
                    val intent = Intent(this, ContactUsActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_terms_condition -> {
                    val intent = Intent(this, TermsAndCondition::class.java)
                    startActivity(intent)
                }

                R.id.nav_privacy_policy -> {
                    val intent = Intent(this, PrivacyPolicyActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_refund_policy -> {
                    val intent = Intent(this, RefundActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_logout -> {
                    showLogoutConfirmationDialog()
                }

            }

            true
        }

        headerBinding = HeaderLayoutBinding.bind(binding.navView.getHeaderView(0))

        headerBinding.tvProfileName.text = Constants.customer_name

        headerBinding.ivEdit.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.toolbar.menuButton.setOnClickListener{
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getProfilePic(Constants.customer_id)
            }

            if (response.isSuccessful) {
                val aboutResponse = response.body()
                aboutResponse?.let { handleProfilePicResponse(it.result) }
            } else {
                Log.i("TAG", "API Call failed with error code: ${response.code()}")
            }
        }
    }

    private fun handleProfilePicResponse(result: List<ProfileImageModel>) {
        if (result.isNotEmpty()) {
            val aboutPage = result[0]
            Glide.with(headerBinding.profileImageView.context)
                .load(aboutPage.profile_image)
                .apply(RequestOptions.placeholderOf(R.drawable.profile))
                .into(headerBinding.profileImageView)
        }
    }

    private fun fetchWalletBalance(customerId: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.api.getWalletBalance(customerId)
                }

                if (response.isSuccessful) {
                    val aboutResponse = response.body()
                    aboutResponse?.let { handleWalletBalanceResponse(it.result) }
                } else {
                    Log.i("TAG", "API Call failed with error code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("TAG", "API Call failed with exception: ${e.message}")
            }
        }
    }

    private fun handleWalletBalanceResponse(result: List<WalletModel>) {
        if (result.isNotEmpty()) {
            val aboutPage = result[0]
            val walletBalance = aboutPage.wallet_balance ?: "0"
            val formattedWalletBalance = "₹ $walletBalance"
            binding.toolbar.tvWalletBalance.text = formattedWalletBalance
            Constants.WalletBalance = walletBalance
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
    private fun showLogoutConfirmationDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.logout_dialog_box, null)
        val dialogOk = dialogView.findViewById<TextView>(R.id.btnOk)
        val dialogCancel = dialogView.findViewById<TextView>(R.id.btnCancel)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogOk.setOnClickListener {
            clearSessionData()
            navigateToLoginScreen()
            alertDialog.dismiss()

            Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show()
        }

        dialogCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun clearSessionData() {
        val sharedPrefHelper = SharedPreferenceHelper(this)
        sharedPrefHelper.clearSession()
    }

    private fun navigateToLoginScreen() {
        isLoggedOut = true
        val intent = Intent(this, LoginMpinACtivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        if (isLoggedOut) {
            navigateToLoginScreen()
        }
    }


    private fun share() {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, "${resources?.getString(R.string.take_a_look_at_lakhpati)} ${Constants.REFER_URL}")
        applicationContext.startActivity(Intent.createChooser(share, null).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}