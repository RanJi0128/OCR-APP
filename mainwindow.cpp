#include "mainwindow.h"

MainWindow::MainWindow(QWidget *parent) : QMainWindow(parent)
{
      QSize size = qApp->screens()[0]->size();
      width = size.width();
      height = size.height();

      interface();

      QAndroidJniObject::callStaticMethod<void>("com/jni/camera/ConvertText", "SetPermission", "()V");
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
   exitBtn->setFont(QFont("Times", titleLabel->height()*20/100, QFont::Bold));
   exitBtn->setStyleSheet("QPushButton{"
                             "color : #14428b;"
                              "background-color: #e3edfb;"
                              "border-style: outset;"
                              "border-width: 2px;"
                              "border-color: #e3edfb;"
                              "padding: 6px;}"
                          "QPushButton:pressed {"
                              "color : #8fd7f1;"
                              "border-style: inset;"
                              "border-color: #8fd7f1;}");
   connect(exitBtn,SIGNAL(clicked()),qApp,SLOT(quit()));

   startBtn = new QPushButton("Start",this);
   startBtn->setGeometry(0,height*5/100,width*25/100,height*5/100);
   startBtn->setFont(QFont("Times", titleLabel->height()*20/100, QFont::Bold));
   startBtn->setStyleSheet("QPushButton{"
                             "color : #14428b;"
                              "background-color: #e3edfb;"
                              "border-style: outset;"
                              "border-width: 2px;"
                              "border-color: #e3edfb;"
                              "padding: 6px;}"
                          "QPushButton:pressed {"
                              "color : #8fd7f1;"
                              "border-style: inset;"
                              "border-color: #8fd7f1;}");
   connect(startBtn,SIGNAL(clicked()),this,SLOT(cameraStart()));
   connect(this,SIGNAL(receiveKey()),this,SLOT(setKeyText()));


}
void MainWindow::cameraStart()
{
   androidMethodCall();
}
void MainWindow::androidMethodCall()
{

   QAndroidJniObject::callStaticMethod<void>("com/jni/camera/ConvertText", "OpenCamera", "(Landroid/content/Context;)V",QtAndroid::androidContext().object<jobject>());

}
void MainWindow::showKeyText(QString keyStr)
{
//
    keyString=keyStr;
    emit receiveKey();

}
void MainWindow::setKeyText()
{
    textShow->setText(keyString);
}

