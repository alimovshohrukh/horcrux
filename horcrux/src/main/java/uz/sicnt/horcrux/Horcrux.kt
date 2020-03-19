package uz.sicnt.horcrux

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.preference.PreferenceManager
import android.util.Base64
import androidx.appcompat.app.AlertDialog
import uz.sicnt.horcrux.Constants.*
import java.util.regex.Pattern

class Horcrux : Activity(), DialogInterface.OnClickListener {

    var tag: String = "HORCRUX"
    var appKey = BuildConfig.API_KEY

    private lateinit var pkcs7: String
    private var serialNumber: CharSequence? = null
    private var signature: ByteArray? = null
    private var subject: CharSequence? = null

    private var regex = ",?(\\\\s)*([A-Za-z]+|[0-9\\\\.]+)=([^=,\\\\]*),?(\\\\s)*"
    private var p: Pattern = Pattern.compile(regex)

    /**
     * @param context      Current activity
     * @param massage      String value to generate PKCS7
     * @param serialNumber App serial number (Optional)
     */
    fun createPKCS7(
        context: Activity,
        massage: String,
        serialNumber: String?
    ) {
        val message = massage.toByteArray()
        val intent = Intent()
        intent.setClassName(
            E_IMZO_APP,
            E_IMZO_ACTIVITY
        )
        intent.putExtra(EXTRA_PARAM_API_KEY, appKey)
        intent.putExtra(EXTRA_PARAM_SERIAL_NUMBER, serialNumber)
        intent.putExtra(EXTRA_PARAM_MESSAGE, message)
        context.startActivityForResult(intent, CREATE_PKCS7)
    }

    /**
     * @param context Current activity
     * @param massage String value to generate PKCS7
     */
    fun createPKCS7(context: Activity, massage: String) {
        val serialNumber = ""
        val message = massage.toByteArray()
        val intent = Intent()
        intent.setClassName(
            E_IMZO_APP,
            E_IMZO_ACTIVITY
        )
        intent.putExtra(EXTRA_PARAM_API_KEY, appKey)
        intent.putExtra(EXTRA_PARAM_SERIAL_NUMBER, serialNumber)
        intent.putExtra(EXTRA_PARAM_MESSAGE, message)
        context.startActivityForResult(intent, CREATE_PKCS7)
    }

    /**
     * Parse PFX file
     */
    fun parsePFX(data: Intent?) {
        pkcs7 = Base64.encodeToString(
            data!!.getByteArrayExtra(EXTRA_RESULT_PKCS7),
            Base64.NO_WRAP
        )
        serialNumber =
            data.getCharSequenceExtra(EXTRA_RESULT_SERIAL_NUMBER)
        signature =
            data.getByteArrayExtra(EXTRA_RESULT_SIGNATURE)
        subject =
            data.getCharSequenceExtra(EXTRA_RESULT_SUBJECT_NAME)

        writeString(EXTRA_RESULT_PKCS7, pkcs7)
        writeString(EXTRA_RESULT_SERIAL_NUMBER, serialNumber as String)
        writeString(EXTRA_RESULT_SUBJECT_NAME, subject as String)
    }

    /**
     * @return Check if user is Legal
     */
    fun isLegal(): Boolean {
        val subject = readString(EXTRA_RESULT_SUBJECT_NAME)
        return subject!!.contains(YUR_TIN)
    }

    /**
     * @return Check if user is Individual
     */
    fun isIndividual(): Boolean {
        val subject = readString(EXTRA_RESULT_SUBJECT_NAME)
        return !subject!!.contains(YUR_TIN) && subject.contains(FIZ_TIN)
    }

    /**
     * Return tin
     */
    fun getTin(): String {
        val subject = readString(EXTRA_RESULT_SUBJECT_NAME)
        val m = p.matcher(subject as CharSequence)
        while (m.find()) {
            if (m.group().contains(YUR_TIN)) {
                val yurTin = m.group().split("#").toTypedArray()
                return decodeHex(yurTin[1]).trim()
            } else if (m.group().contains(FIZ_TIN)) {
                val fizTin = m.group().split("=").toTypedArray()
                return fizTin[1].replace(",", "").trim()
            }
        }
        return ""
    }

    fun getPKCS(): String? {
        return readString(EXTRA_RESULT_PKCS7)
    }

    /**
     * @return Check if E-imzo app installed
     */
    fun isEImzoInstalled(activity: Activity): Boolean {
        val packageManager = activity.packageManager
        var found = true
        try {
            packageManager.getPackageInfo(E_IMZO_APP, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            found = false
        }
        return found
    }

    /**
     * Show installation dialog
     */
    fun showInstallDialog() {
        AlertDialog.Builder(this, R.style.exitDialog)
            .setMessage(getString(R.string.eimzo_not_install))
            .setCancelable(false)
            .setPositiveButton(R.string.yes, this)
            .setNegativeButton(R.string.no, this).create().show()
    }

    /**
     * @param hex String value
     * @return Decoded byte array
     */
    private fun decodeHex(hex: String): String {
        val sb = StringBuilder()
        val hexData = hex.toCharArray()
        var count = 0
        while (count < hexData.size - 1) {
            val firstDigit = Character.digit(hexData[count], 16)
            val lastDigit = Character.digit(hexData[count + 1], 16)
            val decimal = firstDigit * 16 + lastDigit
            sb.append(decimal.toChar())
            count += 2
        }
        return sb.toString().trim { it <= ' ' }
    }

    /**
     * Save temp data   FIXME
     */
    private fun writeString(key: String?, property: String?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.context)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, property)
        editor.apply()
    }

    /**
     * Read temp data   FIXME
     */
    private fun readString(key: String?): String? {
        val sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(MyApplication.context)
        return sharedPreferences.getString(key, "")
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$E_IMZO_APP")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$E_IMZO_APP")
                    )
                )
            }
            DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
        }
    }
}