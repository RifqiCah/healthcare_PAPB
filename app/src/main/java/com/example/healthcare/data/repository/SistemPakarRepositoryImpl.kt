package com.example.healthcare.data.repository

import com.example.healthcare.domain.model.DiagnosisHistory
import com.example.healthcare.domain.repository.SistemPakarRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SistemPakarRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : SistemPakarRepository {

    private val collectionName = "history_diagnosa"

    // --- 1. SIMPAN DATA KE FIREBASE ---
    override suspend fun saveHistory(history: DiagnosisHistory) {
        val user = auth.currentUser ?: return // Pastikan user login

        // Siapkan data yang mau dikirim
        // Kita isi UID user dan Timestamp server saat ini
        val dataToSave = history.copy(
            uid = user.uid,
            timestamp = Date() // Waktu sekarang
        )

        // Simpan ke Firestore menggunakan ID yang sudah dibuat di ViewModel
        // Gunakan .set() agar ID dokumen sesuai dengan history.id
        try {
            db.collection(collectionName)
                .document(history.id)
                .set(dataToSave)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    // --- 2. AMBIL DATA DARI FIREBASE ---
    override suspend fun getHistory(): List<DiagnosisHistory> {
        val user = auth.currentUser ?: return emptyList()

        return try {
            val snapshot = db.collection(collectionName)
                .whereEqualTo("uid", user.uid) // Hanya ambil data milik user ini
                .orderBy("timestamp", Query.Direction.DESCENDING) // Urutkan dari Terbaru
                .get()
                .await()

            // Ubah hasil Firestore menjadi List<DiagnosisHistory>
            snapshot.documents.map { doc ->
                DiagnosisHistory(
                    id = doc.id,
                    uid = doc.getString("uid") ?: "",
                    penyakit = doc.getString("penyakit") ?: "-",
                    persentase = doc.getDouble("persentase") ?: 0.0,
                    tanggal = doc.getString("tanggal") ?: "",
                    waktu = doc.getString("waktu") ?: "",
                    timestamp = doc.getDate("timestamp")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // --- 3. HAPUS DATA DARI FIREBASE ---
    override suspend fun deleteHistory(historyId: String) {
        try {
            // Hapus dokumen spesifik berdasarkan ID
            db.collection(collectionName)
                .document(historyId)
                .delete()
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}