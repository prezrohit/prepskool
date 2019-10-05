package in.prepskool.prepskoolacademy.retrofit_model;

//TODO: price field in resource API

import com.google.gson.annotations.SerializedName;

public class PaymentParams {

    public PaymentParams(String key, String transactionId, String amount, String paymentInfo, String userName, String email, String salt) {
        this.key = key;
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentInfo = paymentInfo;
        this.userName = userName;
        this.email = email;
        this.salt = salt;
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
}
