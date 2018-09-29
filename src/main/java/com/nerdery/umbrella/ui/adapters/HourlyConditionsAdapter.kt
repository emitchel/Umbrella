package com.nerdery.umbrella.ui.adapters

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.nerdery.umbrella.R
import com.nerdery.umbrella.data.DateUtil
import com.nerdery.umbrella.data.model.ForecastCondition
import com.nerdery.umbrella.data.services.IIconService
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_hour_condition.view.iv_image
import kotlinx.android.synthetic.main.view_hour_condition.view.tv_temp
import kotlinx.android.synthetic.main.view_hour_condition.view.tv_time
import timber.log.Timber
import java.lang.Exception
import java.math.RoundingMode
import java.text.DecimalFormat

class HourlyConditionsAdapter(
  private val picasso: Picasso,
  private val context: Context,
  private val iconService: IIconService,
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
    fun init(forecast: ForecastCondition) {
      view.tv_time.text =
          DateUtil.formatDate(forecast.time, DateUtil.DATE_PARSE_TIME_ONLY, true)
      val df = DecimalFormat("#")
      df.roundingMode = RoundingMode.CEILING
      view.tv_temp.text =
          context.getString(R.string.weather_temp, df.format(forecast.temp).toString())

      val imageUrl =
        iconService.getUrlForIcon(forecast.icon!!, forecast.highestTemp || forecast.lowestTemp)
      Timber.i("Attempting to load image $imageUrl")
      //TODO: picasso why isn't current picasso settings working?
      //only did picasso . get because other version wasn't worknig
      Picasso.get()
          .load(imageUrl)
          .error(R.drawable.ic_image_load_error)
          .placeholder(R.drawable.ic_umbrella_black)
          .into(view.iv_image, object : Callback {
            override fun onError(e: Exception?) {
              Timber.e("Couldn't load forecast for url $imageUrl")
            }

            override fun onSuccess() {
              view.iv_image.setColorFilter(
                  ContextCompat.getColor(context, getColorForForecastCondition(forecast))
              )
            }
          })

      view.tv_time.setTextColor(
          ContextCompat.getColor(context, getColorForForecastCondition(forecast))
      )
      view.tv_temp.setTextColor(
          ContextCompat.getColor(context, getColorForForecastCondition(forecast))
      )
    }
  }

  @ColorRes
  private fun getColorForForecastCondition(forecast: ForecastCondition): Int {
    if (forecast.highestTemp) {
      return R.color.weather_warm
    } else if (forecast.lowestTemp) {
      return R.color.weather_cool
    } else {
      return R.color.text_color_primary
    }
  }
}

