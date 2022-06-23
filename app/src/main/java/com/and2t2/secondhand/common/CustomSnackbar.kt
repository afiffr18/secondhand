package com.and2t2.secondhand.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.and2t2.secondhand.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar

fun Snackbar.addAction(@LayoutRes aLayoutId: Int, aListener: View.OnClickListener?) : Snackbar {
    // Add button
    val button = LayoutInflater.from(view.context).inflate(aLayoutId, null) as MaterialButton
    view.findViewById<MaterialButton>(com.google.android.material.R.id.snackbar_action).let {
        // Copy layout
        button.layoutParams = it.layoutParams
        (it.parent as? ViewGroup)?.addView(button)
    }
    button.typeface
    button.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_close_button,0)
    button.setOnClickListener {
        this.dismiss()
        aListener?.onClick(it)
    }
    return this
}

fun showSnackbar(context: Context, view: View, text: String, @ColorRes backgroundColor: Int) {
    val cl = view.findViewById<CoordinatorLayout>(R.id.cl)
    val snackBar = Snackbar.make(cl, text, Snackbar.LENGTH_SHORT)
    snackBar.setBackgroundTint(ContextCompat.getColor(context, backgroundColor))
    snackBar.setTextColor(ContextCompat.getColor(context, R.color.neutral01))
    snackBar.addAction(R.layout.snackbar_extra_button) {
        snackBar.dismiss()
    }
    snackBar.show()
}
