#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include "include.h"

class MainWindow : public QMainWindow
{
    Q_OBJECT
public:
    explicit MainWindow(QWidget *parent = nullptr);

    int width;
    int height;

    QTextEdit *textShow;
    QLabel *titleLabel;
    QPushButton *exitBtn;

    QCamera *camera;
    QCameraViewfinder *viewfinder;
    QCameraImageCapture *imageCapture;


    void interface();
    void androidMethodCall();
    void openCamera();
signals:

public slots:
};

#endif // MAINWINDOW_H
