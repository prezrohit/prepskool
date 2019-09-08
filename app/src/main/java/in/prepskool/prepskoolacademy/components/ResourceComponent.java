package in.prepskool.prepskoolacademy.components;

import javax.inject.Singleton;

import dagger.Component;
import in.prepskool.prepskoolacademy.activities.ResourceActivity;
import in.prepskool.prepskoolacademy.retrofit.ApiClient;

@Singleton
@Component(modules = {ApiClient.class})
public interface ResourceComponent {
    void inject(ResourceActivity resourceActivity);
}
