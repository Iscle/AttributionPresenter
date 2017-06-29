package com.franmontiel.attributionhelper;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.franmontiel.attributionhelper.entities.Attribution;
import com.franmontiel.attributionhelper.entities.LicenseInfo;
import com.franmontiel.attributionhelper.listeners.OnAttributionClickListener;
import com.franmontiel.attributionhelper.listeners.OnLicenseClickListener;
import com.franmontiel.attributionhelper.util.BrowserOpener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Adapter used to show attributions on a ListView.
 */
class AttributionAdapter extends BaseAdapter {

    private List<Attribution> items;
    @LayoutRes
    private int itemLayout;
    @LayoutRes
    private int licenseLayout;
    @Nullable
    private final OnAttributionClickListener onAttributionClickListener;
    @Nullable
    private final OnLicenseClickListener onLicenseClickListener;

    AttributionAdapter(Collection<Attribution> attributions,
                       @LayoutRes int itemLayout,
                       @LayoutRes int licenseLayout,
                       @Nullable OnAttributionClickListener onAttributionClickListener,
                       @Nullable OnLicenseClickListener onLicenseClickListener) {
        this.items = new ArrayList<>(attributions.size());
        this.items.addAll(attributions);
        this.itemLayout = itemLayout;
        this.licenseLayout = licenseLayout;
        this.onAttributionClickListener = onAttributionClickListener;
        this.onLicenseClickListener = onLicenseClickListener;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Attribution getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.copyrightNotices = (TextView) convertView.findViewById(R.id.copyrightNotices);
            holder.licensesLayout = (ViewGroup) convertView.findViewById(R.id.licensesLayout);

            if (holder.name == null || holder.copyrightNotices == null || holder.licensesLayout == null) {
                throw new IllegalStateException("Item layout must contain all of the following required views:\n" +
                        "  - TextView with android:id=\"@+id/name\"\n" +
                        "  - TextView with android:id=\"@+id/copyrightNotices\"\n" +
                        "  - ViewGroup descendant with android:id=\"@+id/licensesLayout\"");
            }

            convertView.setTag(holder);
        } else {
            holder = ((ViewHolder) convertView.getTag());
        }
        final Attribution attribution = getItem(position);

        holder.name.setText(attribution.getName());
        holder.copyrightNotices.setText(attribution.getFormattedCopyrightNotices());
        holder.licensesLayout.removeAllViews();
        for (LicenseInfo licenseInfo : attribution.getLicensesInfo()) {
            addLicense(parent.getContext(), holder.licensesLayout, licenseInfo);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAttributionClickListener == null ||
                        !onAttributionClickListener.onAttributionClick(attribution)) {

                    BrowserOpener.open(parent.getContext(), attribution.getWebsite());
                }
            }
        });

        return convertView;
    }

    private void addLicense(final Context context, ViewGroup licensesLayout, final LicenseInfo licenseInfo) {
        View inflatedView = LayoutInflater.from(context).inflate(licenseLayout, licensesLayout, false);
        TextView licenseTextView = (TextView) inflatedView.findViewById(R.id.license);

        if (licenseTextView == null) {
            throw new IllegalStateException("LicenseInfo layout does not contain a required TextView with android:id=\"@+id/licenseInfo\"");
        }

        licenseTextView.setText(licenseInfo.getName());
        licenseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLicenseClickListener == null ||
                        !onLicenseClickListener.onLicenseClick(licenseInfo)) {

                    BrowserOpener.open(context, licenseInfo.getTextUrl());
                }
            }
        });
        licensesLayout.addView(inflatedView);
    }

    private static class ViewHolder {
        TextView name;
        TextView copyrightNotices;
        ViewGroup licensesLayout;
    }
}
