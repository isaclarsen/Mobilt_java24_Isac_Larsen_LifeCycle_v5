package com.isaclarsen.lifecyclev5

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class DashboardActivity : AppCompatActivity() {
    private lateinit var updateProfileButton: Button;
    private lateinit var gameAPIButton: Button;
    private lateinit var toolbar: MaterialToolbar;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        //Komponenter
        updateProfileButton = findViewById(R.id.updateProfile);
        gameAPIButton = findViewById(R.id.gameAPIButton)
        toolbar = findViewById(R.id.menu)

        setSupportActionBar(toolbar)


        updateProfileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java));
        }

        gameAPIButton.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java));
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_profile -> {
            startActivity(Intent(this, ProfileActivity::class.java)); true
        }

        R.id.menu_logout -> {
            getSharedPreferences("auth", MODE_PRIVATE).edit { putBoolean("autoLogin", false) }
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            true
        }

        else -> super.onOptionsItemSelected(item)
    }
}