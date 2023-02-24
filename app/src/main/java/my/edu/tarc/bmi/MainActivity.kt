package my.edu.tarc.bmi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Linking of UI and code
        val imageViewBMI: ImageView = findViewById(R.id.imageViewBMI)
        val textViewBMI: TextView = findViewById(R.id.textViewBMI)
        val textViewStatus: TextView = findViewById(R.id.textViewStatus)

        val editTextWeight: EditText = findViewById(R.id.editTextWeight)
        val editTextHeight: EditText = findViewById(R.id.editTextHeight)

        val buttonCalculate: Button = findViewById(R.id.buttonCalculate)
        val buttonReset: Button = findViewById(R.id.buttonReset)
        val buttonHistory: Button = findViewById(R.id.buttonHistory)

        //Setup a local storage
        val sharedPreferences = this.getSharedPreferences("LocalStorage", MODE_PRIVATE)

        buttonCalculate.setOnClickListener {
            //BMI = weight / height in meters power 2
            if(editTextWeight.text.isEmpty() ){ //&& editTextWeight.text.toString().toInt() > 0
                editTextWeight.setError(getString(R.string.value_required))
                return@setOnClickListener //terminate program execution here
            }

            if(editTextHeight.text.isEmpty() ){ //&& editTextHeight.text.toString().toFloat() > 0
                editTextHeight.setError(getString(R.string.value_required))
                return@setOnClickListener //terminate program execution here
            }

            var weight = editTextWeight.text.toString().toFloat()
            var height = editTextHeight.text.toString().toFloat()
            var bmi = weight / (height/100).pow(2)

            if(bmi < 18.5){ //Underweight
                imageViewBMI.setImageResource(R.drawable.under)
                //Body Mass Index : 17.52
                textViewBMI.text = String.format("%s : %.2f",getString(R.string.bmi), bmi)
                //Status: Underweight
                textViewStatus.text = String.format("%s : %s",getString(R.string.status),getString(R.string.under))
            } else if(bmi in 18.5..24.9){ //Normal
                imageViewBMI.setImageResource(R.drawable.normal)
                textViewBMI.text = String.format("%s : %.2f",getString(R.string.bmi), bmi)
                //Status: Normal
                textViewStatus.text = String.format("%s : %s",getString(R.string.status),getString(R.string.normal))

            } else if(bmi >= 25){ //Overweight
                imageViewBMI.setImageResource(R.drawable.over)
                textViewBMI.text = String.format("%s : %.2f",getString(R.string.bmi), bmi)
                //Status: Overweight
                textViewStatus.text = String.format("%s : %s",getString(R.string.status),getString(R.string.over))
            }

            //Store each value in local storage
            sharedPreferences.edit().putFloat("Weight", weight).apply()
            sharedPreferences.edit().putFloat("Height", height).apply()
            sharedPreferences.edit().putFloat("BMI", bmi).apply()
            //Store image in local storage
            var spImage = ""
            var spStatus = ""
            if(bmi < 18.5){
                spImage = "R.drawable.under"
                spStatus = "R.string.under"
            } else if(bmi in 18.5..24.9){
                spImage = "R.drawable.normal"
                spStatus = "R.string.normal"
            } else if(bmi >= 25){
                spImage = "R.drawable.over"
                spStatus = "R.string.over"
            }

        }

        buttonReset.setOnClickListener {
            imageViewBMI.setImageResource(R.drawable.empty)
            textViewBMI.text = String.format("%s",getString(R.string.bmi))
            textViewStatus.text = String.format("%s",getString(R.string.status))
            editTextHeight.text.clear()
            editTextWeight.text.clear()
        }

        buttonHistory.setOnClickListener{
            val spBMI = sharedPreferences.getFloat("BMI",0F)

            if(spBMI < 18.5){
                imageViewBMI.setImageResource(R.drawable.under)
                textViewStatus.text = String.format("%s : %s",getString(R.string.status),getString(R.string.under))
            } else if(spBMI in 18.5..24.9){
                imageViewBMI.setImageResource(R.drawable.normal)
                textViewStatus.text = String.format("%s : %s",getString(R.string.status),getString(R.string.normal))
            } else if(spBMI >= 25){
                imageViewBMI.setImageResource(R.drawable.over)
                textViewStatus.text = String.format("%s : %s",getString(R.string.status),getString(R.string.over))
            }

            textViewBMI.text = String.format("%s : %.2f",getString(R.string.bmi),spBMI)
            editTextHeight.setText(sharedPreferences.getFloat("Height", 0F).toString())
            editTextWeight.setText(sharedPreferences.getFloat("Weight", 0F).toString())
        }

    }


}