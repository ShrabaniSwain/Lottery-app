package com.example.lottery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.lottery.databinding.ActivityMainBinding
import com.example.lottery.databinding.HeaderLayoutBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var headerBinding: HeaderLayoutBinding

    private val homeFragment: Fragment = HomeFragment()
    private val ticketFragment: Fragment = MyTicketFragment()
    private val winFragment:Fragment = WinFragment()
    private val historyFragment:Fragment = HistoryFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        // Set the initial fragment
        replaceFragment(homeFragment)

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            binding.drawerLayout.closeDrawers()

            // Handle navigation view item clicks here.
            when (menuItem.itemId) {

                R.id.nav_transaction_history -> {

                }
                R.id.nav_recharge_wallet -> {
                    Toast.makeText(this, "Wallet", Toast.LENGTH_LONG).show()
                }
            }

            true
        }

        headerBinding = HeaderLayoutBinding.bind(binding.navView.getHeaderView(0))

        headerBinding.ivEdit.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.toolbar.menuButton.setOnClickListener{
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}