package com.example.weatherapp

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androdocs.weatherapp.WeatherResponse
import com.androdocs.weatherapp.WeatherService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    private var fusedLocationClient: FusedLocationProviderClient? = null

    val CITY: String = "surat,in"
    val API: String = "9e4fb7e472c5e8f85f85a2b5d47c77dd"
    val BaseUrl: String = "https://api.openweathermap.org/"
    lateinit var progressDialog: ProgressDialog
    private var lastLocation: Location? = null
    private val TAG = "LocationProvider"
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        weatherTask3().execute()

//        if (ContextCompat.checkSelfPermission(this@MainActivity,
//                Manifest.permission.ACCESS_FINE_LOCATION) !==
//            PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//                ActivityCompat.requestPermissions(this@MainActivity,
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
//            } else {
//                ActivityCompat.requestPermissions(this@MainActivity,
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
//            }
//        }
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Please wait")
        progressDialog.show()

        val retrofit = Retrofit.Builder().baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()

        val service = retrofit!!.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(CITY, API).also {

            it.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>) {
                    if (response.code() == 200) {
                        val wheatherResponse = response.body()


                        address.text =
                            wheatherResponse!!.name + ", " + wheatherResponse!!.sys!!.country
                        val updatedAt: Long = wheatherResponse.dt.toLong()

                        updated_at.text = "Updated at: " + SimpleDateFormat(
                            "dd/MM/yyyy hh:mm a",
                            Locale.ENGLISH
                        ).format(Date(wheatherResponse.dt.toLong() * 1000))
                        status1.text =
                            wheatherResponse.weather[0].description!!.capitalize()

                        temp.text = wheatherResponse.main!!.temp.toInt().toString() + "°C"
                        temp_max.text =
                            "Max Temp: " + wheatherResponse.main!!.temp_max.toInt().toString() + "°C"
                        temp_min.text =
                            "Min Temp: " + wheatherResponse.main!!.temp_min.toInt().toString() + "°C"

                        sunrise.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(
                            Date(wheatherResponse.sys!!.sunrise.toLong() * 1000)
                        )
                        sunset.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(
                            Date(wheatherResponse.sys!!.sunset.toLong() * 1000)
                        )

                        wind.text = wheatherResponse.wind!!.speed.toString()
                        pressure.text = wheatherResponse.main!!.pressure.toString()
                        humidity.text = wheatherResponse.main!!.humidity.toString()
                        progressDialog.dismiss()
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                }
            })
        }
    }

//    public override fun onStart() {
//        super.onStart()
//        if (!checkPermissions()) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions()
//            }
//        }
//        else {
//            getLastLocation()
//        }
//    }

    private fun getLastLocation() {
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                lastLocation = task.result

                val long = (lastLocation)!!.longitude.toString()
                val lat = (lastLocation)!!.latitude.toString()

                println("LLLongtitude123")
                println("LLLongtitude $long")
                println("LLLatitude $lat")
                println((lastLocation)!!.latitude.toString())

                val retrofit = Retrofit.Builder().baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create()).build()

                val service = retrofit!!.create(WeatherService::class.java)
                val call = service.getCurrentWeatherData((lastLocation)!!.latitude.toString(),(lastLocation)!!.longitude.toString(), API).also {

                    it.enqueue(object : Callback<WeatherResponse> {
                        override fun onResponse(
                            call: Call<WeatherResponse>,
                            response: Response<WeatherResponse>) {
                            if (response.code() == 200) {
                                val wheatherResponse = response.body()


                                address.text =
                                    wheatherResponse!!.name + ", " + wheatherResponse!!.sys!!.country
                                val updatedAt: Long = wheatherResponse.dt.toLong()

                                updated_at.text = "Updated at: " + SimpleDateFormat(
                                    "dd/MM/yyyy hh:mm a",
                                    Locale.ENGLISH
                                ).format(Date(wheatherResponse.dt.toLong() * 1000))
                                status1.text =
                                    wheatherResponse.weather[0].description!!.capitalize()

                                temp.text = wheatherResponse.main!!.temp.toInt().toString() + "°C"
                                temp_max.text =
                                    "Max Temp: " + wheatherResponse.main!!.temp_max.toInt().toString() + "°C"
                                temp_min.text =
                                    "Min Temp: " + wheatherResponse.main!!.temp_min.toInt().toString() + "°C"

                                sunrise.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(
                                    Date(wheatherResponse.sys!!.sunrise.toLong() * 1000)
                                )
                                sunset.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(
                                    Date(wheatherResponse.sys!!.sunset.toLong() * 1000)
                                )

                                wind.text = wheatherResponse.wind!!.speed.toString()
                                pressure.text = wheatherResponse.main!!.pressure.toString()
                                humidity.text = wheatherResponse.main!!.humidity.toString()
                                progressDialog.dismiss()
                            }
                        }

                        override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                        }
                    })
                }
            }
            else {
            }
        }
    }
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }
    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }
    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (shouldProvideRationale) {

        }
        else {
            startLocationPermissionRequest()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray) {
        Log.i(TAG, "onRequestPermissionResult")

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                    Log.i(TAG, "User interaction was granted.")
                    getLastLocation()
                }
                else -> {
                    showSnackbar("Permission was denied", "Settings",
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                Build.DISPLAY, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }

    private fun showSnackbar(
        mainTextStringId: String, actionStringId: String,
        listener: View.OnClickListener
    ) {
        Toast.makeText(this@MainActivity, mainTextStringId, Toast.LENGTH_LONG).show()
    }

}
