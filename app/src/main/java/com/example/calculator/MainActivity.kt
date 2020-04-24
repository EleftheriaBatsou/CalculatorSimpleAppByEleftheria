package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import java.lang.NumberFormatException

import kotlinx.android.synthetic.main.activity_main.*

private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"
private const val STATE_OPERAND1_STORED = "Operand1_store"

class MainActivity : AppCompatActivity() {
    // Variables to hold the operations and type of calculations
    private var operand1: Double? = null
    private var pendingoperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
        }

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }

            //val value = newNumber.text.toString()
            pendingoperation = op
            operation.text = pendingoperation
        }

        buttonEquals.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonMultiple.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)

        buttonNeg.setOnClickListener({ view ->
            val value = newNumber.text.toString()
            if (value.isEmpty()) {
                newNumber.setText("-")
            } else {
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    newNumber.setText(doubleValue.toString())
                } catch (e: NumberFormatException) {
                    newNumber.setText("")
                }
            }
        })
    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value
        } else {
            if (pendingoperation == "=") {
                pendingoperation = operation
            }

            when (pendingoperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (operand1 != null) {
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND1, true)
        }
        outState.putString(STATE_PENDING_OPERATION, pendingoperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.getBoolean(STATE_OPERAND1, false)) {
            operand1 = savedInstanceState.getDouble(STATE_OPERAND1)
        } else {
            operand1 = null
        }
        
        pendingoperation = savedInstanceState.getString(STATE_PENDING_OPERATION)!!
        operation.text = pendingoperation
    }
}
