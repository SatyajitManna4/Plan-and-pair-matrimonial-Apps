package com.example.planpair;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompatibilityCalculator {

    private static final String TAG = "CompatibilityCalc";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public interface CompatibilityCallback {
        void onComplete();
    }

    public static void calculateAndStoreCompatibilityScores(CompatibilityCallback callback) {
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Log.e(TAG, "No current user logged in");
            callback.onComplete();
            return;
        }

        String currentUid = currentUser.getUid();

        db.collection("UsersData").document(currentUid).get().addOnSuccessListener(currentSnapshot -> {
            if (!currentSnapshot.exists()) {
                Log.e(TAG, "Current user profile not found in Firestore");
                callback.onComplete();
                return;
            }

            UserProfile currentProfile = currentSnapshot.toObject(UserProfile.class);
            if (currentProfile == null) {
                Log.e(TAG, "Failed to parse current user profile");
                callback.onComplete();
                return;
            }

            currentProfile.setUid(currentUid);

            db.collection("UsersData").get().addOnSuccessListener(querySnapshot -> {
                for (DocumentSnapshot doc : querySnapshot) {
                    String otherUid = doc.getId();
                    if (otherUid.equals(currentUid)) continue;

                    UserProfile otherProfile = doc.toObject(UserProfile.class);
                    if (otherProfile == null) continue;

                    otherProfile.setUid(otherUid);

                    int score = computeCompatibility(currentProfile, otherProfile);
                    storeCompatibilityScore(currentUid, otherUid, score);
                }
                Log.d(TAG, "All compatibility scores calculated and saved.");
                callback.onComplete();
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Error fetching all user profiles", e);
                callback.onComplete();
            });

        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error fetching current user profile", e);
            callback.onComplete();
        });
    }

    public static int computeCompatibility(UserProfile user1, UserProfile user2) {
        int score = 0;
        int totalFactors = 0;

        String gender1 = user1.getGender();
        String gender2 = user2.getGender();
        if (gender1 == null || gender2 == null) return 0;

        if (!(
                (gender1.equalsIgnoreCase("male") && gender2.equalsIgnoreCase("female")) ||
                        (gender1.equalsIgnoreCase("female") && gender2.equalsIgnoreCase("male"))
        )) {
            return 0;
        }

        score += compareMultiValue(user1.getTravel(), user2.getTravel()); totalFactors++;
        score += compareMultiValue(user1.getCreative_passions(), user2.getCreative_passions()); totalFactors++;
        score += compareMultiValue(user1.getMovies(), user2.getMovies()); totalFactors++;
        score += compareMultiValue(user1.getMusic(), user2.getMusic()); totalFactors++;
        score += compareMultiValue(user1.getMarriage(), user2.getMarriage()); totalFactors++;
        score += compareMultiValue(user1.getLanguage(), user2.getLanguage()); totalFactors++;
        score += compareMultiValue(user1.getFood(), user2.getFood()); totalFactors++;
        score += compareMultiValue(user1.getFamily_structure(), user2.getFamily_structure()); totalFactors++;
        score += compareMultiValue(user1.getFamilyType(), user2.getFamilyType()); totalFactors++;
        score += compareMultiValue(user1.getWeddingType(), user2.getWeddingType()); totalFactors++;

        score += compareSingleValue(user1.getSocial_media(), user2.getSocial_media()); totalFactors++;
        score += compareSingleValue(user1.getReligion(), user2.getReligion()); totalFactors++;
        score += compareSingleValue(user1.getDegree(), user2.getDegree()); totalFactors++;
        score += compareSingleValue(user1.getSeason(), user2.getSeason()); totalFactors++;

        if (totalFactors == 0) return 0;

        return (int) ((score / (float) totalFactors) * 100);
    }

    private static int compareSingleValue(String val1, String val2) {
        return (val1 != null && val2 != null && val1.equalsIgnoreCase(val2)) ? 1 : 0;
    }

    private static int compareMultiValue(List<String> list1, List<String> list2) {
        if (list1 == null || list2 == null || list1.isEmpty() || list2.isEmpty()) return 0;

        int matchCount = 0;
        for (String item : list1) {
            if (list2.contains(item)) matchCount++;
        }

        float percentage = (float) matchCount / Math.max(list1.size(), list2.size());
        return percentage >= 0.5 ? 1 : 0;
    }

    private static void storeCompatibilityScore(String currentUid, String otherUid, int score) {
        Map<String, Object> data = new HashMap<>();
        data.put("score", score);
        data.put("comparedWith", currentUid);

        db.collection("UsersData")
                .document(otherUid)
                .collection("compatibilityScores")
                .document(currentUid)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Saved compatibility score between " + currentUid + " and " + otherUid))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to save compatibility score", e));
    }
}


/*

public class CompatibilityCalculator {

    public static double calculateCompatibility(UserProfile user1, UserProfile user2) {
        double totalScore = 0;

        // Check if the users' genders are compatible (female with male, or male with female)
        if (isCompatibleGender(user1.getGender(), user2.getGender())) {
            // Weights
            double interestWeight = 50;
            double weddingWeight = 50;

            // --- Interest Page Comparisons (Total 50%) ---
            double interestScore = 0;
            interestScore += matchSubfields(user1.getHobbiesLifestyle(), user2.getHobbiesLifestyle());
            interestScore += matchSubfields(user1.getRelationshipPreferences(), user2.getRelationshipPreferences());
            interestScore += matchSubfields(user1.getPersonalPassions(), user2.getPersonalPassions());
            interestScore += matchSubfields(user1.getCulturalIdentity(), user2.getCulturalIdentity());
            interestScore += matchSubfields(user1.getCommunicationPreferences(), user2.getCommunicationPreferences());

            interestScore = (interestScore / 5.0) * interestWeight; // average of 5 categories scaled to weight

            // --- Wedding Goal Comparisons (Total 50%) ---
            double weddingScore = 0;
            weddingScore += matchExact(user1.getWeddingType(), user2.getWeddingType());
            weddingScore += matchExact(user1.getCelebrationPreference(), user2.getCelebrationPreference());
            weddingScore += matchExact(user1.getWeddingBudget(), user2.getWeddingBudget());
            weddingScore += matchExact(user1.getGuestCount(), user2.getGuestCount());
            weddingScore += matchExact(user1.getWeddingSeason(), user2.getWeddingSeason());
            weddingScore += matchExact(user1.getWeddingTheme(), user2.getWeddingTheme());
            weddingScore += matchExact(user1.getCateringStyle(), user2.getCateringStyle());
            weddingScore += matchSubfields(user1.getWeddingEntertainment(), user2.getWeddingEntertainment());

            weddingScore = (weddingScore / 8.0) * weddingWeight; // average of 8 comparisons scaled to weight

            totalScore = interestScore + weddingScore;
            return Math.round(totalScore * 10.0) / 10.0; // rounded to 1 decimal place
        } else {
            // If genders are not compatible, return a score of 0 or handle the case differently
            return 0.0; // Or you can return a default score or any other behavior you prefer
        }
    }

    // Check if the genders are compatible (female with male, or male with female)
    private static boolean isCompatibleGender(String gender1, String gender2) {
        // Assuming "female" and "male" are the values in the gender field
        if (gender1 == null || gender2 == null) return false;
        return (gender1.equalsIgnoreCase("female") && gender2.equalsIgnoreCase("male")) ||
                (gender1.equalsIgnoreCase("male") && gender2.equalsIgnoreCase("female"));
    }

    private static double matchSubfields(List<String> list1, List<String> list2) {
        if (list1 == null || list2 == null || list1.isEmpty() || list2.isEmpty()) return 0.0;
        Set<String> set1 = new HashSet<>(list1);
        Set<String> set2 = new HashSet<>(list2);

        int matchCount = 0;
        for (String item : set1) {
            if (set2.contains(item)) matchCount++;
        }

        int total = set1.size() + (int) set2.stream().filter(e -> !set1.contains(e)).count();
        return total == 0 ? 0.0 : (double) matchCount / total;
    }

    private static double matchExact(String a, String b) {
        if (a == null || b == null) return 0.0;
        return a.equalsIgnoreCase(b) ? 1.0 : 0.0;
    }
}*/
