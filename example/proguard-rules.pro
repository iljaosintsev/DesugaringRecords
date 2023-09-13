-allowaccessmodification
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-keep public class MainKt {
    public static void main(java.lang.String[]);
}

-keep class User
-keep class Person

# Person(“John”, 42).toString();

# // With D8 or R8 with -dontobfuscate -dontoptimize
# >>> Person[name=John, age=42]

# // With R8 and no keep rule.
# >>> a[a=John]

# // With R8 and -keep,allowshrinking,allowoptimization class Person
# >>> Person[b=John]

# // With R8 and -keepclassmembers,allowshrinking,allowoptimization class Person { <fields>; }
# >>> a[name=John]

# // With R8 and -keepclassmembers,allowobfuscation class Person { <fields>; }
# >>> a[a=John, b=42]

# // With R8 and -keep class Person { <fields>; }
# >>> Person[name=John, age=42]