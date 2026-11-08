
public class GoCepatBFS {

    static class Graph {
        private int jumlahSimpul;
        private int[][] tetangga;
        private int[] derajat;
        private int jarakDriverTerakhir;

        public Graph(int jumlahSimpul) {
            if (jumlahSimpul <= 0) {
                throw new IllegalArgumentException(
                    "Jumlah simpul harus lebih dari 0."
                );
            }

            this.jumlahSimpul = jumlahSimpul;
            this.tetangga = new int[jumlahSimpul][];
            this.derajat = new int[jumlahSimpul];
            this.jarakDriverTerakhir = -1;

            // Kapasitas awal setiap daftar tetangga adalah 2.
            for (int i = 0; i < jumlahSimpul; i++) {
                tetangga[i] = new int[2];
            }
        }

        private void validasiSimpul(int simpul) {
            if (simpul < 0 || simpul >= jumlahSimpul) {
                throw new IllegalArgumentException(
                    "Nomor simpul tidak valid: " + simpul
                );
            }
        }

        private void tambahTetanggaSatuArah(
            int asal, int tujuan
        ) {
            // Mencegah hubungan jalan yang sama tercatat dua kali.
            for (int i = 0; i < derajat[asal]; i++) {
                if (tetangga[asal][i] == tujuan) {
                    return;
                }
            }

            // Memperbesar array jika kapasitas sudah penuh.
            if (derajat[asal] == tetangga[asal].length) {
                int kapasitasBaru =
                    tetangga[asal].length * 2;

                int[] arrayBaru = new int[kapasitasBaru];

                for (int i = 0; i < derajat[asal]; i++) {
                    arrayBaru[i] = tetangga[asal][i];
                }

                tetangga[asal] = arrayBaru;
            }

            tetangga[asal][derajat[asal]] = tujuan;
            derajat[asal]++;
        }

        // Menambahkan jalan dua arah.
        public void tambahJalan(int a, int b) {
            validasiSimpul(a);
            validasiSimpul(b);

            if (a == b) {
                throw new IllegalArgumentException(
                    "Jalan tidak boleh menghubungkan simpul "
                    + "ke dirinya sendiri."
                );
            }

            tambahTetanggaSatuArah(a, b);
            tambahTetanggaSatuArah(b, a);
        }

        // Mencari driver terdekat menggunakan BFS.
        public int bfsCariTerdekat(
            int asal, boolean[] adalahDriver
        ) {
            validasiSimpul(asal);

            if (adalahDriver == null
                || adalahDriver.length != jumlahSimpul) {

                throw new IllegalArgumentException(
                    "Array driver harus memiliki ukuran "
                    + "sesuai jumlah simpul."
                );
            }

            boolean[] visited = new boolean[jumlahSimpul];
            int[] queue = new int[jumlahSimpul];
            int depan = 0;
            int belakang = 0;
            int[] jarak = new int[jumlahSimpul];

            // Memasukkan lokasi awal penumpang.
            visited[asal] = true;
            jarak[asal] = 0;
            queue[belakang++] = asal;

            while (depan < belakang) {
                int simpulSekarang = queue[depan++];

                // Memeriksa apakah simpul merupakan lokasi driver.
                if (adalahDriver[simpulSekarang]) {
                    jarakDriverTerakhir =
                        jarak[simpulSekarang];

                    return simpulSekarang;
                }

                // Memeriksa semua tetangga simpul saat ini.
                for (int i = 0;
                     i < derajat[simpulSekarang]; i++) {

                    int tetanggaBerikutnya =
                        tetangga[simpulSekarang][i];

                    if (!visited[tetanggaBerikutnya]) {

                        visited[tetanggaBerikutnya] = true;

                        jarak[tetanggaBerikutnya] =
                            jarak[simpulSekarang] + 1;

                        queue[belakang++] =
                            tetanggaBerikutnya;
                    }
                }
            }

            jarakDriverTerakhir = -1;
            return -1;
        }

        public int getJarakDriverTerakhir() {
            return jarakDriverTerakhir;
        }

        // Menampilkan hubungan antar-persimpangan.
        public void tampilkanGraph() {
            System.out.println(
                "Daftar hubungan persimpangan:"
            );

            for (int i = 0; i < jumlahSimpul; i++) {
                System.out.print("Simpul " + i + " -> ");

                for (int j = 0; j < derajat[i]; j++) {
                    System.out.print(tetangga[i][j]);

                    if (j < derajat[i] - 1) {
                        System.out.print(", ");
                    }
                }

                System.out.println();
            }
        }
    }

    public static void main(String[] args) {

        // Membuat graph dengan 8 persimpangan.
        Graph peta = new Graph(8);

        // Menambahkan hubungan jalan dua arah.
        peta.tambahJalan(0, 1);
        peta.tambahJalan(0, 2);
        peta.tambahJalan(1, 3);
        peta.tambahJalan(1, 4);
        peta.tambahJalan(2, 5);
        peta.tambahJalan(3, 6);
        peta.tambahJalan(4, 6);
        peta.tambahJalan(5, 7);

        // Driver tersedia di simpul 6 dan 7.
        boolean[] adalahDriver = new boolean[8];

        adalahDriver[6] = true;
        adalahDriver[7] = true;

        peta.tampilkanGraph();

        System.out.println(
            "\n=== Simulasi Pencarian Driver GoCepat ==="
        );

        // Pengujian pertama.
        int penumpang1 = 0;

        int driver1 = peta.bfsCariTerdekat(
            penumpang1, adalahDriver
        );

        System.out.println(
            "\nPenumpang 1 berada di simpul: " + penumpang1
        );

        if (driver1 != -1) {
            System.out.println(
                "Driver terdekat berada di simpul: " + driver1
            );

            System.out.println(
                "Jarak: " + peta.getJarakDriverTerakhir()
                + " hop"
            );
        } else {
            System.out.println(
                "Tidak ada driver yang dapat dijangkau."
            );
        }

        // Pengujian kedua.
        int penumpang2 = 2;

        int driver2 = peta.bfsCariTerdekat(
            penumpang2, adalahDriver
        );

        System.out.println(
            "\nPenumpang 2 berada di simpul: " + penumpang2
        );

        if (driver2 != -1) {
            System.out.println(
                "Driver terdekat berada di simpul: " + driver2
            );

            System.out.println(
                "Jarak: " + peta.getJarakDriverTerakhir()
                + " hop"
            );
        } else {
            System.out.println(
                "Tidak ada driver yang dapat dijangkau."
            );
        }
    }
}
