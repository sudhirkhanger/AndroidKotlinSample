package com.sudhirkhanger.appcompatchip

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

// https://stackoverflow.com/a/55722234/3034693
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chipGrp: ChipGroup = findViewById(R.id.chipGrp)

        val chipArray = arrayListOf(
            "Physics",
            "Political Science",
            "Genetics of Venus Trap",
            "Venus",
            "Trap",
            "Physics",
            "Political Science",
            "Genetics of Venus Trap",
            "Venus",
            "Trap",
            "Physics",
            "Political Science",
            "Genetics of Venus Trap",
            "Venus",
            "Trap"
        )

        for (a in 1..12) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_chip, chipGrp, false)
            val chip = view.findViewById(R.id.chips_item_filter) as Chip
            chip.apply {
                text = chipArray[a]
                isClickable = true
            }
            chipGrp.addView(chip)
        }
    }
}

