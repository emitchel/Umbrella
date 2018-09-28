package com.nerdery.umbrella.ui.activities.base

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.nerdery.umbrella.R
import com.nerdery.umbrella.ui.dialogs.UmbrellaDialog

abstract class BaseActivity : AppCompatActivity() {
  private var umbrellaDialog: UmbrellaDialog? = null

  fun showProgressDialog() {
    if (umbrellaDialog == null) {
      umbrellaDialog = UmbrellaDialog.newInstance()
    }

    if (!umbrellaDialog!!.isAdded && supportFragmentManager.findFragmentByTag(
            UmbrellaDialog::class.java.name
        ) == null
    ) {
      umbrellaDialog!!.show(supportFragmentManager, UmbrellaDialog::class.java.name)
    }
  }

  fun hideProgressDialog() {
    if (umbrellaDialog != null && umbrellaDialog!!.dialog != null && umbrellaDialog!!.dialog
            .isShowing
    ) {
      umbrellaDialog!!.dismiss()
    }
  }

  override fun startActivity(intent: Intent) {
    super.startActivity(intent)
    overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale)
  }

  override fun finish() {
    super.finish()
    overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate)
  }
}
