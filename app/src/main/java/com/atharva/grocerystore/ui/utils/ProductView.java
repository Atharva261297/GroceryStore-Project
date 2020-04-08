package com.atharva.grocerystore.ui.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.atharva.grocerystore.R;
import com.squareup.picasso.Picasso;

public class ProductView {
    public static CardView getMainScreenProductCard(final Context context, Resources res, final String name, String photo) {
        CardView card = getCardView(context, res);

        LinearLayout linearLayout = productLinearLayout(context);

        linearLayout.addView(productPhoto(context, res, photo));
        linearLayout.addView(productName(context, res,  name));

        card.addView(linearLayout);

        return card;
    }

    public static CardView getCartScreenProductCard(final Context context, Resources res, final String name, String photo, int quant) {
        CardView card = getCardView(context, res);

        LinearLayout linearLayout = productLinearLayout(context);

        linearLayout.addView(productPhoto(context, res, photo));
        linearLayout.addView(productName(context, res,  name));
        linearLayout.addView(productQuant(context, res,  quant));

        card.addView(linearLayout);

        return card;
    }

    public static CardView getOrderScreenProductCard(final Context context, Resources res, final String orderNo, int price) {
        CardView card = getCardView(context, res);

        LinearLayout linearLayout = productLinearLayout(context);

        linearLayout.addView(productName(context, res,  orderNo));
        linearLayout.addView(productQuant(context, res,  price));

        card.addView(linearLayout);

        return card;
    }

    private static CardView getCardView(Context context, Resources res) {
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
        int dp15 = res.getDimensionPixelOffset(R.dimen.dp15);
        layoutParams.setMargins(0,0,0,dp15);

        CardView card = new CardView(context);
        card.setLayoutParams(layoutParams);
        int dp10 = res.getDimensionPixelOffset(R.dimen.dp10);
        card.setContentPadding(dp10, dp10, dp10, dp10);
        card.setElevation(dp10);
        card.setCardBackgroundColor(res.getColor(R.color.white));
        return card;
    }

    private static LinearLayout productLinearLayout(Context context) {
        CardView.LayoutParams layoutParams =
                new CardView.LayoutParams(
                        CardView.LayoutParams.MATCH_PARENT,
                        CardView.LayoutParams.WRAP_CONTENT);

        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        return layout;
    }

    private static View productName(Context context, Resources res, String name) {
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
        int dp20 = res.getDimensionPixelOffset(R.dimen.dp20);
        layoutParams.setMargins(dp20,0,0,0);
        layoutParams.weight = 1;

        TextView textView = new TextView(new ContextThemeWrapper(context, R.style.ProductName), null, 0 );
        textView.setLayoutParams(layoutParams);
        textView.setText(name);

        return textView;
    }

    @SuppressLint("DefaultLocale")
    private static View productQuant(Context context, Resources res, int quant) {
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
        int dp20 = res.getDimensionPixelOffset(R.dimen.dp20);
        layoutParams.setMargins(dp20,0,dp20,0);
        layoutParams.weight = 1;

        TextView textView = new TextView(new ContextThemeWrapper(context, R.style.ProductName), null, 0 );
        textView.setLayoutParams(layoutParams);
        textView.setText(String.format("%d", quant));

        return textView;
    }

    @SuppressLint("DefaultLocale")
    private static View productPrice(Context context, Resources res, int price) {
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
        int dp20 = res.getDimensionPixelOffset(R.dimen.dp20);
        layoutParams.setMargins(dp20,0,dp20,0);
        layoutParams.weight = 1;

        TextView textView = new TextView(new ContextThemeWrapper(context, R.style.ProductName), null, 0 );
        textView.setLayoutParams(layoutParams);
        textView.setText(String.format("%d Rs", price));

        return textView;
    }

    private static View productPhoto(Context context, Resources res, String photo) {
        int dp80 = res.getDimensionPixelOffset(R.dimen.dp80);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dp80, dp80);

        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(layoutParams);

        Picasso.get().load(photo).into(imageView);

        return imageView;
    }
}
