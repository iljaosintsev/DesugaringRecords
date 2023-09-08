-allowaccessmodification
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-keep public class MainKt {
    public static void main(java.lang.String[]);
}

-keep class User
-keep class Person

# -keep,allowshrinking class User { <fields>; }