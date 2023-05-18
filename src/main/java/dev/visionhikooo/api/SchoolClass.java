package dev.visionhikooo.api;

import com.iwebpp.crypto.TweetNaclFast;
import kotlin.Pair;

import java.util.HashMap;

public enum SchoolClass {
    CLASS_5("U+35U+20e3", 5, "5. Klasse"),
    CLASS_6("U+36U+20e3", 6, "6. Klasse"),
    CLASS_7("U+37U+20e3", 7, "7. Klasse"),
    CLASS_8("U+38U+20e3", 8, "8. Klasse"),
    CLASS_9("U+39U+20e3", 9, "9. Klasse"),
    CLASS_10("U+1f4da", 10, "10. Klasse"),
    CLASS_11("U+1f616", 11, "11. Klasse"),
    CLASS_12("U+1f9d1U+200dU+1f393", 12, "12. Klasse");

    private final String emoji;
    private final int classNumber;
    private final String roleName;

    private SchoolClass(String emoji, int classNumber, String roleName) {
        this.emoji = emoji;
        this.classNumber = classNumber;
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getEmoji() {
        return emoji;
    }

    public int getClassNumber() {
        return classNumber;
    }

    public SchoolClass getSchoolClass(String emoji) {
        for (SchoolClass schoolClass : SchoolClass.values())
            if (schoolClass.getEmoji().equalsIgnoreCase(emoji))
                return schoolClass;
        return null;
    }

    public SchoolClass getSchoolClass(int classNumber) {
        for (SchoolClass schoolClass : SchoolClass.values())
            if (schoolClass.getClassNumber() == classNumber)
                return schoolClass;
        return null;
    }
}
