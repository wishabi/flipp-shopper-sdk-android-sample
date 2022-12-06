package com.flipp.shoppersdkexample

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.flipp.shoppersdk.core.*
import com.flipp.shoppersdkexample.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

private const val testZoneId = 260678L
private const val testSiteId = 1192075L

class PageAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment = NativeXParentFragment()
}

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("NativeX"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Spec"))
        binding.tabsViewPager.adapter = PageAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.tabsViewPager) { tab, pos ->
            when (pos) {
                0 -> tab.text = "NativeX"
                1 -> tab.text = "Spec"
            }
        }.attach()

        Flipp.init(application, testSiteId, "", true, listOf(testZoneId)) {
            when (it) {
                Flipp.SdkInitCallback.SdkInitResult.OK ->
                    Log.d("SampleApp", "SDK initialized")
                Flipp.SdkInitCallback.SdkInitResult.ERROR ->
                    Log.e("SampleApp", "The impossible has happened?!")
            }
        }
    }
}