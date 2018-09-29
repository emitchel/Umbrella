package com.nerdery.umbrella.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.nerdery.umbrella.R
import com.nerdery.umbrella.data.DateUtil
import com.nerdery.umbrella.data.model.ForecastCondition
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_hour_condition.view.tv_temp
import kotlinx.android.synthetic.main.view_hour_condition.view.tv_time

class HourlyConditionsAdapter(
  private val picasso: Picasso,
  private val context: Context,
  private val forecastConditionsForDay: List<ForecastCondition>
) : BaseAdapter() {

  val inflater = LayoutInflater.from(context)

  override fun getCount(): Int {
    return forecastConditionsForDay.size
  }

  override fun getItem(position: Int): ForecastCondition {
    return forecastConditionsForDay[position]
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getView(
    position: Int,
    convertView: View?,
    parent: ViewGroup
  ): View? {
    var rowView = convertView
    // reuse views
    if (rowView == null) {
      rowView = inflater.inflate(R.layout.view_hour_condition, parent, false)
      // configure view holder
      val viewHolder = ViewHolder(rowView)
      rowView.tag = viewHolder
    }

    // fill data
    val holder = rowView?.tag as ViewHolder
    holder.init(getItem(position))

    return rowView
  }

  inner class ViewHolder(var view: View) {
    fun init(option: ForecastCondition) {
      view.tv_time.text =
          DateUtil.formatDate(option.time, DateUtil.DATE_PARSE_TIME_ONLY, true)
      view.tv_temp.text = context.getString(R.string.weather_temp, option.temp.toString())
    }
  }
}
