# تدبیر (Tadbir Android) — اپلیکیشن مدیریت زمان و زندگی شخصی

اپلیکیشن نیتیو اندروید **«تدبیر»**، با تقویم **جلالی (شمسی)**، رابط کاربری **راست‌چین (RTL)**، معماری مدرن **MVI**، طراحی **Glassmorphism (Liquid Bottom Navigation)** و پایگاه‌داده محلی آفلاین‌محور **Room**.

الهام‌گرفته از استانداردهای مهندسی و Clean Code پروژه **دانشیار (Daneshyar)**.

---

## 🚀 ویژگی‌های کلیدی

* **معماری دقیق MVI (Model-View-Intent):**
  * تفکیک کامل لایه‌های `Contract`، `ViewModel`، `Screen` (کامپوزبل Stateless) و `Route`.
  * مدیریت وضعیت با `StateFlow` و رویدادهای گذرا با بافر `Channel`.
* **باتم‌بار شیشه‌ای مایع (Glassmorphic Liquid Bottom Navigation):**
  * افکت‌های شیشه‌ای بلور، لنز، پالس نوری و نشانگر فنری کپسولی با چیدمان راست‌به‌چپ.
* **تقویم بومی جلالی:**
  * شروع هفته از شنبه، تعطیلی جمعه، تبدیل دقیق تاریخ و ارقام فارسی.
* **برنامه روزانه و خط زمانی (Daily Time-Blocking):**
  * تقسیم کارها به زمان‌دار و شناور، قورباغه روز (Eat the Frog)، و حلقه پیشرفت.
* **پومودورو (Pomodoro Focus):**
  * تایمر تمرکز عمیق، استراحت کوتاه و بلند با نمودار پیشرفت دایره‌ای انیمیت‌شده.
* **عادات و روتین‌ها (Routines & Habits):**
  * ردیابی زنجیره استریک پایبندی و تیک زدن سریع روزانه.
* **ماتریس آیزنهاور (Eisenhower Matrix):**
  * اولویت‌بندی خودکار در ۴ ربع مهم و فوری.
* **آمار و بهره‌وری (Analytics):**
  * ثبت رکوردهای شخصی و نمودار میله‌ای تکمیل هفتگی.

---

## 🛠️ پشته فناوری (Tech Stack)

| لایه | فناوری |
|---|---|
| **Language** | Kotlin 2.0+ (JVM Toolchain 21) |
| **UI Framework** | Jetpack Compose + Material 3 |
| **Architecture** | MVI (Model-View-Intent) + Clean Architecture |
| **Dependency Injection** | Dagger Hilt 2.57 |
| **Database** | Room 2.7 (Kotlin Coroutines & Flow) |
| **Navigation** | Navigation Compose |
| **Design / Effects** | Kyant Backdrop & Capsule + Glassmorphism |
| **Typography** | Vazirmatn (وزیرمتن) |

---

## 📱 اجرا در Android Studio

1. این مخزن را Clone کنید.
2. پوشه را در **Android Studio (Ladybug / Meerkat یا جدیدتر)** باز کنید.
3. پروژه با Gradle Wrapper و JDK 21 به‌صورت خودکار سینک شده و آماده اجرا خواهد بود.
