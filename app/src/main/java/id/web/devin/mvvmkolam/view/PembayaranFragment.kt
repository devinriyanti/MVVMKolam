package id.web.devin.mvvmkolam.view

import android.Manifest
import android.app.Activity
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
    private var remainingTimeMillis: Long = 23 * 60 * 60 * 1000 + 59 * 60 * 1000 + 59 * 1000 // Total waktu dalam milidetik
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
        email = context?.let { Global.getEmail(it)}.toString()
        val sharedPreferences = requireActivity().getSharedPreferences("idkolam", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)
        val sharedPreff = requireActivity().getSharedPreferences("idtransaksi", Context.MODE_PRIVATE)
        val idtrx = sharedPreff.getString("idtrx", null)
        idtransaki = idtrx

        idKolam = id
        handler = Handler(Looper.getMainLooper())
        startCountdown()

        val sharedPref = requireActivity().getSharedPreferences("totalPembelian", Context.MODE_PRIVATE)
        val total = sharedPref.getString("total", null)
        val totalBayar = total

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
                // Handle permission denied
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == imagePickRequestCode && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, imagePickRequestCode)
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
        vMKolam.kolamLD.observe(viewLifecycleOwner, Observer {
            val emailAdmin = it.admin
            Log.d("emailadmin", it.admin.toString())
            vMPengguna.fetchProfil(emailAdmin.toString())
        })

        vMPengguna.userLD.observe(viewLifecycleOwner, Observer {user->
            b.txtNomorRekening.text = user.norekening
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
                    val imageUrl = result.data.imageUrl
                    // Handle success, maybe display the uploaded image or show a message
                }
                is UploadViewModel.Result.Error -> {
                    val errorMessage = result.exception.message
                    // Handle error, show an error message to the user
                }
            }
        })
        vMTransaksi.transaksiDetailLD.observe(viewLifecycleOwner, Observer {
            b.imgUploadBukti.setOnClickListener {
                selectedImageUri?.let { uri ->
//                val inputStream = requireContext().contentResolver.openInputStream(uri)
//                val imageFile = File(requireContext().cacheDir, "temp_image.jpg")
//                inputStream?.use { input ->
//                    imageFile.outputStream().use { output ->
//                        input.copyTo(output)
//                    }
//                }
//                val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
//                val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
//                vMUpload.uploadImage(imagePart)
                    //masih belum berhasil
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val outputFile = File(requireContext().filesDir, "B${it.id}.jpg")
                    inputStream?.use { input ->
                        outputFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    Toast.makeText(context,"Berhasil Mengunggah Bukti",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}