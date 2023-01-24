package com.flipp.shoppersdkexample

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.flipp.shoppersdk.core.*
import com.flipp.shoppersdk.views.FlyerEventsListener
import com.flipp.shoppersdk.views.IntegrationFeatures
import com.flipp.shoppersdkexample.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator

private const val testZoneId = 260678L
private const val testSiteId = 1192075L

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Flipp.init(application, "wishabi-test-publisher", testSiteId, "YOUR_USER_ID", true, listOf(testZoneId)) {
            when (it) {
                Flipp.SdkInitCallback.SdkInitResult.OK ->
                    Log.d("SampleApp", "SDK initialized")

                Flipp.SdkInitCallback.SdkInitResult.ERROR ->
                    Log.e("SampleApp", "The impossible has happened?!")
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val flyerView = binding.flyerView1
        val shoppingList = binding.shoppingList
        val bottomSheet = binding.bottomSheet

        val itemsInShoppingList = mutableListOf<String>()
        val shoppingListAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemsInShoppingList)
        shoppingList.adapter = shoppingListAdapter

        flyerView.setSupportedFeatures(IntegrationFeatures.ADD_TO_SHOPPING_LIST, IntegrationFeatures.GO_TO_URL)

        flyerView.setFlyerEventsListener(object: FlyerEventsListener() {
            override fun onAddItemToList(itemName: String) {
                shoppingListAdapter.add(itemName)
            }
        })

        val bsb = BottomSheetBehavior.from(bottomSheet)
        bsb.peekHeight = 240
        bsb.isDraggable = true
        bsb.isFitToContents = true
        bsb.maxHeight = 1200
    }
}