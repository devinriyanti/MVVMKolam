package id.web.devin.mvvmkolam.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import id.web.devin.mvvmkolam.databinding.FragmentPilihKotaBinding
import id.web.devin.mvvmkolam.viewmodel.ProvinsiListViewModel

class PilihKotaFragment : Fragment() {
    private lateinit var b: FragmentPilihKotaBinding
    private lateinit var vMProvinsi:ProvinsiListViewModel
    private var provinsi:String = ""
    private var selectedKotaId: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentPilihKotaBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vMProvinsi = ViewModelProvider(this).get(ProvinsiListViewModel::class.java)
        vMProvinsi.fetchProvinsi()
        observeView()
    }

    private fun observeView() {
        vMProvinsi.provinsiLD.observe(viewLifecycleOwner, Observer { provinsiList ->
            // Extract nama provinsi dari list provinsi
            val namaProvinsiList = provinsiList.map { it.nama }

            // Set Spinner dengan data nama provinsi
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, namaProvinsiList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            b.spinnerProvinsi.adapter = adapter

            b.spinnerProvinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    provinsi = namaProvinsiList[position]

                    // Ambil data kota dari provinsi yang dipilih
                    val kotaList = provinsiList[position].kota

                    // Extract nama kota dari list kota
                    val namaKotaList = kotaList.map { it.nama }

                    // Set Spinner kota dengan data nama kota
                    val kotaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, namaKotaList)
                    kotaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    b.spinnerKota.adapter = kotaAdapter

                    b.spinnerKota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            // Mengambil ID kota yang dipilih dari list kota
                            selectedKotaId = kotaList[position].idkota
                            b.btnLanjutRegis.setOnClickListener {
                                val action = PilihKotaFragmentDirections.actionRegisPFragment(selectedKotaId)
                                Navigation.findNavController(it).navigate(action)
                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        })
    }
}