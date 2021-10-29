package com.example.phonestore.view

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentHomeBinding
import com.example.phonestore.model.*
import com.example.phonestore.services.Constant
import com.example.phonestore.services.adapter.FeaturedProductAdapter
import com.example.phonestore.services.adapter.ProductAdapter
import com.example.phonestore.services.adapter.SlideshowAdapter
import com.example.phonestore.viewmodel.ProductViewModel
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable
import java.util.*


class FragmentHome : BaseFragment(){
    private lateinit var bindingHome: FragmentHomeBinding
    private var sliderAdapter : SlideshowAdapter? = null
    private var homeViewModel: ProductViewModel? = null
    private var hotSaleAdapter: ProductAdapter<ProductInfo>? = null
    private var supplierAdapter: ProductAdapter<Supplier>? = null
    private var featuredProductAdapter: FeaturedProductAdapter? = null
    private var listSlideshow: ArrayList<Slideshow> = arrayListOf()
    private var slideHandler: Handler = Handler(Looper.getMainLooper())
    private var listProduct: ArrayList<ProductInfo?>? = arrayListOf()
    private var listSupplier: ArrayList<Supplier?>? = arrayListOf()
    private var listFeaturedProduct: ArrayList<ProductInfo?>? = arrayListOf()
    private var sizeSlider: Int = 0
    private var savePage: Int = 2
    private var flag = 0
    private var idSupplier: Int? = null
    private var flagSupplier = 0
    private var slideRunnable = Runnable {
        bindingHome.vpSlideShow.currentItem = bindingHome.vpSlideShow.currentItem.plus(1) ?:0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher
            ?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Toast.makeText(context, R.string.back_exit, Toast.LENGTH_SHORT).show()
                    if(!isEnabled){
                        activity?.onBackPressed()
                    }
                    isEnabled = false
                    Handler(Looper.getMainLooper()).postDelayed({
                        isEnabled = true
                    }, 2000)
                }
            }
            )
    }
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingHome = FragmentHomeBinding.inflate(inflater, container, false)
        return bindingHome.root

    }

    override fun setUI(){
        bindingHome.pbRecommend.setIndeterminateDrawableTiled(
                FoldingCirclesDrawable.Builder(context).colors(resources.getIntArray(R.array.google_colors)).build()
        ) //set progressBar google
        bindingHome.swipe.setColorSchemeColors(Color.BLUE)
        getData()
        bindingHome.rvHotSale.visible()
        init()
        swipeRefresh()
        setOnClickListener()
        flag++ //Đặt cờ, Để không load lại khi quay lại fragment
    }
    override fun setViewModel(){
        homeViewModel = ViewModelProvider(this@FragmentHome).get(ProductViewModel::class.java)

    }
    override fun setObserve() {
        homeViewModel?.listSlideshow?.observe(requireActivity(), {
            if(listSlideshow.size==0) {
                if (it != null) {
                    listSlideshow.addAll(it)
                }
                initSlider()
                setUpIndicator()
                setCurrentIndicator(0)
                bindingHome.shimmerSlideShow.stopShimmer()
                bindingHome.shimmerSlideShow.gone()
            }
        })

        homeViewModel?.listProduct?.observe(requireActivity(), {
            hotSaleAdapter?.setItems(it)
            hotSaleAdapter?.notifyDataSetChanged()
            bindingHome.shimmerLayoutHotSale.stopShimmer()
            bindingHome.shimmerLayoutHotSale.gone()
            bindingHome.rvHotSale.visible()
        })

        homeViewModel?.listSupplier?.observe(viewLifecycleOwner, {
            supplierAdapter?.setItems(it)
            bindingHome.rvLogoSupplier.startAutoScroll()
            bindingHome.rvLogoSupplier.isLoopEnabled = true
            bindingHome.rvLogoSupplier.setCanTouch(true)
        })

        homeViewModel?.listFeaturedProduct?.observe(viewLifecycleOwner, {
            if (it != null) {
                featuredProductAdapter?.setItems(it)
                featuredProductAdapter?.notifyDataSetChanged()
                bindingHome.shimmerLayoutFeaturedProduct.stopShimmer()
                bindingHome.shimmerLayoutFeaturedProduct.gone()
                bindingHome.rvRecommend.visible()
            }

        })
    }
    private fun setOnClickListener(){
        bindingHome.btnSort.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentHome_to_fragmentAllProduct)
            //bindingHome.btnSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up, 0)
        }
        bindingHome.tvRecommended.setOnClickListener {
            listFeaturedProduct?.clear()
            featuredProductAdapter?.notifyDataSetChanged()
            bindingHome.shimmerLayoutFeaturedProduct.visible()
            bindingHome.shimmerLayoutFeaturedProduct.startShimmer()
            idSupplier = null
            flagSupplier = 0
            Handler(Looper.getMainLooper()).postDelayed({
                homeViewModel?.getFeaturedProduct()
            }, 500)

        }
    }
    private fun init(){
        Glide.with(this)
            .load(Constant.URL_ROOT+"images/banner/big-banner.png")
            .error(R.drawable.noimage)
            .into(bindingHome.ivBanner)
        initRecyclerViewLogo()
        initRecyclerViewHotSale()
        initRecyclerViewCateProduct()
    }
    private fun swipeRefresh(){
        bindingHome.swipe.setOnRefreshListener {
            savePage = 2
            viewModelStore.clear()
            init()
            bindingHome.shimmerLayoutHotSale.visible()
            bindingHome.shimmerLayoutFeaturedProduct.visible()
            bindingHome.rvHotSale.gone()
            bindingHome.rvRecommend.gone()
            setViewModel()
            setObserve()
            getData()
            bindingHome.swipe.isRefreshing = false
            (activity as MainActivity).cartViewModel?.getTotalProduct()
        }
    }
    private fun getData(){
        bindingHome.shimmerSlideShow.startShimmer()
        bindingHome.shimmerLayoutHotSale.startShimmer()
        bindingHome.shimmerLayoutFeaturedProduct.startShimmer()
        homeViewModel?.getSlideShow()
        homeViewModel?.getListHotSaleProduct()
        homeViewModel?.getFeaturedProduct()
        homeViewModel?.getListSupplier()
    }
    private fun initRecyclerViewHotSale(){
        hotSaleAdapter = ProductAdapter(listProduct)
        bindingHome.rvHotSale.adapter = hotSaleAdapter
        bindingHome.rvHotSale.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
        )
    }
    private fun initRecyclerViewLogo(){
        supplierAdapter = ProductAdapter(listSupplier)
        bindingHome.rvLogoSupplier.adapter = supplierAdapter
        bindingHome.rvLogoSupplier.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
        )
    }
    private fun initRecyclerViewCateProduct(){
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(
                2,
                LinearLayoutManager.VERTICAL
        )
        featuredProductAdapter = FeaturedProductAdapter(listFeaturedProduct)
        bindingHome.rvRecommend.adapter = featuredProductAdapter
        bindingHome.rvRecommend.layoutManager =  staggeredGridLayoutManager
        bindingHome.rvRecommend.isNestedScrollingEnabled = false //set rv không cuộn trong NestedScrollView
    }
    private fun initSlider(){
        sizeSlider = listSlideshow.size
        sliderAdapter = SlideshowAdapter(listSlideshow, bindingHome.vpSlideShow)
        bindingHome.vpSlideShow.adapter = sliderAdapter
        bindingHome.vpSlideShow.clipToPadding = false
        bindingHome.vpSlideShow.clipChildren = false
        bindingHome.vpSlideShow.offscreenPageLimit = 3
        bindingHome.vpSlideShow.getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        bindingHome.vpSlideShow.setPageTransformer(compositePageTransformer)
        bindingHome.vpSlideShow.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                slideHandler.removeCallbacks(slideRunnable)
                slideHandler.postDelayed(slideRunnable, 3000) //next slide 3 giây
                if (position >= sizeSlider) {
                    setCurrentIndicator(position % sizeSlider) // set vị trí chuyển indicator active sang inactive
                } else setCurrentIndicator(position)
            }
        })
    }
    private fun setUpIndicator(){
        val indicator = sliderAdapter?.itemCount?.let { arrayOfNulls<ImageView>(it) }
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                WRAP_CONTENT,
                WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        activity?.applicationContext?.let {
            for(i in indicator?.indices!!){
                indicator[i] = ImageView(it)
                indicator[i].apply {
                    this?.setImageDrawable(
                            activity?.let {
                                ContextCompat.getDrawable(
                                        it.applicationContext,
                                        R.drawable.indicator_inactive
                                )
                            }
                    )
                    this?.layoutParams = layoutParams
                }
                bindingHome.indicatorContainer.addView(indicator[i])
            }
        }

    }
    private fun setCurrentIndicator(index: Int){
        val childCount = bindingHome.indicatorContainer.childCount
        for(i in 0 until childCount){
            val imageView = bindingHome.indicatorContainer.get(i) as ImageView
            if(i==index){
                imageView.setImageDrawable(
                        activity?.applicationContext?.let {
                            ContextCompat.getDrawable(
                                    it.applicationContext,
                                    R.drawable.indicator_active
                            )
                        }
                )
            }else{
                imageView.setImageDrawable(
                        activity?.applicationContext?.let {
                            ContextCompat.getDrawable(
                                    it.applicationContext,
                                    R.drawable.indicator_inactive
                            )
                        }
                )
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        //gán lại rổng, để không bị tăng item khi backPress
        listSlideshow.clear()
        sliderAdapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).cartViewModel?.getTotalProduct()
    }
}

