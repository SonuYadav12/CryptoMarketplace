package com.example.crypto.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.crypto.R
import com.example.crypto.adapter.MarketAdapter
import com.example.crypto.apis.ApiInterface
import com.example.crypto.apis.ApiUtilities
import com.example.crypto.databinding.FragmentWatchListBinding
import com.example.crypto.models.CryptoCurrency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WatchListFragment : Fragment() {

    private lateinit var binding: FragmentWatchListBinding
    private lateinit var watchList:ArrayList<String>
    private lateinit var watchListItem:ArrayList<CryptoCurrency>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentWatchListBinding.inflate(layoutInflater)
        readData()

        lifecycleScope.launch(Dispatchers.IO) {
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if (res.body() != null) {
                withContext(Dispatchers.Main) {
                    watchListItem = ArrayList()
                    watchListItem.clear()


                    for (watchData in watchList) {
                        for (item in res.body()!!.data.cryptoCurrencyList) {
                            if (watchData == item.symbol) {
                                watchListItem.add(item)
                            }
                        }
                    }

                    binding.spinKitView.visibility = GONE
                    binding.watchlistRecyclerView.adapter =
                        MarketAdapter(requireContext(), watchListItem, "watchfragment")
                }
            }
        }

            return binding.root

    }

    private fun readData() {
        val sharedPreferences=requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val gson= Gson()
        val json=sharedPreferences.getString("watchlist",ArrayList<String>().toString())
        val type=object : TypeToken<ArrayList<String>>(){}.type
        watchList=gson.fromJson(json,type)
    }


}