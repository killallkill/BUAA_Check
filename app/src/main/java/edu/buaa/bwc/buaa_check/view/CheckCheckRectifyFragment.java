package edu.buaa.bwc.buaa_check.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gigamole.navigationtabstrip.NavigationTabStrip;

import edu.buaa.bwc.buaa_check.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckCheckRectifyFragment extends Fragment {

    private ViewPager mPager;
    private PagerAdapter mAdapter;
    private NavigationTabStrip nts;

    public CheckCheckRectifyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rectify_check, container, false);
        init(inflater, view);
        return view;
    }

    private void init(LayoutInflater inflater, View view) {

        final View test = inflater.inflate(R.layout.view_test, null);
        final View test2 = inflater.inflate(R.layout.view_test2, null);
        mPager = (ViewPager) view.findViewById(R.id.rectify_check_viewpager);
        mAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((position == 0) ? test : test2);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView((position == 0) ? test : test2);

                return (position == 0) ? test : test2;
            }

        };
        mPager.setAdapter(mAdapter);

        nts = (NavigationTabStrip) view.findViewById(R.id.rectify_check_nts);
        nts.setTitles("正在整改", "完成整改");
        nts.setViewPager(mPager);
    }

}
