package home.product.calculationofductcrossssection

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import home.product.calculationofductcrossssection.databinding.ActivityMainBinding
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val airExchangeRates = listOf(20.0, 30.0, 40.0, 50.0, 60.0) // м³/ч на человека
    private var selectedAirExchangeRate = 30.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Настройка Spinner для выбора нормы воздухообмена
        val adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item,
            airExchangeRates.map { "${it} м³/ч" }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.airExchangeSpinner.adapter = adapter

        binding.airExchangeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedAirExchangeRate = airExchangeRates[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.calculateButton.setOnClickListener {
            if (validateInput()) {
                calculateDuct()
            }
        }
    }

    private fun validateInput(): Boolean {
        val residentsText = binding.residentsEditText.text.toString().trim()
        return if (residentsText.isNotEmpty() && (residentsText.toIntOrNull() ?: 0) > 0) {
            true
        } else {
            Toast.makeText(
                this,
                "Введите корректное количество проживающих (целое положительное число)",
                Toast.LENGTH_SHORT
            ).show()
            false
        }
    }

    private fun calculateDuct() {
        val residents = binding.residentsEditText.text.toString().toInt()
        val airFlow = residents * selectedAirExchangeRate
        val velocity = 3.0 // м/с - нормативная скорость для жилых помещений
        val area = airFlow / (velocity * 3600)
        val diameter = sqrt(4 * area / Math.PI)

        val result = "Результаты расчета:\n" +
                "Количество проживающих: $residents\n" +
                "Нормативная скорость для жилых помещений V :3.0 м/с\n"+
                "Норма воздухообмена: ${selectedAirExchangeRate} м³/ч на человека\n" +
                "Общий расход воздуха Q: ${String.format("%.2f", airFlow)} м³/ч\n" +
                "Площадь сечения S: ${String.format("%.3f", area)} м²\n" +
                "Диаметр воздуховода D: ${String.format("%.2f", diameter * 1000)} мм"+
                "\n"+
                "В расчёте использовались формулы: D = \u221A(4*S/\u03C0)\n"+
                "S = Q/(V*3600)"

        binding.resultTextView.text = result
    }
}
