package com.example.phonestore.view

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.phonestore.Extension.gone
import com.example.phonestore.Extension.visible
import com.example.phonestore.R
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivityMainBinding
import com.example.phonestore.services.BadgeDrawable
import com.example.phonestore.viewmodel.CartViewModel

class MainActivity : BaseActivity() {
    companion object{
        var icon: LayerDrawable? = null
        fun intentFor(context: Context): Intent =
                Intent(context, MainActivity::class.java)
        fun setBadgeCount(context: Context, icon: LayerDrawable, count: String){
            val badge: BadgeDrawable
            val reuse = icon.findDrawableByLayerId(R.id.ic_badge)
            badge = if (reuse != null && reuse is BadgeDrawable) {
                reuse as BadgeDrawable
            } else {
                BadgeDrawable(context)
            }
            badge.setCount(count)
            icon.mutate()
            icon.setDrawableByLayerId(R.id.ic_badge, badge)
        }
    }
    private lateinit var bindingMain: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var cartViewModel: CartViewModel
    override fun setBinding() {
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)
    }

    override fun setViewModel() {
        cartViewModel = ViewModelProvider(this@MainActivity).get(CartViewModel::class.java)
        val totalProductObserver = Observer<Int?>{
            icon?.let { it1 -> setBadgeCount(this, icon = it1, it.toString()) }
        }
        cartViewModel.totalProduct.observe(this@MainActivity, totalProductObserver)
    }

    override fun setUI() {
        cartViewModel.getTotalProduct()
    }
    override fun setToolBar() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentMain) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration.Builder(R.id.fragmentHome, R.id.fragmentSuccessOrder)
                .build()
        setSupportActionBar(bindingMain.toolbarMain.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        bindingMain.bottomNavigationView.setupWithNavController(navController)
        visibilityNavElements(navController)
    }
    override fun onSupportNavigateUp(): Boolean { //Setup appBarConfiguration có mũi tên back
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    private fun visibilityNavElements(navController: NavController){
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.fragmentDetailProduct -> {
                    hideBottomNavigation()
                }
                R.id.fragmentDetailCart -> {
                    hideBottomNavigation()
                }
                R.id.fragmentOrder -> {
                    hideBottomNavigation()
                }
                R.id.fragmentSuccessOrder -> {
                    hideBottomNavigation()
                }
                else-> showBottomNavigation()
            }
        }
    }
    fun setToolBarColor(colorID: Int){
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, colorID)))
    }
    private fun hideBottomNavigation(){
        bindingMain.bottomNavigationView.gone()
    }
    private fun showBottomNavigation(){
        bindingMain.bottomNavigationView.visible()
        setUpNavigation()
    }
    private fun setUpNavigation(){
        bindingMain.bottomNavigationView.setupWithNavController(navController)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val itemCart = menu?.findItem(R.id.fragmentDetailCart)
        icon = itemCart?.icon as LayerDrawable
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }



}