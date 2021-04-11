package com.example.phonestore.view

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.phonestore.Extension.*
import com.example.phonestore.R
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentDetailProductBinding
import com.example.phonestore.model.*
import com.example.phonestore.services.Constant
import com.example.phonestore.services.DetailProductAdapter
import com.example.phonestore.viewmodel.CartViewModel
import com.example.phonestore.viewmodel.DetailProductViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable


class FragmentDetailProduct: BaseFragment(), YouTubePlayer.OnInitializedListener {
    companion object {
        fun actionToFragmentDetail(v: View, idCate: Bundle){
            v.findNavController().navigate(
                    R.id.action_fragmentHome_to_fragmentDetailProduct,
                    idCate
            )
        }
    }
    private lateinit var bindingProductDetail: FragmentDetailProductBinding
    private lateinit var cartViewModel: CartViewModel
    private lateinit var detailViewModel: DetailProductViewModel
    private var idCate: Int = 0
    private var idProduct: Int?=0
    private var color: String? = null
    private var storage: String? = null
    private var idYT: String? = null
    private var img: String? = null
    private var price: Int? = 0
    private var supplier: Supplier? = null
    private var countAddToCart: Int = 0
    private var flag =  0
    private lateinit var youtubePlayerFragment: YouTubePlayerSupportFragmentX
    private var relatedProductAdapter: DetailProductAdapter<CateProductInfo>? = null
    private var voteAdapter: DetailProductAdapter<Vote>? = null
    private var listRelatedProduct: ArrayList<CateProductInfo>? = arrayListOf()
    private var productBuyNow: ArrayList<ProductOrder>? = arrayListOf()
    private var listVote: ArrayList<Vote>? = arrayListOf()
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        bindingProductDetail = FragmentDetailProductBinding.inflate(inflater, container, false)
//        youtubePlayerFragment = YouTubePlayerSupportFragmentX.newInstance()
//        val transaction = childFragmentManager.beginTransaction()
//        transaction.replace(R.id.ytTrailer, youtubePlayerFragment).commit()
        youtubePlayerFragment = YouTubePlayerSupportFragmentX.newInstance()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.ytTrailer, youtubePlayerFragment).commit()
        return bindingProductDetail.root
    }
    override fun setUI() {
        bindingProductDetail.pbLoadVote.setIndeterminateDrawableTiled(
                FoldingCirclesDrawable.Builder(context).colors(resources.getIntArray(
                        R.array.google_colors)).build())
        context?.let {
            Glide.with(it)
                .asGif()
                .load(R.drawable.settings)
                .into(bindingProductDetail.ivTechnology)
        }
        productBuyNow?.clear()
        idCate = arguments?.getInt("idCate") ?:1
        val query = arguments?.getString("name")
        MainActivity.searchView?.get()?.isIconified = true //Xóa tìm kiếm
        MainActivity.searchView?.get()?.clearFocus()
        MainActivity.itemSearch?.collapseActionView()
        (requireActivity() as MainActivity).supportActionBar?.title = query //setTitle

        initRecyclerViewRelated()
        initRecyclerViewVote()
        if(flag==0) {
            detailViewModel.getListCateProductByID(idCate)
            flag++
        }
        setSpinner()
        bindingProductDetail.shimmerLayoutRelated.startShimmer()
        detailViewModel.getRelatedProduct(idCate)
        bindingProductDetail.btnAddToCart.setOnClickListener {
            if(checkSelectSpinner()){
                if(countAddToCart<2) {
                    countAddToCart++
                    cartViewModel.addToCart(idProduct)
                }else view?.let { it1 -> Snackbar.make(it1, "Bạn được mua tối đa 2 sản phẩm", Snackbar.LENGTH_SHORT).show() }
            }
        }
        bindingProductDetail.btnBuyNow.setOnClickListener {
            if (checkSelectSpinner()) {
                val product = DetailCart(null, null,
                        idProduct,
                        1,
                        this.price,
                        color,
                        storage,
                        bindingProductDetail.tvDetailName.text.toString(), this.img)
                val productOrder = ProductOrder(product, 1, this.price)
                this.productBuyNow?.add(productOrder)
                val item = bundleOf("listProduct" to productBuyNow)
                it.findNavController().navigate(R.id.action_fragmentDetailProduct_to_fragmentOrder, item)
            }
        }
        bindingProductDetail.btnSendVote.setOnClickListener {
            if(validateVote()){
                val vote = Vote(idUser = Constant.idUser, content = bindingProductDetail.edtVote.text.toString(), vote = bindingProductDetail.rbVote.rating.toInt())
                detailViewModel.postVote(idCate, vote)
                bindingProductDetail.pbLoadVote.visible()
            }
        }
        bindingProductDetail.btnViewAllVote.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentDetailProduct_to_fragmentAllVote, bundleOf("idCate" to idCate))
        }
        bindingProductDetail.ivSupplierLogo.setOnClickListener {
            it.findNavController().navigate(R.id.action_fragmentDetailProduct_to_fragmentSupplier, bundleOf("supplier" to supplier ))
        }
        detailViewModel.getListVote(idCate)
    }
    private fun validateVote(): Boolean{
        return if(bindingProductDetail.rbVote.rating == 0f){
            Toast.makeText(context, "Bạn chưa chọn thang đánh giá", Toast.LENGTH_SHORT).show()
            false
        }else if(bindingProductDetail.edtVote.text.isNullOrBlank()){
            bindingProductDetail.edtVote.error = "Bạn chưa điền đánh giá"
            false
        }else true
    }
    private fun checkSelectSpinner(): Boolean{
        return if(bindingProductDetail.spDetailColor.selectedItem.toString()== Constant.TITLE_COLOR) {
            makeToast(Constant.PLEASE_CHOOSE_COLOR)
            false
        }else if(bindingProductDetail.spDetailStorage.selectedItem.toString() == Constant.TITLE_STORAGE) {
            makeToast(Constant.PLEASE_CHOOSE_STORAGE)
            false
        }else true
    }
    private fun setSpinner(){
        bindingProductDetail.spDetailColor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                            detailViewModel.getColorOrStorageProduct(
                                    idCate,
                                    color = color!!,
                                    storage = storage!!
                            )
                        }else  detailViewModel.getColorOrStorageProduct(
                                idCate,
                                color = color!!,
                                storage = ""
                        )
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        bindingProductDetail.spDetailStorage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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
                        bindingProductDetail.tvStorageDetail.text = storage
                        if(color!=null){
                            detailViewModel.getColorOrStorageProduct(
                                    idCate,
                                    color = color!!,
                                    storage = storage!!
                            )
                        }else detailViewModel.getColorOrStorageProduct(
                                idCate,
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
        cartViewModel = ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
    }
    override fun setObserve() {
        setObserveDetailViewModel()
        setObserveCartViewModel()
    }
    private fun setObserveDetailViewModel(){
        val detailObserve = Observer<CateProductInfo?>{
            setData(it)
        }
        detailViewModel.cateProductByID?.observe(viewLifecycleOwner, detailObserve)
        val listColorObserver = Observer<List<String>> {
            setSpinner(it, bindingProductDetail.spDetailColor)
        }
        detailViewModel.listColor?.observe(viewLifecycleOwner, listColorObserver)
        val listStorageObserver = Observer<List<String>> {
            setSpinner(it, bindingProductDetail.spDetailStorage)
        }
        detailViewModel.listStorage?.observe(viewLifecycleOwner, listStorageObserver)
        val colorObserve = Observer<String> {
            setImg(it, bindingProductDetail.ivDetailPhoto)
            this.img = it
        }
        detailViewModel.color?.observe(viewLifecycleOwner, colorObserve)
        val priceOldObserver = Observer<Int> {
            bindingProductDetail.tvDetaiPriceOld.text = it.toVND()

        }
        detailViewModel.priceOld?.observe(viewLifecycleOwner, priceOldObserver)
        val priceNewObserver = Observer<Int> {
            bindingProductDetail.tvDetailPrice.text = it.toVND()
            this.price = it

        }
        detailViewModel.priceNew?.observe(viewLifecycleOwner, priceNewObserver)
        val idProductObserve = Observer<Int?>{
            idProduct = it
        }
        detailViewModel.idProduct?.observe(viewLifecycleOwner, idProductObserve)
        val relatedProductObserver = Observer<ArrayList<CateProductInfo>?> {
            listRelatedProduct?.addAll(it)
            relatedProductAdapter?.setItems(it)
            bindingProductDetail.shimmerLayoutRelated.stopShimmer()
            bindingProductDetail.shimmerLayoutRelated.gone()
        }
        detailViewModel.relatedProduct?.observe(viewLifecycleOwner, relatedProductObserver)
        val voteObserve = Observer<ArrayList<Vote>?>{
            if(it.size > 0) {
                bindingProductDetail.tvNoVote.gone()
                bindingProductDetail.btnViewAllVote.visible()
            }
            listVote?.addAll(it)
            voteAdapter?.setItems(it)
        }
        detailViewModel.listVote?.observe(viewLifecycleOwner, voteObserve)
        val boughtObserver = Observer<Boolean?>{
            if(!it){
                bindingProductDetail.groupSendVote.gone()
            }
        }
        detailViewModel.bought?.observe(viewLifecycleOwner, boughtObserver)
        val postVoteObserver = Observer<Boolean> {
            if(it){
                bindingProductDetail.edtVote.text.clear()
                bindingProductDetail.rbVote.rating = 0f
                view?.let { it1 -> Snackbar.make(it1, Constant.SUCCESS_VOTED, Snackbar.LENGTH_SHORT).show() }
                detailViewModel.getListVote(idCate)
                bindingProductDetail.pbLoadVote.gone()
            }else view?.let { it1 -> Snackbar.make(it1, Constant.ERROR_VOTED, Snackbar.LENGTH_SHORT).show() }
        }
        detailViewModel.messageSuccess?.observe(viewLifecycleOwner, postVoteObserver)
    }


    private fun setObserveCartViewModel(){
        val totalProductObserver = Observer<Int?>{
            context?.let { it1 -> MainActivity.icon?.let { it2 -> MainActivity.setBadgeCount(it1, icon = it2, it.toString()) } }
        }
        cartViewModel.totalProduct.observe(requireActivity(), totalProductObserver)
        val resultsObserve = Observer<Boolean?> {
            if(it) {
                makeToast("Thêm vào giỏ hàng thành công")
                cartViewModel.getTotalProduct()
            }else view?.let { it1 -> Snackbar.make(it1, "Bạn được mua tối đa 2 sản phẩm", Snackbar.LENGTH_SHORT).show() }
        }
        cartViewModel.resultAddToCart.observe(requireActivity(), resultsObserve)
    }
    private fun setData(product: CateProductInfo?){
        setDetailProduct(product)
        setDetailSupplier(product)
    }
    private fun setDetailProduct(product: CateProductInfo?){
        bindingProductDetail.tvDetailName.text = product?.name
        bindingProductDetail.tvDetailPrice.text = product?.priceNew.toVND()
        bindingProductDetail.tvDetaiPriceOld.text = product?.priceOld.toVND()
        bindingProductDetail.tvDetaiPriceOld.paintFlags = bindingProductDetail.tvDetaiPriceOld.strikeThrough()
        bindingProductDetail.ratingBarDetail.rating = product?.vote?.ratingBar() ?: 1f
        bindingProductDetail.tvDetailTechnology.text = product?.description
        bindingProductDetail.tvStorageDetail.text = product?.listStorage?.get(1)
        setImg(product?.img, bindingProductDetail.ivDetailPhoto)
        supplier = product?.supplier
        idYT = product?.trailer
        youtubePlayerFragment.initialize(Constant.KEY_API_YOUTUBE, this)
    }
    private fun setSpinner(list: List<String>, spinner: Spinner){
        val adapterSpinner = context?.let { ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                list.toList()
        ) }
        adapterSpinner?.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = adapterSpinner
    }
    private fun setDetailSupplier(product: CateProductInfo?){
        when(product?.supplier?.auth){
            0 -> bindingProductDetail.ivAuth.gone()
            else -> bindingProductDetail.ivAuth.visible()
        }
        bindingProductDetail.tvSupplierName.text = product?.supplier?.name
        setImg(product?.supplier?.logoSupplier, bindingProductDetail.ivSupplierLogo)
    }
    private fun setImg(img: String?, v: ImageView){
        Glide.with(this)
                .load(img)
                .error(R.drawable.noimage)
                .skipMemoryCache(true)
                .into(v)
    }
    private fun initRecyclerViewRelated(){
        relatedProductAdapter = DetailProductAdapter(listRelatedProduct)
        bindingProductDetail.rvRelatedProduct.adapter = relatedProductAdapter
        bindingProductDetail.rvRelatedProduct.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
    private fun initRecyclerViewVote(){
        voteAdapter = DetailProductAdapter(listVote)
        bindingProductDetail.rvVote.adapter = voteAdapter
        bindingProductDetail.rvVote.layoutManager = LinearLayoutManager(context)
    }
    override fun onInitializationSuccess(
            p0: YouTubePlayer.Provider?,
            player: YouTubePlayer?,
            wasRestore: Boolean
    ) {
        if(!wasRestore){
            Log.d("YOUTUBE", idYT.toString())
            player?.cueVideo(idYT)
        }

    }

    override fun onInitializationFailure(
            p0: YouTubePlayer.Provider?,
            p1: YouTubeInitializationResult?
    ) {
        Toast.makeText(context, "Load trailer failure", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        val itemCart = menu.findItem(R.id.fragmentDetailCart)
        val icon: LayerDrawable = itemCart?.icon as LayerDrawable
        bindingProductDetail.btnAddToCart.setOnClickListener {
            MainActivity.setBadgeCount(requireContext(), icon, "10")
        }
    }
}
