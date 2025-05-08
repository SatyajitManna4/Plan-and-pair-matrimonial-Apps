package com.example.planpair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
}