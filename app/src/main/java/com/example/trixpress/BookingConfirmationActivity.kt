package com.example.trixpress

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BookingConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_confirmation)

        // Safe findViewById checks to prevent crashes
        val tvDriverName = findViewById<TextView?>(R.id.tvDriverName)
        val tvPickup = findViewById<TextView?>(R.id.tvPickup)
        val tvDestination = findViewById<TextView?>(R.id.tvDestination)
        val tvFare = findViewById<TextView?>(R.id.tvFare)
        val btnDone = findViewById<Button?>(R.id.btnDone)

        // Get intent data passed from MapActivity
        val driverName = intent.getStringExtra("DRIVER_NAME") ?: "No Driver Selected"
        val pickupLoc = intent.getStringExtra("PICKUP_LOC") ?: "BatStateU ARASOF Campus"
        val destLoc = intent.getStringExtra("DEST_LOC") ?: "Nasugbu Public Market"
        val estFare = intent.getStringExtra("EST_FARE") ?: "₱50.00"

        // Set text safely
        tvDriverName?.text = driverName
        tvPickup?.text = pickupLoc
        tvDestination?.text = destLoc
        tvFare?.text = estFare

        Toast.makeText(this, "Booking Confirmed for $driverName", Toast.LENGTH_SHORT).show()

        btnDone?.setOnClickListener {
            val intent = Intent(this, PassengerDashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}
