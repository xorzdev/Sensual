package gavin.sensual.app.daily;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class PageAdapter extends PagerAdapter {

	private List<View> list;

	public PageAdapter(List<View> list) {
		this.list = list;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		ViewPager pViewPager = ((ViewPager) container);
		pViewPager.removeView(list.get(position));
	}

	@Override
	public void finishUpdate(ViewGroup container) {
	}

	@Override
	public int getCount() {
		return list.size();
	}


	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ViewPager pViewPager = ((ViewPager) container);
		if (list.get(position).getParent() == null) {
			pViewPager.addView(list.get(position));
		}
		return list.get(position);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(ViewGroup container) {
	}
}