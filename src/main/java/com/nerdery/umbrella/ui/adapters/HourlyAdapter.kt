package com.nerdery.umbrella.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.nerdery.umbrella.data.model.DayForecastCondition
import com.squareup.picasso.Picasso

class HourlyAdapter(
  private val hourlyResponse: List<DayForecastCondition>,
  private val context: Context,
  private val picasso: Picasso
) : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return null
  }

  override fun onBindViewHolder(
    holder: ViewHolder,
    position: Int
  ) {

  }

  override fun getItemCount(): Int {
    return 0
  }

  inner class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView)

}
