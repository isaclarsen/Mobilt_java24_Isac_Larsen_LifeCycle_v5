package com.isaclarsen.lifecyclev5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.core.content.edit

class ProfileActivity : AppCompatActivity() {
    var email = "";
    var age = 0;
    var country = "";
    var gender = "";
    var hasLicense = false;
    var notifications = false;

    private lateinit var emailInput: EditText;
    private lateinit var ageInput: EditText;
    private lateinit var countrySpinner: Spinner;
    private lateinit var genderRadioGroup: RadioGroup;
    private lateinit var licenseCheckBox: CheckBox;
    private lateinit var notificationsSwitch: Switch;
    private lateinit var submitInfoButton: Button;
    private lateinit var toolbar: MaterialToolbar;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        //Komponenter
        emailInput = findViewById(R.id.emailTextField);
        ageInput = findViewById(R.id.ageTextField);
        countrySpinner = findViewById(R.id.country);
        genderRadioGroup = findViewById(R.id.genderGroup);
        licenseCheckBox = findViewById(R.id.licenseTrue);
        notificationsSwitch = findViewById(R.id.notificationsSwitch);
        submitInfoButton = findViewById(R.id.submitInfoButton);
        toolbar = findViewById(R.id.menu)

        loadProfileIntoUi()
        setSupportActionBar(toolbar)

        submitInfoButton.setOnClickListener {
            email = emailInput.getText().toString();
            age = ageInput.text.toString().toInt();
            country = countrySpinner.selectedItem.toString();

            if (licenseCheckBox.isChecked) {
                hasLicense = true;
            } else {
                hasLicense = false;
            }

            if (notificationsSwitch.isChecked) {
                notifications = true;
            } else {
                notifications = false;
            }

            gender = when (genderRadioGroup.checkedRadioButtonId) {
                R.id.genderMale -> "male"
                R.id.genderFemale -> "female"
                else -> ""
            }

            saveProfile()
            showSavedProfileDialog()

            Toast.makeText(baseContext, "Profil sparad", Toast.LENGTH_LONG).show()
            Log.i(
                "Login",
                "Email: $email\n Age: $age\n Country: $country\n Gender: $gender\n License: $hasLicense\n Notifications: $notifications"
            )


        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onPause() {
        super.onPause()
        email = emailInput.text.toString()
        age = ageInput.text.toString().toIntOrNull() ?: 0
        country = countrySpinner.selectedItem?.toString().orEmpty()
        hasLicense = licenseCheckBox.isChecked
        notifications = notificationsSwitch.isChecked
        gender = when (genderRadioGroup.checkedRadioButtonId) {
            R.id.genderMale -> "male"
            R.id.genderFemale -> "female"
            else -> ""
        }
        saveProfile()
    }

    private val prefs by lazy { getSharedPreferences("profile", MODE_PRIVATE) }

    private fun saveProfile() {
        prefs.edit()
            .putString("email", email)
            .putInt("age", age)
            .putString("country", country)
            .putString("gender", gender)
            .putBoolean("license", hasLicense)
            .putBoolean("notifications", notifications)
            .apply()
    }

    private fun showSavedProfileDialog() {
        val p = prefs;
        val msg = """
                E-post: ${p.getString("email", "")}
                Ålder: ${p.getInt("age", 0)}
                Land:  ${p.getString("country", "")}
                Kön:   ${p.getString("gender", "")}
                Körkort: ${if (p.getBoolean("license", false)) "Ja" else "Nej"}
                Notiser: ${if (p.getBoolean("notifications", false)) "På" else "Av"}
                """.trimIndent();

        MaterialAlertDialogBuilder(this)
            .setTitle("Sparad profil")
            .setMessage(msg)
            .setPositiveButton("OK", null)
            .show();
    }

    private fun loadProfileIntoUi() {
        emailInput.setText(prefs.getString("email", ""))

        val savedAge = prefs.getInt("age", 0)
        ageInput.setText(if (savedAge != 0) savedAge.toString() else "")

        val countries = resources.getStringArray(R.array.countries)
        val savedCountry = prefs.getString("country", "") ?: ""
        countries.indexOf(savedCountry).takeIf { it >= 0 }?.let { countrySpinner.setSelection(it) }

        when (prefs.getString("gender", "")) {
            "male" -> genderRadioGroup.check(R.id.genderMale)
            "female" -> genderRadioGroup.check(R.id.genderFemale)
        }
        licenseCheckBox.isChecked = prefs.getBoolean("license", false)
        notificationsSwitch.isChecked = prefs.getBoolean("notifications", false)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_profile -> {
            startActivity(Intent(this, ProfileActivity::class.java)); true
        }

        R.id.menu_savedProfile -> {
            showSavedProfileDialog(); true
        }

        R.id.menu_logout -> {
            getSharedPreferences("auth", MODE_PRIVATE).edit {
                putBoolean("autoLogin", false)
            }
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            startActivity(Intent(this, LoginActivity::class.java)); finish(); true
        }

        else -> super.onOptionsItemSelected(item)
    }


}