package com.example.phonestore.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.phonestore.base.BaseFragment
import com.example.phonestore.databinding.FragmentContactBinding


class FragmentContact: BaseFragment() {
    private lateinit var binding: FragmentContactBinding
    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun setUI() {
        super.setUI()
        binding.wvContact.webViewClient = WebViewClient()
        binding.wvContact.loadUrl("http://ldmobile.cdth18d.asia/lienhe")
        binding.wvContact.settings.javaScriptEnabled = true
        binding.wvContact.settings.useWideViewPort = true
        binding.wvContact.settings.loadWithOverviewMode = true
        binding.wvContact.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                binding.progressBar6.progress = progress
                if (progress == 100) {
                    binding.progressBar6.visibility = View.GONE
                } else {
                    binding.progressBar6.visibility = View.VISIBLE
                }
            }
        }
    }
    class WebViewController : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
            view.loadUrl(url?:"")
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }
    }
}