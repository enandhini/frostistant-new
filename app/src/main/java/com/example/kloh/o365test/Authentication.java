package com.example.kloh.o365test;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.aad.adal.AuthenticationCallback;
import com.microsoft.aad.adal.AuthenticationContext;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.services.odata.impl.ADALDependencyResolver;
import com.microsoft.services.odata.interfaces.LogLevel;


public class Authentication {


    public static AuthenticationContext context = null;


    public static SettableFuture<Void> acquireToken(final Activity rootActivity) {


        final SettableFuture<Void> result = SettableFuture.create();


        getAuthenticationContext(rootActivity).acquireToken(
                rootActivity,
                ServiceConstants.RESOURCE_ID,
                ServiceConstants.CLIENT_ID,
                ServiceConstants.REDIRECT_URL, "",
                new AuthenticationCallback<AuthenticationResult>() {


                    @Override
                    public void onSuccess(final AuthenticationResult authenticationResult) {


                        if (authenticationResult != null && !TextUtils.isEmpty(authenticationResult.getAccessToken())) {
                            ADALDependencyResolver adalDependencyResolver= new ADALDependencyResolver(getAuthenticationContext(rootActivity),
                                    ServiceConstants.RESOURCE_ID, ServiceConstants.CLIENT_ID);
                                    adalDependencyResolver.getLogger().setEnabled(true);
                                    adalDependencyResolver.getLogger().setLogLevel(LogLevel.VERBOSE);



                            Controller.getInstance().setDependencyResolver(adalDependencyResolver);
                            result.set(null);
                        }
                    }


                    @Override
                    public void onError(Exception t) {
                        result.setException(t);
                    }
                });


        return result;
    }


    /**
     * Gets AuthenticationContext for AAD.
     *
     * @return authenticationContext, if successful
     */
    public static AuthenticationContext getAuthenticationContext(Activity activity) {


        try {
            context = new AuthenticationContext(activity, ServiceConstants.AUTHORITY_URL, false);
        } catch (Throwable t) {
            Log.e("SampleApp", t.toString());
        }
        return context;
    }
}
