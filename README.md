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

Создание сборки:

```bash
Скопируйте ваш API_KEY в файл gradle.properties следующим образом: API_KEY = "ВАШ API_KEY"
```


### Пример использования

Пример проекта который предоставляет примеры  использование классов в этом проекте доступен в папке `app /`

```kotlin
class MainActivity : AppCompatActivity() {
    val horcrux = Horcrux()
    . . . .
```
Для авторизации пользователя с помощью ЭЦП используется метод `createPKCS7` с параметром `string` (например, `random_hash`). Этот метод передает параметр в приложение E-IMZO и ожидает результат в `onActivityResult`. Здесь вы можете соответствующим образом пропарсить данные (используя RegEx `horcrux.regex`). Определения регулярных выражений можно найти в `res / strings`. Вы можете использовать данные, как вы хотите, но для использования методов `horcrux` вы ДОЛЖНЫ вызывать `parsePFX (data)` в `onActivityResult`.
```kotlin
. . . .
horcrux.createPKCS7(this, random_hash)
. . . .
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CREATE_PKCS7) {
            when (resultCode) {
                RESULT_ACCESS_DENIED -> {
                    Toast.makeText(this, "Доступ запрещен", Toast.LENGTH_SHORT).show()
                    Log.e(horcrux.tag, "Проверьте, правильно ли вы ввели API_KEY")
                    return
                }
                RESULT_OK -> {
                    horcrux.parsePFX(data)
                    return
                }
                RESULT_ERROR -> return
                else -> return
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
```
Данные ЭЦП будут проанализированы и сохранены в кеше. Теперь вы можете использовать другие методы.

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
