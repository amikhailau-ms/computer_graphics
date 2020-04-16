from PyQt5 import QtCore
from PyQt5.QtGui import QPen, QColor


def get_code(a, rect):
    code = [0, 0, 0, 0]
    if a[0] < rect[0]:
        code[0] = 1
    if a[0] > rect[1]:
        code[1] = 1
    if a[1] < rect[2]:
        code[2] = 1
    if a[1] > rect[3]:
        code[3] = 1
    return code


def log_prod(code1, code2):
    p = 0
    for i in range(4):
        p += code1[i] & code2[i]

    return p


def is_visible(bar, rect):
    """Видимость - 0 = невидимый
                   1 = видимый
                   2 = частично видимый"""
    # вычисление кодов концевых точек отрезка
    s1 = sum(get_code(bar[0], rect))
    s2 = sum(get_code(bar[1], rect))

    # предположим, что отрезок частично видим
    vis = 2

    # проверка полной видимости отрезка
    if not s1 and not s2:
        vis = 1
    else:
        # проверка тривиальной невидимости отрезка
        l = log_prod(get_code(bar[0], rect), get_code(bar[1], rect))
        if l != 0:
            vis = 0

    return vis


def get_qpen():
    pen = QPen()
    pen.setWidth(1)
    pen.setStyle(QtCore.Qt.SolidLine)
    pen.setColor(QColor(51, 204, 255))
    pen.setCapStyle(QtCore.Qt.SquareCap)
    pen.setJoinStyle(QtCore.Qt.RoundJoin)
    return pen


def get_all_qpen():
    pen = QPen()
    pen.setWidth(0.5)
    pen.setStyle(QtCore.Qt.SolidLine)
    pen.setColor(QColor(252, 3, 3))
    pen.setCapStyle(QtCore.Qt.SquareCap)
    pen.setJoinStyle(QtCore.Qt.RoundJoin)
    return pen


def get_transit_pen():
    pen = QPen()
    pen.setWidth(1)
    pen.setStyle(QtCore.Qt.DashLine)
    pen.setColor(QColor(168, 50, 153))
    return pen