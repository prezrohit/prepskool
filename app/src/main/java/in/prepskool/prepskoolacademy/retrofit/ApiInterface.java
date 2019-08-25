package in.prepskool.prepskoolacademy.retrofit;

import java.util.ArrayList;

import in.prepskool.prepskoolacademy.retrofit_model.HomeResponse;
import in.prepskool.prepskoolacademy.retrofit_model.ResourceResponse;
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
    Call<ArrayList<HomeResponse>> getHomeResponse();
}
