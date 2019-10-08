package in.prepskool.prepskoolacademy.retrofit;

import in.prepskool.prepskoolacademy.retrofit_model.HomeResponse;
import in.prepskool.prepskoolacademy.retrofit_model.Login;
import in.prepskool.prepskoolacademy.retrofit_model.LoginResponse;
import in.prepskool.prepskoolacademy.retrofit_model.Register;
import in.prepskool.prepskoolacademy.retrofit_model.RegisterResponse;
import in.prepskool.prepskoolacademy.retrofit_model.PaymentParams;
import in.prepskool.prepskoolacademy.retrofit_model.ResourceResponse;
import in.prepskool.prepskoolacademy.retrofit_model.ResourceTypeResponse;
import in.prepskool.prepskoolacademy.retrofit_model.StandardResponse;
import in.prepskool.prepskoolacademy.retrofit_model.StreamResponse;
import in.prepskool.prepskoolacademy.retrofit_model.SubjectResponse;
import in.prepskool.prepskoolacademy.utils.ApiHeaders;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {


    @Headers({"Accept: application/json"})
    @GET("resources/{board_id}/{standard_id}/{subject_id}/{resource_type_id}")
    Call<ResourceResponse> getBoardResources(@Header("Authorization") String auth, @Path("board_id") int categoryId, @Path("standard_id") int standardId, @Path("subject_id") int subjectId, @Path("resource_type_id") int resourceTypeId);


    @Headers({"Accept: application/json"})
    @GET("resources/{resource_type_id}/{standard_id}/{subject_id}")
    Call<ResourceResponse> getOtherResources(@Header("Authorization") String auth, @Path("resource_type_id") int resourceTypeId, @Path("standard_id") int standardId, @Path("subject_id") int subjectId);


    @Headers({"Accept: application/json"})
    @GET("resourceTypes/{standard_id}/{subject_id}")
    Call<ResourceTypeResponse> getResourceTypes(@Header("Authorization") String auth, @Path("standard_id") int standardId, @Path("subject_id") int subjectId);


    @Headers({"Accept: application/json"})
    @GET("subjects/{standard_id}")
    Call<SubjectResponse> getSubjects(@Header("Authorization") String auth, @Path("standard_id") int standardId);


    @Headers({"Accept: application/json"})
    @GET("subjects/{standard_id}")
    Call<StreamResponse> getStreams(@Header("Authorization") String auth, @Path("standard_id") int standardId);


    @Headers({"Accept: application/json"})
    @GET("standards")
    Call<StandardResponse> getStandards(@Header("Authorization") String auth);


    @Headers({"Accept: application/json"})
    @GET("home")
    Call<HomeResponse> getHomeResponse(@Header("Authorization") String auth);


    @POST("login")
    Call<LoginResponse> login(@Body Login login);


    @POST("signup")
    Call<RegisterResponse> register(@Body Register register);
}
