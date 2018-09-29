package com.nerdery.umbrella.ui.activities

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import com.nerdery.umbrella.R
import com.nerdery.umbrella.UmbrellaApp
import com.nerdery.umbrella.data.constants.SettingKeys
import com.nerdery.umbrella.data.constants.TempUnit
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

    rl_units.setOnClickListener {
      val builderSingle = AlertDialog.Builder(this)
      builderSingle.setTitle(getString(R.string.units))

      val arrayAdapter =
        ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice)
      arrayAdapter.add(TempUnit.FAHRENHEIT.toString(this));
      arrayAdapter.add(TempUnit.CELSIUS.toString(this));

      builderSingle.setNegativeButton(
          getString(R.string.cancel)
      ) { dialog, which -> dialog.dismiss() }

      builderSingle.setAdapter(arrayAdapter) { dialog, which ->
        dialog.dismiss()
        sharedPreferences.edit()
            .putString(
                SettingKeys.TEMP_UNIT,
                if (which == 0) TempUnit.FAHRENHEIT.toString(this) else TempUnit.CELSIUS.toString(
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
      val lp = LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.MATCH_PARENT,
          LinearLayout.LayoutParams.MATCH_PARENT
      )
      input.layoutParams = lp
      val dialog = AlertDialog.Builder(this)
          .setTitle(R.string.zip)
      dialog.setView(input)
      dialog.setPositiveButton(R.string.ok) { _, _ ->
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
