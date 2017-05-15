package gavin.sensual.app.setting;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.tbruyelle.rxpermissions2.RxPermissions;

import gavin.sensual.R;
import gavin.sensual.app.main.MainFragment;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.base.RequestCode;
import gavin.sensual.databinding.LayoutBlankBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Android 6.0 需要时权限
 *
 * @author gavin.xiong 2017/4/25
 */
public class PermissionFragment extends BindingFragment<LayoutBlankBinding> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static PermissionFragment newInstance() {
        return new PermissionFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_blank;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            requestPermission();
        }
        binding.root.setOnClickListener(v -> requestPermission());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.dispose();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        requestPermission();
    }

    private void requestPermission() {
        new RxPermissions(_mActivity)
                .requestEach(Manifest.permission.READ_PHONE_STATE)
                .doOnSubscribe(compositeDisposable::add)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(permission -> {
                    if (permission.granted) {
                        startWithPop(MainFragment.newInstance());
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        Snackbar.make(binding.root, "应用需要获取您的设备信息", Snackbar.LENGTH_INDEFINITE)
                                .setAction("重试", v -> requestPermission())
                                .show();
                    } else {
                        Snackbar.make(binding.root, "应用缺少必要权限", Snackbar.LENGTH_INDEFINITE)
                                .setAction("开启", v -> openAppSetting())
                                .show();
                    }
                });
    }

    private void openAppSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + _mActivity.getPackageName()));
        startActivityForResult(intent, RequestCode.PERMISSION_SETTING);
    }
}
