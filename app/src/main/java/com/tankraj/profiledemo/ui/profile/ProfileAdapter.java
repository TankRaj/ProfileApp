package com.tankraj.profiledemo.ui.profile;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tankraj.profiledemo.R;
import com.tankraj.profiledemo.entity.ProfileEntity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.profilesViewHolder> {

    private static final String DATE_FORMAT = "dd/MM/yyy";

    final private ItemClickListener mItemClickListener;
    private List<ProfileEntity> profileEntities;
    private Context mContext;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());


    public ProfileAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }


    @Override
    public profilesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_profile, parent, false);

        return new profilesViewHolder(view);
    }


    @Override
    public void onBindViewHolder(profilesViewHolder holder, int position) {

        ProfileEntity profileEntity = profileEntities.get(position);
        String description = profileEntity.getName();
        String updatedAt = dateFormat.format(profileEntity.getUpdatedAt());

        holder.tvName.setText(description);
        holder.tvDate.setText(updatedAt);
        holder.ivProfile.setImageURI(Uri.parse(profileEntity.getImageUri()));
        holder.tvEmail.setText(profileEntity.getEmail());
        holder.tvPhone.setText(profileEntity.getPhone());
        holder.tvBio.setText(profileEntity.getBio());
        holder.tvGender.setText(profileEntity.getGender());
        holder.tvDevice.setText(profileEntity.getDeviceType());

//        setDeviceType(holder.ivDeviceType,profileEntity.getDeviceType());
    }

    @Override
    public int getItemCount() {
        if (profileEntities == null) {
            return 0;
        }
        return profileEntities.size();
    }

    public void setProfiles(List<ProfileEntity> profiles) {
        profileEntities = profiles;
        notifyDataSetChanged();
    }


    public List<ProfileEntity> getProfiles() {
        return profileEntities;
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    class profilesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName;
        TextView tvDate;
        TextView tvEmail;
        TextView tvPhone;
        TextView tvBio;
        TextView tvGender;
        ImageView ivProfile;
        TextView tvDevice;


        public profilesViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvDevice = itemView.findViewById(R.id.tv_device);
            tvBio = itemView.findViewById(R.id.tv_bio);
            tvGender = itemView.findViewById(R.id.tv_gender);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = profileEntities.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}