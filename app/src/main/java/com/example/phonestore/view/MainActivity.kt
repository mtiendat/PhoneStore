package com.example.phonestore.view

import android.content.Context
import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.phonestore.R
import com.example.phonestore.base.BaseActivity
import com.example.phonestore.databinding.ActivityMainBinding
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.services.BadgeDrawable
import com.example.phonestore.services.Constant
import com.example.phonestore.viewmodel.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPaySDK
import java.lang.ref.WeakReference


class MainActivity : BaseActivity() {
    companion object{
        var icon: LayerDrawable? = null
        var searchView: WeakReference<SearchView>? = null
        var itemCart: MenuItem? = null
        var itemSearch: MenuItem? = null
        var bottomNav: BottomNavigationView? = null
        fun intentFor(context: Context): Intent =
                Intent(context, MainActivity::class.java)
        fun setBadgeCount(context: Context, icon: LayerDrawable, count: String){
            val badge: BadgeDrawable
            val reuse = icon.findDrawableByLayerId(R.id.ic_badge)
            badge = if (reuse != null && reuse is BadgeDrawable) {
                reuse
            } else {
                BadgeDrawable(context)
            }
            badge.setCount(count)
            icon.mutate()
            icon.setDrawableByLayerId(R.id.ic_badge, badge)
        }
    }
    private var bindingMain: ActivityMainBinding? = null
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var cartViewModel: CartViewModel? = null
    private var badgeNotification: com.google.android.material.badge.BadgeDrawable? = null
    override fun setBinding() {
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain?.root)
    }

    override fun setViewModel() {
        cartViewModel = ViewModelProvider(this@MainActivity).get(CartViewModel::class.java)
        cartViewModel?.totalProduct?.observe(this@MainActivity, {
                icon?.let {
                        it1 -> setBadgeCount(this, icon = it1, it.toString())
                }
        })

        cartViewModel?.totalNotification?.observe(this@MainActivity, {
            if(it>0) {
                badgeNotification = bottomNav!!.getOrCreateBadge(R.id.fragmentNotification)
                badgeNotification?.isVisible = true
                badgeNotification?.number = it
            }
        })

    }

    override fun setToolBar() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentMain) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration.Builder(
                R.id.fragmentHome,
                R.id.fragmentNotification,
                R.id.fragmentMe
        )
                .build()
        setSupportActionBar(bindingMain?.toolbarMain?.toolbar)
        navController.addOnDestinationChangedListener { _, _, _ ->
            bindingMain?.toolbarMain?.appBarLayout?.setExpanded(true, true)
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNav = bindingMain?.bottomNavigationView

        bindingMain?.bottomNavigationView?.setupWithNavController(navController)
        visibilityNavElements(navController)
    }

    override fun setUI() {
        cartViewModel?.getTotalNotification()
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
                    showIconCart()
                }
                R.id.fragmentDetailCart -> {
                    hideBottomNavigation()
                    hideIconSearch()
                    hideIconCart()
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
                    hideIconSearch()
                    hideIconCart()
                }
                R.id.fragmentSupplier -> {
                    hideBottomNavigation()
                    showIconSearch()
                    showIconCart()
                }
                R.id.fragmentHelper ->{
                    hideBottomNavigation()
                    showIconCart()
                    showIconSearch()
                }
                R.id.fragmentShippingOption ->{
                    hideBottomNavigation()
                    hideIconCart()
                    hideIconSearch()
                }
                R.id.fragmentPaymentOption ->{
                    hideBottomNavigation()
                    hideIconCart()
                    hideIconSearch()
                }
                R.id.fragmentFollowOrder ->{
                    hideIconCart()
                    hideIconSearch()
                    hideBottomNavigation()
                }
                R.id.fragmentWarranty ->{
                    hideIconCart()
                    hideIconSearch()
                    hideBottomNavigation()
                }
                R.id.fragmentSelectAddress ->{
                    hideIconCart()
                    hideIconSearch()
                    hideBottomNavigation()
                }

                R.id.fragmentDetailTechnology ->{
                    hideIconCart()
                    hideIconSearch()
                    hideBottomNavigation()
                }
                R.id.fragmentAllProduct ->{
                    hideBottomNavigation()
                }
                R.id.fragmentEditAddress ->{
                    hideIconCart()
                    hideIconSearch()
                    hideBottomNavigation()
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
        bindingMain?.bottomNavigationView?.gone()
    }

    private fun showBottomNavigation(){
        bindingMain?.bottomNavigationView?.visible()
        setUpNavigation()
    }
    private fun setUpNavigation(){
        bindingMain?.bottomNavigationView?.setupWithNavController(navController)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        itemSearch = menu?.findItem(R.id.fragmentSearch)
        itemCart = menu?.findItem(R.id.fragmentDetailCart)
        icon = itemCart?.icon as LayerDrawable
        val s = itemSearch?.actionView as SearchView
        s.queryHint ="Bạn cần tìm gì ?"
        searchView = WeakReference(s)

        val clearButton =s.findViewById(R.id.search_close_btn) as ImageView
        clearButton.setImageResource(R.drawable.ic_clear)
        return true

    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        cartViewModel?.getTotalProduct()
        return super.onPrepareOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }



}