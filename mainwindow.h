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
    QPushButton *startBtn;

    void interface();
    void androidMethodCall();
    void showKeyText(QString keyStr);

    QString keyString;

signals:

    void receiveKey();
public slots:
    void cameraStart();
    void setKeyText();
};

#endif // MAINWINDOW_H
