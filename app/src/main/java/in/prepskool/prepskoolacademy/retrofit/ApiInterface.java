package in.prepskool.prepskoolacademy.retrofit;

import in.prepskool.prepskoolacademy.retrofit_model.HomeResponse;
import in.prepskool.prepskoolacademy.retrofit_model.Login;
import in.prepskool.prepskoolacademy.retrofit_model.LoginResponse;
import in.prepskool.prepskoolacademy.retrofit_model.Register;
import in.prepskool.prepskoolacademy.retrofit_model.RegisterResponse;
import in.prepskool.prepskoolacademy.retrofit_model.ResourceResponse;
import in.prepskool.prepskoolacademy.retrofit_model.ResourceTypeResponse;
import in.prepskool.prepskoolacademy.retrofit_model.StandardResponse;
import in.prepskool.prepskoolacademy.retrofit_model.StreamResponse;
import in.prepskool.prepskoolacademy.retrofit_model.SubjectResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {


    @GET("standards")
    Call<StandardResponse> getStandards();


    @GET("resources/{board_id}/{standard_id}/{subject_id}/{resource_type_id}")
    Call<ResourceResponse> getBoardResources(@Path("board_id") int categoryId, @Path("standard_id") int standardId, @Path("subject_id") int subjectId, @Path("resource_type_id") int resourceTypeId);


    @GET("resources/{resource_type_id}/{standard_id}/{subject_id}")
    Call<ResourceResponse> getOtherResources(@Path("resource_type_id") int resourceTypeId, @Path("standard_id") int standardId, @Path("subject_id") int subjectId);


    @GET("subjects/{standard_id}")
    Call<SubjectResponse> getSubjects(@Path("standard_id") int standardId);


    @GET("subjects/{standard_id}")
    Call<StreamResponse> getStreams(@Path("standard_id") int standardId);


    @GET("home")
    Call<HomeResponse> getHomeResponse();


    @GET("resourceTypes/{standard_id}/{subject_id}")
    Call<ResourceTypeResponse> getResourceTypes(@Path("standard_id") int standardId, @Path("subject_id") int subjectId);


    @POST("login")
    Call<LoginResponse> login(@Body Login login);


    @POST("register")
    Call<RegisterResponse> register(@Body Register register);
}
