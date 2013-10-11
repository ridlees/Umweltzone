package de.avpptr.umweltzone.adapters;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.avpptr.umweltzone.R;
import de.avpptr.umweltzone.contract.LowEmissionZoneNumbers;
import de.avpptr.umweltzone.models.LowEmissionZone;

public class CityListAdapter extends ArrayAdapter<LowEmissionZone> {

    protected Context mContext;
    protected int mResourceId;
    protected LowEmissionZone[] mLowEmissionZones;

    public CityListAdapter(Context context, int resource, LowEmissionZone[] lowEmissionZones) {
        super(context, resource, lowEmissionZones);
        mContext = context;
        mResourceId = resource;
        mLowEmissionZones = lowEmissionZones;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(mResourceId, null);
            if (view != null) {
                viewHolder = new ViewHolder();
                viewHolder.zoneName = (TextView) view.findViewById(R.id.city_row_name);
                viewHolder.zoneBadge = (TextView) view.findViewById(R.id.city_row_badge);
                LayerDrawable badgeBackground = (LayerDrawable) viewHolder.zoneBadge.getBackground();
                if (badgeBackground != null) {
                    viewHolder.zoneShape =
                            (GradientDrawable) badgeBackground.findDrawableByLayerId(R.id.zone_shape);
                }
                view.setTag(viewHolder);
            } else {
                throw new IllegalStateException("City row could be inflated.");
            }
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        LowEmissionZone lowEmissionZone = mLowEmissionZones[position];
        viewHolder.zoneName.setText(lowEmissionZone.displayName);
        int zoneNumber = lowEmissionZone.zoneNumber;
        viewHolder.zoneBadge.setText(String.valueOf(zoneNumber));
        int badgeColor = zoneNumberToColor(mContext.getResources(), zoneNumber);

        int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.HONEYCOMB) {
            // Replaces the round badge with a colored square.
            ColorDrawable colorDrawable = new ColorDrawable(badgeColor);
            viewHolder.zoneBadge.setBackgroundDrawable(colorDrawable);
        } else {
            viewHolder.zoneShape.setColor(badgeColor);
        }
        return view;
    }

    private int zoneNumberToColor(Resources resources, int zoneNumber) {
        switch (zoneNumber) {
            case LowEmissionZoneNumbers.RED:
                return resources.getColor(R.color.city_zone_2);
            case LowEmissionZoneNumbers.YELLOW:
                return resources.getColor(R.color.city_zone_3);
            case LowEmissionZoneNumbers.GREEN:
                return resources.getColor(R.color.city_zone_4);
        }
        return resources.getColor(R.color.city_zone_none);
    }

    private static class ViewHolder {
        TextView zoneName;
        TextView zoneBadge;
        GradientDrawable zoneShape;
    }

}