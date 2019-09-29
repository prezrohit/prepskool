package in.prepskool.prepskoolacademy.retrofit;

import in.prepskool.prepskoolacademy.retrofit_model.HomeResponse;
import in.prepskool.prepskoolacademy.retrofit_model.ResourceResponse;
import in.prepskool.prepskoolacademy.retrofit_model.ResourceTypeResponse;
import in.prepskool.prepskoolacademy.retrofit_model.StandardResponse;
import in.prepskool.prepskoolacademy.retrofit_model.StreamResponse;
import in.prepskool.prepskoolacademy.retrofit_model.SubjectResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("standards")
    Call<StandardResponse> getStandards();


    @GET("resources")
    Call<ResourceResponse> getBoardResources(@Query("board_id") int categoryId, @Query("standard_id") int standardId,
                                        @Query("subject_id") int subjectId, @Query("resource_type_id") int resourceTypeId);


    @GET("resources")
    Call<ResourceResponse> getOtherResources(@Query("resource_type_id") int resourceTypeId, @Query("standard_id") int standardId,
                                             @Query("subject_id") int subjectId);


    @GET("subjects")
    Call<SubjectResponse> getSubjects(@Query("standard_id") int standardId);


    @GET("home")
    Call<HomeResponse> getHomeResponse();


    @GET("streams")
    Call<StreamResponse> getStreams(@Query("standard_id") int standardId);


    @GET("resourceTypes")
    Call<ResourceTypeResponse> getResourceTypes(@Query("standard_id") int standardId, @Query("subject_id") int subjectId);


    @GET("boards")
    Call getBoards();
}
