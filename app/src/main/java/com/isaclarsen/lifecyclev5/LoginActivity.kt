package com.isaclarsen.lifecyclev5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.edit

class LoginActivity : AppCompatActivity() {
    val username = "isac";
    val password = "kotlinFun";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if (getSharedPreferences("auth", MODE_PRIVATE).getBoolean("autoLogin", false)) {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
            return
        }
        //Komponenter
        val logInButton = findViewById<Button>(R.id.button);
        val loginStatusText = findViewById<TextView>(R.id.loginStatusText);
        val usernameEditField = findViewById<EditText>(R.id.usernameTextField);
        val passwordEditField = findViewById<EditText>(R.id.passwordTextField);


        logInButton.setOnClickListener {
            val usernameInput = usernameEditField.getText().toString();
            val passwordInput = passwordEditField.getText().toString();

            Log.i("Login", "Username: $usernameInput\nPassword: $passwordInput")

            if (usernameInput == username && passwordInput == password) {
                getSharedPreferences("auth", MODE_PRIVATE)
                    .edit { putBoolean("autoLogin", true) }
                //Ändra activity här
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent);
            } else {
                loginStatusText.setText("Fel användarnamn eller lösenord")
                Log.i("Login", "Fel credentials :(")
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->

            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}