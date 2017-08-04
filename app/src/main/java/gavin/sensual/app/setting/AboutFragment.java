package gavin.sensual.app.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.sensual.R;
import gavin.sensual.base.BindingFragment;
import gavin.sensual.databinding.FragAboutBinding;
import gavin.sensual.util.L;
import gavin.sensual.util.VersionHelper;

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
    }
}
