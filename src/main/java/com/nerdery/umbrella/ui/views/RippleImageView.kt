package com.nerdery.umbrella.ui.views

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue

class RippleImageView : android.support.v7.widget.AppCompatImageView {

  constructor(context: Context) : super(context) {
    init()
  }

  constructor(
    context: Context,
    attrs: AttributeSet
  ) : super(context, attrs) {
    init()
  }

  constructor(
    context: Context,
    attrs: AttributeSet,
    defStyle: Int
  ) : super(context, attrs, defStyle) {
    init()
  }

  private fun init() {
    setBackgroundResource(getThemeSelectableBackgroundId(context))
  }

  fun getThemeSelectableBackgroundId(context: Context): Int {
    //Get selectableItemBackgroundBorderless defined for AppCompat
    var colorAttr = context.resources
        .getIdentifier("selectableItemBackgroundBorderless", "attr", context.packageName)

    if (colorAttr == 0) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        colorAttr = android.R.attr.selectableItemBackgroundBorderless
      } else {
        colorAttr = android.R.attr.selectableItemBackground
      }
    }

    val outValue = TypedValue()
    context.theme.resolveAttribute(colorAttr, outValue, true)
    return outValue.resourceId
  }
}
