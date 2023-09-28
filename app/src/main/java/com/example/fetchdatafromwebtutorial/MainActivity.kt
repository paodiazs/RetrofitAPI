package com.example.fetchdatafromwebtutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fetchdatafromwebtutorial.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //binding explicaicón libreta
      binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//incializamos app
      fetchCurrencyData().start()
    }
//se usan hilos
  private fun fetchCurrencyData(): Thread{
    return Thread{
      val url = URL("https://open.er-api.com/v6/latest/aud")
      //conexión por http
      val connection = url.openConnection()  as HttpURLConnection
      //if code of response = 200
      //200 OK - la solicitud ha tenido éxito
      if(connection.responseCode == 200)
      {
        val inputSystem  = connection.inputStream
        val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
        //biblioteca  que permite la serialización y deserialización entre objetos Java y su representación en notación JSON
        val request = Gson().fromJson(inputStreamReader, Request::class.java)
        updatedUI(request)
        inputStreamReader.close()
        inputSystem.close()
      }else{
        binding.baseCurrency.text = "Failed Connection"
      }

    }
  }

  private fun updatedUI(request: Request) {
    runOnUiThread{
  kotlin.run {
    binding.LastUpdated.text = request.time_last_update_utc
    binding.nzd.text = String.format("NZD: %.2f", request.rates.NZD)
    binding.usd.text = String.format("USD: %.2f", request.rates.USD)
    binding.mxn.text = String.format("MXN: %.2f", request.rates.MXN)

  }
    }
  }
}
