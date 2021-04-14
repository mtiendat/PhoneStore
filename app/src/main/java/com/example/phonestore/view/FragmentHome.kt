package com.example.phonestore.view

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.example.phonestore.services.EndlessRecyclerViewScrollListener
import com.example.phonestore.extendsion.gone
import com.example.phonestore.extendsion.visible
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentHomeBinding
import com.example.phonestore.model.*
import com.example.phonestore.services.ProductAdapter
import com.example.phonestore.services.SlideAdapter
import com.example.phonestore.viewmodel.ProductViewModel
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable
import java.util.*
import kotlin.math.abs


class FragmentHome : BaseFragment(){
    private var bindingHome: FragmentHomeBinding? = null
    private var sliderAdapter : SlideAdapter? = null
    private var homeViewModel: ProductViewModel? = null
    private var hotSaleAdapter: ProductAdapter<ProductInfo>? = null
    private var supplierAdapter: ProductAdapter<Supplier>? = null
    private var cateProductAdapter: ProductAdapter<CateProductInfo>? =null
    private var listSlideshow: ArrayList<Slideshow> = arrayListOf()
    private var slideHandler: Handler = Handler(Looper.getMainLooper())
    private var productInfo: ProductInfo = ProductInfo()
    private var listProduct: ArrayList<ProductInfo>? = arrayListOf(productInfo)
    private var listSupplier: ArrayList<Supplier>? = arrayListOf()
    private var listCateProduct: ArrayList<CateProductInfo>? = arrayListOf()
    private var listSaveCateProductPrevious: ArrayList<CateProductInfo>? = arrayListOf()
    private var sizeSlider: Int = 0
    private var savePage: Int = 2
    private var flag = 0
    private var idSupplier: Int? = null
    private var orderBy: Int = 0
    private var slideRunnable = Runnable {
        bindingHome?.vpSlideShow?.currentItem = bindingHome?.vpSlideShow?.currentItem?.plus(1) ?:0
    }

    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingHome = FragmentHomeBinding.inflate(inflater, container, false)
        return bindingHome?.root

    }

    override fun setUI(){
        bindingHome?.pbRecommend?.setIndeterminateDrawableTiled(
                FoldingCirclesDrawable.Builder(context).colors(resources.getIntArray(R.array.google_colors)).build()
        ) //set progressBar google
        bindingHome?.swipe?.setColorSchemeColors(Color.BLUE)
        if(listSaveCateProductPrevious?.size?: 1 > 5) { //Nếu trường hợp đã load more nhiều hơn 1 page rồi, thì mới add list cũ vào
            listSaveCateProductPrevious?.let { listCateProduct?.addAll(it) }
        }
        listSaveCateProductPrevious?.clear()
        if(flag==0) { //Nếu chưa
            getData()
        }
        setImg(R.drawable.fire, bindingHome?.ivHotSale)
        setImg(R.drawable.light,bindingHome?.ivRecommend)
        init()
        swipeRefresh()
        setOnClickListener()
        flag++ //Đặt cờ, Để không load lại khi quay lại fragment
    }
    private fun setImg(i: Int, imageView: ImageView?){
        context?.let {
            if (imageView != null) {
                Glide.with(it)
                        .asGif()
                        .load(i)
                        .into(imageView)
            }
        }
    }
    override fun setViewModel(){
        homeViewModel = ViewModelProvider(this@FragmentHome).get(ProductViewModel::class.java)

    }
    override fun setObserve() {
        val slideShowObserve = Observer<ArrayList<Slideshow>?>{
            if(listSlideshow.size==0) {
                listSlideshow.addAll(it)
                initSlider()
                setUpIndicator()
                setCurrentIndicator(0)
            }
        }
        homeViewModel?.listSlideshow?.observe(requireActivity(), slideShowObserve)
        val hotSaleProductObserve = Observer<ArrayList<ProductInfo>?>{
            listProduct?.addAll(it)
            hotSaleAdapter?.setItems(it)
            bindingHome?.shimmerLayoutHotSale?.stopShimmer()
            bindingHome?.shimmerLayoutHotSale?.gone()
        }
        homeViewModel?.listProduct?.observe(requireActivity(), hotSaleProductObserve)
        val logoSupplierObserver = Observer<ArrayList<Supplier>?>{
            supplierAdapter?.setItems(it)
        }
        homeViewModel?.listSupplier?.observe(viewLifecycleOwner, logoSupplierObserver)
        val cateListProduct = Observer<ArrayList<CateProductInfo>?> {
            listCateProduct?.addAll(it)
            cateProductAdapter?.notifyDataSetChanged()
            bindingHome?.shimmerLayoutRecommend?.stopShimmer()
            bindingHome?.shimmerLayoutRecommend?.gone()

        }
        homeViewModel?.listCateProduct?.observe(viewLifecycleOwner, cateListProduct)
    }
    private fun setOnClickListener(){
        bindingHome?.ivDiscount?.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentHome_to_fragmentDiscount)
        }
        bindingHome?.ivListSupplier?.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentHome_to_fragmentListSupplier)
        }
        bindingHome?.btnSortPrice?.setOnClickListener {
            bindingHome?.pbRecommend?.visible()
            if(orderBy ==0) {
                listCateProduct?.sortWith { o1, o2 ->
                    o1.priceNew - o2.priceNew //Tăng dần
                }
                cateProductAdapter?.notifyDataSetChanged()
                bindingHome?.pbRecommend?.gone()
                orderBy = 1
                bindingHome?.btnSortPrice?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0)
                return@setOnClickListener
            }else if(orderBy==1)
                listCateProduct?.sortWith { o1, o2 ->
                    o2.priceNew - o1.priceNew //Giảm dần
                }
            cateProductAdapter?.notifyDataSetChanged()
            bindingHome?.pbRecommend?.gone()
            orderBy = 0
            bindingHome?.btnSortPrice?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up, 0)
        }
        bindingHome?.tvRecommended?.setOnClickListener {
            listCateProduct?.clear()
            cateProductAdapter?.notifyDataSetChanged()
            bindingHome?.shimmerLayoutRecommend?.visible()
            bindingHome?.shimmerLayoutRecommend?.startShimmer()
            Handler(Looper.getMainLooper()).postDelayed({
                homeViewModel?.getListCateProduct(1)
            }, 2000)

        }
    }
    private fun init(){
        initRecyclerViewLogo()
        initRecyclerViewHotSale()
        initRecyclerViewCateProduct()
    }
    private fun swipeRefresh(){
        bindingHome?.swipe?.setOnRefreshListener {
            savePage = 2
            idSupplier = null
            viewModelStore.clear()
            clearList()
            init()
            bindingHome?.shimmerLayoutHotSale?.visible()
            bindingHome?.shimmerLayoutRecommend?.visible()
            setViewModel()
            setObserve()
            getData()
            bindingHome?.swipe?.isRefreshing = false

        }
    }
    private fun getData(){
        homeViewModel?.getSlideShow()
        bindingHome?.shimmerLayoutHotSale?.startShimmer()
        bindingHome?.shimmerLayoutRecommend?.startShimmer()
        Handler(Looper.getMainLooper()).postDelayed({
            homeViewModel?.getListHotSaleProduct()
        }, 2000)
        homeViewModel?.getListSupplier()
        Handler(Looper.getMainLooper()).postDelayed({
            homeViewModel?.getListCateProduct(1)
        }, 2000)

    }
    private fun initRecyclerViewHotSale(){
        hotSaleAdapter = ProductAdapter(listProduct)
        bindingHome?.rvHotSale?.adapter = hotSaleAdapter
        bindingHome?.rvHotSale?.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
        )
    }
    private fun initRecyclerViewLogo(){
        supplierAdapter = ProductAdapter(listSupplier)
        supplierAdapter?.onItemSupplierClick = {
            idSupplier = it
            savePage = 2
            listCateProduct?.clear()
            cateProductAdapter?.notifyDataSetChanged()
            bindingHome?.shimmerLayoutRecommend?.visible()
            bindingHome?.shimmerLayoutRecommend?.startShimmer()
            homeViewModel?.getListCateProduct(1, idSupplier = it)
        }
        bindingHome?.rvLogoSupplier?.adapter = supplierAdapter
        bindingHome?.rvLogoSupplier?.layoutManager = LinearLayoutManager(
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
        cateProductAdapter = ProductAdapter(listCateProduct)
        bindingHome?.rvRecommend?.adapter = cateProductAdapter
        bindingHome?.rvRecommend?.layoutManager =  staggeredGridLayoutManager
        bindingHome?.nsvDetail?.setOnScrollChangeListener(object : EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (page == 2) {
                    //Nếu back lại fragment, thì page khi scroll quay lại = 2, để giữ lại page trước đó
                    homeViewModel?.getMoreListCateProduct(savePage, idSupplier)
                } else {
                    savePage = page // lưu lại page hiện tại
                    homeViewModel?.getMoreListCateProduct(savePage, idSupplier)
                }
            }

        })
        bindingHome?.rvRecommend?.isNestedScrollingEnabled = false //set rv không cuộn trong NestedScrollView
    }
    private fun initSlider(){
        sizeSlider = listSlideshow.size
        sliderAdapter = SlideAdapter(listSlideshow, bindingHome?.vpSlideShow)
        bindingHome?.vpSlideShow?.adapter = sliderAdapter
        bindingHome?.vpSlideShow?.clipToPadding = false
        bindingHome?.vpSlideShow?.clipChildren = false
        bindingHome?.vpSlideShow?.offscreenPageLimit = 3
        bindingHome?.vpSlideShow?.getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 1.3f + r * 0.2f
            page.scaleX = 1.2f + r * 0.1f

        }
        bindingHome?.vpSlideShow?.setPageTransformer(compositePageTransformer)
        bindingHome?.vpSlideShow?.registerOnPageChangeCallback(object :
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
        for(i in indicator?.indices!!){
            indicator[i] = ImageView(activity?.applicationContext)
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
            bindingHome?.indicatorContainer?.addView(indicator[i])
        }

    }
    private fun setCurrentIndicator(index: Int){
        val childCount = bindingHome?.indicatorContainer?.childCount
        for(i in 0 until childCount!!){
            val imageView = bindingHome?.indicatorContainer?.get(i) as ImageView
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
    private fun clearList(){
        listCateProduct?.clear()
        listProduct?.clear()
        listSupplier?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //gán lại rổng, để không bị tăng item khi backPress
        listCateProduct?.let { listSaveCateProductPrevious?.addAll(it) }
        clearList()
        listSlideshow.clear()
        sliderAdapter?.notifyDataSetChanged()
    }
}

