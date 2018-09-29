package com.nerdery.umbrella.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nerdery.umbrella.R
import com.nerdery.umbrella.data.DateUtil
import com.nerdery.umbrella.data.model.DayForecastCondition
import com.nerdery.umbrella.data.services.IIconService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_hourly_condition.view.grid_hours
import kotlinx.android.synthetic.main.adapter_hourly_condition.view.tv_day
import java.util.Calendar

class DayForecastConditionAdapter(
  private val iconService: IIconService,
  private val dayForecastConditions: List<DayForecastCondition>,
  private val context: Context,
  private val picasso: Picasso
) : RecyclerView.Adapter<DayForecastConditionAdapter.ViewHolder>() {

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

    fun bind(dayForecastCondition: DayForecastCondition) {
      var day = itemView.tv_day
      var grid = itemView.grid_hours
      val calendarDay = DateUtil.dateToCalendar(dayForecastCondition.day)
      when {
        calendarDay.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) -> day.text =
            context.getString(R.string.today)
        (calendarDay.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR)) == 1 -> day.text =
            context.getString(R.string.tomorrow)
        else -> day.text =
            DateUtil.formatDate(dayForecastCondition.day, DateUtil.DATE_PARSE_MONDAY_DAY, true)
      }

      //TODO: this is NOT efficient, what is the better approach here?
      val gridAdapter =
        HourlyConditionsAdapter(picasso, context, dayForecastCondition.forecastConditions)
      grid.adapter = gridAdapter

    }

  }
}


