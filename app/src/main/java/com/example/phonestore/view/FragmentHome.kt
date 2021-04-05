package com.example.phonestore.view

import android.content.Context
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
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.news.services.EndlessRecyclerViewScrollListener
import com.example.phonestore.Extension.gone
import com.example.phonestore.Extension.visible
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentHomeBinding
import com.example.phonestore.model.CateProductInfo
import com.example.phonestore.model.ProductInfo
import com.example.phonestore.model.SliderItem
import com.example.phonestore.model.Supplier
import com.example.phonestore.services.ProductAdapter
import com.example.phonestore.services.SlideAdapter
import com.example.phonestore.viewmodel.ProductViewModel
import kotlin.math.abs


class FragmentHome : BaseFragment(){
    private lateinit var bindingHome: FragmentHomeBinding
    private lateinit var sliderAdapter : SlideAdapter
    private lateinit var homeViewModel: ProductViewModel
    private var hotSaleAdapter: ProductAdapter<ProductInfo>? = null
    private var supplierAdapter: ProductAdapter<Supplier>? = null
    private var cateProductAdapter: ProductAdapter<CateProductInfo>? =null
    private var sliderItem: ArrayList<SliderItem> = arrayListOf()
    private var slideHandler: Handler = Handler(Looper.getMainLooper())
    private var listProduct: ArrayList<ProductInfo>? = arrayListOf()
    private var listSupplier: ArrayList<Supplier>? = arrayListOf()
    private var listCateProduct: ArrayList<CateProductInfo>? = arrayListOf()
    private var listSaveCateProductPrevious: ArrayList<CateProductInfo>? = arrayListOf()
    private var sizeSlider: Int = 0
    private var savePage: Int = 2
    private var flag = 0
    private var slideRunnable = Runnable {
        bindingHome.vpSlideShow.currentItem = bindingHome.vpSlideShow.currentItem + 1
    }
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        bindingHome = FragmentHomeBinding.inflate(inflater, container, false)
        return bindingHome.root

    }

    override fun setUI(){
        bindingHome.cardViewTop.setBackgroundResource(R.drawable.background_gradient)
        Log.d("listSaveCateProduct", listCateProduct?.size.toString())
        Log.d("listSaveCateProduct", listSaveCateProductPrevious?.size.toString())
        if(listSaveCateProductPrevious?.size?: 1 > 5) { //Nếu trường hợp đã load more nhiều hơn 1 page rồi, thì mới add list cũ vào
            listSaveCateProductPrevious?.let { listCateProduct?.addAll(it) }
        }
        listSaveCateProductPrevious?.clear()
        initSlider()
        setUpIndicator()
        setCurrentIndicator(0)
        init()
        swipeRefresh()
        if(flag==0) {
            getData()
        }
        flag++
//        cateProductAdapter.onItemClick = { id ->
//                            it.findNavController().navigate(R.id.action_fragmentHome_to_fragmentDetailProduct, id)
//
//        }
    }
    private fun init(){
        initRecyclerViewLogo()
        initRecyclerViewHotSale()
        initRecyclerViewCateProduct()
    }
    private fun swipeRefresh(){
        bindingHome.swipe.setOnRefreshListener {
            savePage = 2
            viewModelStore.clear()
            clearList()
            init()
            bindingHome.shimmerLayoutHotSale.visible()
            bindingHome.shimmerLayoutRecommend.visible()
            setViewModel()
            setObserve()
            getData()
            bindingHome.swipe.isRefreshing = false
        }
    }
    private fun getData(){
        bindingHome.shimmerLayoutHotSale.startShimmer()
        bindingHome.shimmerLayoutRecommend.startShimmer()
        Handler(Looper.getMainLooper()).postDelayed({
            homeViewModel.getListHotSaleProduct()
        }, 2000)
        homeViewModel.getListSupplier()
        Handler(Looper.getMainLooper()).postDelayed({
            homeViewModel.getListCateProduct(1)
        }, 2000)
    }
    override fun setViewModel(){
        homeViewModel = ViewModelProvider(this@FragmentHome).get(ProductViewModel::class.java)

    }

    override fun setObserve() {
        val hotSaleProductObserve = Observer<ArrayList<ProductInfo>?>{
            listProduct?.addAll(it)
            hotSaleAdapter?.setItems(it)
            bindingHome.shimmerLayoutHotSale.stopShimmer()
            bindingHome.shimmerLayoutHotSale.gone()
        }
        homeViewModel.listProduct.observe(requireActivity(), hotSaleProductObserve)
        val logoSupplierObserver = Observer<ArrayList<Supplier>?>{
            supplierAdapter?.setItems(it)
        }
        homeViewModel.listSupplier.observe(viewLifecycleOwner, logoSupplierObserver)
        val cateListProduct = Observer<ArrayList<CateProductInfo>?> {
            listCateProduct?.addAll(it)
            cateProductAdapter?.notifyDataSetChanged()
            bindingHome.shimmerLayoutRecommend.stopShimmer()
            bindingHome.shimmerLayoutRecommend.gone()

        }
        homeViewModel.listCateProduct.observe(viewLifecycleOwner, cateListProduct)
    }
    private fun clearList(){
        listCateProduct?.clear()
        listProduct?.clear()
        listSupplier?.clear()
        //sliderItem.clear()

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
        cateProductAdapter = ProductAdapter(listCateProduct)
        bindingHome.rvRecommend.adapter = cateProductAdapter
        bindingHome.rvRecommend.layoutManager =  staggeredGridLayoutManager
        bindingHome.nsvDetail.setOnScrollChangeListener(object : EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (page == 2) {
                    Log.d("SAVEPAGE", savePage.toString()) /////////////////
                    //Nếu back lại fragment, thì page khi scroll quay lại = 2, để giữ lại page trước đó
                    homeViewModel.getMoreListCateProduct(savePage)
                } else {
                    savePage = page // lưu lại page hiện tại
                    homeViewModel.getMoreListCateProduct(savePage)
                }
                Log.d("PAGE", page.toString())
            }

        })
        bindingHome.rvRecommend.isNestedScrollingEnabled = false //set rv không cuộn trong NestedScrollView
    }
    private fun initSlider(){
        sliderItem.add(SliderItem(R.drawable.slide3))
        sliderItem.add(SliderItem(R.drawable.slide2))
        sliderItem.add(SliderItem(R.drawable.slide1))
        sliderItem.add(SliderItem(R.drawable.slide3))
        sizeSlider = sliderItem.size
        sliderAdapter = SlideAdapter(sliderItem, bindingHome.vpSlideShow)
        bindingHome.vpSlideShow.adapter = sliderAdapter
        bindingHome.vpSlideShow.clipToPadding = false
        bindingHome.vpSlideShow.clipChildren = false
        bindingHome.vpSlideShow.offscreenPageLimit = 3
        bindingHome.vpSlideShow.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 1.05f + r * 0.2f
            page.scaleX = 0.85f + r * 0.1f

        }
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
        val indicator = arrayOfNulls<ImageView>(sliderAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                WRAP_CONTENT,
                WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        for(i in indicator.indices){
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
            bindingHome.indicatorContainer.addView(indicator[i])
        }

    }
    private fun setCurrentIndicator(index: Int){
        val childCount = bindingHome.indicatorContainer.childCount
        for(i in 0 until  childCount){
            val imageView = bindingHome.indicatorContainer[i] as ImageView
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

    override fun onPause() {
        super.onPause()
        Log.d("TEST", "onPause")
    }
    override fun onResume() {
        super.onResume()
        Log.d("TEST", "onResume")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("TEST", "onAttach")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("TEST", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("TEST", "onDetach")
    }

    override fun onStart() {
        super.onStart()
        Log.d("TEST", "onStart")

    }

    override fun onStop() {
        super.onStop()
        Log.d("TEST", "onStop")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("TEST", "onDESTROYVIEW  ")
        //gán lại rổng, để không bị tăng item khi backPress
        listCateProduct?.let { listSaveCateProductPrevious?.addAll(it) }
        clearList()
        sliderItem.clear()
        sliderAdapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TEST", "onDESTROYVIEW  ")
    }

}

