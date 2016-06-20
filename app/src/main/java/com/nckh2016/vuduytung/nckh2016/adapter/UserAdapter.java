package com.nckh2016.vuduytung.nckh2016.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;
import com.nckh2016.vuduytung.nckh2016.R;
import com.nckh2016.vuduytung.nckh2016.animation.ResizeHeightAnimation;
import com.nckh2016.vuduytung.nckh2016.main.Utils;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUser;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Tung on 13/5/2016.
 * new adapter (RecycleView)
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private int lastPosition = -1;
    private ArrayList<ObjectUser> objects = new ArrayList<>();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public int position = -1;
        public FrameLayout container;
        public LinearLayout mainLayout;
        public TextView txtHoTen, txtMaSinhVien, txtKhoa, txtNganh, txtChuyenSau, txtNamThu;
        public Button btnXoa;

        public ViewHolder(View itemView) {
            super(itemView);
            this.container = (FrameLayout) itemView.findViewById(R.id.card_view);
            this.mainLayout = (LinearLayout) itemView.findViewById(R.id.mainLayout);
            this.txtHoTen = (TextView) itemView.findViewById(R.id.txtHoTen);
            this.txtMaSinhVien = (TextView) itemView.findViewById(R.id.txtMaSinhVien);
            this.txtKhoa = (TextView) itemView.findViewById(R.id.txtKhoa);
            this.txtNganh = (TextView) itemView.findViewById(R.id.txtNganh);
            this.txtChuyenSau = (TextView) itemView.findViewById(R.id.txtChuyenSau);
            this.txtNamThu = (TextView) itemView.findViewById(R.id.txtNamThu);
            this.btnXoa = (Button) itemView.findViewById(R.id.btnXoa);
        }

        public void clearAnimation() {
            this.container.clearAnimation();
        }
    }

    public UserAdapter(Context mContext, ArrayList<ObjectUser> objects) {
        this.mContext = mContext;
        this.objects = objects;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_adapter, parent, false);
        // set the view's size, margins, paddings and layout parameters, bla bla bla...
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (objects.size() != 0) {
            final ObjectUser mUser = objects.get(position);
            if (mUser != null) {
                holder.position = position;
                holder.txtHoTen.setText(mUser.getHoten());
                holder.txtMaSinhVien.setText(mUser.getMasv());
                holder.txtNamThu.setText(mUser.getNamhoc());
                //animation
                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Utils.pxToDp(v.getHeight()) == 50){
                            //v.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, Utils.dpToPx(200)));
                            new AsyncTask<ViewHolder, Void, Void>() {
                                private ViewHolder viewHolder;
                                private String tenKhoa, tenNganh, tenChuyenSau;

                                @Override
                                protected Void doInBackground(ViewHolder... params) {
                                    viewHolder = params[0];
                                    SQLiteDataController data = SQLiteDataController.getInstance(mContext);
                                    try {
                                        data.isCreatedDatabase();
                                    } catch (IOException e) {
                                        Log.e("tag", e.getMessage());
                                    }
                                    tenKhoa = data.getTenKhoa(mUser.getMakhoa());
                                    tenNganh = data.getTenNganh(mUser.getManganh());
                                    tenChuyenSau = data.getTenChuyenSau(mUser.getManganh(), Integer.parseInt(mUser.getMachuyensau()));
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    holder.txtKhoa.setText(tenKhoa);
                                    holder.txtNganh.setText(tenNganh);
                                    holder.txtChuyenSau.setText(tenChuyenSau);
                                }
                            }.execute(holder);
                            ResizeHeightAnimation anim = new ResizeHeightAnimation(v, Utils.dpToPx(200));
                            anim.setInterpolator(new AccelerateDecelerateInterpolator());
                            anim.setDuration(Utils.ANIM_OFFSET);
                            v.startAnimation(anim);
                        } else {
                            //v.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, Utils.dpToPx(50)));
                            ResizeHeightAnimation anim = new ResizeHeightAnimation(v, Utils.dpToPx(50));
                            anim.setInterpolator(new AccelerateDecelerateInterpolator());
                            anim.setDuration(Utils.ANIM_OFFSET);
                            v.startAnimation(anim);
                        }
                    }
                });
                holder.btnXoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(mContext)
                                .setTitle("Xóa hồ sơ")
                                .setMessage("Xóa hồ sơ có mã sinh viên " + mUser.getMasv() + "?")
                                .setIcon(R.drawable.error)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        new AsyncTask<ViewHolder, Void, Integer>(){
                                            private ViewHolder viewHolder;
                                            private ProgressDialog progressDelete;

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                progressDelete = new ProgressDialog(mContext);
                                                progressDelete.setMessage("Xóa hồ sơ");
                                                progressDelete.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                progressDelete.setIndeterminate(true);
                                                progressDelete.setProgressNumberFormat(null);
                                                progressDelete.setProgressPercentFormat(null);
                                                progressDelete.show();
                                            }

                                            @Override
                                            protected Integer doInBackground(ViewHolder... params) {
                                                viewHolder = params[0];
                                                SQLiteDataController data = SQLiteDataController.getInstance(mContext);
                                                try {
                                                    data.isCreatedDatabase();
                                                } catch (IOException e) {
                                                    Log.e("tag", e.getMessage());
                                                }
                                                return data.deleteUser(mUser.getMasv());
                                            }

                                            @Override
                                            protected void onPostExecute(Integer integer) {
                                                super.onPostExecute(integer);
                                                if (integer != -1) {
                                                    remove(viewHolder.position);
                                                }
                                                progressDelete.dismiss();
                                            }
                                        }.execute(holder);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                });
                // Here you apply the animation when the view is bound
                setAnimation(holder.container, position, Utils.ANIM_OFFSET);
            }
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void add(int position, ObjectUser obj) {
        this.objects.add(position, obj);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public void remove(int position) {
        this.objects.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.clearAnimation();
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position, long animationOffset) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            //animation.setStartOffset((position + 1) * animationOffset); // có tý vấn đề khi scroll
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.setStartOffset(animationOffset); // setting the offset
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
