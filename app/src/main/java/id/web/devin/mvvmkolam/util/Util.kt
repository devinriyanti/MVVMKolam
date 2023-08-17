package id.web.devin.mvvmkolam.util

import android.app.Activity
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import id.web.devin.mvvmkolam.R
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.NumberFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

fun ImageView.loadImage(url:String, progressBar: ProgressBar){
    Picasso.get()
        .load(url)
        .resize(1000,1000)
        .centerCrop()
        .error(R.drawable.ic_baseline_error_24)
        .into(this, object :Callback{
            override fun onSuccess() {
                progressBar.visibility = View.GONE
            }
            override fun onError(e: Exception?) {
                progressBar.visibility = View.GONE
            }
        })
}

fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return format.format(amount)
}

fun calculateTotalYears(dateString: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateString, formatter)
    val currentDate = LocalDate.now()
    val period = Period.between(date, currentDate)
    return period.years
}

fun formatDate(inputDate: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale("id","ID"))

    val date = inputFormat.parse(inputDate)
    return outputFormat.format(date)
}

fun formatDate2(inputDate: String): String {
    val inputFormat = SimpleDateFormat("d MMMM yyyy", Locale("id","ID"))
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val date = inputFormat.parse(inputDate)
    return outputFormat.format(date)
}

object EncryptionUtils{
    private const val secretKey = "mySecretKey"
    fun encrypt(text: String): String {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        val secretKeySpec = SecretKeySpec(generateKey(secretKey), "AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        val encryptedBytes = cipher.doFinal(text.toByteArray(StandardCharsets.UTF_8))
        return android.util.Base64.encodeToString(encryptedBytes, android.util.Base64.DEFAULT)
    }

    fun decrypt(encryptedText: String): String {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        val secretKeySpec = SecretKeySpec(generateKey(secretKey), "AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
        val encryptedBytes = android.util.Base64.decode(encryptedText, android.util.Base64.DEFAULT)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, StandardCharsets.UTF_8)
    }

    private fun generateKey(secretKey: String): ByteArray? {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(secretKey.toByteArray(StandardCharsets.UTF_8))
    }
}

object Global {
    const val SHARED_PREFERENCES = "LOKOWAI"
    const val SHARED_PREF_EMAIL = "EMAIL"
    const val SHARED_PREF_KEY_ROLE = "ROLE"
    fun getEmail(context: Context): String? {
        return context.getSharedPreferences(SHARED_PREFERENCES, Activity.MODE_PRIVATE)
            .getString(SHARED_PREF_EMAIL, null)
    }

    fun getRole(context: Context): String? {
        return context.getSharedPreferences(SHARED_PREFERENCES, Activity.MODE_PRIVATE)
            .getString(SHARED_PREF_KEY_ROLE, null)
    }
}