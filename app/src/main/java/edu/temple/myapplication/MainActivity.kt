package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var binder: TimerService.TimerBinder? = null

    private val timerTextView: TextView by lazy {
        findViewById(R.id.timerTextView)
    }

    val timerHandler = Handler(Looper.getMainLooper()){
        timerTextView.text = it.what.toString()
        true
    }

    private val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            binder = service as TimerService.TimerBinder
            binder?.setHandler(timerHandler)




        }



        override fun onServiceDisconnected(name: ComponentName?) {
            binder = null
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )

        findViewById<Button>(R.id.startButton).setOnClickListener {
            binder?.start(100)
        }

        findViewById<Button>(R.id.pauseButton).setOnClickListener {
            binder?.pause()
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            binder?.stop()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mymenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.startMenu -> {
                binder?.start(100)
                true
            }
            R.id.pauseMenu -> {
                binder?.pause()
                true
            }
            R.id.stopMenu -> {
                binder?.stop()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}