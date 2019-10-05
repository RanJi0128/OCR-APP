#include "mainwindow.h"

static QObject *pRootObject = nullptr;

void callOfWindow(JNIEnv *env, jobject thiz, jstring body)
{
    Q_UNUSED(env)
    Q_UNUSED(thiz)

    if(pRootObject != nullptr)
    {
        QAndroidJniObject string = body;
        QString qstring = string.toString();

        static_cast<MainWindow*>(pRootObject)->showKeyText(qstring);

    }

}
void readKey()
{
    JNINativeMethod methods[] = {

            {  "KeyInformation","(Ljava/lang/String;)V",reinterpret_cast<void*>(callOfWindow) }
        };
     QAndroidJniObject keyInfor_j("com/jni/camera/ConvertText");
     QAndroidJniEnvironment env;

     jclass objectClass = env->GetObjectClass(keyInfor_j.object<jobject>());

     env->RegisterNatives(objectClass,methods,sizeof(methods) / sizeof(methods[0]));
     env->DeleteLocalRef(objectClass);
}

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    MainWindow w;
    w.showFullScreen();

    pRootObject=&w;
    readKey();

    return a.exec();
}
