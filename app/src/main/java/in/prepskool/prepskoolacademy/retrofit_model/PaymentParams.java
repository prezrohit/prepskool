package in.prepskool.prepskoolacademy.retrofit_model;

import com.google.gson.annotations.SerializedName;

public class PaymentParams {

    public PaymentParams(String key, String transactionId, String amount, String paymentInfo, String userName, String email, String salt, String udf1, String udf2, String udf3, String udf4, String udf5) {
        this.key = key;
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentInfo = paymentInfo;
        this.userName = userName;
        this.email = email;
        this.salt = salt;
        this.udf1 = udf1;
        this.udf2 = udf2;
        this.udf3 = udf3;
        this.udf4 = udf4;
        this.udf5 = udf5;
    }

    @SerializedName("key")
    private String key;

    @SerializedName("txnid")
    private String transactionId;

    @SerializedName("amount")
    private String amount;

    @SerializedName("pinfo")
    private String paymentInfo;

    @SerializedName("fname")
    private String userName;

    @SerializedName("email")
    private String email;

    @SerializedName("salt")
    private String salt;

    @SerializedName("udf1")
    private String udf1;

    @SerializedName("udf2")
    private String udf2;

    @SerializedName("udf3")
    private String udf3;

    @SerializedName("udf4")
    private String udf4;

    @SerializedName("udf5")
    private String udf5;
}
