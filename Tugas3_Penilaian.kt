fun main() {

    // Menampilkan judul program
    println("===== SISTEM PENILAIAN =====")

    // user memasukkan nama mahasiswa
    print("Masukkan Nama Mahasiswa: ")
    val nama = readLine() ?: ""   // membaca input dari keyboard

    // user memasukkan nilai UTS
    print("Masukkan Nilai UTS (0-100): ")
    val uts = readLine()?.toDoubleOrNull() ?: 0.0   //mengubah teks ke angka desimal

    if (uts < 0 || uts > 100) {
        println("Nilai UTS harus antara 0 sampai 100!")
        return 
    }

    // user memasukkan nilai UAS
    print("Masukkan Nilai UAS (0-100): ")
    val uas = readLine()?.toDoubleOrNull() ?: 0.0  

    if (uas < 0 || uas > 100) {
        println("Nilai UAS harus antara 0 sampai 100!") 
        return
    }

    // user memasukkan nilai Tugas
    print("Masukkan Nilai Tugas (0-100): ")
    val tugas = readLine()?.toDoubleOrNull() ?: 0.0 

    if (tugas < 0 || tugas > 100) {
    println("Nilai Tugas harus antara 0 sampai 100!")
    return 
    }

    //  HITUNG NILAI AKHIR 
    val nilaiAkhir = (uts * 0.3) + (uas * 0.4) + (tugas * 0.3)

    //KONVERSI NILAI KE HURUF

    val nilaiInt = nilaiAkhir.toInt()   // ubah ke Int supayaa bisa pakai range

    val grade = when (nilaiInt) { //cek nilaiAkhir masuk di range mana
        in 85..100 -> "A"
        in 70..84  -> "B"
        in 60..69  -> "C"
        in 50..59  -> "D"
        else       -> "E"  
    }

    //KETERANGAN BERDASARKAN Grade 

    val keterangan = when (grade) { // untuk menentukan keterangan dari grade
        "A" -> "Sangat Baik"
        "B" -> "Baik"
        "C" -> "Cukup"
        "D" -> "Kurang"
        else -> "Sangat Kurang"
    }

    //CEK STATUS KELULUSAN

    val lulus = nilaiAkhir >= 60 

    val status = if (lulus) "LULUS" else "TIDAK LULUS"

    //TAMPILKAN HASIL
    println()
    println("===== HASIL PENILAIAN =====")
    println("Nama        : $nama")
    println("Nilai UTS   : $uts (Bobot 30%)")
    println("Nilai UAS   : $uas (Bobot 40%)")
    println("Nilai Tugas : $tugas (Bobot 30%)")
    println("-----------------------------")
    println("Nilai Akhir : $nilaiAkhir")
    println("Grade       : $grade")
    println("Keterangan  : $keterangan")
    println("Status      : $status")
    println()

    //status kelulusan
    if (lulus) {
        println("Selamat! Anda dinyatakan LULUS. Seperti Sucitasari Rahmadani NIM F1D02310138")
    } else {
        println("Maaf, Anda dinyatakan TIDAK LULUS. Semangat belajar!")
    }
}
