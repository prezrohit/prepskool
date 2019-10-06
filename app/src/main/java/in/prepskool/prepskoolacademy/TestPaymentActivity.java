package in.prepskool.prepskoolacademy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import in.prepskool.prepskoolacademy.retrofit.ApiInterface;
import in.prepskool.prepskoolacademy.retrofit_model.PaymentParams;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TestPaymentActivity extends AppCompatActivity {

    @Inject
    Retrofit retrofit;

    private EditText edtName;
    private EditText edtEmail;
    private EditText edtNumber;
    private EditText edtAmount;

    PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

    PayUmoneySdkInitializer.PaymentParam paymentParam = null;

    private String txnId;
    private ApiInterface apiInterface;

    private static final String TAG = "TestPaymentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_payment);

        edtAmount = findViewById(R.id.edt_amount);
        edtEmail = findViewById(R.id.edt_email);
        edtName = findViewById(R.id.edt_name);
        edtNumber = findViewById(R.id.edt_number);

        ((PrepskoolApplication) getApplication()).getPaymentComponent().inject(this);
        apiInterface = retrofit.create(ApiInterface.class);
    }

    public void startPayment(View view) {
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        String amount = edtAmount.getText().toString();
        String number = edtNumber.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(amount) || TextUtils.isEmpty(number) || !email.contains("@")
                || Integer.parseInt(amount) < 1) {
            Toast.makeText(this, "inputs invalid", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "startPayment: " + name);
            Log.d(TAG, "startPayment: " + email);
            Log.d(TAG, "startPayment: " + Integer.parseInt(amount));
            Log.d(TAG, "startPayment: " + number);

        } else {
            initPaymentHandler(name, email, amount, number);
        }
    }

    private void initPaymentHandler(String name, String email, String amount, String number) {
        builder.setKey("4xCiKohu")
                .setMerchantId("6814167")
                .setAmount(amount)
                .setPhone(number)
                .setIsDebug(true)
                .setTxnId("txn_ps")
                .setProductName("prepskool")
                .setFirstName(name)
                .setEmail(email)
                .setsUrl("https://google.com")
                .setfUrl("https://gmail.com")
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("");

        try {
            paymentParam = builder.build();
            //PaymentParams paymentParams = new PaymentParams("4xCiKohu", "txn_ps", "20", "prepskool", "prezrohit", "prezrohit@gmail.com", "1nAqMe8r4d", "", "", "" ,"" ,"");

            String hashSequence = "4xCiKohu|txn_ps|" + amount + "|prepskool|" + name + "|" + email + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "||||||1nAqMe8r4d";
            String serverCalculatedHash = calculateHash("SHA-512", hashSequence);
            paymentParam.setMerchantHash(serverCalculatedHash);
            PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, TestPaymentActivity.this, R.style.AppTheme_default, false);

            //getHashKey(apiInterface, paymentParams);

        } catch (Exception e) {
            Log.e(TAG, " error s: " + e.toString());
        }
    }

    private void getHashKey(ApiInterface apiInterface, final PaymentParams paymentParams) {
        Call<String> call = apiInterface.getServerHash("application/json", "", paymentParams);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.body() != null) {
                    Log.d(TAG, "hash: " + response.body());
                    String hash = response.body();
                    paymentParam.setMerchantHash(hash);
                    PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, TestPaymentActivity.this, R.style.AppTheme_default, false);

                } else {
                    Log.d(TAG, "onResponse: server hash empty: " + response.body());
                    Toast.makeText(TestPaymentActivity.this, "some error occurred", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                Toast.makeText(TestPaymentActivity.this, "error communicating the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static String calculateHash(String type, String hashString) {
        StringBuilder hash = new StringBuilder();
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(type);
            messageDigest.update(hashString.getBytes());
            byte[] mdbytes = messageDigest.digest();
            for (byte hashByte : mdbytes) {
                hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
// PayUMoneySdk: Success -- payuResponse{"id":225642,"mode":"CC","status":"success","unmappedstatus":"captured","key":"9yrcMzso","txnid":"223013","transaction_fee":"20.00","amount":"20.00","cardCategory":"domestic","discount":"0.00","addedon":"2018-12-31 09:09:43","productinfo":"a2z shop","firstname":"kamal","email":"kamal.bunkar07@gmail.com","phone":"9144040888","hash":"b22172fcc0ab6dbc0a52925ebbd0297cca6793328a8dd1e61ef510b9545d9c851600fdbdc985960f803412c49e4faa56968b3e70c67fe62eaed7cecacdfdb5b3","field1":"562178","field2":"823386","field3":"2061","field4":"MC","field5":"167227964249","field6":"00","field7":"0","field8":"3DS","field9":" Verification of Secure Hash Failed: E700 -- Approved -- Transaction Successful -- Unable to be determined--E000","payment_source":"payu","PG_TYPE":"AXISPG","bank_ref_no":"562178","ibibo_code":"VISA","error_code":"E000","Error_Message":"No Error","name_on_card":"payu","card_no":"401200XXXXXX1112","is_seamless":1,"surl":"https://www.payumoney.com/sandbox/payment/postBackParam.do","furl":"https://www.payumoney.com/sandbox/payment/postBackParam.do"}
        // Result Code is -1 send from Payumoney activity
        Log.e("StartPaymentActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Payment Status: success");

                } else {
                    //Failure Transaction
                    Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Payment Status: failure");
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();
                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();
                Log.e(TAG, "transaction " + payuResponse + "---" + merchantResponse);
            } /* else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }*/
        }
    }
}
