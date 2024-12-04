package ken.abel.coba_firebase

import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val db = Firebase.firestore
        lateinit var lvAdapter : ArrayAdapter<daftarProvinsi>
        lateinit var _etProvinsi : EditText
        lateinit var _etIbukota : EditText

        val _btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val _lvData = findViewById<ListView>(R.id.lvData)
        var DataProvinsi = ArrayList<daftarProvinsi>()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        lvAdapter = ArrayAdapter(
                this,
        android.R.layout.simple_list_item_1,
        DataProvinsi
        )
        _lvData.adapter = lvAdapter

        fun TambahData(db: FirebaseFirestore, provinsi: String, ibukota: String) {
            val dataBaru = daftarProvinsi(provinsi, ibukota)
            db.collection("tbProvinsi")
                .add(dataBaru)
                .addOnSuccessListener {
                    _etProvinsi.setText("")
                    _etIbukota.setText("")
                    Log.d("Firebase", "Data Berhasil Disimpan")
                }
                .addOnFailureListener {
                    Log.d("Firebase", it.message.toString())
                }
        }
        _btnSimpan.setOnClickListener {
            val provinsi = _etProvinsi.text.toString()
            val ibukota = _etIbukota.text.toString()
            if (provinsi.isEmpty() && ibukota.isEmpty()) {
                TambahData(db, provinsi, ibukota)
            } else {
                Toast.makeText(this, "Provinsi dan Ibukota harus diisi!", Toast.LENGTH_SHORT).show()
            }
        }
        fun readData(db: FirebaseFirestore) {
            db.collection("tbProvinsi").get()
                .addOnSuccessListener {
                    result ->
                    DataProvinsi.clear()
                    for (document in result) {
                        val readData = daftarProvinsi (
                            document.data.get("provinsi").toString(),
                            document.data.get("ibukota").toString()
                        )
                        DataProvinsi.add(readData)
                    }
                    lvAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener {
                    Log.d("Firebase", it.message.toString())
                }
        }
    }
}