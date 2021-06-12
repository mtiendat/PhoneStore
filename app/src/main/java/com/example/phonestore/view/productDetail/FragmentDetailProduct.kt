package com.example.phonestore.view.productDetail

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentDetailProductBinding
import com.example.phonestore.extendsion.*
import com.example.phonestore.model.*
import com.example.phonestore.model.cart.DetailCart
import com.example.phonestore.model.technology.Technology
import com.example.phonestore.services.Constant
import com.example.phonestore.services.adapter.*
import com.example.phonestore.view.MainActivity
import com.example.phonestore.viewmodel.CartViewModel
import com.example.phonestore.viewmodel.DetailProductViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable
import kotlin.math.abs


class FragmentDetailProduct: BaseFragment(), YouTubePlayer.OnInitializedListener {
    companion object {
        fun actionToFragmentDetail(v: View, idCate: Bundle){
            v.findNavController().navigate(
                    R.id.action_fragmentHome_to_fragmentDetailProduct,
                    idCate
            )
        }
    }
    private var bindingProductDetail: FragmentDetailProductBinding? = null
    private var cartViewModel: CartViewModel? = null
    private var detailViewModel: DetailProductViewModel? = null
    private var product: ProductInfo? = null
    private var technology: Technology? = null
    private var idProduct: Int?=0
    private var color: String? = null
    private var storage: String? = null
    private var idYT: String? = null
    private var img: String? = null
    private var price: Int? = 0
    private var supplier: Supplier? = null
    private var countAddToCart: Int = 0
    private var hadData: Boolean = false
    private var slideRunnable = Runnable {
        bindingProductDetail?.vpSlideShow?.currentItem = bindingProductDetail?.vpSlideShow?.currentItem?.plus(1) ?:0
    }
    private var youtubePlayerFragment: YouTubePlayerSupportFragmentX? = null
    private var sliderAdapter : SlideshowProductAdapter? = null
    private var slideHandler: Handler = Handler(Looper.getMainLooper())
    private var relatedProductAdapter: RelatedProductAdapter? = null
    private var voteAdapter: DetailProductAdapter<Vote>? = null
    private var compareAdapter: CompareProductAdapter? = null
    private var listSlideshow: ArrayList<String> = arrayListOf()
    private var listRelatedProduct: ArrayList<ProductInfo>? = arrayListOf()
    private var listCompareProduct: ArrayList<ProductInfo>? = arrayListOf()
    private var productBuyNow: ArrayList<ProductOrder>? = arrayListOf()
    private var listVote: ArrayList<Vote>? = arrayListOf()
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingProductDetail = FragmentDetailProductBinding.inflate(inflater, container, false)

        youtubePlayerFragment = YouTubePlayerSupportFragmentX.newInstance()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.ytTrailer, youtubePlayerFragment!!).commit()

        return bindingProductDetail?.root
    }
    override fun setUI() {
        MainActivity.itemSearch?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                return true
            }

        })
        bindingProductDetail?.pbLoadVote?.setIndeterminateDrawableTiled(
                FoldingCirclesDrawable.Builder(context).colors(resources.getIntArray(
                        R.array.google_colors)).build())
        context?.let {
            bindingProductDetail?.ivTechnology?.let { imgV ->
                Glide.with(it)
                    .asGif()
                    .load(R.drawable.settings)
                    .into(imgV)
            }
        }
        productBuyNow?.clear()
        product = arguments?.getParcelable("product")
        val query = arguments?.getString("name")
//        MainActivity.searchView?.get()?.isIconified = false //Xóa tìm kiếm
//        MainActivity.searchView?.get()?.clearFocus()
        MainActivity.itemSearch?.collapseActionView()
        (requireActivity() as MainActivity).supportActionBar?.title = query //setTitle
        bindingProductDetail?.shimmerLayoutRelated?.startShimmer()
        bindingProductDetail?.shimmerLayoutCompareProduct?.startShimmer()
        initRecyclerViewRelated()
        initRecyclerViewCompare()
        initRecyclerViewVote()
        setOnClickListener()
        setSpinner()
        getData()
        setDetailProduct(product)
    }
    fun setOnClickListener(){
        bindingProductDetail?.btnAddToCart?.setOnClickListener {
            if(checkSelectSpinner()){
                if(countAddToCart<2) {
                    countAddToCart++
                    cartViewModel?.addToCart(idProduct)
                }else view?.let { it1 -> Snackbar.make(it1, Constant.VALIDATE_BUY_PRODUCT, Snackbar.LENGTH_SHORT).show() }
            }else bindingProductDetail?.nsvDetail?.fullScroll(View.FOCUS_UP)
        }
        bindingProductDetail?.btnBuyNow?.setOnClickListener {
            if (checkSelectSpinner()) {
                val product = DetailCart(null, null,
                        idProduct,
                        1,
                        this.price,
                        color,
                        storage,
                        bindingProductDetail?.tvDetailName?.text.toString(),
                        this.img)
                val productOrder = ProductOrder(product, 1, this.price)
                this.productBuyNow?.add(productOrder)
                val item = bundleOf("listProduct" to productBuyNow)
                it.findNavController().navigate(R.id.action_fragmentDetailProduct_to_fragmentOrder, item)
            }else bindingProductDetail?.nsvDetail?.fullScroll(View.FOCUS_UP)
        }
//        bindingProductDetail?.btnSendVote?.setOnClickListener {
//            if(validateVote()){
//                val vote = Vote(idUser = Constant.idUser, content = bindingProductDetail?.edtVote?.text.toString(), vote = bindingProductDetail?.rbVote?.rating?.toInt() ?:1)
////                detailViewModel?.postVote(idCate, vote)
//                bindingProductDetail?.pbLoadVote?.visible()
//            }
//        }
        bindingProductDetail?.btnViewAllVote?.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentDetailProduct_to_fragmentAllVote, bundleOf("idCate" to product?.idCate))
        }
        bindingProductDetail?.ivSupplierLogo?.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentDetailProduct_to_fragmentSupplier, bundleOf("supplier" to supplier))
        }
        bindingProductDetail?.tvSupplierName?.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentDetailProduct_to_fragmentSupplier, bundleOf("supplier" to supplier))
        }
        bindingProductDetail?.btnTechnologyDetailMore?.setOnClickListener {
            activity?.supportFragmentManager?.let {
                val dialogTechnology = FragmentDetailTechnology()
                dialogTechnology.arguments = bundleOf("technology" to technology)
                dialogTechnology.show(it, "Technolgy")
            }
        }
        bindingProductDetail?.btnSlideShowNext?.setOnClickListener {
            bindingProductDetail?.vpSlideShow?.currentItem = bindingProductDetail?.vpSlideShow?.currentItem?.plus(
                1
            )?: 0

        }
        bindingProductDetail?.btnSlideShowBack?.setOnClickListener {
            bindingProductDetail?.vpSlideShow?.currentItem = bindingProductDetail?.vpSlideShow?.currentItem?.minus(
                1
            )?: 0
        }
    }
    private fun checkSelectSpinner(): Boolean{
        return when {
            bindingProductDetail?.spDetailColor?.selectedItem.toString()== Constant.TITLE_COLOR -> {
                makeToast(Constant.PLEASE_CHOOSE_COLOR)
                false
            }
            bindingProductDetail?.spDetailStorage?.selectedItem.toString() == Constant.TITLE_STORAGE -> {
                makeToast(Constant.PLEASE_CHOOSE_STORAGE)
                false
            }
            else -> true
        }
    }
    private fun setSpinner(){
        bindingProductDetail?.spDetailColor?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                when(position){
                    0 -> {
                    }
                    else -> {
                        color = parent?.getItemAtPosition(position).toString()
                        if(storage!=null) {
                            detailViewModel?.getColorOrStorageProduct(
                                    idCate = product?.idCate,
                                    color = color!!,
                                    storage = storage!!
                            )
                        }else  detailViewModel?.getColorOrStorageProduct(
                                idCate = product?.idCate,
                                color = color!!,
                                storage = ""
                        )
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        bindingProductDetail?.spDetailStorage?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                when(position){
                    0 -> {
                    }
                    else -> {
                        storage =  parent?.getItemAtPosition(position).toString()
                        if(color!=null){
                            detailViewModel?.getColorOrStorageProduct(
                                    product?.idCate,
                                    color = color!!,
                                    storage = storage!!
                            )
                        }else detailViewModel?.getColorOrStorageProduct(
                                 product?.idCate,
                                color = "",
                                storage = storage!!
                        )
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
    private fun makeToast(s: String?){
        context?.let{Toast.makeText(it, s, Toast.LENGTH_SHORT).show()}
    }
    override fun setViewModel() {
        detailViewModel = ViewModelProvider(this@FragmentDetailProduct).get(DetailProductViewModel::class.java)
        cartViewModel = ViewModelProvider(this@FragmentDetailProduct).get(CartViewModel::class.java)
    }
    override fun setObserve() {
        setObserveDetailViewModel()
        setObserveCartViewModel()
    }
    private fun setObserveDetailViewModel(){
        detailViewModel?.detailProduct?.observe(viewLifecycleOwner, {
            setDetailCateProduct(it)
            detailViewModel?.getTechnology(product?.technology!!)
        })
        detailViewModel?.technologyResponse?.observe(viewLifecycleOwner,{
            technology = it.technology
            setDataTechnology(it.technology)
            detailViewModel?.getRelatedProduct(product?.id)
        })

        detailViewModel?.listColor?.observe(viewLifecycleOwner, {
            setSpinner(it, bindingProductDetail?.spDetailColor)
        })

        detailViewModel?.listStorage?.observe(viewLifecycleOwner, {
                setSpinner(it, bindingProductDetail?.spDetailStorage)
        })

        detailViewModel?.color?.observe(viewLifecycleOwner, {
                if(this.img !=it) {
                    setImg(it, bindingProductDetail?.ivDetailPhoto)
                    this.img = it
                }
        })

        detailViewModel?.priceOld?.observe(viewLifecycleOwner, {
            bindingProductDetail?.tvDetaiPriceOld?.text = it.toVND()
        })

        detailViewModel?.priceNew?.observe(viewLifecycleOwner, {
            bindingProductDetail?.tvDetailPrice?.text = it
        })

        detailViewModel?.idProduct?.observe(viewLifecycleOwner, {
            idProduct = it
        })
        detailViewModel?.listImageSlideshow?.observe(viewLifecycleOwner, {
                listSlideshow = it
                initSlider()
                bindingProductDetail?.shimmerSlideShow?.stopShimmer()
                bindingProductDetail?.shimmerSlideShow?.gone()
        })
        detailViewModel?.relatedProduct?.observe(viewLifecycleOwner, {
            listRelatedProduct?.addAll(it)
            relatedProductAdapter?.setItems(it)
            bindingProductDetail?.shimmerLayoutRelated?.stopShimmer()
            bindingProductDetail?.shimmerLayoutRelated?.gone()
            detailViewModel?.getCompareProduct(product?.price)
        })
        detailViewModel?.compareProduct?.observe(viewLifecycleOwner,{
            compareAdapter?.setItems(it)
            bindingProductDetail?.shimmerLayoutCompareProduct?.stopShimmer()
            bindingProductDetail?.shimmerLayoutCompareProduct?.gone()

        })
        detailViewModel?.listVote?.observe(viewLifecycleOwner, {
            if(it.size > 0) {
                bindingProductDetail?.tvNoVote?.gone()
                bindingProductDetail?.btnViewAllVote?.visible()
            }
            listVote?.addAll(it)
            voteAdapter?.setItems(it)
        })

        detailViewModel?.bought?.observe(viewLifecycleOwner, {
            if(!it){
                //bindingProductDetail?.groupSendVote?.gone()
            }
        })
        detailViewModel?.messageSuccess?.observe(viewLifecycleOwner, {
            if(it){
                //bindingProductDetail?.edtVote?.text?.clear()
                //bindingProductDetail?.rbVote?.rating = 0f
                view?.let { it1 -> Snackbar.make(it1, Constant.SUCCESS_VOTED, Snackbar.LENGTH_SHORT).show() }
                detailViewModel?.getListVote(product?.idCate)
                bindingProductDetail?.pbLoadVote?.gone()
            }else view?.let { it1 -> Snackbar.make(it1, Constant.ERROR_VOTED, Snackbar.LENGTH_SHORT).show() }
        })
    }
    private fun setObserveCartViewModel(){
        val totalProductObserver = Observer<Int?>{
            context?.let { it1 -> MainActivity.icon?.let { it2 ->
                MainActivity.setBadgeCount(
                    it1,
                    icon = it2,
                    it.toString()
                )
            } }
        }
        cartViewModel?.totalProduct?.observe(requireActivity(), totalProductObserver)
        val resultsObserve = Observer<Boolean?> {
            if(it) {
                makeToast(Constant.SUCCESS_ADD_TO_CART)
                cartViewModel?.getTotalProduct()
            }else view?.let { it1 -> Snackbar.make(it1, Constant.VALIDATE_BUY_PRODUCT, Snackbar.LENGTH_SHORT).show() }
        }
        cartViewModel?.resultAddToCart?.observe(viewLifecycleOwner, resultsObserve)
    }
    private fun getData(){
        hadData = true
        detailViewModel?.getDetailProduct(product?.id)
        detailViewModel?.getListImageSlideshow(product?.id)
//        detailViewModel?.getListVote(idCate)
    }

    private fun setDetailProduct(product: ProductInfo?){
        bindingProductDetail?.tvDetailName?.text = product?.name
        bindingProductDetail?.tvDetailPrice?.text = (product?.price?.minus((product.price * product.discount)))?.toInt().toVND()
        bindingProductDetail?.tvDetaiPriceOld?.text = product?.price.toVND()
        bindingProductDetail?.tvDetaiPriceOld?.paintFlags = bindingProductDetail?.tvDetaiPriceOld?.strikeThrough()!!
        bindingProductDetail?.ratingBarDetail?.rating = product?.totalVote?: 0.1f

        this.img = product?.img
        setImg(product?.img, bindingProductDetail?.ivDetailPhoto)

    }
    private fun setSpinner(list: List<String>, spinner: Spinner?){
        val adapterSpinner = context?.let { ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                list.toList()
        ) }
        adapterSpinner?.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner?.adapter = adapterSpinner
    }
    private fun setDetailCateProduct(product: DetailProduct?){
        (requireActivity() as MainActivity).supportActionBar?.title = product?.name
        bindingProductDetail?.tvSupplierName?.text = product?.supplier?.name
        setImg(product?.supplier?.logoSupplier, bindingProductDetail?.ivSupplierLogo)
        supplier = product?.supplier
        idYT = product?.trailer
        youtubePlayerFragment?.initialize(Constant.KEY_API_YOUTUBE, this)
    }

    private fun initRecyclerViewRelated(){
        relatedProductAdapter = RelatedProductAdapter(listRelatedProduct)
        bindingProductDetail?.rvRelatedProduct?.adapter = relatedProductAdapter
        bindingProductDetail?.rvRelatedProduct?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
    private fun initRecyclerViewCompare(){
        compareAdapter = CompareProductAdapter(listCompareProduct)
        bindingProductDetail?.rvCompareProduct?.adapter = compareAdapter
        bindingProductDetail?.rvCompareProduct?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
    private fun initRecyclerViewVote(){
        voteAdapter = DetailProductAdapter(listVote)
        bindingProductDetail?.rvVote?.adapter = voteAdapter
        bindingProductDetail?.rvVote?.layoutManager = LinearLayoutManager(context)
    }
//    private fun validateVote(): Boolean{
//        return when {
//            bindingProductDetail?.rbVote?.rating == 0f -> {
//                Toast.makeText(context, Constant.PLEASE_CHOOSE_VOTE, Toast.LENGTH_SHORT).show()
//                false
//            }
//            bindingProductDetail?.edtVote?.text.isNullOrBlank() -> {
//                bindingProductDetail?.edtVote?.error = Constant.VALIDATE_VOTE
//                false
//            }
//            else -> true
//        }
//    }
    override fun onInitializationSuccess(
            p0: YouTubePlayer.Provider?,
            player: YouTubePlayer?,
            wasRestore: Boolean
    ) {
        if(!wasRestore){
            Log.d("YOUTUBE", idYT.toString())
            player?.cueVideo(idYT)
            player?.fullscreenControlFlags = FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
        }

    }

    override fun onInitializationFailure(
            p0: YouTubePlayer.Provider?,
            p1: YouTubeInitializationResult?
    ) {
        Toast.makeText(context, Constant.YOUTUBE_FAILURE, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        val itemCart = menu.findItem(R.id.fragmentDetailCart)
        val icon: LayerDrawable = itemCart?.icon as LayerDrawable
        bindingProductDetail?.btnAddToCart?.setOnClickListener {
            MainActivity.setBadgeCount(requireContext(), icon, "10")
        }
    }
    private fun setImg(img: String?, v: ImageView?){
        if (v != null) {
            Glide.with(this)
                .load(img)
                .error(R.drawable.noimage)
                .skipMemoryCache(true)
                .into(v)
        }
    }
    private fun setDataTechnology(technology: Technology){
        bindingProductDetail?.tvTechnologyScreenDetail?.text = technology.screen?.technology
        bindingProductDetail?.tvTechnologyOSDetail?.text = technology.os_cpu?.name
        bindingProductDetail?.tvTechnologyRearCameraDetail?.text = technology.rearCamera?.pixel
        bindingProductDetail?.tvTechnologyFrontCameraDetail?.text = technology.frontCamera?.pixel
        bindingProductDetail?.tvTechnologyChipDetail?.text = technology.os_cpu?.cpu
        bindingProductDetail?.tvTechnologyRamDetail?.text = technology.storage?.ram
        bindingProductDetail?.tvDetailStorageInternalDetail?.text = technology.storage?.internal
        bindingProductDetail?.tvDetailStorageSimDetail?.text = technology.network?.sim
        bindingProductDetail?.tvDetailStoragePinDetail?.text = technology.battery?.capacity
    }

    private fun initSlider(){
        sliderAdapter = SlideshowProductAdapter(listSlideshow, bindingProductDetail?.vpSlideShow)
        bindingProductDetail?.vpSlideShow?.adapter = sliderAdapter
        bindingProductDetail?.vpSlideShow?.clipToPadding = false
        bindingProductDetail?.vpSlideShow?.clipChildren = false
        bindingProductDetail?.vpSlideShow?.offscreenPageLimit = 3
        bindingProductDetail?.vpSlideShow?.getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        bindingProductDetail?.vpSlideShow?.setPageTransformer(compositePageTransformer)
        bindingProductDetail?.vpSlideShow?.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                slideHandler.removeCallbacks(slideRunnable)
                slideHandler.postDelayed(slideRunnable, 3000) //next slide 3 giây

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}
