package com.nerdery.umbrella.ui.activities

import android.R.layout
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog.Builder
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout.LayoutParams
import com.nerdery.umbrella.R
import com.nerdery.umbrella.R.string
import com.nerdery.umbrella.UmbrellaApp
import com.nerdery.umbrella.data.constants.SettingKeys
import com.nerdery.umbrella.data.constants.TempUnit.CELSIUS
import com.nerdery.umbrella.data.constants.TempUnit.FAHRENHEIT
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

    setPreferenceValues()

    setupOnClickListeners()
  }

  private fun setupOnClickListeners() {
    rl_units.setOnClickListener {
      val builderSingle = Builder(this)
      builderSingle.setTitle(getString(string.units))

      val arrayAdapter =
        ArrayAdapter<String>(this, layout.select_dialog_item)
      //text color isn't correct
      arrayAdapter.add(FAHRENHEIT.toString(this))
      arrayAdapter.add(CELSIUS.toString(this))
      builderSingle.setNegativeButton(
          getString(string.cancel)
      ) { dialog, which -> dialog.dismiss() }

      builderSingle.setAdapter(arrayAdapter) { dialog, which ->
        dialog.dismiss()
        sharedPreferences.edit()
            .putString(
                SettingKeys.TEMP_UNIT,
                if (which == 0) FAHRENHEIT.toString(this) else CELSIUS.toString(
                    this
                )
            )
            .apply()
        setPreferenceValues()
      }
      builderSingle.show()
    }

    //TODO: clean up alert dialog
    rl_zip.setOnClickListener {

      val input = EditText(this)
      val lp = LayoutParams(
          LayoutParams.MATCH_PARENT,
          LayoutParams.MATCH_PARENT
      )
      input.layoutParams = lp
      input.setPadding(16, 0, 16, 0)
      val dialog = Builder(this)
          .setTitle(string.zip)
      dialog.setView(input)
      dialog.setPositiveButton(string.ok) { _, _ ->
        if (input.text.toString().isNotEmpty()) {
          sharedPreferences.edit()
              .putLong(SettingKeys.ZIP, input.text.toString().toLong())
              .apply()
          setPreferenceValues()
        }
      }
      dialog.show()
    }
  }

  private fun setPreferenceValues() {
    tv_units_value.text =
        sharedPreferences.getString(SettingKeys.TEMP_UNIT, FAHRENHEIT.toString(this))
    tv_zip_value.text =
        sharedPreferences.getLong(SettingKeys.ZIP, ZipCodes.DEFAULT_ZIPCODE)
            .toString()
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
