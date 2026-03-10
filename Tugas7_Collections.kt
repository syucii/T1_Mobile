

data class NilaiMahasiswa(
    val nim: String,        // Nomor Induk Mahasiswa
    val nama: String,       // Nama mahasiswa
    val mataKuliah: String, // Nama mata kuliah
    val nilai: Int          // Nilai 0-100
)

//Konversi Nilai ke Grade

fun getGrade(nilai: Int): String {
    return when (nilai) {
        in 85..100 -> "A"
        in 70..84  -> "B"
        in 60..69  -> "C"
        in 50..59  -> "D"
        else       -> "E"
    }
}

fun main() {

    // BUAT DATA MAHASISWA
    //membuat list yang isinya tidak bisa diubah 
    val dataMahasiswa = listOf(
        NilaiMahasiswa("F1D02310138", "Sucitasari Rahmadani", "Pemrograman Mobile", 95),
        NilaiMahasiswa("F1D02410114", "HAIDAR WAHYU ASHARI",   "Pemrograman Mobile", 85),
        NilaiMahasiswa("F1D02410117", "LALU AHMAD FAIZ HAQIQI", "Pemrograman Mobile", 85),
        NilaiMahasiswa("F1D02410075", "MUHAMMAD AKBAR", "Pemrograman Mobile", 87),
        NilaiMahasiswa("F1D02410090", "RIFDAH ASYLIYAH", "Pemrograman Mobile", 80),
        NilaiMahasiswa("F1D02410101", "AJRIYA DANUARTA", "Pemrograman Mobile", 85),
        NilaiMahasiswa("F1D02410057", "INDIRA RAMADHANI SABRINA",  "Pemrograman Mobile", 85)
    )

    // 1. TAMPILKAN SEMUA DATA MAHASISWA
    println("===== DATA NILAI MAHASISWA =====")
    println("No  NIM      Nama              MataKuliah            Nilai  Grade")
    println("--  -------  ----------------  --------------------  -----  -----")

    // forEachIndexed = loop sambil mendapat nomor urut (index mulai dari 0)
    dataMahasiswa.forEachIndexed { index, mhs ->
        
        val no     = (index + 1).toString().padEnd(4) //padEnd() = tambah spasi di kanan agar lurus/rapi
        val nim    = mhs.nim.padEnd(9)
        val nama   = mhs.nama.padEnd(18)
        val mk     = mhs.mataKuliah.padEnd(22)
        val nilai  = mhs.nilai.toString().padEnd(7)
        val grade  = getGrade(mhs.nilai)
        println("$no$nim$nama$mk$nilai$grade")
    }

    // 2. STATISTIK
    println()
    println("===== STATISTIK =====")

    //menghitung jumlah elemen dalam list
    val totalMahasiswa = dataMahasiswa.count()

    //hitung rata-rata dari list angka
    val rataRata = dataMahasiswa.map { it.nilai }.average() // ambil hanya nilai dari setiap mahasiswa, hasilnya List<Int>

    val nilaiTertinggi = dataMahasiswa.maxByOrNull { it.nilai } //cari elemen dengan nilai terbesar
    val nilaiTerendah  = dataMahasiswa.minByOrNull { it.nilai } //cari elemen dengan nilai terkecil

    // ? = null 
    println("Total Mahasiswa : $totalMahasiswa")
    println("Rata-rata Nilai : $rataRata")
    println("Nilai Tertinggi : ${nilaiTertinggi?.nilai} (${nilaiTertinggi?.nama})")
    println("Nilai Terendah  : ${nilaiTerendah?.nilai} (${nilaiTerendah?.nama})")

    // 3. MAHASISWA LULUS (nilai >= 70)
    println()
    println("===== MAHASISWA LULUS =====")

    val mahasiswaLulus = dataMahasiswa.filter { it.nilai >= 70 }.sortedByDescending { it.nilai } //mengambil yang sesuai kondisi dan urutkan dari nilai tertinggi

    if (mahasiswaLulus.isEmpty()) {
        println("Tidak ada mahasiswa yang lulus.")
    } else {
        mahasiswaLulus.forEachIndexed { index, mhs ->
            println("${index + 1}. ${mhs.nama} - ${mhs.nilai} (${getGrade(mhs.nilai)})")
        }
    }

    // 4. JUMLAH MAHASISWA PER GRADE
    println()
    println("===== JUMLAH PER GRADE =====")

    //key = grade, value = list mahasiswa dengan grade itu
    val kelompokGrade = dataMahasiswa.groupBy { getGrade(it.nilai) } //mengelompokkan list berdasarkan suatu nilai

    for (grade in listOf("A", "B", "C", "D", "E")) {
        val jumlah = kelompokGrade[grade]?.size ?: 0   //size = jumlah elemen
        println("Grade $grade: $jumlah mahasiswa")
    }
   
}