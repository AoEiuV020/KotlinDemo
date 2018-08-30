# Umeng, https://developer.umeng.com/docs/66632/detail/66889#h2-u81EAu52A8u96C6u62103
-keep class com.umeng.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class **.R$*{
public static final int *;
}

#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
# 保留行号，区分混淆后的同名方法，虽然会被inline影响，
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute ''
# 不同类的成员用不同名字，同一个类还是会用相同名字，
-useuniqueclassmembernames

#各种问题通通无视
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-ignorewarnings
