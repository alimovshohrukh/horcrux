package uz.sicnt.eimzo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import uz.sicnt.eimzo.MyApplication.Companion.horcrux
import uz.sicnt.horcrux.Constants.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createButton.setOnClickListener {
            horcrux.createPKCS7(this, "MESSAGE")
        }

        checkButton.setOnClickListener {
            //  Check if users is Legal             horcrux.isLegal()
            //  Check if users is Individual        horcrux.isIndividual()
            //  Get user tin                        horcrux.getTin()
            //  Get encoded message                 horcrux.getPKCS()
            //  Get serial number                   horcrux.getSerialNumber()
            //  Get subject name                    horcrux.getSubjectName()
            Log.e(horcrux.tag, horcrux.getTin())
        }

        appendButton.setOnClickListener {
            //  С пользовательскими параметрами
            //  horcrux.appendPkcs7(this, horcrux.getPKCS(), horcrux.getSerialNumber())
            //  horcrux.appendPkcs7(this, horcrux.getPKCS()) With custom params

            horcrux.appendPkcs7(this)   //  С параметрами по умолчанию
        }

        attachButton.setOnClickListener {
            val timestamp = "Ваш  таймстамп"
            //  С пользовательскими параметрами
            //  horcrux.attachPkcs7(this, horcrux.getPKCS(), timestamp)
            //  horcrux.attachPkcs7(this, horcrux.getPKCS(), horcrux.getSerialNumber(),timestamp)

            horcrux.attachPkcs7(this, timestamp)    //  С параметрами по умолчанию
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CREATE_PKCS7 -> onCreate(resultCode, data)
            APPEND_CODE -> onAppend(resultCode, data)
            else -> onAttached(resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

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
                //  Здесь вы можете соответствующим образом проанализировать данные (используя этот RegEx `horcrux.regex`). Проверьте res / strings для определения регулярных выражений
                //  Вы можете использовать данные, как вы хотите. Но для использования методов horcrux вы ДОЛЖНЫ вызывать `parsePFX (data)`
                horcrux.parsePFX(data)
                return
            }
            RESULT_ERROR -> return
            else -> return
        }
    }

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

    private fun attachTimeStampToPkcs7() {
        TODO("Вызовите метод для добавки таймстамп")
        //  val timestamp = "Ваш  таймстамп"
        //  horcrux.attachPkcs7(this, horcrux.getSerialNumber(), timestamp, horcrux.getSubjectName())
    }

    private fun onAttached(resultCode: Int, data: Intent?) {
        when (resultCode) {
            RESULT_ACCESS_DENIED -> {
                Toast.makeText(this, "Доступ запрещен", Toast.LENGTH_SHORT).show()
                Log.e(horcrux.tag, "Проверьте, правильно ли вы ввели API_KEY")
                return
            }
            Activity.RESULT_OK -> {
                //  Ваш готовый подписанный PKCS7. Готов к отправке
                val pkcs = data?.getByteArrayExtra(EXTRA_RESULT_PKCS7) //  ByteArray
                val pkcsString = Base64.encodeToString(pkcs, Base64.NO_WRAP)    //  String
                return
            }
            RESULT_ERROR -> return
            else -> return
        }

    }
}
