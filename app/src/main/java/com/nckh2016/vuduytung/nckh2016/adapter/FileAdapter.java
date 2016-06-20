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

import com.google.gson.Gson;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;
import com.nckh2016.vuduytung.nckh2016.R;
import com.nckh2016.vuduytung.nckh2016.animation.ResizeHeightAnimation;
import com.nckh2016.vuduytung.nckh2016.main.Utils;
import com.nckh2016.vuduytung.nckh2016.object.ObjectUser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Tung on 14/5/2016.
 * new adapter (RecycleView)
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    private Context mContext;
    private int lastPosition = -1;
    private ArrayList<File> objects = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public int position = -1;
        public FrameLayout container;
        public LinearLayout mainLayout;
        public TextView txtTenFile, txtKichThuoc, txtDuongDan;
        public Button btnXoa, btnRestore;

        public ViewHolder(View itemView) {
            super(itemView);
            this.container = (FrameLayout) itemView.findViewById(R.id.card_view);
            this.mainLayout = (LinearLayout) itemView.findViewById(R.id.mainLayout);
            this.txtTenFile = (TextView) itemView.findViewById(R.id.txtTenFile);
            this.txtKichThuoc = (TextView) itemView.findViewById(R.id.txtKichThuoc);
            this.txtDuongDan = (TextView) itemView.findViewById(R.id.txtDuongDan);
            this.btnXoa = (Button) itemView.findViewById(R.id.btnXoa);
            this.btnRestore = (Button) itemView.findViewById(R.id.btnRestore);
        }

        public void clearAnimation() {
            this.container.clearAnimation();
        }
    }

    public FileAdapter(Context mContext, ArrayList<File> objects) {
        this.mContext = mContext;
        this.objects = objects;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_adapter, parent, false);
        // set the view's size, margins, paddings and layout parameters, bla bla bla...
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (objects.size() != 0) {
            final File mFile = objects.get(position);
            if (mFile != null) {
                holder.position = position;
                holder.txtTenFile.setText(mFile.getName());
                String size = "";
                long fileSize = mFile.length();
                if (fileSize < 1024) {
                    size = String.valueOf(fileSize) + " bytes";
                } else if (fileSize < 1024 * 1024) {
                    size = String.valueOf(new DecimalFormat("####0.##").format((double) fileSize / 1024)) + " KB";
                } else if (fileSize < 1024 * 1024 * 1024) {
                    size = String.valueOf(new DecimalFormat("####0.##").format((double) fileSize / 1024 / 1024)) + " MB";
                }
                holder.txtKichThuoc.setText(size);
                holder.txtDuongDan.setText(mFile.getPath());
                //animation
                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Utils.pxToDp(v.getHeight()) == 50){
                            ResizeHeightAnimation anim = new ResizeHeightAnimation(v, Utils.dpToPx(200));
                            anim.setInterpolator(new AccelerateDecelerateInterpolator());
                            anim.setDuration(Utils.ANIM_OFFSET);
                            v.startAnimation(anim);
                        } else {
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
                                .setTitle("Xóa")
                                .setMessage("Xóa tệp sao lưu có mã sinh viên " + mFile.getName().replace(Utils.BACKUP_FILE_PATTERN, "") + "?")
                                .setIcon(R.drawable.error)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        new AsyncTask<ViewHolder, Void, Boolean>(){
                                            private ViewHolder viewHolder;
                                            private ProgressDialog progressDelete;

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                progressDelete = new ProgressDialog(mContext);
                                                progressDelete.setMessage("Xóa tệp");
                                                progressDelete.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                progressDelete.setIndeterminate(true);
                                                progressDelete.setProgressNumberFormat(null);
                                                progressDelete.setProgressPercentFormat(null);
                                                progressDelete.show();
                                            }

                                            @Override
                                            protected Boolean doInBackground(ViewHolder... params) {
                                                viewHolder = params[0];
                                                File file = new File(mFile.getAbsolutePath());
                                                return file.delete();
                                            }

                                            @Override
                                            protected void onPostExecute(Boolean aBoolean) {
                                                super.onPostExecute(aBoolean);
                                                progressDelete.dismiss();
                                                if (aBoolean) {
                                                    remove(viewHolder.position);
                                                } else {
                                                    Utils.showMessage(mContext, "Xóa tệp tin thất bại");
                                                }
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
                holder.btnRestore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(mContext)
                                .setTitle("Khôi phục dữ liệu")
                                .setMessage("Việc khôi phục dữ liệu sẽ ghi đè lên hồ sơ có mã sinh viên " + mFile.getName().replace(Utils.BACKUP_FILE_PATTERN, "") + "\nTiếp tục?")
                                .setIcon(R.drawable.backup_restore)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        new AsyncTask<ViewHolder, Void, Void>(){
                                            private ViewHolder viewHolder;
                                            private ProgressDialog progressRestore;
                                            private int mCase = -1;

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                progressRestore = new ProgressDialog(mContext);
                                                progressRestore.setMessage("Khôi phục hồ sơ");
                                                progressRestore.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                progressRestore.setIndeterminate(true);
                                                progressRestore.setProgressNumberFormat(null);
                                                progressRestore.setProgressPercentFormat(null);
                                                progressRestore.show();
                                            }

                                            @Override
                                            protected Void doInBackground(ViewHolder... params) {
                                                viewHolder = params[0];
                                                SQLiteDataController data = SQLiteDataController.getInstance(mContext);
                                                try {
                                                    data.isCreatedDatabase();
                                                } catch (IOException e) {
                                                    Log.e("tag", e.getMessage());
                                                }
                                                try {
                                                    FileInputStream fis = new FileInputStream(mFile);
                                                    InputStreamReader isr = new InputStreamReader(fis);
                                                    BufferedReader bufferedReader = new BufferedReader(isr);
                                                    StringBuilder sb = new StringBuilder();
                                                    String line;
                                                    while ((line = bufferedReader.readLine()) != null) {
                                                        sb.append(line);
                                                    }
                                                    String json = sb.toString();
                                                    Gson gson = new Gson();
                                                    ObjectUser user = gson.fromJson(json, ObjectUser.class);
                                                    if (user.getMasv() == null) {
                                                        mCase = 0;
                                                    } else {
                                                        if (data.insertUser(user)) {
                                                            mCase = 1;
                                                        } else {
                                                            mCase = 2;
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                } finally {
                                                    return null;
                                                }
                                            }

                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                super.onPostExecute(aVoid);
                                                progressRestore.dismiss();
                                                switch(mCase){
                                                    case 0:{
                                                        Utils.showMessage(mContext, "Không thể đọc dữ liệu từ tệp đã chọn");
                                                        break;
                                                    }
                                                    case 1:{
                                                        Utils.showMessage(mContext, "Đã khôi phục");
                                                        break;
                                                    }
                                                    case 2:{
                                                        Utils.showMessage(mContext, "Khôi phục thất bại");
                                                        break;
                                                    }
                                                    default:{
                                                        Utils.showMessage(mContext, "Khôi phục thất bại");
                                                        break;
                                                    }
                                                }
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
            }
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void add(int position, File obj) {
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
