package com.nerdery.umbrella.ui.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import com.nerdery.umbrella.R
import com.nerdery.umbrella.UmbrellaApp
import com.nerdery.umbrella.data.constants.SettingKeys
import com.nerdery.umbrella.data.constants.TempUnit
import com.nerdery.umbrella.data.constants.ZipCodes
import com.nerdery.umbrella.ui.activities.base.BaseActivity
import kotlinx.android.synthetic.main.activity_settings.rl_units
import kotlinx.android.synthetic.main.activity_settings.rl_zip
import kotlinx.android.synthetic.main.activity_settings.tv_units_value
import kotlinx.android.synthetic.main.activity_settings.tv_zip_value
import javax.inject.Inject

class SettingsActivity : BaseActivity() {
  @Inject lateinit var sharedPreferences: SharedPreferences

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    UmbrellaApp.INSTANCE?.component?.inject(this)
    setContentView(R.layout.activity_settings)
    supportActionBar?.setTitle(R.string.app_name)
    supportActionBar?.setHomeButtonEnabled(true)

    tv_units_value.text =
        sharedPreferences.getString(SettingKeys.TEMP_UNIT, TempUnit.FAHRENHEIT.toString(this))
    tv_zip_value.text =
        sharedPreferences.getLong(SettingKeys.ZIP, ZipCodes.DEFAULT_ZIPCODE)
            .toString()

    rl_units.setOnClickListener {

    }

    rl_zip.setOnClickListener {

    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle item selection
    when (item.itemId) {
      android.R.id.home -> {
        //We do this because pressing home actually creates the previous activity rather than using what was existing....
        onBackPressed()
        return true
      }
    }
    return false
  }

}
