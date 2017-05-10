package com.zcdyy.personalparameter.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zcdyy.personalparameter.R;
import com.zcdyy.personalparameter.application.MyApplication;
import com.zcdyy.personalparameter.base.BaseActivity;
import com.zcdyy.personalparameter.bean.HealthCircle;
import com.zcdyy.personalparameter.constant.Constants;
import com.zcdyy.personalparameter.permission.PermissionsChecker;
import com.zcdyy.personalparameter.utils.BmobUtils;
import com.zcdyy.personalparameter.utils.ImageUploadUtil;
import com.zcdyy.personalparameter.utils.ImageUtil;
import com.zcdyy.personalparameter.utils.ToastUtils;
import com.zcdyy.personalparameter.views.ActionSheet;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;


public class PublishActivity extends BaseActivity implements View.OnClickListener, ActionSheet.OnSheetItemClickListener {

    private TextView title,publish;
    private EditText content;
    public ImageView img;

    private String[] items = {"拍照", "我的相册"};
    // 头像文件、上传头像的名称、本地图片uri
    private File imageFile;
    private Uri imageUri; // 图片路径
    public ImageView civ_head;
    private String saveFileName = "";
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    public static int type = 0;
    //拍照、从相册选择、照片保存成功
    private final int TAKE_PHOTO = 1, CHOOSE_PHOTO = 2, SAVE_IMAGE_SUCCESS = 3;
    private final int REQUEST_CODE = 0;

    private BmobUtils bmobUtils;
    public boolean isPic = false;
    public HealthCircle healthCircle;
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.ResultCode.SUCCESS:
                    dialog.dismiss();
                    ToastUtils.shortToast(PublishActivity.this,"发布成功");
                    finish();
                    break;
                case Constants.ResultCode.UPLOAD_SUCCESS:
                    dialog.dismiss();
                    break;
            }
        }
    };
    @Override
    protected void findViewByIDS() {
        title = findViewsById(R.id.top_tv_title);
        publish = findViewsById(R.id.top_tv_right);
        publish.setVisibility(View.VISIBLE);
        content = findViewsById(R.id.content);
        img = findViewsById(R.id.img);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initData();
        bind();
    }

    private void bind() {
        img.setOnClickListener(this);
        findViewsById(R.id.top_rl_right).setOnClickListener(this);
    }

    private void initData() {
        publish.setText("发布");
        title.setText("发布动态");
        bmobUtils = new BmobUtils(this);
        healthCircle = new HealthCircle();
        findViewsById(R.id.top_rl_back).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img:
                showActionSheetDialog(items);
                break;
            case R.id.top_rl_right:
                publishFriend();

                break;
        }
    }

    /**
     * 发布动态
     */
    private void publishFriend() {
        if (!content.getText().toString().equals("")){
            dialog = ProgressDialog.show(this,null,"正在发布......");
            healthCircle.setId(loginuser.getId());
            healthCircle.setHead(loginuser.getHead());
            healthCircle.setName(loginuser.getName());
            healthCircle.setContent(content.getText().toString());
            healthCircle.setPic(isPic);
            Log.e("publishFriend","publishFriend");
            bmobUtils.publishFriendCircle(healthCircle);
        }else {
            ToastUtils.shortToast(this,"内容不能为空");
        }
    }

    //    //头像选择对话框
    private void showActionSheetDialog(String[] items) {
        ActionSheet actionSheet = new ActionSheet(this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true);
        for (int i = 0; i < items.length; i++) {
            actionSheet.addSheetItem(items[i], ActionSheet.SheetItemColor.Blue, PublishActivity.this);
        }
        actionSheet.show();

    }
    @Override
    public void onClick(int which) {
        switch (which) {
            case TAKE_PHOTO:
                //拍照
                imageFile = ImageUploadUtil.createImageFile(ImageUploadUtil.CAMERA_SAVEDIR2, ImageUploadUtil.createImageName());
                startActivityForResult(ImageUploadUtil.intentImageCapture(imageFile), ImageUploadUtil.TAKE_PHOTO);
                break;
            case CHOOSE_PHOTO:
                //从相册选择
                startActivityForResult(ImageUtil.intentChooseImg(),
                        CHOOSE_PHOTO);
                break;
        }
    }
    //
//    /**
//     * Activity回调事件处理
//     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == 0) {
                    return;
                }
                //拍照
                ImageUtil.paizhaocreateImagefile(this, imageFile);
                if (imageFile == null) {
                    ToastUtils.shortToast(this, "图片太大无法上传");
                    return;
                }
                doSendThread();//上传图片
                break;
            case CHOOSE_PHOTO:
                //相册
                if (resultCode == 0) return;
                imageUri = data.getData();
                imageFile = MyApplication.getInstance().createimagefile(imageUri, getVmWidth(), getVmHeight());
                if (imageFile == null) {
                    ToastUtils.shortToast(this, "imageFile为空2");
                    return;
                }
                doSendThread();//上传图片
                break;
        }
    }
    //
//    /**
//     * 上传头像
//     */
    private void doSendThread() {
//        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
//        civ_head.setImageBitmap(bitmap);
        dialog = ProgressDialog.show(this, null, "正在上传图片");
        BmobFile bmobFile = new BmobFile(imageFile);
        bmobUtils.PublishUpLoadFile(bmobFile);
//        bmobUtils.upLoadFile(bmobFile,imageFile);
        //initInfo();
    }
}
