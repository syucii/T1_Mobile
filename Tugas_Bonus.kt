data class Produk(
    val id: String,
    val nama: String,
    val harga: Double,
    val kategori: String,
    var stok: Int // bisa diubah nilainya (stok bisa berkurang)
)   

data class Item( // untuk menyimpan produk di keranjang beserta jumlahnya
    val produk: Produk,
    var jumlah: Int         // var = jumlah bisa diubah
)

data class Customer( //untuk menyimpan data pelanggan
    val id: String,
    val nama: String,
    val email: String,
    val alamat: String? 
)

sealed class OrderStatus { //status pesanan yang hanya bisa salah satu dari 5 pilihan ini
    object Pending    : OrderStatus()   // Menunggu konfirmasi
    object Processing : OrderStatus()   // Sedang diproses
    object Shipped    : OrderStatus()   // Sedang dikirim
    object Delivered  : OrderStatus()   // Sudah diterima
    object Cancelled  : OrderStatus()   // Dibatalkan
}

sealed class PaymentMethod { //metode pembayaran yang hanya bisa salah satu dari 3 pilihan ini
    object Cash     : PaymentMethod()   // Tunai
    object Transfer : PaymentMethod()   // Transfer bank
    object EWallet  : PaymentMethod()   // Dompet digital (GoPay, OVO, dll)
}

interface Diskon {
    fun hitungDiskon(persentase: Double): Double   // fungsi wajib ada
}

data class Order( // untuk menyimpan data pesanan
    val id: String,
    val customer: Customer,
    val items: List<Item>,
    var status: OrderStatus,            // var = status bisa diubah
    val paymentMethod: PaymentMethod,
    val totalHarga: Double
)

class Keranjang { // untuk menyimpan produk yang akan dibeli

    //list yang bisa ditambah/dikurangi isinya
    private val items = mutableListOf<Item>()

    //menambah produk ke keranjang
    fun tambahProduk(produk: Produk, jumlah: Int = 1) {
        // Cek apakah stok cukup
        if (produk.stok < jumlah) {
            println("Stok ${produk.nama} tidak cukup! Stok tersedia: ${produk.stok}")
            return
        }

        // Cek apakah produk sudah ada di keranjang
        val itemAda = items.find { it.produk.id == produk.id }

        if (itemAda != null) {
            // Kalau sudah ada, tambah jumlahnya saja
            itemAda.jumlah += jumlah
        } else {
            // Kalau belum ada, tambahkan sebagai item baru
            items.add(Item(produk, jumlah))
        }

        println("\"${produk.nama}\" berhasil ditambahkan ke keranjang!")
    }

    // Fungsi untuk menghapus produk dari keranjang
    fun hapusProduk(produkId: String) {
        //hapus elemen yang memenuhi kondisi
        val dihapus = items.removeIf { it.produk.id == produkId }
        if (dihapus) {
            println("Produk berhasil dihapus dari keranjang.")
        } else {
            println("Produk tidak ditemukan di keranjang.")
        }
    }

    // Fungsi untuk menghitung total harga dengna menjumlahkan semua nilai
    fun hitungTotal(): Double {
        return items.sumOf { it.produk.harga * it.jumlah }
    }

    // Fungsi untuk menerapkan diskon
    fun terapkanDiskon(discountCalculator: (Double) -> Double): Double { //Higher-Order Function fungsi yang menerima fungsi lain sebagai parameter
        val total = hitungTotal()
        return discountCalculator(total)   // panggil fungsi yang diterima
    }

    // Fungsi untuk menampilkan isi keranjang
    fun tampilkanKeranjang() {
        if (items.isEmpty()) {
            println("Keranjang kosong.")
            return
        }

        println("===== KERANJANG BELANJA =====")
        items.forEachIndexed { index, item ->
            val subtotal = item.produk.harga * item.jumlah
            println("${index + 1}. ${item.produk.nama}")
            println("   Harga: Rp${item.produk.harga} x ${item.jumlah} = Rp$subtotal")
        }
        println("-----------------------------")
        println("Total: Rp${hitungTotal()}")
    }

    // Fungsi untuk mengambil daftar item daat checkout 
    fun getItems(): List<Item> = items.toList()   //buat salinan agar tidak bisa diubah dari luar

    // Fungsi untuk mengosongkan keranjang
    fun kosongkan() = items.clear()
}

class TokoOnline {

    // Daftar produk yang dijual
    private val produkList = mutableListOf<Produk>()

    // Daftar pesanan
    private val riwayatPesanan = mutableListOf<Order>()

    // ID pesanan
    private var orderId = 1

    // Fungsi untuk menambah produk ke toko
    fun tambahProdukKeToko(produk: Produk) {
        produkList.add(produk)
    }

    // Fungsi untuk menampilkan semua produk
    fun tampilkanSemuaProduk() {
        println("===== DAFTAR PRODUK =====")
        if (produkList.isEmpty()) {
            println("Belum ada produk.")
            return
        }
        produkList.forEachIndexed { index, produk ->
            println("${index + 1}. [${produk.id}] ${produk.nama}")
            println("   Harga: Rp${produk.harga} | Kategori: ${produk.kategori} | Stok: ${produk.stok}")
        }
    }

    // Fungsi untuk mencari produk berdasarkan ID
    fun cariProduk(id: String): Produk? {
        return produkList.find { it.id == id }
    }

    // Fungsi untuk filter produk berdasarkan kategori
    fun filterProdukByKategori(kategori: String): List<Produk> {
        return produkList.filter { it.kategori.lowercase() == kategori.lowercase() } // filter dengan tanpa case sensitive
    }

    // Fungsi untuk urutkan produk berdasarkan harga
    fun produkTermurah(): List<Produk> = produkList.sortedBy { it.harga }
    fun produkTermahal(): List<Produk> = produkList.sortedByDescending { it.harga }

    // Fungsi untuk proses checkout
    fun checkout(customer: Customer, cart: Keranjang, payment: PaymentMethod, diskon: Double = 0.0): Order? {
        if (cart.getItems().isEmpty()) {
            println("Keranjang kosong! Tidak bisa checkout.")
            return null
        }

        // Hitung total dengan diskon
        val totalSetelahDiskon = cart.terapkanDiskon { total ->
            total - (total * diskon / 100)
        }

        // Buat pesanan baru
        val order = Order(
            id            = "ORD-${orderId++}",   // auto increment ID
            customer      = customer,
            items         = cart.getItems(),
            status        = OrderStatus.Pending,  // status awal selalu Pending baru menunggu konfirmasi soalnya
            paymentMethod = payment,
            totalHarga    = totalSetelahDiskon
        )

        // Kurangi stok produk yang tersedia sesuai jumlah yang dibeli
        order.items.forEach { item ->
            item.produk.stok -= item.jumlah
        }

        // Simpan ke riwayat dan kosongkan keranjang
        riwayatPesanan.add(order)
        cart.kosongkan()

        return order
    }

    // Fungsi untuk update status pesanan
    fun updateStatus(orderId: String, statusBaru: OrderStatus) {
        val order = riwayatPesanan.find { it.id == orderId }
        if (order != null) {
            order.status = statusBaru
            val namaStatus = getStatusText(statusBaru)
            println("Status pesanan ${order.id} diperbarui menjadi: $namaStatus")
        } else {
            println("Pesanan dengan ID $orderId tidak ditemukan.")
        }
    }

    // Fungsi untuk menampilkan riwayat pesanan
    fun tampilkanRiwayatPesanan() {
        println("===== RIWAYAT PESANAN =====")
        if (riwayatPesanan.isEmpty()) {
            println("Belum ada pesanan.")
            return
        }
        riwayatPesanan.forEach { order ->
            println("ID Pesanan  : ${order.id}")
            println("Customer    : ${order.customer.nama}")
            println("Status      : ${getStatusText(order.status)}")
            println("Pembayaran  : ${getPaymentText(order.paymentMethod)}")
            println("Total       : Rp${order.totalHarga}")
            // ?. berarti: akses alamat hanya jika tidak null
            // ?: "Tidak ada" berarti: jika null, tampilkan "Tidak ada"
            println("Alamat      : ${order.customer.alamat ?: "Tidak ada"}")
            println("---------------------------")
        }
    }

    // Fungsi pembantu untuk mengubah OrderStatus menjadi teks
    private fun getStatusText(status: OrderStatus): String {
        return when (status) {
            is OrderStatus.Pending    -> "Menunggu Konfirmasi"
            is OrderStatus.Processing -> "Sedang Diproses"
            is OrderStatus.Shipped    -> "Sedang Dikirim"
            is OrderStatus.Delivered  -> "Sudah Diterima"
            is OrderStatus.Cancelled  -> "Dibatalkan"
        }
    }

    // Fungsi pembantu untuk mengubah PaymentMethod menjadi teks
    private fun getPaymentText(payment: PaymentMethod): String {
        return when (payment) {
            is PaymentMethod.Cash     -> "Tunai"
            is PaymentMethod.Transfer -> "Transfer Bank"
            is PaymentMethod.EWallet  -> "E-Wallet"
        }
    }
}

fun main() {

    println("========================================")
    println("     SELAMAT DATANG DI TOKO ONLINE      ")
    println("   Sucitasari Rahmadani - F1D02310138   ")
    println("========================================")

    // 1. Inisialisasi toko dan tambahkan produk
    val toko = TokoOnline()

    toko.tambahProdukKeToko(Produk("P001", "Laptop Asus TUF",       85000000.0, "Elektronik",  5))
    toko.tambahProdukKeToko(Produk("P002", "Mouse Wireless",      150000.0, "Elektronik", 20))
    toko.tambahProdukKeToko(Produk("P003", "Keyboard", 450000.0, "Elektronik", 10))
    toko.tambahProdukKeToko(Produk("P004", "Baju Polo",            89000.0, "Fashion",    30))
    toko.tambahProdukKeToko(Produk("P005", "Celana Cutbray",        175000.0, "Fashion",    15))
    toko.tambahProdukKeToko(Produk("P006", "Sepatu Sneakers",     320000.0, "Fashion",     8))
    toko.tambahProdukKeToko(Produk("P007", "Matcha 250gr",   65000.0, "Makanan",    50))
    toko.tambahProdukKeToko(Produk("P008", "Minyak Goreng 2L",     35000.0, "Makanan",    40))

    // 2. Tampilkan semua produk
    toko.tampilkanSemuaProduk()

    // 3. Filter produk berdasarkan kategori
    println()
    println("===== PRODUK KATEGORI ELEKTRONIK =====")
    val elektronik = toko.filterProdukByKategori("Elektronik")
    elektronik.forEach { println("- ${it.nama}: Rp${it.harga}") }

    // 4. Buat customer
    val customer = Customer(
        id     = "C001",
        nama   = "Budi Santoso",
        email  = "budi@email.com",
        alamat = "Jl. Merdeka No.10, Jakarta"
    )

    // 5. Buat keranjang dan tambahkan produk
    println()
    val keranjang = Keranjang()

    val laptop = toko.cariProduk("P001")
    val mouse  = toko.cariProduk("P002")
    val kopi   = toko.cariProduk("P007")

    // ?. = hanya jalankan jika tidak null
    if (laptop != null) keranjang.tambahProduk(laptop, 1)
    if (mouse != null)  keranjang.tambahProduk(mouse, 2)
    if (kopi != null)   keranjang.tambahProduk(kopi, 3)

    // 6. Tampilkan keranjang
    println()
    keranjang.tampilkanKeranjang()

    // 7. Proses checkout dengan diskon 10%
    println()
    println("Memproses checkout dengan diskon 10%...")
    val pesanan = toko.checkout(
        customer = customer,
        cart     = keranjang,
        payment  = PaymentMethod.Transfer,
        diskon   = 10.0   // diskon 10%
    )

    // 8. Tampilkan detail pesanan
    if (pesanan != null) {
        println()
        println("===== PESANAN BERHASIL DIBUAT =====")
        println("ID Pesanan : ${pesanan.id}")
        println("Customer   : ${pesanan.customer.nama}")
        println("Total      : Rp${pesanan.totalHarga}")
        println("Pembayaran : Transfer Bank")
        println("Status     : Menunggu Konfirmasi")
    }

    // 9. Update status pesanan
    println()
    if (pesanan != null) {
        toko.updateStatus(pesanan.id, OrderStatus.Processing)
        toko.updateStatus(pesanan.id, OrderStatus.Shipped)
        toko.updateStatus(pesanan.id, OrderStatus.Delivered)
    }

    // 10. Tampilkan riwayat pesanan
    println()
    toko.tampilkanRiwayatPesanan()

    // 11. Produk termurah dan termahal
    println("===== PRODUK TERMURAH =====")
    toko.produkTermurah().take(3).forEach { println("- ${it.nama}: Rp${it.harga}") }

    println()
    println("===== PRODUK TERMAHAL =====")
    toko.produkTermahal().take(3).forEach { println("- ${it.nama}: Rp${it.harga}") }

    println()
    println("========================================")
    println("     Terima kasih telah berbelanja!      ")
    println("========================================")
}
