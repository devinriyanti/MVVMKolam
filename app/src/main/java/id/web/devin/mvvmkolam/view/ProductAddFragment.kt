package id.web.devin.mvvmkolam.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import id.web.devin.mvvmkolam.R
import id.web.devin.mvvmkolam.databinding.FragmentProductAddBinding
import id.web.devin.mvvmkolam.viewmodel.PelatihListViewModel
import id.web.devin.mvvmkolam.viewmodel.ProductListViewModel
import id.web.devin.mvvmkolam.viewmodel.UploadViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ProductAddFragment : Fragment() {
    private lateinit var b:FragmentProductAddBinding
    private lateinit var vMProduk: ProductListViewModel
    private lateinit var vMUpload: UploadViewModel
    private var nama:String = ""
    private var qty:Int = 0
    private var deskripsi:String = ""
    private var harga:Double = 0.0
    private var diskon:Double = 0.0
    private var berat:Int = 0
    private var kolamID:String = ""
    private val imagePickRequestCode = 100
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentProductAddBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vMProduk = ViewModelProvider(this).get(ProductListViewModel::class.java)
        vMUpload = ViewModelProvider(this).get(UploadViewModel::class.java)
        val sharedPreferences = requireActivity().getSharedPreferences("kolam", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)
        kolamID = id.toString()

        b.btnPilihFileProduk.setOnClickListener {
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

        b.btnAddProduk.setOnClickListener {
            nama = b.editTextNamaProdukAdd.text.toString()
            qty = b.editTextKuantitasProdukAdd.text.toString().toInt()
            deskripsi = b.editTextDeskripsiProdukAdd.text.toString()
            harga = b.editTextHargaProdukAdd.text.toString().toDouble()
            diskon = b.editTextDiskonProdukAdd.text.toString().toDouble()
            berat = b.editTextBeratProdukAdd.text.toString().toInt()

            if (nama.isNotEmpty() && qty.toString().isNotEmpty() && deskripsi.isNotEmpty() && harga.toString().isNotEmpty() && diskon.toString().isNotEmpty() && berat.toString().isNotEmpty()){
                if (selectedImageUri != null){
                    // Dapatkan path file dari Uri
                    val produk = nama.replace(" ", "")
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = requireContext().contentResolver.query(selectedImageUri!!, filePathColumn, null, null, null)

                    if (cursor != null) {
                        cursor.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val imagePath = cursor.getString(columnIndex)
                        cursor.close()
                        val file = File(imagePath)
                        val requestBody = RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file)
                        val imagePart = MultipartBody.Part.createFormData("image", "PD$produk.jpg", requestBody)
                        val url = "https://lokowai.shop/image/produk/PD$produk.jpg"
                        val folderValue = "produk"
                        val folderRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), folderValue)
                        b.txtPilihFileProduk.setText("PD$produk.jpg")
                        vMUpload.uploadImage(imagePart,folderRequestBody)
                        vMProduk.insertProduct(nama,deskripsi,qty,harga,diskon,url,berat,kolamID)
                        insertProduk()
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
            }else{
                AlertDialog.Builder(context).apply {
                    val message = SpannableString("Data Tidak Boleh Kosong!")
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
        b.btnBatalAddProduk.setOnClickListener {
            AlertDialog.Builder(context).apply {
                val title = SpannableString("Peringatan")
                title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                val message = SpannableString("Batal Menambahkan Pelatih?")
                message.setSpan(
                    AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0,
                    message.length,
                    0
                )
                setTitle(title)
                setMessage(message)
                setPositiveButton("BATAL"){ dialog,_->
                    val action = ProductAddFragmentDirections.actionPADetailKolamFragment()
                    Navigation.findNavController(it).navigate(action)
                }
                setNegativeButton("TIDAK"){ dialog,_->
                    dialog.dismiss()
                }
                create().show()
            }
        }
    }

    private fun insertProduk() {
        vMProduk.statusLD.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it == true){
                AlertDialog.Builder(context).apply {
                    val message = SpannableString("Berhasil Menambah Produk")
                    message.setSpan(
                        AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0,
                        message.length,
                        0
                    )
                    setMessage(message)
                    setPositiveButton("OK") { _, _ ->
                        val action = ProductAddFragmentDirections.actionPADetailKolamFragment()
                        findNavController().navigate(action)
                    }
                    create().show()
                }
            }else{
                AlertDialog.Builder(context).apply {
                    val message = SpannableString("Gagal Menambahkan Produk")
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
        })
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, imagePickRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == imagePickRequestCode && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            b.txtPilihFileProduk.setText("Gambar Terpilih")
        }
    }
}