/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demo.android.capsules.navigation

import java.util.*
import android.os.Build
import android.util.Log
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import io.karn.notify.Notify
import android.view.MenuItem
import android.content.Intent
import androidx.navigation.ui.*
import android.app.PendingIntent
import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.navigation.NavController
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.navigation.findNavController
import androidx.appcompat.app.AppCompatActivity
import com.example.android.codelabs.navigation.R
import androidx.core.graphics.drawable.IconCompat
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val host: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        // Set up Action Bar
        val navController = host.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBar(navController, appBarConfiguration)


        setupBottomNavMenu(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                Integer.toString(destination.id)
            }

            Toast.makeText(this@MainActivity, "Navigated to $dest",
                    Toast.LENGTH_SHORT).show()
            Log.d("NavigationActivity", "Navigated to $dest")
        }
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }



    private fun setupActionBar(navController: NavController,
                               appBarConfig : AppBarConfiguration) {
        // This allows NavigationUI to decide what label to show in the action bar
        // By using appBarConfig, it will also determine whether to
        // show the up arrow or drawer menu icon
        setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val retValue = super.onCreateOptionsMenu(menu)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        // The NavigationView already has these same navigation items, so we only add
        // navigation items to the menu here if there isn't a NavigationView
        if (navigationView == null) {
            menuInflater.inflate(R.menu.overflow_menu, menu)
            return true
        }
        return retValue
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
        //  the old return statement above
        // Have the NavigationUI look for an action or destination matching the menu
        // item id and navigate there if found.
        // Otherwise, bubble up to the parent.
//        return item.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment))
//                || super.onOptionsItemSelected(item)
    }


    override fun onSupportNavigateUp(): Boolean {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        return findNavController(R.id.my_nav_host_fragment).navigateUp(appBarConfiguration)
    }

    fun notifyDefault(view: View) {
        Notify.with(this)
            .content {
                title = "New dessert menu"
                text = "The Cheesecake Factory has a new dessert for you to try!"
            }
            .stackable {
                key = "test_key"
                summaryContent = "test summary content"
                summaryTitle = { count -> "Summary title" }
                summaryDescription = { count -> count.toString() + " new notifications." }
            }
            .show()
    }

    fun notifyTextList(view: View) {
        Notify.with(this)
            .asTextList {
                lines = Arrays.asList("New! Fresh Strawberry Cheesecake.",
                    "New! Salted Caramel Cheesecake.",
                    "New! OREO Dream Dessert.")
                title = "New menu items!"
                text = lines.size.toString() + " new dessert menu items found."
            }
            .show()

    }

    fun notifyBigText(view: View) {
        Notify.with(this)
            .asBigText {
                title = "Chocolate brownie sundae"
                text = "Try our newest dessert option!"
                expandedText = "Mouthwatering deliciousness."
                bigText = "Our own Fabulous Godiva Chocolate Brownie, Vanilla Ice Cream, Hot Fudge, Whipped Cream and Toasted Almonds.\n" +
                        "\n" +
                        "Come try this delicious new dessert and get two for the price of one!"
            }
            .show()
    }

    fun notifyBigPicture(view: View) {
        Notify.with(this)
            .asBigPicture {
                title = "Chocolate brownie sundae"
                text = "Get a look at this amazing dessert!"
                expandedText = "The delicious brownie sundae now available."
                image = BitmapFactory.decodeResource(this@MainActivity.resources,
                    R.drawable.chocolate_brownie_sundae
                )
            }
            .show()
    }

    fun notifyMessage(view: View) {
        Notify.with(this)
            .asMessage {
                userDisplayName = "Karn"
                conversationTitle = "Sundae chat"
                messages = Arrays.asList(
                    NotificationCompat.MessagingStyle.Message("Are you guys ready to try the Strawberry sundae?",
                        System.currentTimeMillis() - (6 * 60 * 1000), // 6 Mins ago
                        "Karn"),
                    NotificationCompat.MessagingStyle.Message("Yeah! I've heard great things about this place.",
                        System.currentTimeMillis() - (5 * 60 * 1000), // 5 Mins ago
                        "Nitish"),
                    NotificationCompat.MessagingStyle.Message("What time are you getting there Karn?",
                        System.currentTimeMillis() - (1 * 60 * 1000), // 1 Mins ago
                        "Moez")
                )
            }
            .show()
    }

    fun notifyBubble(view: View) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Toast.makeText(this, "Notification Bubbles are only supported on a device running Android Q or later.", Toast.LENGTH_SHORT).show()
            return
        }

        Notify.with(this)
            .content {
                title = "New dessert menu"
                text = "The Cheesecake Factory has a new dessert for you to try!"
            }
            .bubblize {
                // Create bubble intent
                val target = Intent(this@MainActivity, SettingsFragment::class.java)
                val bubbleIntent = PendingIntent.getActivity(this@MainActivity, 0, target, 0 /* flags */)

                bubbleIcon = IconCompat.createWithResource(this@MainActivity,
                    R.drawable.ic_app_icon
                )
                targetActivity = bubbleIntent
                suppressInitialNotification = true
            }
            .show()
    }

    fun notifyIndeterminateProgress(view: View) {

        Notify.with(this)
            .asBigText {
                title = "Uploading files"
                expandedText = "The files are being uploaded!"
                bigText = "Daft Punk - Get Lucky.flac is uploading to server /music/favorites"
            }
            .progress {
                showProgress = true
            }
            .show()
    }

    fun notifyDeterminateProgress(view: View) {

        Notify.with(this)
            .asBigText {
                title = "Bitcoin payment processing"
                expandedText = "Your payment was sent to the Bitcoin network"
                bigText = "Your payment #0489 is being confirmed 2/4"
            }
            .progress {
                showProgress = true
                enablePercentage = true
                progressPercent = 30
            }
            .show()
    }

}
