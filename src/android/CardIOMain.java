/**
 * CardIO.java
 * card.io Cordova plugin
 * @Copyright 2015 Keepe UP, Inc
 * @author Vishal <vishal@keepe.com>
 * MIT licensed
 */

package com.keepe.plugins.cardio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class CardIOMain extends Activity {

    private static int MY_SCAN_REQUEST_CODE=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //		setContentView(R.layout.activity_main);

        Intent scanIntent = new Intent(CardIOMain.this, CardIOActivity.class);

        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, CardIO.hideLogo);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, CardIO.expiry);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, CardIO.cvv);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, CardIO.zip);
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, CardIO.confirm);
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, CardIO.suppressManual);
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, CardIO.usePaypalIcon);

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                JSONObject cardObj = new JSONObject();

                try {
                    cardObj.put("card_number",scanResult.cardNumber);
                    cardObj.put("card_type",scanResult.getCardType().getDisplayName("en-US"));
                    cardObj.put("redacted_card_number", scanResult.getFormattedCardNumber());
                    if (scanResult.isExpiryValid()) {
                        cardObj.put("expiry_month",scanResult.expiryMonth);
                        cardObj.put("expiry_year",scanResult.expiryYear);
                    }

                    if (scanResult.cvv != null) {
                        cardObj.put("cvv",scanResult.cvv);

                    }

                    if (scanResult.postalCode != null) {
                        cardObj.put("zip",scanResult.postalCode);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                CardIO.cardNumber = cardObj;
            }
        }
        CardIOMain.this.finish();
    }

}