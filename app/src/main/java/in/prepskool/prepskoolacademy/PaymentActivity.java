package in.prepskool.prepskoolacademy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import in.prepskool.prepskoolacademy.activities.DownloadActivity;
import in.prepskool.prepskoolacademy.app.AppSharedPreferences;
import in.prepskool.prepskoolacademy.retrofit.ApiInterface;
import in.prepskool.prepskoolacademy.retrofit_model.PaymentParams;
import in.prepskool.prepskoolacademy.utils.ApiHeaders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PaymentActivity extends AppCompatActivity {

    @Inject
    Retrofit retrofit;

    private AppSharedPreferences appSharedPreferences;

    private String amount;
    private String link;
    private String slug;
    private String resourceName;
    private String transactionId;

    private static final String TAG = "PaymentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Complete the Payment");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        amount = getIntent().getStringExtra("price");
        resourceName = getIntent().getStringExtra("name");
        link = getIntent().getStringExtra("link");
        slug = getIntent().getStringExtra("slug");

        TextView lblResourceName = findViewById(R.id.lbl_resource_name);
        TextView lblResourcePrice = findViewById(R.id.lbl_resource_price);
        lblResourceName.setText(resourceName);
        lblResourcePrice.setText("Price: Rs. " + amount);

        appSharedPreferences = new AppSharedPreferences(this);
    }

    public void startPayment(View view) {
        String name = appSharedPreferences.getName();
        String email = appSharedPreferences.getEmail();
        String number = appSharedPreferences.getPhone();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(amount) || TextUtils.isEmpty(number) || !email.contains("@")
                || Integer.parseInt(amount) < 1 || number.length() != 10) {
            Toast.makeText(this, "input invalid", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "startPayment: " + name);
            Log.d(TAG, "startPayment: " + email);
            Log.d(TAG, "startPayment: " + Integer.parseInt(amount));
            Log.d(TAG, "startPayment: " + number);

        } else {
            transactionId = "txn_id-" + System.currentTimeMillis();
            initPaymentHandler(name, email, amount, number);
        }
    }

    private void initPaymentHandler(String name, String email, String amount, String number) {
        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
        builder.setKey("3osEr6BM")
                .setMerchantId("6634466")
                .setAmount(amount)
                .setPhone(number)
                .setIsDebug(false)
                .setTxnId(transactionId)
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
            PayUmoneySdkInitializer.PaymentParam paymentParam = builder.build();

            String hashSequence = "3osEr6BM|" + transactionId + "|" + amount + "|prepskool|" + name + "|" + email + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "||||||EvBjxtd03M";
            String serverCalculatedHash = calculateHash("SHA-512", hashSequence);
            paymentParam.setMerchantHash(serverCalculatedHash);
            PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, PaymentActivity.this, R.style.AppTheme_default, false);

        } catch (Exception e) {
            Log.e(TAG, " error s: " + e.toString());
        }
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

        Log.v("StartPaymentActivity", "request code: " + requestCode + " resultcode: " + resultCode);

        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Payment Status: success");
                    Intent intent = new Intent(PaymentActivity.this, DownloadActivity.class);
                    intent.putExtra("name", resourceName);
                    intent.putExtra("link", link);
                    intent.putExtra("slug", slug);
                    startActivity(intent);
                    finish();

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
