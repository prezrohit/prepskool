package in.prepskool.prepskoolacademy;

import android.app.Application;

import javax.inject.Inject;

import in.prepskool.prepskoolacademy.components.BoardComponent;
import in.prepskool.prepskoolacademy.components.DaggerHomeComponent;
import in.prepskool.prepskoolacademy.components.DaggerResourceComponent;
import in.prepskool.prepskoolacademy.components.DaggerStandardComponent;
import in.prepskool.prepskoolacademy.components.DaggerSubjectComponent;
import in.prepskool.prepskoolacademy.components.DaggerBoardComponent;
import in.prepskool.prepskoolacademy.components.HomeComponent;
import in.prepskool.prepskoolacademy.components.ResourceComponent;
import in.prepskool.prepskoolacademy.components.StandardComponent;
import in.prepskool.prepskoolacademy.components.SubjectComponent;
import in.prepskool.prepskoolacademy.retrofit.ApiClient;
import in.prepskool.prepskoolacademy.utils.Endpoints;

public class PrepskoolApplication extends Application {

    private StandardComponent standardComponent;
    private ResourceComponent resourceComponent;
    private SubjectComponent subjectComponent;
    private BoardComponent boardComponent;
    private HomeComponent homeComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        standardComponent = DaggerStandardComponent.builder()
                .apiClient(new ApiClient(Endpoints.BASE_URL))
                .build();

        resourceComponent = DaggerResourceComponent.builder()
                .apiClient(new ApiClient(Endpoints.BASE_URL))
                .build();

        subjectComponent = DaggerSubjectComponent.builder()
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
    public StandardComponent getStandardComponent() {
        return standardComponent;
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
    public BoardComponent getBoardComponent() {
        return boardComponent;
    }

    @Inject
    public HomeComponent getHomeComponent() {
        return homeComponent;
    }
}
