package com.example.cryptomarket.view.customs

import android.R
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.example.cryptomarket.databinding.DialogLoadingBinding

class LoadingDialog(private val ctx: Context): Dialog(ctx, android.R.style.Theme_Material_Dialog){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (window != null) {
            window!!.setDimAmount(0.1f)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        val binding = DialogLoadingBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
    }
}