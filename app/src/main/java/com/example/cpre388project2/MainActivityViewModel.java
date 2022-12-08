package com.example.cpre388project2;

import androidx.lifecycle.ViewModel;

/**
 * ViewModel to store login information for main activity.
 */
public class MainActivityViewModel extends ViewModel {
    private boolean mIsSigningIn;

    /**
     * Construct main activity view model.
     */
    public MainActivityViewModel() {
        mIsSigningIn = false;
    }

    /**
     * Returns whether the app is signing in to firebase currently or not.
     *
     * @return Boolean for if app is signing in.
     */
    public boolean getIsSigningIn() {
        return mIsSigningIn;
    }

    /**
     * Set whether the app is signing in to firebase currently or not.
     *
     * @param mIsSigningIn Boolean for if app is signing in.
     */
    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }
}
