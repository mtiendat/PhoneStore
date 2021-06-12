package com.example.phonestore.services

import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


abstract class EndlessRecyclerViewScrollListener(layoutManager: StaggeredGridLayoutManager) :
    RecyclerView.OnScrollListener() {
    // Số lượng item tối thiểu phải có bên dưới vị trí cuộn hiện tại trước khi tải thêm.
    private var visibleThreshold = 5
    //số trang
    private var currentPage = 1

    // Tổng số item trong array list sau call api tải gần nhất
    private var previousTotalItemCount = 0

    // true nếu vẫn còn đang call api chưa xong
    private var loading = true
    private var mLayoutManager: RecyclerView.LayoutManager = layoutManager
    private val startingPageIndex = 1
    private var distanceToEnd: Int  = 0

    // The minimum amount of pixels to have below your current scroll position
    // before loading more.
    private val visibleThresholdDistance = 30
    init {
        visibleThreshold *= layoutManager.spanCount
    }
    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(view, dx, dy)
        var lastVisibleItemPosition = 0
        val totalItemCount = mLayoutManager.itemCount
        if (mLayoutManager is StaggeredGridLayoutManager) {
            val lastVisibleItemPositions = (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
            // lấy vị trí phần tử cuối trong danh sách
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
        } else if (mLayoutManager is GridLayoutManager) {
            lastVisibleItemPosition = (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
        } else if (mLayoutManager is LinearLayoutManager) {
            lastVisibleItemPosition = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }

        //nếu vẫn đang call api chưa xong
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            currentPage++
            onLoadMore(currentPage, totalItemCount)
            loading = true
        }
    }
//    override fun onScroll(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
//        val view = v?.getChildAt(v.childCount - 1)
//        if (view != null) {
//            var lastVisibleItemPosition = 0
//            distanceToEnd = view.bottom - (v.height + v.scrollY)
//            val totalItemCount = mLayoutManager.itemCount
//            val lastVisibleItemPositions = (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
//            // lấy vị trí phần tử cuối trong danh sách
//            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
//            // If the total item count is zero and the previous isn't, assume the
//            // list is invalidated and should be reset back to initial state
//            // If the             val totalItemCount = mLayoutManager.itemCount item count is zero and the previous isn't, assume the
//            // list is invalidated and should be reset back to initial state
//            if (totalItemCount < previousTotalItemCount) {
//                currentPage = this.startingPageIndex
//                previousTotalItemCount = totalItemCount
//                if (totalItemCount == 0) {
//                    loading = true
//                }
//            }
//
//            // If it’s still loading, we check to see if the dataset count has
//            // changed, if so we conclude it has finished loading and update the current page
//            // number and total item count.
//
//            // If it’s still loading, we check to see if the dataset count has
//            // changed, if so we conclude it has finished loading and update the current page
//            // number and total item count.
//            if (loading && totalItemCount > previousTotalItemCount) {
//                loading = false
//                previousTotalItemCount = totalItemCount
//
//            }
//
//            // If it isn’t currently loading, we check to see if we have breached
//            // the visibleThreshold and need to reload more data.
//            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
//            // threshold should reflect how many total columns there are too
//
//            // If it isn’t currently loading, we check to see if we have breached
//            // the visibleThreshold and need to reload more data.
//            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
//            // threshold should reflect how many total columns there are too
//            if (!loading && distanceToEnd <= visibleThresholdDistance && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
//                currentPage++
//                onLoadMore(currentPage, totalItemCount)
//                loading = true
//            }
//        }
//    }
    abstract fun onLoadMore(page: Int, totalItemsCount: Int)
}