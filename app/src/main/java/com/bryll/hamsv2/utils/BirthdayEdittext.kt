package com.bryll.hamsv2.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class BirthdayEdittext(context: Context, attrs: AttributeSet)  :  AppCompatEditText(context, attrs){

    private var isFormatting = false

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable) {
                if (isFormatting) return

                isFormatting = true

                if (editable.length == 2) {
                    val month = editable.toString()
                    if (month.toInt() > 12) {
                        editable.replace(0, 2, "12")
                    }
                } else if (editable.length == 5) {
                    val day = editable.substring(3, 5)
                    if (day.toInt() > 31) {
                        editable.replace(3, 5, "31")
                    }
                } else if (editable.length == 10) {
                    val year = editable.substring(6, 10)
                    if (year.toInt() < 1900) {
                        editable.replace(6, 10, "1900")
                    }
                }

                // Apply the "MM/DD/YYYY" format
                if (editable.length == 2 || editable.length == 5) {
                    editable.append("/")
                } else if (editable.length == 11) {
                    editable.replace(10, 11, "")
                }

                isFormatting = false
            }
        })
    }
}