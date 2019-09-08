package in.prepskool.prepskoolacademy.retrofit;

import in.prepskool.prepskoolacademy.retrofit_model.HomeResponse;
import in.prepskool.prepskoolacademy.retrofit_model.ResourceResponse;
import in.prepskool.prepskoolacademy.retrofit_model.ResourceTypeResponse;
import in.prepskool.prepskoolacademy.retrofit_model.StandardResponse;
import in.prepskool.prepskoolacademy.retrofit_model.SubjectResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("standards")
    Call<StandardResponse> getStandards();

    @GET("resources")
    Call<ResourceResponse> getResources();

    @GET("subjects")
    Call<SubjectResponse> getSubjects();

    @GET("home")
    Call<HomeResponse> getHomeResponse();

    @GET("streams")
    Call getStreams();

    @GET("resourceTypes")
    Call<ResourceTypeResponse> getResourceTypes();

    @GET("boards")
    Call getBoards();
}
