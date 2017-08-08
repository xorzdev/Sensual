package gavin.sensual.app.setting;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;

import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.databinding.FragAboutBinding;
import gavin.sensual.util.L;
import gavin.sensual.util.VersionHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 关于
 *
 * @author gavin.xiong 2017/5/12
 */
public class AboutFragment extends BindingFragment<FragAboutBinding> {

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_about;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
    }

    private void init() {
        binding.includeToolbar.toolbar.setTitle("关于");
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());

        binding.tvVersion.setText(String.format("声色 v%s", VersionHelper.getVersionName(_mActivity)));

        binding.tvCheckVersion.setOnClickListener(v -> checkVersion());

        binding.tvLicense.setOnClickListener(v -> start(LicenseFragment.newInstance()));
        binding.tvContact.setOnClickListener(v -> {
            try {
                Intent data = new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:gavinxxxxxx@gmail.com"));
                startActivity(data);
            } catch (Exception e) {
                L.e(e);
            }
        });
        binding.tvShare.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT,
                    String.format("声色 %s：不可描述@声色  http://www.coolapk.com/apk/gavin.sensual", VersionHelper.getVersionName(_mActivity)));
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, "分享"));
        });
    }

    private void checkVersion() {
        getDataLayer().getSettingService().getVersion()
                // .doOnSubscribe(mCompositeDisposable::add)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(version -> {
                    if (version.getCode() <= VersionHelper.getVersionCode(getContext())) {
                        Snackbar.make(binding.tvVersion, "当前已是最新版本", Snackbar.LENGTH_LONG);
                    } else {
                        AlertDialog dialog = new AlertDialog.Builder(_mActivity)
                                .setTitle("发现新版本")
                                .setMessage(version.getMsg())
                                .setPositiveButton("更新", (arg0, which) -> {
                                    VersionHelper.downloadApk(_mActivity, version.getUrl(), "Sensual_" + version.getName() + ".apk");
                                    Snackbar.make(binding.tvVersion, "正在下载新版本安装包", Snackbar.LENGTH_LONG).show();
                                })
                                .setNegativeButton("忽略", null)
                                .show();
                        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(0xFF777777);
                    }
                }, e -> Snackbar.make(binding.tvVersion, e.getMessage(), Snackbar.LENGTH_LONG));
    }
}
