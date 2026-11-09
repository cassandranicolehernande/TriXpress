package com.example.trixpress

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PassengerDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passenger_dashboard)

        // Listahan ng mga lokasyon sa Nasugbu, Batangas lamang
        val nasugbuLocations = arrayOf(
            "BatStateU ARASOF - Nasugbu",
            "Nasugbu Poblacion",
            "Wawa, Nasugbu",
            "Bucana, Nasugbu",
            "Kaylaway, Nasugbu",
            "Bunducan, Nasugbu",
            "Natipuan, Nasugbu",
            "Palico, Nasugbu",
            "Looc, Nasugbu",
            "Pantalan, Nasugbu",
            "Banilad, Nasugbu",
            " Aga, Nasugbu",
            "7-Eleven Westmead Gate",
            "Nasugbu Public Market"
        )

        // Adapter para lumitaw ang mga choices
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nasugbuLocations)

        val etPickup = findViewById<AutoCompleteTextView>(R.id.etPickup)
        val etDropoff = findViewById<AutoCompleteTextView>(R.id.etDropoff)
        val btnBook = findViewById<Button>(R.id.btnBook)

        // I-set ang adapter at sabihing magpakita agad kahit 0 letters pa lang ang i-type o i-click
        etPickup.setAdapter(adapter)
        etPickup.threshold = 0
        etPickup.setOnClickListener { etPickup.showDropDown() }

        etDropoff.setAdapter(adapter)
        etDropoff.threshold = 0
        etDropoff.setOnClickListener { etDropoff.showDropDown() }

        btnBook.setOnClickListener {
            val pickup = etPickup.text.toString().trim()
            val dropoff = etDropoff.text.toString().trim()

            if (pickup.isNotEmpty() && dropoff.isNotEmpty()) {
                Toast.makeText(this, "Finding your tricycle...", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter pickup and destination", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
