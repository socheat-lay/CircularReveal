package com.socheat.circularrevealactivity

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import com.socheat.circularrevealactivity.databinding.ActivityNextBinding
import kotlin.math.hypot


class NextActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNextBinding
    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        binding = ActivityNextBinding.inflate(layoutInflater)
        root = binding.root
        setContentView(binding.root)

        if (savedInstanceState == null) {
            root.isVisible = true
            val viewTreeObserver = root.viewTreeObserver

            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        circularRevealActivity()
                        root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
            }
        }
    }

    private fun circularRevealActivity() {
        val cx = root.right - getDips(44)
        val cy = root.bottom - getDips(44)
        val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
        val circularReveal = ViewAnimationUtils.createCircularReveal(
            root,
            cx,
            cy,
            0f,
            finalRadius
        )
        circularReveal.duration = 3000
        root.visibility = View.VISIBLE
        circularReveal.start()
    }

    private fun getDips(dps: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dps.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    override fun onBackPressed() {
        val cx = root.width - getDips(44)
        val cy = root.bottom - getDips(44)

        val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
        val circularReveal =
            ViewAnimationUtils.createCircularReveal(root, cx, cy, finalRadius, 0f)

        circularReveal.doOnEnd {
            root.visibility = View.INVISIBLE
            finish()
        }
        circularReveal.duration = 3000
        circularReveal.start()
    }
}