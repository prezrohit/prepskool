package in.prepskool.prepskoolacademy.components;

import javax.inject.Singleton;

import dagger.Component;
import in.prepskool.prepskoolacademy.TestPaymentActivity;
import in.prepskool.prepskoolacademy.retrofit.ApiClient;

@Singleton
@Component(modules = {ApiClient.class})
public interface PaymentComponent {
    void inject(TestPaymentActivity testPaymentActivity);
}
