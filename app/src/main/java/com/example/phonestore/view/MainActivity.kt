package com.example.phonestore.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.LayerDrawable
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
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
import java.lang.ref.WeakReference
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : BaseActivity() {
    companion object{
        var icon: LayerDrawable? = null
        var searchView: WeakReference<SearchView>? = null
        var itemCart: MenuItem? = null
        var itemSearch: MenuItem? = null
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

    @SuppressLint("PackageManagerGetSignatures")
    override fun setUI() {

        cartViewModel.getTotalProduct()
    }
    override fun setToolBar() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentMain) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.fragmentHome,
            R.id.fragmentSuccessOrder,
            R.id.fragmentMe
        )
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
                    hideIconSearch()
                }
                R.id.fragmentDetailCart -> {
                    hideBottomNavigation()
                    hideIconSearch()
                }
                R.id.fragmentOrder -> {
                    hideBottomNavigation()
                    hideIconCart()
                    hideIconSearch()
                }
                R.id.fragmentSuccessOrder -> {
                    hideBottomNavigation()
                    hideIconCart()
                    hideIconSearch()
                }
                R.id.fragmentSearch -> {
                    hideBottomNavigation()
                    hideIconCart()
                    showIconSearch()
                }
                R.id.fragmentAllFollowOrder -> {
                    hideBottomNavigation()
                    hideIconCart()
                    showIconSearch()
                }
                R.id.fragmentAllVote -> {
                    hideBottomNavigation()
                    showIconSearch()
                }
                R.id.fragmentChangeMyInfo -> {
                    hideBottomNavigation()
                    showIconSearch()
                }
                else-> {
                    showBottomNavigation()
                    showIconCart()
                    showIconSearch()
                }
            }
        }
    }
    private fun showIconCart(){
        itemCart?.isVisible = true
    }
    private fun hideIconCart(){
        itemCart?.isVisible = false
    }
    private fun showIconSearch(){
        itemSearch?.isVisible = true
    }
    private fun hideIconSearch(){
        itemSearch?.isVisible = false
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
        itemSearch = menu?.findItem(R.id.fragmentSearch)
        itemCart = menu?.findItem(R.id.fragmentDetailCart)
        icon = itemCart?.icon as LayerDrawable
        val s = itemSearch?.actionView as SearchView
        val clearButton =s.findViewById(R.id.search_close_btn) as ImageView
        clearButton.setImageResource(R.drawable.ic_clear)
        s.queryHint ="Bạn cần tìm gì ?"
        searchView = WeakReference(s)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

}