package in.prepskool.prepskoolacademy.components;

import javax.inject.Singleton;

import dagger.Component;
import in.prepskool.prepskoolacademy.activities.ResourceTypeActivity;
import in.prepskool.prepskoolacademy.retrofit.ApiClient;

@Singleton
@Component(modules = {ApiClient.class})
public interface ResourceTypeComponent {
    void inject(ResourceTypeActivity resourceTypeActivity);
}
