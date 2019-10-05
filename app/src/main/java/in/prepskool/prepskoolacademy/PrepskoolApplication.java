package in.prepskool.prepskoolacademy;

import android.app.Application;

import com.google.android.gms.common.api.Api;

import javax.inject.Inject;

import in.prepskool.prepskoolacademy.activities.StreamActivity;
import in.prepskool.prepskoolacademy.components.BoardComponent;
import in.prepskool.prepskoolacademy.components.DaggerBoardComponent;
import in.prepskool.prepskoolacademy.components.DaggerHomeComponent;
import in.prepskool.prepskoolacademy.components.DaggerLoginComponent;
import in.prepskool.prepskoolacademy.components.DaggerRegisterComponent;
import in.prepskool.prepskoolacademy.components.DaggerResourceComponent;
import in.prepskool.prepskoolacademy.components.DaggerResourceTypeComponent;
import in.prepskool.prepskoolacademy.components.DaggerStandardComponent;
import in.prepskool.prepskoolacademy.components.DaggerStreamComponent;
import in.prepskool.prepskoolacademy.components.DaggerSubjectComponent;
import in.prepskool.prepskoolacademy.components.HomeComponent;
import in.prepskool.prepskoolacademy.components.LoginComponent;
import in.prepskool.prepskoolacademy.components.RegisterComponent;
import in.prepskool.prepskoolacademy.components.ResourceComponent;
import in.prepskool.prepskoolacademy.components.ResourceTypeComponent;
import in.prepskool.prepskoolacademy.components.StandardComponent;
import in.prepskool.prepskoolacademy.components.StreamComponent;
import in.prepskool.prepskoolacademy.components.SubjectComponent;
import in.prepskool.prepskoolacademy.retrofit.ApiClient;
import in.prepskool.prepskoolacademy.retrofit_model.Login;
import in.prepskool.prepskoolacademy.utils.Endpoints;

public class PrepskoolApplication extends Application {

    private ResourceTypeComponent resourceTypeComponent;
    private RegisterComponent registerComponent;
    private StandardComponent standardComponent;
    private ResourceComponent resourceComponent;
    private SubjectComponent subjectComponent;
    private StreamComponent streamComponent;
    private LoginComponent loginComponent;
    private BoardComponent boardComponent;
    private HomeComponent homeComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        resourceTypeComponent = DaggerResourceTypeComponent.builder()
                .apiClient(new ApiClient(Endpoints.BASE_URL))
                .build();

        registerComponent = DaggerRegisterComponent.builder()
                .apiClient(new ApiClient(Endpoints.BASE_URL))
                .build();

        standardComponent = DaggerStandardComponent.builder()
                .apiClient(new ApiClient(Endpoints.BASE_URL))
                .build();

        resourceComponent = DaggerResourceComponent.builder()
                .apiClient(new ApiClient(Endpoints.BASE_URL))
                .build();

        subjectComponent = DaggerSubjectComponent.builder()
                .apiClient(new ApiClient(Endpoints.BASE_URL))
                .build();

        streamComponent = DaggerStreamComponent.builder()
                .apiClient(new ApiClient(Endpoints.BASE_URL))
                .build();

        loginComponent = DaggerLoginComponent.builder()
                .apiClient(new ApiClient(Endpoints.BASE_URL))
                .build();

        boardComponent = DaggerBoardComponent.builder()
                .apiClient(new ApiClient(Endpoints.BASE_URL))
                .build();

        homeComponent = DaggerHomeComponent.builder()
                .apiClient(new ApiClient(Endpoints.BASE_URL))
                .build();
    }

    @Inject
    public ResourceTypeComponent getResourceTypeComponent() {
        return resourceTypeComponent;
    }

    @Inject
    public StandardComponent getStandardComponent() {
        return standardComponent;
    }

    @Inject
    public RegisterComponent getRegisterComponent() {
        return registerComponent;
    }

    @Inject
    public ResourceComponent getResourceComponent() {
        return resourceComponent;
    }

    @Inject
    public SubjectComponent getSubjectComponent() {
        return subjectComponent;
    }

    @Inject
    public StreamComponent getStreamComponent() {
        return streamComponent;
    }

    @Inject
    public LoginComponent getLoginComponent() {
        return loginComponent;
    }

    @Inject
    public BoardComponent getBoardComponent() {
        return boardComponent;
    }

    @Inject
    public HomeComponent getHomeComponent() {
        return homeComponent;
    }
}
