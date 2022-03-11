# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-repackageclasses

#Data Class in Local Directory
-keep class com.onelink.nrlp.android.data.local**{
    public ** component1();
    <fields>;
}
-keep class com.onelink.nrlp.android.features.select.country.model**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.features.select.city.model**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.features.login.model**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.features.viewStatement.models**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.core**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.features.beneficiary.models**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.features.changePassword.models**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.features.faqs.model**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.features.forgotPassword.models**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.features.home.sidemenu**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.features.profile.models**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.features.redeem.model**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.features.register.models**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.features.select.country.model**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.features.viewStatement.models**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.models**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.utils.view.hometiles**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.features.managePoints.model**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.features.home.model**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.features.uuid.model**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.features.nrlpBenefits.model**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.features.complaint.models**{
    public ** component1();
    <fields>;
}

-keep class com.onelink.nrlp.android.features.receiver.models**{
    public ** component1();
    <fields>;
}

#Custom Views

-keep class com.onelink.nrlp.android.common.views**{
    <fields>;
    <methods>;
    <init>();
}


-assumenosideeffects class android.util.Log {
    public static *** e(...);
    public static *** d(...);
    public static *** i(...);
    public static *** v(...);
    public static *** w(...);
    public static *** wtf(...);
}

-keep public class * extends android.app.Activity
-keep public class * extends androidx.appcompat.app.AppCompatActivity
-keep public class * extends android.app.Fragment
-keep public class * extends androidx.fragment.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference

-keep public class * extends android.view.View {
 public <init>(android.content.Context);
 public <init>(android.content.Context, android.util.AttributeSet);
 public <init>(android.content.Context, android.util.AttributeSet, int);
 public void set*(...);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers enum * {
 public static **[] values();
 public static ** valueOf(java.lang.String);
}

-keepclassmembers class **.R$* {
 public static <fields>;
}

#Debugging & Stack Traces
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception



# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# OKHTTP

# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

#OKIO

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*