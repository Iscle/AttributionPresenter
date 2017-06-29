package com.franmontiel.attributionhelper;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.franmontiel.attributionhelper.entities.Attribution;
import com.franmontiel.attributionhelper.entities.Library;
import com.franmontiel.attributionhelper.listeners.OnAttributionClickListener;
import com.franmontiel.attributionhelper.listeners.OnLicenseClickListener;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Class to present a view to show license attributions.
 */
public final class AttributionPresenter {

    private Context context;
    private SortedSet<Attribution> attributions;
    private final int itemLayout;
    private final int licenseLayout;
    private OnAttributionClickListener onAttributionClickListener;
    private OnLicenseClickListener onLicenseClickListener;

    private AttributionAdapter attributionAdapter;

    private AttributionPresenter(Context context,
                                 SortedSet<Attribution> attributions,
                                 @LayoutRes int itemLayout,
                                 @LayoutRes int licenseLayout,
                                 @Nullable OnAttributionClickListener onAttributionClickListener,
                                 @Nullable OnLicenseClickListener onLicenseClickListener) {
        this.context = context;
        this.attributions = attributions;
        this.itemLayout = itemLayout == 0 ? R.layout.default_item_attribution : itemLayout;
        this.licenseLayout = licenseLayout == 0 ? R.layout.default_license_text : licenseLayout;
        this.onAttributionClickListener = onAttributionClickListener;
        this.onLicenseClickListener = onLicenseClickListener;
    }

    /**
     * Show a dialog with the configured attributions.
     *
     * @param title optional title of the dialog
     * @return the dialog itself
     */
    public Dialog showDialog(@Nullable String title) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setAdapter(getAdapter(), null)
                .show();
    }

    /**
     * Gets the adapter used to show the attributions on a ListView.
     *
     * @return
     */
    public AttributionAdapter getAdapter() {
        if (attributionAdapter == null) {
            attributionAdapter = new AttributionAdapter(
                    attributions,
                    itemLayout,
                    licenseLayout,
                    onAttributionClickListener,
                    onLicenseClickListener
            );
        }
        return attributionAdapter;
    }

    public static class Builder {
        private Context context;
        private SortedSet<Attribution> attributions;
        @LayoutRes
        private int itemLayout;
        @LayoutRes
        private int licenseLayout;
        private OnAttributionClickListener onAttributionClickListener;
        private OnLicenseClickListener onLicenseClickListener;

        public Builder(Context context) {
            this.context = context;
            this.attributions = new TreeSet<>();
        }

        public Builder addAttributions(Attribution... attributions) {
            this.attributions.addAll(Arrays.asList(attributions));
            return this;
        }

        public Builder addAttributions(Library... libraries) {
            for (Library library : libraries) {
                this.attributions.add(library.getAttribution());
            }
            return this;
        }

        /**
         * Sets an optional custom layout for the attribution item.
         * <p>
         * The layout must contain all of the following views:
         * <ul>
         * <li>a TextView with android:id="@+id/name"
         * <li>a TextView with android:id="@+id/copyrightNotices"
         * <li>a ViewGroup descendant with android:id="@+id/licensesLayout"
         * </ul>
         *
         * @param itemLayoutResId the layout file to be used
         * @return
         */
        public Builder setItemLayout(@LayoutRes int itemLayoutResId) {
            this.itemLayout = itemLayoutResId;
            return this;
        }

        /**
         * Sets an optional custom layout for the licenses names
         * <p>
         * The layout must contain a TextView with android:id="@+id/licenseInfo"
         *
         * @param licenseLayoutResId the layout file to be used
         * @return
         */
        public Builder setLicenseLayout(@LayoutRes int licenseLayoutResId) {
            this.licenseLayout = licenseLayoutResId;
            return this;
        }

        public Builder setOnAttributionClickListener(OnAttributionClickListener onAttributionClickListener) {
            this.onAttributionClickListener = onAttributionClickListener;
            return this;
        }

        public Builder setOnLicenseClickListener(OnLicenseClickListener onLicenseClickListener) {
            this.onLicenseClickListener = onLicenseClickListener;
            return this;
        }

        public AttributionPresenter build() {
            return new AttributionPresenter(context,
                    attributions,
                    itemLayout,
                    licenseLayout,
                    onAttributionClickListener,
                    onLicenseClickListener);
        }
    }
}
