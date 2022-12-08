package rserenity.enums;

public enum City {
    Adana(1),
    Adiyaman(2),
    Afyonkarahisar(3),
    Agri(4),
    Aksaray(68),
    Amasya(5),
    Ankara(6),
    Antalya(7),
    Ardahan(75),
    Artvin(8),
    Aydin(9),
    Balikesir(10),
    Bartin(74),
    Batman(72),
    Bayburt(69),
    Bilecik(11),
    Bingol(12),
    Bitlis(13),
    Bolu(14),
    Burdur(15),
    Bursa(16),
    Canakkale(17),
    Cankiri(18),
    Corum(19),
    Denizli(20),
    Diyarbakir(21),
    Duzce(81),
    Edirne(22),
    Elazig(23),
    Erzincan(24),
    Erzurum(25),
    Eskisehir(26),
    Gaziantep(27),
    Giresun(28),
    Gumushane(29),
    Hakkari(30),
    Hatay(31),
    Igdir(76),
    Isparta(32),
    Istanbul(34),
    Izmir(35),
    Kahramanmaras(46),
    Karabuk(78),
    Karaman(70),
    Kars(36),
    Kastamonu(37),
    Kayseri(38),
    Kilis(79),
    Kirikkale(71),
    Kirklareli(39),
    Kirsehir(40),
    Kocaeli(41),
    Konya(42),
    Kutahya(43),
    Malatya(44),
    Manisa(45),
    Mardin(47),
    Mersin(33),
    Mugla(48),
    Mus(49),
    Nevsehir(50),
    Nigde(51),
    Ordu(52),
    Osmaniye(80),
    Rize(53),
    Sakarya(54),
    Samsun(55),
    Sanliurfa(63),
    Siirt(56),
    Sinop(57),
    Sivas(58),
    Sirnak(73),
    Tekirdag(59),
    Tokat(60),
    Trabzon(61),
    Tunceli(62),
    Usak(64),
    Van(65),
    Yalova(77),
    Yozgat(66),
    Zonguldak(67);


    private final int plaka;
    City(int plaka) { this.plaka = plaka; }
    public int getValue() { return plaka; }

    public static City getCityByPlaka(int plaka){
        for (City city : City.values()) {
            if(plaka==city.plaka){
                return city;
            }
        }
        return null;
    }

}
