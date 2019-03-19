package com.example.robber_hadi.MyMoneyManager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Spending tab1 = new Spending();
                return tab1;
            case 1:
                Transaction tab2 = new Transaction();
                return tab2;
            case 2:
                Categories tab3 = new Categories();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    /*@Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }*/

    /*@Override
    public int getItemPosition(Object spending, Object transaction, Object categories) {

        Spending s = (Spending) spending;
        Transaction t = (Transaction) transaction;
        Categories c = (Categories) categories;

        if (s!= null)
        {
            s.update();
        }
        else if (t!= null)
        {
            t.update();
        }
        else if (c!= null)
        {
            c.update();
        }

        return super.getItemPosition(spending);
        return super.getItemPosition(transaction);
        return super.getItemPosition(categories);
    }*/
}
