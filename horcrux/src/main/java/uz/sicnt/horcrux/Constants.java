package uz.sicnt.horcrux;

public class Constants {

    /**
     * SETTINGS
     */
    public static final String E_IMZO_APP = "uz.yt.eimzo";
    public static final String E_IMZO_ACTIVITY = "uz.yt.eimzo.activity.MainActivity";

    public static final int RESULT_ERROR = 9;
    public static final int RESULT_ACCESS_DENIED = 10;
    public static final int CREATE_PKCS7 = 100;
    public static final int APPEND_CODE = 200;
    public static final int ATTACH_CODE = 300;

    /**
     * CALLBACK keys
     */
    public static final String EXTRA_PARAM_ATTACH_TST = "tst";
    public static final String EXTRA_PARAM_ATTACH_PKCS7 = "attach_pkcs7";
    public static final String EXTRA_PARAM_ATTACH_SERIAL_NUMBER = "attach_serial_number";
    public static final String EXTRA_PARAM_SERIAL_NUMBER = "serial_number";
    public static final String EXTRA_PARAM_APPEND_PKCS7 = "append_pkcs7";
    public static final String EXTRA_PARAM_MESSAGE = "message";
    public static final String EXTRA_PARAM_API_KEY = "api_key";
    public static final String EXTRA_RESULT_PKCS7 = "pkcs7";
    public static final String EXTRA_RESULT_SERIAL_NUMBER = "serial_number";
    public static final String EXTRA_RESULT_SUBJECT_NAME = "subject_name";
    public static final String EXTRA_RESULT_SIGNATURE = "signature";
    public static final String EXTRA_RESULT_ERROR_CLASS = "error_class";
    public static final String EXTRA_RESULT_ERROR_MESSAGE = "error_message";
    public static final String RESULT_DOC_HASH = "doc_hash";

    /**
     * REGEX
     **/
    public static final String FIO = "CN";                      //  Ф.И.О
    public static final String YUR_TIN = "1.2.860.3.16.1.1";    //  ЮРИДИЧЕСКИЙ ИНН
    public static final String FIZ_TIN = "UID";                 //  ИНДИВИДУАЛЬНИЙ ИНН
    public static final String FORENAME = "Name";               //  ИМЯ
    public static final String SURNAME = "SURNAME";             //  ФАМИЛИЯ
    public static final String AREA = "L";                      //  ОКРУГ
    public static final String REGION = "ST";                   //  ОБЛАСТЬ
    public static final String COUNTRY = "C";                   //  СТРАНА
    public static final String PINFL = "1.2.860.3.16.1.2";      //  ПИНФЛ
    public static final String EMAIL = "E";                     //  ЭЛ. АДРЕС
    public static final String JOBTITLE = "T";                  //  ДОЛЖНОСТЬ
    public static final String ORGANIZATION = "O";              //  ОРГАНИЗАЦИЯ
    public static final String DEPARTMENT = "OU";               //  ОТДЕЛ

}
