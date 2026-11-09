package com.example.trixpress

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

data class TricycleDriver(
    val name: String,
    val toda: String,
    val location: LatLng,
    val fare: String
)

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    // Mock Passenger Location (Default: BatStateU ARASOF Campus, Nasugbu)
    private val passengerLocation = LatLng(14.0689, 120.6295)

    // All available drivers across different TODAs in Nasugbu
    private val allDrivers = listOf(
        // ARASOF Campus TODA (Very Close to Passenger ~0m to 300m)
        TricycleDriver("Kuya Roberto", "ARASOF Campus TODA", LatLng(14.0689, 120.6295), "₱40.00"),
        TricycleDriver("Kuya Lito", "ARASOF Campus TODA", LatLng(14.0695, 120.6302), "₱40.00"),
        TricycleDriver("Kuya Cardo", "ARASOF Campus TODA", LatLng(14.0682, 120.6288), "₱40.00"),

        // Town Proper TODA (Nearby ~500m to 800m)
        TricycleDriver("Kuya Juaning", "Town Proper TODA", LatLng(14.0722, 120.6341), "₱45.00"),
        TricycleDriver("Kuya Dodong", "Town Proper TODA", LatLng(14.0728, 120.6350), "₱45.00"),

        // Bunducan TODA (Farther ~1.2km)
        TricycleDriver("Kuya Mario", "Bunducan TODA", LatLng(14.0757, 120.6318), "₱50.00"),
        TricycleDriver("Kuya Ben", "Bunducan TODA", LatLng(14.0762, 120.6325), "₱50.00"),

        // Far TODAs (Wawa & Natipuan - Should NOT appear if passenger is in ARASOF)
        TricycleDriver("Kuya Nilo", "Wawa TODA", LatLng(14.0950, 120.6400), "₱60.00"),
        TricycleDriver("Kuya Mark", "Natipuan Beach TODA", LatLng(14.1200, 120.6100), "₱80.00")
    )

    // List to hold filtered nearby drivers only
    private var nearbyDrivers = listOf<TricycleDriver>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Filter drivers within 1500 meters (1.5 km) radius from passenger
        nearbyDrivers = filterDriversByDistance(allDrivers, passengerLocation, maxDistanceMeters = 1500f)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val btnZones = findViewById<Button>(R.id.btnZones)
        val btnActiveDrivers = findViewById<Button>(R.id.btnActiveDrivers)

        btnZones.setOnClickListener {
            Toast.makeText(this, "Nasugbu Service Zones boundary displayed", Toast.LENGTH_SHORT).show()
        }

        btnActiveDrivers.setOnClickListener {
            if (nearbyDrivers.isEmpty()) {
                Toast.makeText(this, "No active drivers available nearby", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val displayList = nearbyDrivers.map { "${it.name} - ${it.toda} (${it.fare})" }.toTypedArray()

            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Nearby TODA Drivers Only")

            builder.setItems(displayList) { _, which ->
                val selectedDriver = nearbyDrivers[which]

                val intent = Intent(this, BookingConfirmationActivity::class.java).apply {
                    putExtra("DRIVER_NAME", "${selectedDriver.name} (${selectedDriver.toda})")
                    putExtra("PICKUP_LOC", "BatStateU ARASOF Campus")
                    putExtra("DEST_LOC", "Nasugbu Public Market")
                    putExtra("EST_FARE", selectedDriver.fare)
                }
                startActivity(intent)
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            builder.show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Focus map camera on Passenger's current location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(passengerLocation, 15f))

        // Add Marker for Passenger Pickup Location
        mMap.addMarker(
            MarkerOptions()
                .position(passengerLocation)
                .title("Your Pickup Location")
                .snippet("BatStateU ARASOF Campus")
        )

        // ONLY display map markers for NEARBY drivers (within 1.5 km)
        for (driver in nearbyDrivers) {
            mMap.addMarker(
                MarkerOptions()
                    .position(driver.location)
                    .title("${driver.name} - ${driver.toda}")
                    .snippet("Fare: ${driver.fare}")
            )
        }
    }

    // Helper function to calculate distance using Android Location service
    private fun filterDriversByDistance(
        drivers: List<TricycleDriver>,
        origin: LatLng,
        maxDistanceMeters: Float
    ): List<TricycleDriver> {
        val filteredList = mutableListOf<TricycleDriver>()
        val results = FloatArray(1)

        for (driver in drivers) {
            Location.distanceBetween(
                origin.latitude, origin.longitude,
                driver.location.latitude, driver.location.longitude,
                results
            )

            // If driver is within maximum radius (e.g., 1500m), include in the list
            if (results[0] <= maxDistanceMeters) {
                filteredList.add(driver)
            }
        }
        return filteredList
    }
}
