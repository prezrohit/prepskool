package in.prepskool.prepskoolacademy.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.ArrayList;

import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.retrofit_model.SectionedResource;

public class SectionRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Object> sectionedResourceList;

    // Resource Item ViewType
    private static final int RESOURCE_ITEM_TYPE = 0;

    // unified native ad view type.
    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;

    public SectionRecyclerViewAdapter(Context context, ArrayList<Object> sectionedResourceList) {
        this.context = context;
        this.sectionedResourceList = sectionedResourceList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                View unifiedNativeLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_unified_ad, parent, false);
                return new SectionRecyclerViewAdapter.UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
            case RESOURCE_ITEM_TYPE:
                // Fall through.
            default:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_resource_section, parent, false);
                return new SectionRecyclerViewAdapter.SectionViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);
        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                UnifiedNativeAd nativeAd = (UnifiedNativeAd) sectionedResourceList.get(position);
                populateNativeAdView(nativeAd, ((SectionRecyclerViewAdapter.UnifiedNativeAdViewHolder) holder).getAdView());
                break;
            case RESOURCE_ITEM_TYPE:
                // fall through
            default:
                SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
                SectionedResource sectionedResource = (SectionedResource) sectionedResourceList.get(position);

                sectionViewHolder.lblSectionLabel.setText(sectionedResource.getSectionLabel());

                sectionViewHolder.rvResourceItems.setHasFixedSize(true);
                sectionViewHolder.rvResourceItems.setNestedScrollingEnabled(false);
                sectionViewHolder.rvResourceItems.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                sectionViewHolder.rvResourceItems.setAdapter(new ItemRecyclerViewAdapter(context, sectionedResource.getResourceList()));
        }
    }

    @Override
    public int getItemCount() {
        return sectionedResourceList.size();
    }

    @Override
    public int getItemViewType(int position) {

        Object recyclerViewItem = sectionedResourceList.get(position);
        if (recyclerViewItem instanceof UnifiedNativeAd) {
            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }
        return RESOURCE_ITEM_TYPE;
    }

    class SectionViewHolder extends RecyclerView.ViewHolder {
        private TextView lblSectionLabel;
        private RecyclerView rvResourceItems;

        SectionViewHolder(View itemView) {
            super(itemView);

            lblSectionLabel = (TextView) itemView.findViewById(R.id.lbl_resource_label);
            rvResourceItems = (RecyclerView) itemView.findViewById(R.id.rv_resource_items);
        }
    }

    class UnifiedNativeAdViewHolder extends RecyclerView.ViewHolder {

        private UnifiedNativeAdView adView;

        UnifiedNativeAdView getAdView() {
            return adView;
        }

        UnifiedNativeAdViewHolder(@NonNull View itemView) {
            super(itemView);

            adView = (UnifiedNativeAdView) itemView.findViewById(R.id.ad_native_resource);

            // The MediaView will display a video asset if one is present in the ad, and the
            // first image asset otherwise.
            //adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

            // Register the view used for each individual asset.
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_icon));
            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        }
    }

    private void populateNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {

        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }
}
