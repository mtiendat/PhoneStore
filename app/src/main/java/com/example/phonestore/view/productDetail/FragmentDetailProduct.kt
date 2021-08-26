package com.example.phonestore.view.productDetail

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.*
import androidx.core.app.ShareCompat
import androidx.core.os.bundleOf
import androidx.core.view.doOnLayout
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
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
import com.example.phonestore.model.cart.Cart
import com.example.phonestore.model.cart.ParamCart
import com.example.phonestore.model.technology.Technology
import com.example.phonestore.services.Constant
import com.example.phonestore.services.Constant.IMAGE
import com.example.phonestore.services.Constant.STORAGE
import com.example.phonestore.services.adapter.*
import com.example.phonestore.view.MainActivity
import com.example.phonestore.viewmodel.CartViewModel
import com.example.phonestore.viewmodel.DetailProductViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX
import java.math.BigDecimal
import java.math.RoundingMode
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
    private var idProduct: Int= 0
    private var idComment: Int= 0
    private var positionDelete: Int? = 0
    private var color: String? = null
    private var storage: String? = null
    private var idYT: String? = null
    private var img: String? = null
    private var price: Int = 0
    private var supplier: Supplier? = null
    private var hadData: Boolean = false
    private var isStorage: Boolean = false
    private var isStorageOrColor: Boolean = false
    private var isLoveFirst: Boolean = true
    private var isSetImgFirst: Boolean = true
    private var img2: String? =""
    private var slideRunnable = Runnable {
        bindingProductDetail?.vpSlideShow?.currentItem = bindingProductDetail?.vpSlideShow?.currentItem?.plus(1) ?:0
    }
    private var youtubePlayerFragment: YouTubePlayerSupportFragmentX? = null
    private var sliderAdapter : SlideshowProductAdapter? = null
    private var slideHandler: Handler = Handler(Looper.getMainLooper())
    private var relatedProductAdapter: RelatedProductAdapter? = null
    private var commentAdapter: CommentAdapter? = null
    private var compareAdapter: CompareProductAdapter? = null
    private var listSlideshow: ArrayList<String?>? = arrayListOf()
    private var listRelatedProduct: ArrayList<ProductInfo?>? = arrayListOf()
    private var listCompareProduct: ArrayList<ProductInfo?>? = arrayListOf()
    private var productBuyNow: ArrayList<ProductOrder>? = arrayListOf()
    private var listComment: ArrayList<Comment>? = arrayListOf()
    private var listIDComment: ArrayList<Int>? = arrayListOf()
    private var listColor: ArrayList<String>? = arrayListOf()
    private var listStorage: ArrayList<String>? = arrayListOf()

    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        bindingProductDetail = FragmentDetailProductBinding.inflate(inflater, container, false)

        youtubePlayerFragment = YouTubePlayerSupportFragmentX.newInstance()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.ytTrailer, youtubePlayerFragment!!).commit()

        return bindingProductDetail?.root
    }
    override fun setUI() {
        (activity as MainActivity).handleToolbar()
        MainActivity.itemSearch?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                return true
            }

        })
        productBuyNow?.clear()
        product = arguments?.getParcelable("product")
        val query = arguments?.getString("name")
        MainActivity.itemSearch?.collapseActionView()
        (requireActivity() as MainActivity).supportActionBar?.title = query //setTitle
        bindingProductDetail?.shimmer?.shimmerDetailProduct?.startShimmer()
        initRecyclerViewRelated()
        initRecyclerViewCompare()
        initRecyclerViewVote()
        setSpinner()
        getData()
        setDetailProduct(product)
        setOnClickListener()
    }
    private fun setOnClickListener(){
        bindingProductDetail?.btnAddToCart?.setOnClickListener {
            val listStorage =  listStorage
            it.findNavController().navigate(R.id.action_fragmentDetailProduct_to_bottomSheetCart, bundleOf(IMAGE to listColor, STORAGE to listStorage, "color" to color, "storage" to storage))
//            if(checkSelectSpinner()){
//                cartViewModel?.addToCart(product?.id)
//            }else{
//                bindingProductDetail?.nsvDetail?.fling(0)
//                bindingProductDetail?.nsvDetail?.smoothScrollTo(0, 0)
//            }
        }
        bindingProductDetail?.btnBuyNow?.setOnClickListener {
            if (checkSelectSpinner()) {
                val product = Cart( null, null,
                        idProduct = if(idProduct==0) product?.id else idProduct,
                        qty = 1,
                        price = product?.price?:0,
                        color = color,
                        storage = storage,
                        priceRoot = this.product?.price ?:0,
                        name = bindingProductDetail?.tvDetailName?.text.toString(),
                        avatar = this.img)
                val productOrder = ProductOrder(product, 1)
                this.productBuyNow?.add(productOrder)
                val item = bundleOf("listProduct" to productBuyNow, "totalMoney" to product.price)
                it.findNavController().navigate(R.id.action_fragmentDetailProduct_to_fragmentOrder, item)
            }else bindingProductDetail?.nsvDetail?.fullScroll(View.FOCUS_UP)
        }
        bindingProductDetail?.btnSendReview?.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentDetailProduct_to_fragmentComment, bundleOf("listId" to listIDComment))

        }
        bindingProductDetail?.btnViewAllVote?.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentDetailProduct_to_fragmentAllVote, bundleOf("idProduct" to product?.id))
        }
//        bindingProductDetail?.ivSupplierLogo?.setOnClickListener {
//            it.findNavController().navigate(R.id.action_fragmentDetailProduct_to_fragmentSupplier, bundleOf("supplier" to supplier))
//        }
//        bindingProductDetail?.tvSupplierName?.setOnClickListener {
//            it.findNavController().navigate(R.id.action_fragmentDetailProduct_to_fragmentSupplier, bundleOf("supplier" to supplier))
//        }
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
        bindingProductDetail?.cbWishList?.setOnCheckedChangeListener { _, isChecked ->
            if(isLoveFirst||isStorageOrColor){
                if(isChecked){
                    detailViewModel?.addToWishList(product?.id)
                }else detailViewModel?.deleteToWishList(product?.id)
          }
        }
        bindingProductDetail?.cbShare?.setOnClickListener {
            activity?.let { it1 ->
                ShareCompat.IntentBuilder.from(it1)
                    .setType("text/plain")
                    .setChooserTitle("Chia sẻ sản phẩm này")
                    .setText("https://ldmobile.cdth18d.asia/dienthoai/${product?.name?.replace(" ", "-")}" )
                    .startChooser()
            }
        }
    }
    private fun checkSelectSpinner(): Boolean{
        var string = ""
        var boolean = true

        if(bindingProductDetail?.spDetailColor?.selectedItem.toString() == Constant.TITLE_COLOR){
            string = Constant.PLEASE_CHOOSE_COLOR
            boolean = false
        }
        if(bindingProductDetail?.spDetailStorage?.selectedItem.toString() == Constant.TITLE_STORAGE){
            string = Constant.PLEASE_CHOOSE_STORAGE
            boolean = false
        }
        if(bindingProductDetail?.spDetailColor?.selectedItem.toString() == Constant.TITLE_COLOR && bindingProductDetail?.spDetailStorage?.selectedItem.toString() == Constant.TITLE_STORAGE){
            string ="${Constant.PLEASE_CHOOSE_COLOR} và dung lượng"
            boolean = false
        }
        if(!boolean) makeToast(string)
        return boolean
    }
    private fun setSpinner(){
        bindingProductDetail?.spDetailColor?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                        if(!isSetImgFirst){
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
                            isStorage = false
                            isStorageOrColor = false
                            isLoveFirst = false
                        }else isSetImgFirst = false
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
                if (!isSetImgFirst) {
                            storage = parent?.getItemAtPosition(position).toString()
                            if (color != null) {
                                detailViewModel?.getColorOrStorageProduct(
                                    product?.idCate,
                                    color = color!!,
                                    storage = storage!!
                                )
                            } else detailViewModel?.getColorOrStorageProduct(
                                product?.idCate,
                                color = "",
                                storage = storage!!
                            )
                            isStorage = true
                            isStorageOrColor = false
                            isLoveFirst = false
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
            detailViewModel?.checkComment(product?.id)
        })
        detailViewModel?.technologyResponse?.observe(viewLifecycleOwner,{
            technology = it?.technology
            setDataTechnology(it?.technology)
            detailViewModel?.getRelatedProduct(product?.id)
        })
        detailViewModel?.technologyCompareResponse?.observe(viewLifecycleOwner, {
            //view?.findNavController()?.navigate(R.id.action_fragmentDetailProduct_to_fragmentCompareProduct, bundleOf("technology1" to technology, "technology2" to it.technology, "iv1" to product?.img, "iv2" to img2))
            activity?.startActivity(ActivityCompareProduct.intentFor(context, product?.img, img2, technology, it.technology))
        })

        detailViewModel?.listColor?.observe(viewLifecycleOwner, {
            setSpinner(it, bindingProductDetail?.spDetailColor)
            setFirstSpinner(product?.color?:"", it, bindingProductDetail?.spDetailColor)
        })

        detailViewModel?.listStorage?.observe(viewLifecycleOwner, {
            setSpinner(it, bindingProductDetail?.spDetailStorage)
            setFirstSpinner(product?.storage?:"", it, bindingProductDetail?.spDetailStorage)

        })

        detailViewModel?.color?.observe(viewLifecycleOwner, {
                if(this.img != it) {
                    setImg(it, bindingProductDetail?.ivDetailPhoto)
                    this.img = it
                }
        })

        detailViewModel?.priceOld?.observe(viewLifecycleOwner, {
            bindingProductDetail?.tvDetaiPriceOld?.text = it.toVND()
        })

        detailViewModel?.priceNew?.observe(viewLifecycleOwner, {
            bindingProductDetail?.tvDetailPrice?.text = it.toVND()
            product?.price = it
        })
        detailViewModel?.wish?.observe(viewLifecycleOwner, {
           bindingProductDetail?.cbWishList?.isChecked = it
            isStorageOrColor = true
        })
        detailViewModel?.idProduct?.observe(viewLifecycleOwner, {
            product?.id = it
            if(isStorage){
                detailViewModel?.checkComment(it)
                detailViewModel?.getListComment(it)
            }
            idProduct = it

        })
        detailViewModel?.checkComment?.observe(viewLifecycleOwner, {
            if(it?.status == true){
                listIDComment = it.listId
                bindingProductDetail?.btnSendReview?.visible()
            }else bindingProductDetail?.btnSendReview?.gone()
        })

        detailViewModel?.listImageSlideshow?.observe(viewLifecycleOwner, {
                listSlideshow = it
                initSlider()
        })
        detailViewModel?.relatedProduct?.observe(viewLifecycleOwner, {
            relatedProductAdapter?.setItems(it)
            detailViewModel?.getCompareProduct(product?.idCate, product?.price)
        })
        detailViewModel?.compareProduct?.observe(viewLifecycleOwner,{
            compareAdapter?.setItems(it)
            detailViewModel?.getListComment(product?.id)

        })
        detailViewModel?.listComment?.observe(viewLifecycleOwner, {
            if(it?.size?:-1 > 0) {
                bindingProductDetail?.tvNoVote?.gone()
                bindingProductDetail?.btnViewAllVote?.visible()
                bindingProductDetail?.chartVote?.root?.visible()
                bindingProductDetail?.viewCharVote?.visible()
                bindingProductDetail?.rvVote?.visible()
                listComment?.clear()
                if(it?.size?:0 > 5){
                    it?.subList(0,5)?.let { it1 -> listComment?.addAll(it1) }
                }else it?.let { it1 -> listComment?.addAll(it1) }
                commentAdapter?.setItems(listComment)
                setViewRating(it)
            }else {
                bindingProductDetail?.chartVote?.root?.gone()
                bindingProductDetail?.tvNoVote?.visible()
                bindingProductDetail?.rvVote?.gone()
                bindingProductDetail?.viewCharVote?.gone()
                bindingProductDetail?.btnViewAllVote?.gone()
            }
            if(bindingProductDetail?.groupDetailProduct?.visibility == View.GONE){
                bindingProductDetail?.shimmer?.shimmerDetailProduct?.stopShimmer()
                bindingProductDetail?.shimmer?.ctrlDetail?.gone()
                bindingProductDetail?.groupDetailProduct?.visible()
                (activity as MainActivity).handleToolbar()
            }

        })

        detailViewModel?.statusComment?.observe(viewLifecycleOwner, {
            if(it?.status == true){
                view?.let { it1 -> Snackbar.make(it1, Constant.SUCCESS_VOTED, Snackbar.LENGTH_SHORT).show() }
            }else view?.let { it1 -> Snackbar.make(it1, Constant.ERROR_VOTED, Snackbar.LENGTH_SHORT).show() }
        })
        detailViewModel?.statusReply?.observe(viewLifecycleOwner, {
            if(it == true){
                view?.let { it1 -> Snackbar.make(it1, Constant.SUCCESS_REPLY, Snackbar.LENGTH_SHORT).show() }
                detailViewModel?.getListReply(idComment)
            }else view?.let { it1 -> Snackbar.make(it1, Constant.ERROR_VOTED, Snackbar.LENGTH_SHORT).show() }
        })
        detailViewModel?.statusDeleteComment?.observe(viewLifecycleOwner, {
            if(it.status){
                view?.let { it1 -> Snackbar.make(it1, it.message?:"", Snackbar.LENGTH_SHORT).show() }
                positionDelete?.let {
                        it1 -> commentAdapter?.listComment?.removeAt(it1)
                    commentAdapter?.notifyItemRemoved(it1)
                }

            }else view?.let { it1 -> Snackbar.make(it1, "", Snackbar.LENGTH_SHORT).show() }
        })
    }
    private fun setObserveCartViewModel(){
        cartViewModel?.totalProduct?.observe(requireActivity(), {
            context?.let { it1 -> MainActivity.icon?.let { it2 ->
                MainActivity.setBadgeCount(
                    it1,
                    icon = it2,
                    it.toString()
                )
            } }
        })

        cartViewModel?.cartResponse?.observe(viewLifecycleOwner, {
            if(it.status == true) cartViewModel?.getTotalProduct()
            view?.let { it1 -> Snackbar.make(it1, it.message.toString(), Snackbar.LENGTH_SHORT).show() }
        })
        findNavController().currentBackStackEntry?.savedStateHandle?.apply {
            getLiveData<ParamCart>("paramCart").observe(viewLifecycleOwner) {
                cartViewModel?.addToCart(it.storage, it. image)
            }

        }
    }
    private fun getData(){
        hadData = true
        detailViewModel?.getDetailProduct(product?.id)
        detailViewModel?.getListImageSlideshow(product?.id)
    }

    private fun setDetailProduct(product: ProductInfo?){
        bindingProductDetail?.tvDetailName?.text = product?.name
        bindingProductDetail?.tvDetailPrice?.text = (product?.price?.minus((product.price * product.discount)))?.toInt().toVND()
        bindingProductDetail?.tvDetaiPriceOld?.text = product?.price.toVND()
        bindingProductDetail?.tvDetaiPriceOld?.paintFlags = bindingProductDetail?.tvDetaiPriceOld?.strikeThrough()!!

        this.img = product?.img
        setImg(product?.img, bindingProductDetail?.ivDetailPhoto)

        bindingProductDetail?.chartVote?.ctrlVote?.setOnClickListener {
            if(bindingProductDetail?.chartVote?.lnVote?.visibility == View.GONE) {
                bindingProductDetail?.chartVote?.lnVote?.visible()
                bindingProductDetail?.chartVote?.btnTotalJudge?.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_arrow_up,0)
            }
            else  {
                bindingProductDetail?.chartVote?.lnVote?.gone()
                bindingProductDetail?.chartVote?.btnTotalJudge?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down,0)
            }
        }
        bindingProductDetail?.chartVote?.btnTotalJudge?.setOnClickListener {
            if(bindingProductDetail?.chartVote?.lnVote?.visibility == View.GONE) {
                bindingProductDetail?.chartVote?.lnVote?.visible()
                bindingProductDetail?.chartVote?.btnTotalJudge?.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_arrow_up,0)
            }
            else  {
                bindingProductDetail?.chartVote?.lnVote?.gone()
                bindingProductDetail?.chartVote?.btnTotalJudge?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down,0)
            }
        }
    }
    private fun setSpinner(list: ArrayList<String?>?, spinner: Spinner?){
        val adapterSpinner = CustomDropDownAdapter(requireContext(), R.layout.item_spinner, list)
        //adapterSpinner?.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner?.adapter = adapterSpinner
        spinner?.doOnLayout {
            spinner.dropDownWidth = spinner.width
        }
    }
    private fun setDetailCateProduct(product: DetailProduct?){
        (requireActivity() as MainActivity).supportActionBar?.title = product?.name
        bindingProductDetail?.tvSupplierName?.text = product?.supplier?.name
        setImg(product?.supplier?.logoSupplier, bindingProductDetail?.ivSupplierLogo)
        supplier = product?.supplier
        idYT = product?.trailer
        bindingProductDetail?.cbWishList?.isChecked = product?.like?:false
        youtubePlayerFragment?.initialize(Constant.KEY_API_YOUTUBE, this)
        listColor?.addAll(product?.listImage?: arrayListOf<String>())
        listStorage?.addAll(product?.listStorage?: arrayListOf<String>())
    }

    private fun initRecyclerViewRelated(){
        relatedProductAdapter = RelatedProductAdapter(listRelatedProduct)
        bindingProductDetail?.rvRelatedProduct?.adapter = relatedProductAdapter
        bindingProductDetail?.rvRelatedProduct?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
    private fun initRecyclerViewCompare(){
        compareAdapter = CompareProductAdapter(listCompareProduct)
        compareAdapter?.clickCompare = {
            img2 = it?.img
            detailViewModel?.getTechnologyCompare(it?.technology!!)
        }
        bindingProductDetail?.rvCompareProduct?.adapter = compareAdapter
        bindingProductDetail?.rvCompareProduct?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
    private fun initRecyclerViewVote(){
        commentAdapter = CommentAdapter()
        commentAdapter?.intoReply = {
            findNavController().navigate(R.id.action_fragmentDetailProduct_to_fragmentReply, bundleOf("comment" to it))
        }
        commentAdapter?.likeComment = { id, state ->
            if(state) detailViewModel?.postLike(id) else detailViewModel?.deleteLike(id)
        }
        commentAdapter?.deleteComment = { id, position ->
            positionDelete = position
            detailViewModel?.deleteComment(id)
        }
        bindingProductDetail?.rvVote?.adapter = commentAdapter
        bindingProductDetail?.rvVote?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        bindingProductDetail?.rvVote?.layoutManager = LinearLayoutManager(context)
    }
    //YOUTUBE
    override fun onInitializationSuccess(
            p0: YouTubePlayer.Provider?,
            player: YouTubePlayer?,
            wasRestore: Boolean
    ) {
        if(!wasRestore){
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
    private fun setDataTechnology(technology: Technology?){
        bindingProductDetail?.tvTechnologyScreenDetail?.text = technology?.screen?.technology
        bindingProductDetail?.tvTechnologyOSDetail?.text = technology?.os_cpu?.name
        bindingProductDetail?.tvTechnologyRearCameraDetail?.text = technology?.rearCamera?.pixel
        bindingProductDetail?.tvTechnologyFrontCameraDetail?.text = technology?.frontCamera?.pixel
        bindingProductDetail?.tvTechnologyChipDetail?.text = technology?.os_cpu?.cpu
        bindingProductDetail?.tvTechnologyRamDetail?.text = technology?.storage?.ram
        bindingProductDetail?.tvDetailStorageInternalDetail?.text = technology?.storage?.internal
        bindingProductDetail?.tvDetailStorageSimDetail?.text = technology?.network?.sim
        bindingProductDetail?.tvDetailStoragePinDetail?.text = technology?.battery?.capacity
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

    private fun setViewRating(listComment: ArrayList<Comment>?){
        var star1 = 0
        var star2 = 0
        var star3 = 0
        var star4 = 0
        var star5 = 0
        var pro1 = 0f
        var pro2 = 0f
        var pro3 = 0f
        var pro4 = 0f
        var pro5 = 0f
        var totalJudge = 0
        listComment?.forEach {
            when(it.vote){
                5 -> star5++
                4 -> star4++
                3 -> star3++
                2 -> star2++
                1 -> star1++
            }
            totalJudge+=it.vote
        }
        bindingProductDetail?.chartVote?.tvNum5?.text = star5.toString()
        bindingProductDetail?.chartVote?.tvNum4?.text = star4.toString()
        bindingProductDetail?.chartVote?.tvNum3?.text = star3.toString()
        bindingProductDetail?.chartVote?.tvNum2?.text = star2.toString()
        bindingProductDetail?.chartVote?.tvNum1?.text = star1.toString()
        val total: Float = (star1 + star2 + star3 + star4 + star5).toFloat()
        pro1 = star1.div((total / 100))
        pro2 = star2.div(total.div(100))
        pro3 = star3.div(total.div(100))
        pro4 = star4.div(total.div(100))
        pro5 = star5.div(total.div(100))
        bindingProductDetail?.chartVote?.progressBar1?.progress = pro1.toInt()
        bindingProductDetail?.chartVote?.progressBar2?.progress = pro2.toInt()
        bindingProductDetail?.chartVote?.progressBar3?.progress = pro3.toInt()
        bindingProductDetail?.chartVote?.progressBar4?.progress = pro4.toInt()
        bindingProductDetail?.chartVote?.progressBar5?.progress = pro5.toInt()
        //comment
        val decimal = BigDecimal((totalJudge.div(listComment?.size?.toFloat()?:0f)).toDouble()).setScale(1, RoundingMode.HALF_EVEN)
        bindingProductDetail?.chartVote?.tvTotalVote?.text = decimal.toString()
        bindingProductDetail?.chartVote?.ratingBarVote?.rating = totalJudge.div(listComment?.size?.toFloat()?:0f).toFloat()
        bindingProductDetail?.chartVote?.btnTotalJudge?.text = "${listComment?.size} đánh giá"
        bindingProductDetail?.ratingBarDetail?.rating = totalJudge.div(listComment?.size?.toFloat()?:0f).toFloat()
    }
    fun setFirstSpinner(key: String, list: ArrayList<String?>?, spinner: Spinner?){
        for (i in list?.indices?: listOf()) {
            if(list?.get(i) == key){
                spinner?.setSelection(i)
                break
            }
        }

    }
}
