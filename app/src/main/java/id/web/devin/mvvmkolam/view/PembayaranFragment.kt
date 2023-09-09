package id.web.devin.mvvmkolam.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import id.web.devin.mvvmkolam.databinding.FragmentPembayaranBinding
import id.web.devin.mvvmkolam.model.StatusTransaksi
import id.web.devin.mvvmkolam.util.formatCurrency
import id.web.devin.mvvmkolam.viewmodel.*
import java.io.File
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import id.web.devin.mvvmkolam.util.Global
import id.web.devin.mvvmkolam.util.SisaWaktu
import id.web.devin.mvvmkolam.util.add24HoursToDateTime
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.time.LocalDateTime


class PembayaranFragment : Fragment() {
    private lateinit var b:FragmentPembayaranBinding
    private lateinit var vMKolam:DetailKolamViewModel
    private lateinit var vMPengguna:ProfilViewModel
    private lateinit var vMTransaksi:TransactionViewModel
    private lateinit var vMUpload: UploadViewModel
    private lateinit var email:String
    private var idKolam:String? = null
    private var idtransaki:String? = null
    private lateinit var handler: Handler
    private var remainingTimeMillis: Long = 0
    private val updateIntervalMillis: Long = 1000
    private val imagePickRequestCode = 100
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentPembayaranBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.progressBarUpload.visibility = View.GONE
        email = context?.let { Global.getEmail(it)}.toString()
        val sharedPreferences = requireActivity().getSharedPreferences("idkolam", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)
        val sharedPreff = requireActivity().getSharedPreferences("idtransaksi", Context.MODE_PRIVATE)
        val idtrx = sharedPreff.getString("idtrx", null)
        idtransaki = idtrx

        idKolam = id
        handler = Handler(Looper.getMainLooper())

        val sharedPref = requireActivity().getSharedPreferences("totalPembelian", Context.MODE_PRIVATE)
        val total = sharedPref.getString("total", null)
        val totalBayar = total
        Log.d("testt", "$idtransaki, $idKolam, $totalBayar")
        b.txtTotalPembayaranPem.text = formatCurrency(totalBayar!!.toDouble())

        b.btnOK.setOnClickListener {
            val action = PembayaranFragmentDirections.actionToPembayaranWaitFragment()
            Navigation.findNavController(it).navigate(action)
        }

        b.btnPilihFile.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openImagePicker()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    imagePickRequestCode
                )
            }
        }

        vMKolam = ViewModelProvider(this).get(DetailKolamViewModel::class.java)
        vMPengguna = ViewModelProvider(this).get(ProfilViewModel::class.java)
        vMTransaksi = ViewModelProvider(this).get(TransactionViewModel::class.java)
        vMUpload = ViewModelProvider(this).get(UploadViewModel::class.java)
        vMKolam.fetchData(idKolam.toString())
        vMTransaksi.fetchTransactionDetail(email, StatusTransaksi.Diproses.name, idtransaki.toString())
        observeView()
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, imagePickRequestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == imagePickRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker()
            } else {
                val intent = Intent(context,PembayaranFragment::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == imagePickRequestCode && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            b.txtNamaBukti.text = "Terpilih"
        }
    }

    private fun copyToClipboard(disalin: String) {
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("label", disalin)
        clipboardManager.setPrimaryClip(clipData)
    }

    private fun startCountdown() {
        handler.post(object : Runnable {
            override fun run() {
                val hours = remainingTimeMillis / (60 * 60 * 1000)
                val minutes = (remainingTimeMillis % (60 * 60 * 1000)) / (60 * 1000)
                val seconds = (remainingTimeMillis % (60 * 1000)) / 1000

                val countdownText = String.format("%02d jam %02d menit %02d detik", hours, minutes, seconds)
                b.txtWaktuPembayaran.text = countdownText

                if (remainingTimeMillis > 0) {
                    remainingTimeMillis -= updateIntervalMillis
                    handler.postDelayed(this, updateIntervalMillis)
                } else {
                    vMTransaksi.updateStatus(idtransaki.toString(),StatusTransaksi.Dibatalkan.name)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }

    private fun observeView() {
        vMTransaksi.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it){
                b.progressBarUpload.visibility = View.VISIBLE
            }else{
                b.progressBarUpload.visibility = View.GONE
            }
        })
        vMKolam.kolamLD.observe(viewLifecycleOwner, Observer {
            val emailAdmin = it.admin
            Log.d("emailadmin", it.admin.toString())
            vMPengguna.fetchProfil(emailAdmin.toString())
        })

        vMPengguna.userLD.observe(viewLifecycleOwner, Observer {user->
            b.txtNomorRekening.text = user.norekening
            b.txtNamaRekening.text = user.nama_rekening
            Log.d("rekening", user.norekening.toString())
            b.salinButton.setOnClickListener {
                val disalin = user.norekening
                copyToClipboard(disalin!!)
                Toast.makeText(context,"Disalin", Toast.LENGTH_SHORT).show()
            }
        })
        vMUpload.uploadResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is UploadViewModel.Result.Success -> {
                    val imageUrl = result.data.result
                    Toast.makeText(context,"Berhasil Mengunggah Bukti",Toast.LENGTH_SHORT).show()
                    Log.d("succes", imageUrl)
                }
                is UploadViewModel.Result.Error -> {
                    val errorMessage = result.exception.message
                    Log.d("error", errorMessage.toString())
                }
                else -> {}
            }
        })
        vMTransaksi.transaksiDetailLD.observe(viewLifecycleOwner, Observer {trx->
            b.btnUploadBukti.setOnClickListener {
                if (selectedImageUri != null){
                    // Dapatkan path file dari Uri
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = requireContext().contentResolver.query(selectedImageUri!!, filePathColumn, null, null, null)

                    if (cursor != null) {
                        cursor.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val imagePath = cursor.getString(columnIndex)
                        cursor.close()
                        val file = File(imagePath)
                        val requestBody = RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file)
                        val imagePart = MultipartBody.Part.createFormData("image", "B${trx.id}.jpg", requestBody)
                        val url = "https://lokowai.shop/image/bukti/B${trx.id}.jpg"
                        val folderValue = "bukti"
                        val folderRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), folderValue)
                        b.txtNamaBukti.text = "B${trx.id}.jpg"
                        vMUpload.uploadImage(imagePart,folderRequestBody)
                        vMTransaksi.updateBuktiPembayaran(url,trx.id)
                    }

                }else{
                    AlertDialog.Builder(context).apply {
                        val message = SpannableString("Anda belum memilih foto")
                        message.setSpan(
                            AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                            0,
                            message.length,
                            0
                        )
                        setMessage(message)
                        setPositiveButton("OK", null)
                        create().show()
                    }
                }
            }
            remainingTimeMillis =  SisaWaktu(trx.produk[0].tanggal)
            Log.d("waktu",trx.produk[0].tanggal)
            Log.d("24jam", add24HoursToDateTime(trx.produk[0].tanggal))
            startCountdown()
        })
    }

}