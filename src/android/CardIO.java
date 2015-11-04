/**
 * CardIO.java
 * card.io Cordova plugin
 * @Copyright 2015 Keepe UP, Inc
 * @author Vishal <vishal@keepe.com>
 * MIT licensed
 */

package com.keepe.plugins.cardio;

import io.card.payment.CardIOActivity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;


public class CardIO extends CordovaPlugin {

    public CallbackContext callbackContext;
    public static JSONObject cardNumber = null;
    public static Boolean expiry;
    public static Boolean cvv;
    public static Boolean zip;
    public static Boolean confirm;
    public static Boolean hideLogo;
    public static Boolean suppressManual;
    public static Boolean usePaypalIcon;

    @Override
    public boolean execute(String action, JSONArray args,
                           CallbackContext callbackContext) {

        // TODO Auto-generated method stub
        this.callbackContext = callbackContext;

        try {
            // set configurations
            if( action.equals("scan") ) {
                JSONObject config = args.getJSONObject(0);
                expiry = config.getBoolean("expiry");
                cvv = config.getBoolean("cvv");
                zip = config.getBoolean("zip");
                confirm = config.getBoolean("suppressConfirm");
                hideLogo = config.getBoolean("hideLogo");
                suppressManual = config.getBoolean("suppressManual");
                usePaypalIcon = config.getBoolean("usePaypalIcon");

                Intent scanIntent = new Intent(cordova.getActivity(),
                                           CardIOMain.class);
                cordova.getActivity().startActivity(scanIntent);

                PluginResult cardData = new PluginResult(
                                                     PluginResult.Status.NO_RESULT);
                cardData.setKeepCallback(true);
                callbackContext.sendPluginResult(cardData);
                return true;
            } else if( action.equals("canScan") ) {
                if (CardIOActivity.canReadCardWithCamera()) {
                    callbackContext.success("PASS");
                    return true;
                } else {
                    callbackContext.error("FAIL");
                    return false;
                }
            }
            return false;
        } catch (JSONException e) {
            e.printStackTrace();

            PluginResult res = new PluginResult(
                                                PluginResult.Status.JSON_EXCEPTION);
            callbackContext.sendPluginResult(res);
            return false;
        }

    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);

        // send plugin result if success
        JSONObject mImagepath = cardNumber;
        if (mImagepath != null) {
            PluginResult cardData = new PluginResult(PluginResult.Status.OK,
                                                     cardNumber);
            cardData.setKeepCallback(false);
            callbackContext.sendPluginResult(cardData);
            cardNumber = null;
        } else {
            PluginResult cardData = new PluginResult(
                                                     PluginResult.Status.NO_RESULT);
            cardData.setKeepCallback(false);
            callbackContext.sendPluginResult(cardData);
        }

    }
}