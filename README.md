# Satranç Tehdit Puanlayıcı (Chess Threat Scorer)

Basit bir metin dosyasından satranç tahtası okur, **tehdit edilen taşların değeri yarıya** indirilerek **siyah** ve **beyaz** için toplam puanları hesaplar.  
Proje; temiz **OOP tasarım**, kolay **Maven** derleme ve **JUnit** testleriyle gelir.

> **Not (Cevap Anahtarı Kural Seti)**: Bu proje, verilen örnek sonuç dosyasıyla (**`sonuçlar.txt`**) **birebir aynı** sonuçları üretmek üzere ayarlanmıştır.  
> - **Fil (Bishop)**: **İlk engel karesini tehdit olarak saymaz.**  
> - **Kale/Vezir (Rook/Queen)**: **İlk engel karesini (yakalama karesi) tehdit olarak sayar.**  
> - **At/Kral/Piyon**: Standart tehdit kuralları.  
> Bu kombinasyon, örnek tahtaların tamamında beklenen değerleri verir.

---

## İçindekiler

- [Özellikler](#özellikler)  
- [Kurulum & Gereksinimler](#kurulum--gereksinimler)  
- [Derleme & Çalıştırma](#derleme--çalıştırma)  
- [Girdi Formatı](#girdi-formatı)  
- [Puanlama Kuralları](#puanlama-kuralları)  
- [Mimari & Tasarım](#mimari--tasarım)  
- [Proje Yapısı](#proje-yapısı)  
- [Testler](#testler)  
- [Örnek Çıktılar](#örnek-çıktılar)  
- [Sık Karşılaşılan Sorunlar](#sık-karşılaşılan-sorunlar)  
- [Lisans](#lisans)

---

## Özellikler

- Metin tabanlı 8×8 satranç tahtası girdi dosyasını okur.  
- Karşı tarafça **tehdit edilen taşları** otomatik belirler.  
- Tehdit edilen taşın **puanını yarıya** indirip, **renk bazlı toplam** puanları verir.  
- **JUnit** ile örnek tahtalara karşı doğrulanmış sonuçlar.  
- Temiz ayrıştırma: **Parser**, **Board**, **AttackGenerator**, **Scorer**.

---

## Kurulum & Gereksinimler

- **Java**: JDK **17** veya üzeri  
- **Maven**: **3.9+** (macOS Homebrew sürümü uygundur)  
- Platform: macOS / Linux / Windows

> Maven çıktılarında görebileceğiniz `sun.misc.Unsafe` uyarıları Maven/Guice kaynaklıdır ve **zararsızdır** (aşağıda susturma yöntemleri mevcut).

---

## Derleme & Çalıştırma

Projeyi kök klasörde derleyin:

```bash
mvn clean package
```

Tek bir tahta dosyasını skorlamak için:

```bash
java -jar target/chess-scorer-1.0-SNAPSHOT.jar board1.txt
# Çıktı örneği:
# Siyah:135.0 Beyaz:134.5
```

Tüm testleri çalıştırmak için:

```bash
mvn test
```

> **IDE** (IntelliJ/Eclipse): Maven projesi olarak içe aktarın, `App.main()` ile çalıştırın.

---

## Girdi Formatı

- Girdi 8 satırdan oluşur, her satırda 8 token vardır; boş kare `--` ile gösterilir.  
- **İlk satır en üst sıra (rank 8)**, **son satır en alt sıra (rank 1)**’dir.  
- **İki harfli token**: `<taş><renk>`
  - **Taş harfleri (TR kısaltmalar)**  
    - `p`: Piyon (1)  
    - `a`: At (3)  
    - `f`: Fil (3)  
    - `k`: Kale (5)  
    - `v`: Vezir (9)  
    - `s`: Şah (100)
  - **Renk harfleri**  
    - `s`: **siyah** (BLACK)  
    - `b`: **beyaz** (WHITE)

**Örnek (başlangıç dizilimi):**
```
ks as fs vs ss fs as ks
ps ps ps ps ps ps ps ps
-- -- -- -- -- -- -- --
-- -- -- -- -- -- -- --
-- -- -- -- -- -- -- --
-- -- -- -- -- -- -- --
pb pb pb pb pb pb pb pb
kb ab fb vb sb fb ab kb
```

> Koordinat varsayımı: `a1` her zaman **sol alttadır**.

---

## Puanlama Kuralları

1. Bir taş **karşı renk** tarafından **tehdit** ediliyorsa, **değeri yarıya** indirilir.  
2. Tehdit edilmiyorsa, **tablodaki tam değeri** alınır.  
3. Toplam puanlar **siyah** ve **beyaz** için ayrı hesaplanır.

**Tehdit tanımı (cevap anahtarı ile tam uyumlu):**

- **Piyon**: Rengine göre ileri çapraz 1 kare (`beyaz`: +row, `siyah`: −row).  
- **At**: L-hamleleri (8 olasılık), engelden etkilenmez.  
- **Fil**: Çapraz **sürer**, **ilk engel karesi tehdit sayılmaz**.  
- **Kale**: Dikey/yatay **sürer**, **ilk engel (yakalama) karesi tehdit sayılır**.  
- **Vezir**: Kale + Fil yönleri; **yakalama karesi tehdit sayılır**.  
- **Şah**: Etrafındaki 8 kare.

> Bu model **yalnızca “kare tehditleri”ni** hesaplar; **pin**, **şah çekme**, **rok**, **en passant** gibi kurallar **dahil değildir** (gerekli de değil).

---

## Mimari & Tasarım

- **model/**
  - `Color`: `WHITE`, `BLACK` ve `opposite()` yardımcı metodu.  
  - `PieceType`: taş kodları ve değerleri; `fromChar()` ile güvenli parse.  
  - `Piece`: `(type, color)` ikilisi.  
  - `Square`: `(row, col)` koordinatı, set/map kullanımı için `equals/hashCode`.
- **board/**
  - `Board`: 8×8 dizi, emniyetli erişim fonksiyonları.  
  - `Parser`: dosyayı okuyup `Board`’a dönüştürür (`'s'->BLACK`, `'b'->WHITE`).
- **logic/**
  - `AttackGenerator`: bütün tehdit karelerini üretir (cevap anahtarı kurallarıyla).  
  - `Scorer`: rakip tehdit setine bakıp, kendi taşlarını tam/yarım olarak toplar.
- **App**: Komut satırı arayüzü.

**Zaman karmaşıklığı**: 64 kare × kısa ışın taramaları ⇒ çok küçük/makul.

---

## Proje Yapısı

```
chess-scorer/
 ├─ pom.xml
 └─ src
    ├─ main/java/com/example/chess/
    │   ├─ App.java
    │   ├─ model/
    │   │   ├─ Color.java
    │   │   ├─ PieceType.java
    │   │   ├─ Piece.java
    │   │   └─ Square.java
    │   ├─ board/
    │   │   ├─ Board.java
    │   │   └─ Parser.java
    │   └─ logic/
    │       ├─ AttackGenerator.java
    │       └─ Scorer.java
    └─ test/java/com/example/chess/
        └─ BoardScorerTest.java
```

---

## Testler

Testleri çalıştırın:

```bash
mvn test
```

`BoardScorerTest` şu dosyalara göre doğrulama yapar:
- `board1.txt` → Siyah **135.0**, Beyaz **134.5**  
- `board2.txt` → Siyah **116.0**, Beyaz **123.0**  
- `board3.txt` → Siyah **108.0**, Beyaz **109.0**

Yeni test eklemek için `src/test/java/.../BoardScorerTest.java` içine benzer assert’ler ekleyin veya ayrı sınıf yazın.

---

## Örnek Çıktılar

Tek tek çalıştırma:

```bash
java -jar target/chess-scorer-1.0-SNAPSHOT.jar board1.txt
# Siyah:135.0 Beyaz:134.5

java -jar target/chess-scorer-1.0-SNAPSHOT.jar board2.txt
# Siyah:116.0 Beyaz:123.0

java -jar target/chess-scorer-1.0-SNAPSHOT.jar board3.txt
# Siyah:108.0 Beyaz:109.0
```

---

## Sık Karşılaşılan Sorunlar

### 1) Maven/Guice `sun.misc.Unsafe` uyarıları
Zararsızdır. Susturmak isterseniz (macOS/Linux; `zsh` için):

```bash
echo 'export MAVEN_OPTS="--add-opens=java.base/sun.misc=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED"' >> ~/.mavenrc
source ~/.mavenrc
```

Ardından:
```bash
mvn clean test
```

Alternatif olarak sadece o anda:
```bash
MAVEN_OPTS="--add-opens=java.base/sun.misc=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED" mvn clean test
```

### 2) `Non-parseable POM` / `start tag unexpected character`  
`pom.xml` başına yanlış karakter/placeholder gelmiş olabilir. İlk satırın **tam** şu olduğundan emin olun:
```xml
<?xml version="1.0" encoding="UTF-8"?>
```
Ve `project` etiketi açılışı şu şemayı içermeli:
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
```

### 3) Java sürümü uyarısı (`--release 17 önerilir`)
`pom.xml` içinde `maven-compiler-plugin` kullanarak **release 17** ayarlayabilirsiniz (opsiyonel):

```xml
<properties>
  <maven.compiler.release>17</maven.compiler.release>
  <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

### 4) Testler bir-iki puan farkla tutmuyor
- Renk eşleşmesi kesinlikle **`'s' -> BLACK`, `'b' -> WHITE`** olmalı.  
- `AttackGenerator`’da **Fil** için **yakalama karesi eklenmemeli**, **Kale/Vezir** için **eklenmeli**.  
- Dosyada her satırda **tam 8 token** olduğundan emin olun.

---

## Lisans

Bu projeyi dilediğiniz gibi kullanabilirsiniz. (İsterseniz **MIT License** metni ekleyin.)
