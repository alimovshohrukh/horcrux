package uz.sicnt.eimzo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import uz.sicnt.horcrux.Constants.*
import uz.sicnt.horcrux.Horcrux

class MainActivity : AppCompatActivity() {

    private val horcrux = Horcrux()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createButton.setOnClickListener {
            /*  MESSAGE is basically your data in Base64 format

                For example: Case 1 ->
                                You have to authorize user with his/her EDK, so you call `createPKCS7`
                                with message you get from your API (random_hash for example)

                             Case 2 ->
                                You need to sign a Document with EDK, so you call `createPKCS7`
                                with Document parsed as Base64
             */
            horcrux.createPKCS7(this, "MESSAGE")

        }

        checkButton.setOnClickListener {
            //  Check if users is Legal             horcrux.isLegal()
            //  Check if users is Individual        horcrux.isIndividual()
            //  Get user tin                        horcrux.getTin()
            //  Get encoded message                 horcrux.getPKCS()
            Log.e(horcrux.tag, horcrux.getTin())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CREATE_PKCS7) {
            when (resultCode) {
                RESULT_ACCESS_DENIED -> {
                    Toast.makeText(this, "Доступ запрещен", Toast.LENGTH_SHORT).show()
                    Log.e(horcrux.tag, "Проверьте, правильно ли вы ввели API_KEY")
                    return
                }
                Activity.RESULT_OK -> {
                    //  Here you can parse data accordingly (using this RegEx `horcrux.regex`). Check res/strings for regex definitions
                    //  You can use data as you wish. But, to use horcrux methods you MUST call `parsePFX(data)`
                    horcrux.parsePFX(data)
                    return
                }
                RESULT_ERROR -> return
                else -> return
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

}
