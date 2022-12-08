package com.example.cpre388project2.util;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Utility class for Firebase methods.
 */
public class FirebaseUtil {

    private static FirebaseFirestore FIRESTORE;
    private static FirebaseAuth AUTH;
    private static AuthUI AUTH_UI;

    /**
     * Returns the current firebase firestore instance.
     *
     * @return Current FirebaseFirestore instance.
     */
    public static FirebaseFirestore getFirestore() {
        if (FIRESTORE == null) {
            FIRESTORE = FirebaseFirestore.getInstance();
        }

        return FIRESTORE;
    }

    /**
     * Returns the current firebase auth instance.
     *
     * @return Current FirebaseAuth instance.
     */
    public static FirebaseAuth getAuth() {
        if (AUTH == null) {
            AUTH = FirebaseAuth.getInstance();
        }

        return AUTH;
    }

    /**
     * Gets the Firebase Auth UI.
     *
     * @return AuthUI instance.
     */
    public static AuthUI getAuthUI() {
        if (AUTH_UI == null) {
            AUTH_UI = AuthUI.getInstance();
        }

        return AUTH_UI;
    }
}
