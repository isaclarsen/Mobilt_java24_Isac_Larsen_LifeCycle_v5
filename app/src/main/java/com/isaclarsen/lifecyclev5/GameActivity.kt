package com.isaclarsen.lifecyclev5

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONObject
import kotlin.random.Random


class GameActivity : AppCompatActivity() {
    //API nyckel hårdkodad för enkelhetensskull
    private val API_KEY = "b1f452f0a2ff47788eedd777cd7d572e";

    private lateinit var gameTitleText: TextView;
    private lateinit var releaseDateText: TextView;
    private lateinit var gameCoverImage: ImageView;
    private lateinit var metaCriticScoreText: TextView;
    private lateinit var fetchGameButton: Button;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)

        //Komponenter
        gameTitleText = findViewById(R.id.gameTitle);
        releaseDateText = findViewById(R.id.gameReleaseDate);
        gameCoverImage = findViewById(R.id.gameCover);
        metaCriticScoreText = findViewById(R.id.metacriticScore);
        fetchGameButton = findViewById(R.id.fetchGameButton);

        fetchRandomGame()

        fetchGameButton.setOnClickListener {
            fetchRandomGame();
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun fetchRandomGame(){
        val randomPage = Random.nextInt(1, 100);
        val url = "https://rawg.io/api/games?key=$API_KEY&page=$randomPage";
        val rq: RequestQueue = Volley.newRequestQueue(this)
        var request = StringRequest(
            Request.Method.GET, url,
            { res ->
                Log.d("RAWG", res.toString());
                val json = JSONObject(res);
                val results = json.getJSONArray("results");

                //Hämta random spel från listan
                val randomIndex = Random.nextInt(results.length());
                val randomGame = results.getJSONObject(randomIndex);

                //Values från spelet
                val gameTitle = randomGame.getString("name");
                val releaseDate = randomGame.optString("released", "Okänt releasedatum");
                val gameCoverUrl = randomGame.optString("background_image", null);
                val metacriticScore = "Metacritic: " + randomGame.getString("metacritic")
 
                gameTitleText.setText(gameTitle);
                releaseDateText.setText(releaseDate);
                metaCriticScoreText.setText(metacriticScore);

                //Använder ett externt dependency "Glide" som laddar in bilden genom länken från API-responsen.
                Glide.with(this)
                    .load(gameCoverUrl)
                    .into(gameCoverImage);

                Log.d("RAWG", gameTitle + releaseDate)
            },
            { err -> Log.e("RAWG", err.toString()) }
        )
        rq.add(request)
    }

}