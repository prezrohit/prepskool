package in.prepskool.prepskoolacademy.components;

import javax.inject.Singleton;

import dagger.Component;
import in.prepskool.prepskoolacademy.activities.HomeActivity;
import in.prepskool.prepskoolacademy.activities.NonBoardActivity;
import in.prepskool.prepskoolacademy.activities.StreamActivity;
import in.prepskool.prepskoolacademy.fragments.ArtFragment;
import in.prepskool.prepskoolacademy.fragments.CommerceFragment;
import in.prepskool.prepskoolacademy.fragments.ScienceFragment;
import in.prepskool.prepskoolacademy.retrofit.ApiClient;

@Singleton
@Component(modules = {ApiClient.class})
public interface SubjectComponent {
    void inject(ArtFragment artFragment);

    void inject(ScienceFragment scienceFragment);

    void inject(CommerceFragment commerceFragment);

    void inject(NonBoardActivity nonBoardActivity);
}
