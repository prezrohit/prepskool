package in.prepskool.prepskoolacademy.components;

import javax.inject.Singleton;

import dagger.Component;
import in.prepskool.prepskoolacademy.activities.HomeActivity;
import in.prepskool.prepskoolacademy.activities.StandardActivity;
import in.prepskool.prepskoolacademy.retrofit.ApiClient;

@Singleton
@Component(modules = {ApiClient.class})
public interface StandardComponent {

    void inject(HomeActivity standardActivity);
}
