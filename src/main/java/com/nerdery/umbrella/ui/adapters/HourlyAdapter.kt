package com.nerdery.umbrella.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nerdery.umbrella.R
import com.nerdery.umbrella.data.DateUtil
import com.nerdery.umbrella.data.model.DayForecastCondition
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_hourly_condition.view.fb_hour_conditions
import kotlinx.android.synthetic.main.adapter_hourly_condition.view.tv_day
import java.util.Calendar

class HourlyAdapter(
  private val dayForecastConditions: List<DayForecastCondition>,
  private val context: Context,
  private val picasso: Picasso
) : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {

  private var inflater: LayoutInflater = LayoutInflater.from(context)
  private var today = DateUtil.getTodayAt0()
  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    return ViewHolder(inflater.inflate(R.layout.adapter_hourly_condition, parent, false))
  }

  override fun onBindViewHolder(
    holder: ViewHolder,
    position: Int
  ) {
    holder.bind(dayForecastConditions[position])
  }

  override fun getItemCount(): Int {
    return dayForecastConditions.count()
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var day = itemView.tv_day
    var flexBox = itemView.fb_hour_conditions
    fun bind(dayForecastCondition: DayForecastCondition) {
//      time.text = DateUtil.formatDate(dayForecastCondition.day, DateUtil.DATE_PARSE_TIME_ONLY, true)
//      temp.text = context.getString(R.string.weather_temp, dayFore)
      val calendarDay = DateUtil.dateToCalendar(dayForecastCondition.day)
      when {
        calendarDay.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) -> day.text =
            context.getString(R.string.today)
        (calendarDay.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR)) == 1 -> day.text =
            context.getString(R.string.tomorrow)
        else -> day.text =
            DateUtil.formatDate(dayForecastCondition.day, DateUtil.DATE_PARSE_MONDAY_DAY, true)
      }

    }
  }

}
