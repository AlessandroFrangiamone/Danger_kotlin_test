package com.example.ircchatbot.view.custom;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityRecordCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public final class CarouselViewPager extends ViewPager {

    public CarouselViewPager(Context context) {
        super(context);

        init();
    }

    public CarouselViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        ViewCompat.setAccessibilityDelegate(this, new CarouselAccessibilityDelegate());
    }

    public <T extends PagerAdapter & CarouselAdapter> void setCarouselAdapter(T adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        throw new RuntimeException("You must use CarouselViewPager#setCarouselAdapter instead.");
    }

    private class CarouselAccessibilityDelegate extends AccessibilityDelegateCompat {

        @Override
        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(ViewPager.class.getName());
            final AccessibilityRecordCompat recordCompat =
                    AccessibilityEventCompat.asRecord(event);
            recordCompat.setScrollable(canScroll());
            final CarouselAdapter adapter = getAdapter() instanceof CarouselAdapter ? (CarouselAdapter) getAdapter() : null;
            if (event.getEventType() == AccessibilityEventCompat.TYPE_VIEW_SCROLLED && adapter != null) {
                recordCompat.setItemCount(adapter.getRealCount());
                recordCompat.setFromIndex(adapter.getRealPosition(getCurrentItem()));
                recordCompat.setToIndex(getCurrentItem());
            }
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.setClassName(ViewPager.class.getName());
            info.setScrollable(canScroll());
            if (canScrollHorizontally(1)) {
                info.addAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD);
            }
            if (canScrollHorizontally(-1)) {
                info.addAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD);
            }
        }

        @Override
        public boolean performAccessibilityAction(View host, int action, Bundle args) {
            if (super.performAccessibilityAction(host, action, args)) {
                return true;
            }
            switch (action) {
                case AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD: {
                    if (canScrollHorizontally(1)) {
                        setCurrentItem(getCurrentItem() + 1);
                        return true;
                    }
                }
                return false;
                case AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD: {
                    if (canScrollHorizontally(-1)) {
                        setCurrentItem(getCurrentItem() - 1);
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        private boolean canScroll() {
            return (getAdapter() != null) && (getAdapter().getCount() > 1);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        // Unspecified means that the ViewPager is in a ScrollView WRAP_CONTENT.
        // At Most means that the ViewPager is not in a ScrollView WRAP_CONTENT.
        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
            // super has to be called in the beginning so the child views can be initialized.
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int height = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int childMeasuredHeight = child.getMeasuredHeight();
                if (childMeasuredHeight > height) {
                    height = childMeasuredHeight;
                }
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        // super has to be called again so the new specs are treated as exact measurements
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public interface CarouselAdapter {

        int getRealCount();

        int getRealPosition(int position);

    }

}