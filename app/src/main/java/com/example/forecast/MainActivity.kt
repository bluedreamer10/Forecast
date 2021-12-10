package com.example.forecast

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var CITY: String = "jabalpur,in"
    val API: String = "915964c4a18a2da7b1fbc726e158aae1"

    private fun hideKeyboard(){
        // since our app extends AppCompatActivity, it has access to context
        val imm=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // we have to tell hide the keyboard from what. inorder to do is we have to pass window token
        // all of our views,like message, name, button have access to same window token. since u have button
        imm.hideSoftInputFromWindow(saveInputText.windowToken, 0)

        // if you are using binding object
        // imm.hideSoftInputFromWindow(binding.button.windowToken,0)

    }

    fun loadCityData(view: View) {
        var cityName:String? = inputCity.text.toString()
        if (cityName != null) {
            if (cityName.isNotEmpty()){
                CITY = cityName+",in"
                weatherTask().execute()
            }
        }
        inputCity.text.clear()
        hideKeyboard()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        developerDetails.setOnClickListener{
            Toast.makeText(this,"Developer Details",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DeveloperDetails::class.java)
            startActivity(intent)
            finish()
        }

        weatherTask().execute()
    }

    inner class weatherTask():AsyncTask<String,Void,String>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
            loader.visibility = View.VISIBLE
            mainContainer.visibility = View.GONE
            errorText.visibility = View.GONE
        }

        override fun doInBackground(vararg p0: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API")
                    .readText(Charsets.UTF_8)
            }
            catch(e:Exception)
            {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try{
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at: "+SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
                val temp = main.getString("temp")+"°C"
                val tempMin = "Min Temp: "+main.getString("temp_min")+"°C"
                val tempMax = "Max Temp: "+main.getString("temp_max")+"°C"
                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = jsonObj.getString("name")+", "+sys.getString("country")

                addressfinal.text = address
                updated_at.text = updatedAtText
                sunriseTime.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                sunsetTime.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                statusfinal.text = weatherDescription.capitalize()
                tempfinal.text = temp
                temp_min.text = tempMin
                temp_max.text = tempMax
                windSpeedfinal.text = windSpeed + " m/s"

                loader.visibility = View.GONE
                mainContainer.visibility = View.VISIBLE

            }

            catch (e:Exception)
            {
                loader.visibility = View.GONE
                errorText.visibility = View.VISIBLE
            }
        }
    }

}