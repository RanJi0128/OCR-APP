#include "mainwindow.h"

MainWindow::MainWindow(QWidget *parent) : QMainWindow(parent)
{
      QSize size = qApp->screens()[0]->size();
      width = size.width();
      height = size.height();

      interface();
}
void MainWindow::interface()
{

   textShow = new QTextEdit(this);
   textShow->setGeometry(0,height*10/100, width, height*85/100);
   textShow->setReadOnly(1);

   titleLabel = new QLabel("OCR Sample",this);
   titleLabel->setAlignment(Qt::AlignCenter);
   titleLabel->setGeometry(width*15/100,height*5/100,width*70/100,height*5/100);
   titleLabel->setFont(QFont("Times", titleLabel->height()*25/100, QFont::Bold));

   exitBtn = new QPushButton("Exit",this);
   exitBtn->setGeometry(width*75/100,height*5/100,width*25/100,height*5/100);
   exitBtn->setFont(QFont("Times", titleLabel->height()*25/100, QFont::Bold));
   connect(exitBtn,SIGNAL(clicked()),qApp,SLOT(quit()));

   openCamera();
    //androidMethodCall();
}
void MainWindow::androidMethodCall()
{

   QAndroidJniObject string = QAndroidJniObject::callStaticObjectMethod("com/jni/camera/ConvertText", "ReadDataConvertText", "(Landroid/content/Context;)Ljava/lang/String;",QtAndroid::androidContext().object<jobject>());
   QString text = string.toString();

   textShow->setText(text);



}
void MainWindow::openCamera()
{
    camera = new QCamera;

    viewfinder = new QCameraViewfinder();
    viewfinder->show();

    camera->setViewfinder(viewfinder);

    imageCapture = new QCameraImageCapture(camera);

    camera->setCaptureMode(QCamera::CaptureStillImage);
    camera->start();
}