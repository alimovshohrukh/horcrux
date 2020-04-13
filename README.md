# horcrux

Модуль для легкой и удобной интеграции с E-IMZO

Этот модуль помогает использовать функции E-IMZO с меньшими усилиями. На данный момент доступны следующие функции: аутентификация пользователя и подпись документов / данных с помощью ЭЦП (электронная цифровая подпись).


### Бинарники

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.alimninja:horcrux:latest_version'
```

* Jitpack.io: [![](https://jitpack.io/v/alimninja/horcrux.svg)](https://jitpack.io/#alimninja/horcrux)


### Сборка

У вас должен быть API_KEY, сгенерированный специально для вашего applicationId. Для получения дополнительной информации обращайтесь по номеру (71-202-3232)

Создание сборки (Kotlin):

```kotlin
class MyApplication : Application() {
 . . . .
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        // Глобальная инициализация класса
        horcrux = Horcrux(context,"ВАШ API_KEY")
    }
}
```

Зарегистрируйте свой класс в Manifest

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example">

    <application
    ....
    android:name="com.example.MyApplication"
    ....>
        <activity android:name="com.example.MainActivity"/>
    </application>

</manifest>
```


### Пример использования

Пример проекта который предоставляет примеры  использование классов в этом проекте доступен в папке `app /`. Для авторизации пользователя с помощью ЭЦП используется метод `createPKCS7` с параметром `string` (например, `random_hash`). Этот метод передает параметр в приложение E-IMZO и ожидает результат в `onActivityResult`. Здесь вы можете соответствующим образом пропарсить данные (используя RegEx `horcrux.regex`). Определения регулярных выражений можно найти в `res / strings`. Вы можете использовать данные, как вы хотите, но для использования методов `horcrux` вы ДОЛЖНЫ вызывать `parsePFX (data)` в `onActivityResult`.
```kotlin
import com.example.MyApplication.Companion.horcrux

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        //  Для вызова метода onCreate
         createButton.setOnClickListener {
            horcrux.createPKCS7(this, "MESSAGE")
        }
        
        //  Для проверки/теста
        checkButton.setOnClickListener {
            //  Check if users is Legal             horcrux.isLegal()
            //  Check if users is Individual        horcrux.isIndividual()
            //  Get user tin                        horcrux.getTin()
            //  Get encoded message                 horcrux.getPKCS()
            //  Get serial number                   horcrux.getSerialNumber()
            //  Get subject name                    horcrux.getSubjectName()
            Log.e(horcrux.tag, horcrux.getTin())
        }
        
        //  Для вызова метода onAppend
        appendButton.setOnClickListener {
            //  С пользовательскими параметрами
            //  horcrux.appendPkcs7(this, horcrux.getPKCS(), horcrux.getSerialNumber())
            //  horcrux.appendPkcs7(this, horcrux.getPKCS()) With custom params

            horcrux.appendPkcs7(this)   //  С параметрами по умолчанию
        }
        
        //  //  Для вызова метода onAttached
        attachButton.setOnClickListener {
            val timestamp = "Ваш  таймстамп"
            //  С пользовательскими параметрами
            //  horcrux.attachPkcs7(this, horcrux.getPKCS(), timestamp)
            //  horcrux.attachPkcs7(this, horcrux.getPKCS(), horcrux.getSerialNumber(),timestamp)

            horcrux.attachPkcs7(this, timestamp)    //  С параметрами по умолчанию
        }
. . . .
         override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CREATE_PKCS7 -> onCreate(resultCode, data)
            APPEND_CODE -> onAppend(resultCode, data)
            else -> onAttached(resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
```
Данные ЭЦП будут проанализированы и сохранены в кеше. Теперь вы можете использовать другие методы:
* Пример метода `onCreate()`
```kotlin
    private fun onCreate(
        resultCode: Int,
        data: Intent?
    ) {
        when (resultCode) {
            RESULT_ACCESS_DENIED -> {
                Toast.makeText(this, "Доступ запрещен", Toast.LENGTH_SHORT).show()
                Log.e(horcrux.tag, "Проверьте, правильно ли вы ввели API_KEY")
                return
            }
            Activity.RESULT_OK -> {
                //  Здесь вы можете соответствующим образом проанализировать данные (используя этот RegEx `horcrux.regex`). Для определения регулярных выражений примеры прилагаются ниже.
                //  Вы можете использовать данные, как вы хотите. Но для использования методов horcrux вы ДОЛЖНЫ вызывать `parsePFX (data)`
                horcrux.parsePFX(data)
                return
            }
            RESULT_ERROR -> return
            else -> return
        }
    }

```
* Пример метода `onAppend()`
```kotlin
    private fun onAppend(resultCode: Int, data: Intent?) {
        when (resultCode) {
            RESULT_ACCESS_DENIED -> {
                Toast.makeText(this, "Доступ запрещен", Toast.LENGTH_SHORT).show()
                Log.e(horcrux.tag, "Проверьте, правильно ли вы ввели API_KEY")
                return
            }
            Activity.RESULT_OK -> {
                //  Здесь вы можете соответствующим образом проанализировать данные (используя этот RegEx `horcrux.regex`). Проверьте res / strings для определения регулярных выражений
                //  Вы можете использовать данные, как вы хотите. Но для использования методов horcrux вы ДОЛЖНЫ вызывать `parsePFX (data)`
                horcrux.parsePFX(data)
                //  Timestamp call
                attachTimeStampToPkcs7()
                return
            }
            RESULT_ERROR -> return
            else -> return
        }
    }
```
Метод `onAppend()` генерирует `Base64` к которому нужно будет прикрепить таймстамп:
```kotlin
    private fun attachTimeStampToPkcs7() {
        TODO("Вызовите метод для добавки таймстамп")

        val timestamp = "Ваш  таймстамп"
            //  С пользовательскими параметрами
            //  horcrux.attachPkcs7(this, horcrux.getPKCS(), timestamp)
            //  horcrux.attachPkcs7(this, horcrux.getPKCS(), horcrux.getSerialNumber(),timestamp)

            horcrux.attachPkcs7(this, timestamp)    //  С параметрами по умолчанию
    }
```
* Пример метода `onAttached()`
```kotlin
    private fun onAttached(resultCode: Int, data: Intent?) {
        when (resultCode) {
            RESULT_ACCESS_DENIED -> {
                Toast.makeText(this, "Доступ запрещен", Toast.LENGTH_SHORT).show()
                Log.e(horcrux.tag, "Проверьте, правильно ли вы ввели API_KEY")
                return
            }
            Activity.RESULT_OK -> {
                //  Ваш подписанный Base64. Готов к отправке
                val pkcs = data?.getByteArrayExtra(EXTRA_RESULT_PKCS7) //  ByteArray
                val pkcsString = Base64.encodeToString(pkcs, Base64.NO_WRAP)    //  String
                return
            }
            RESULT_ERROR -> return
            else -> return
        }
    }
```
Другие дополнительные методы:
* Чтобы проверить статус пользователя
```kotlin
horcrux.isLegal()   - Юридическое лицо             
horcrux.isIndividual() - Физическое лицо
```
* Получить ИНН пользователя
```kotlin
horcrux.getTin()
```
* Подписанный параметр`random_hash` в формате `Base64`
```kotlin
horcrux.getPKCS()
```
* Получить серийный номер ЭЦП
```kotlin
horcrux.getSerialNumber()
```
* Распарсить и получить сохраненную информацию о ЭЦП
```kotlin
val subjecName = horcrux.getSubjectName()
 val sub = subjectName.split(",")
        arrayData.forEach {
            when {
                it.startsWith("CN") -> {
                    val owner = it.substringAfter("CN=")
                }
                it.startsWith("O") -> {
                    val companyName = it.substringAfter("O=")
                }
                it.startsWith("ST") -> {
                    val address = it.substringAfter("ST=")
                }
            }
        }
```
Кроме того, данные парсить с помощью RegEx (регулярных выражений)
```kotlin
    FIO = "CN"                      //  Ф.И.О
    YUR_TIN = "1.2.860.3.16.1.1"    //  ЮРИДИЧЕСКИЙ ИНН
    FIZ_TIN = "UID"                 //  ИНДИВИДУАЛЬНИЙ ИНН
    FORENAME = "Name"               //  ИМЯ
    SURNAME = "SURNAME"             //  ФАМИЛИЯ
    AREA = "L"                      //  ОКРУГ
    REGION = "ST"                   //  ОБЛАСТЬ
    COUNTRY = "C"                   //  СТРАНА
    PINFL = "1.2.860.3.16.1.2"      //  ПИНФЛ
    EMAIL = "E"                     //  ЭЛ. АДРЕС
    JOBTITLE = "T"                  //  ДОЛЖНОСТЬ
    ORGANIZATION = "O"              //  ОРГАНИЗАЦИЯ
    DEPARTMENT = "OU"               //  ОТДЕЛ
```


### Ошибки и обратная связь

Для ошибок, запросов функций и обсуждения, пожалуйста, используйте [GitHub Issues](https://github.com/alimninja/horcrux/issues).
ТОЛЬКО для вопросов общего пользования, пожалуйста свяжитесь по [Telegram](https://t.me/AlimovShohrukh).


### ЛИЦЕНЗИЯ

    Copyright 2020 SICNT, Tashkent, Uzbekistan

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
